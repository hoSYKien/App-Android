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

import com.example.hosykien211604449.Department;

// Note: Add in app/build.gradle: implementation 'net.sourceforge.jtds:jtds:1.3.1'
public class DatabaseHelper {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DatabaseHelper(Context context) {
    }

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
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";instance=" + instance + ";user=" + username + ";password=" + password + ";";
            conn = DriverManager.getConnection(connectionUrl);
            Log.i("DBConnection", "Kết nối thành công!");
        } catch (SQLException se) {
            Log.e("DBConnection", "Lỗi kết nối: " + se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("DBConnection", "Không tìm thấy driver JDBC: " + e.getMessage());
        }
        return conn;
    }

    // ... existing methods: getUserList, updateUser, insertUser, etc.

    /**
     * Lấy danh sách khoa từ CSDL
     */
    public void getDepartmentList(Callback<List<Department>> callback) {
        executorService.execute(() -> {
            List<Department> list = new ArrayList<>();
            Connection conn = connect();
            if (conn != null) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(
                             "SELECT maKhoa, tenKhoa, vitri, pass FROM khoa")) {

                    while (rs.next()) {
                        list.add(new Department(
                                rs.getString("maKhoa"),
                                rs.getString("tenKhoa"),
                                rs.getString("vitri"),
                                rs.getString("pass")
                        ));
                    }

                    // ← thêm dòng này để in ra kích thước list
                    Log.i("DBHelper", "Tìm thấy departments: " + list.size());
                } catch (SQLException e) {
                    Log.e("DBHelper", "Lỗi query: " + e.getMessage());
                } finally {
                    try { conn.close(); } catch (Exception ignored) {}
                }
            } else {
                Log.e("DBHelper", "Connection trả về null");
            }

            mainHandler.post(() -> callback.onResult(list));
        });
    }

    /** Lấy danh sách phòng theo mã khoa */
    public void getRoomList(String maKhoaFilter, Callback<List<Room>> callback) {
        executorService.execute(() -> {
            List<Room> list = new ArrayList<>();
            Connection conn = connect();
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    String sql = "SELECT maPhong, maKhoa, tenPhong, viTri FROM Phong WHERE maKhoa = N'"
                            + maKhoaFilter + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        list.add(new Room(
                                rs.getString("maPhong"),
                                rs.getString("maKhoa"),
                                rs.getString("tenPhong"),
                                rs.getString("viTri")
                        ));
                    }
                    rs.close();
                } catch (SQLException e) {
                    Log.e("DBHelper", "Lỗi load rooms: " + e.getMessage());
                } finally {
                    try { conn.close(); } catch (Exception ignored) {}
                }
            } else {
                Log.e("DBHelper", "Connection trả về null");
            }
            mainHandler.post(() -> callback.onResult(list));
            Log.i("DBHelper", "Tìm thấy rooms: " + list.size());
        });
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}
