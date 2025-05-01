package com.example.hosykien211604449;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DatabaseHelper(Context context) {
        // context nếu cần
    }

    /**
     * Thiết lập kết nối đến SQL Server
     */
    public static Connection connect() {
        Connection conn = null;
        String ip = "171.244.38.118";
        String port = "1433";
        String dbName = "iot2024";
        String username = "NCKH_2025";
        String password = "12345678";
        String instance = "SQLEXPRESS";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";instance=" + instance + ";databaseName=" + dbName
                    + ";user=" + username + ";password=" + password + ";";
            conn = DriverManager.getConnection(url);
            Log.i("DBConnection", "Connected successfully");
        } catch (ClassNotFoundException | SQLException e) {
            Log.e("DBConnection", "Connection error: " + e.getMessage());
        }
        return conn;
    }

    public interface Callback<T> { void onResult(T data); }

    public void getDepartmentList(Callback<List<Department>> callback) {
        executorService.execute(() -> {
            List<Department> list = new ArrayList<>();
            try (Connection conn = connect(); Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT maKhoa, tenKhoa, vitri, pass FROM khoa")) {
                while (rs.next()) {
                    list.add(new Department(
                            rs.getString("maKhoa"),
                            rs.getString("tenKhoa"),
                            rs.getString("vitri"),
                            rs.getString("pass")
                    ));
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error load departments: " + e.getMessage());
            }
            Log.i("DBHelper", "Loaded departments count=" + list.size());
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getRoomList(String maKhoaFilter, Callback<List<Room>> callback) {
        executorService.execute(() -> {
            List<Room> list = new ArrayList<>();
            String sql = "SELECT maPhong, maKhoa, tenPhong, viTri FROM Phong WHERE maKhoa = ?";
            try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maKhoaFilter);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Room(
                                rs.getString("maPhong"),
                                rs.getString("maKhoa"),
                                rs.getString("tenPhong"),
                                rs.getString("viTri")
                        ));
                    }
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error load rooms: " + e.getMessage());
            }
            Log.i("DBHelper", "Loaded rooms count=" + list.size());
            mainHandler.post(() -> callback.onResult(list));
        });
    }
    /**
     * Tìm kiếm phiếu chuẩn đoán qua stored procedure dbo.SearchPCD
     */
    public void searchPCD(String keyword, Callback<List<PCDRecord>> callback) {
        executorService.execute(() -> {
            List<PCDRecord> list = new ArrayList<>();
            String sql = "EXEC dbo.SearchPCD ?";
            try (Connection conn = connect();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, keyword);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        PCDRecord r = new PCDRecord();
                        r.setMaPCD(rs.getString("ID"));
                        r.setMaBS(rs.getString("Mã BS"));
                        r.setTenBS(rs.getString("Tên BS"));
                        r.setMaBN(rs.getString("Mã BN"));
                        r.setTenBN(rs.getString("Tên BN"));
                        r.setNgayCD(rs.getString("Ngày Chuẩn Đoán"));
                        r.setTongTien(rs.getString("Thành Tiền"));
                        r.setTrangThai(rs.getString("Trạng Thái"));
                        r.setStt(rs.getInt("stt"));
                        list.add(r);
                    }
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error search PCD: " + e.getMessage());
            }
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void getPatientList(String maPhongFilter, Callback<List<Patient>> callback) {
        executorService.execute(() -> {
            List<Patient> list = new ArrayList<>();
            String sql = "exec dbo.slPKP ?";
            try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maPhongFilter);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Patient(
                                rs.getString("Tên Bệnh Nhân"),
                                rs.getString("Ngày Khám"),
                                rs.getString("Tiền Khám")  // tenBN
                        ));
                    }
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error load patients: " + e.getMessage());
            }
            Log.i("DBHelper", "Loaded patients count=" + list.size());
            mainHandler.post(() -> callback.onResult(list));
        });
    }

    public void handlePatient(String maPhieu, Callback<Void> callback) {
        executorService.execute(() -> {
            try (Connection conn = connect()) {
                try (PreparedStatement ps1 = conn.prepareStatement(
                        "UPDATE phieuKham SET trangThai='true' WHERE maPK = ?")) {
                    ps1.setString(1, maPhieu);
                    ps1.executeUpdate();
                }
                String location = null;
                try (PreparedStatement ps2 = conn.prepareStatement("SELECT dbo.locationPCD(?)")) {
                    ps2.setString(1, maPhieu);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        if (rs2.next()) location = rs2.getString(1);
                    }
                }
                String maDH = null;
                try (PreparedStatement ps3 = conn.prepareStatement("SELECT dbo.maDH(?)")) {
                    ps3.setString(1, maPhieu);
                    try (ResultSet rs3 = ps3.executeQuery()) {
                        if (rs3.next()) maDH = rs3.getString(1);
                    }
                }
                if (location != null && maDH != null) {
                    Function.sendData(location, maDH + "_esp32_receive");
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error handle patient: " + e.getMessage());
            }
            mainHandler.post(() -> callback.onResult(null));
        });
    }
}
