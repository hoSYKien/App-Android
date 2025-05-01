package com.example.hosykien211604449;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerDepartments;
    private ScrollView scrollPatients;
    private TableLayout tablePatients;
    private TextView tvTitle;
    private ImageButton btnHome, btnDepartments, btnMap, btnNotifications;
    private boolean showingDepartments = true;

    private List<Department> departmentList = new ArrayList<>();
    private List<Room> roomList = new ArrayList<>();
    private List<PCDRecord> pcdList = new ArrayList<>();

    private DepartmentAdapter departmentAdapter;
    private RoomAdapter roomAdapter;
    private PcdRecordAdapter pcdAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        tvTitle = findViewById(R.id.tvTitle);
        recyclerDepartments = findViewById(R.id.recyclerDepartments);
        scrollPatients = findViewById(R.id.scrollPatients);
        tablePatients = findViewById(R.id.tablePatients);
        btnHome = findViewById(R.id.btnHome);
        btnDepartments = findViewById(R.id.btnDepartments);
        btnMap = findViewById(R.id.btnMap);
        btnNotifications = findViewById(R.id.btnNotifications);

        recyclerDepartments.setLayoutManager(new GridLayoutManager(this, 2));
        dbHelper = new DatabaseHelper(this);

        // setup adapters
        departmentAdapter = new DepartmentAdapter(departmentList);
        departmentAdapter.setOnItemClickListener(this::onDepartmentClicked);

        roomAdapter = new RoomAdapter(roomList);
        roomAdapter.setOnItemClickListener(this::onRoomClicked);

        pcdAdapter = new PcdRecordAdapter(pcdList);
        pcdAdapter.setOnItemClickListener(r -> {
            // handle PCD item click if needed
        });

        // bottom panel buttons
        btnHome.setOnClickListener(v -> showDepartments());
        btnDepartments.setOnClickListener(v -> showDepartments());
        btnMap.setOnClickListener(v -> showPCDRecords(""));
        btnNotifications.setOnClickListener(v -> {
            // future: handle notifications
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show();
        });

        // initial load
        showDepartments();
    }

    private void showDepartments() {
        showingDepartments = true;
        scrollPatients.setVisibility(View.GONE);
        recyclerDepartments.setVisibility(View.VISIBLE);
        tvTitle.setText("Danh sách khoa");
        recyclerDepartments.setAdapter(departmentAdapter);
        departmentList.clear();
        dbHelper.getDepartmentList(depts -> runOnUiThread(() -> {
            if (depts != null) departmentList.addAll(depts);
            departmentAdapter.notifyDataSetChanged();
        }));
    }

    private void onDepartmentClicked(Department dept) {
        EditText input = new EditText(this);
        input.setHint("Nhập mật khẩu");
        new AlertDialog.Builder(this)
                .setTitle(dept.getTenKhoa())
                .setView(input)
                .setPositiveButton("OK", (d, w) -> {
                    try {
                        if (input.getText().toString().equals(dept.getPass())) {
                            showRooms(dept.getMaKhoa());
                        } else {
                            Toast.makeText(this, "Mật khẩu sai", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showRooms(String maKhoa) {
        showingDepartments = false;
        scrollPatients.setVisibility(View.GONE);
        recyclerDepartments.setVisibility(View.VISIBLE);
        tvTitle.setText("Danh sách phòng");
        recyclerDepartments.setAdapter(roomAdapter);
        roomList.clear();
        dbHelper.getRoomList(maKhoa, rooms -> runOnUiThread(() -> {
            if (rooms != null) roomList.addAll(rooms);
            roomAdapter.notifyDataSetChanged();
        }));
    }

    private void onRoomClicked(Room room) {
        showingDepartments = false;
        recyclerDepartments.setVisibility(View.GONE);
        scrollPatients.setVisibility(View.VISIBLE);
        tvTitle.setText("Danh sách bệnh nhân");
        showPatients(room.getMaPhong());
    }

    private void showPatients(String maPhong) {
        while (tablePatients.getChildCount() > 1) tablePatients.removeViewAt(1);
        dbHelper.getPatientList(maPhong, patients -> runOnUiThread(() -> {
            if (patients == null || patients.isEmpty()) {
                TableRow row = new TableRow(this);
                TextView tv = createCell("Không có bệnh nhân");
                tv.setGravity(Gravity.CENTER);
                row.addView(tv);
                tablePatients.addView(row);
            } else {
                for (Patient p : patients) {
                    TableRow row = new TableRow(this);
                    row.addView(createCell(p.getMaPhieu()));
                    row.addView(createCell(p.getMaBN()));
                    row.addView(createCell(p.getTenBN()));
                    row.setOnClickListener(v -> dbHelper.handlePatient(p.getMaPhieu(), u -> runOnUiThread(() ->
                            Toast.makeText(this, "Đã xử lý phiếu " + p.getMaPhieu(), Toast.LENGTH_SHORT).show())));
                    tablePatients.addView(row);
                }
            }
        }));
    }

    private void showPCDRecords(String keyword) {
        showingDepartments = false;
        // vertical list, 1 item per row
        GridLayoutManager glm = new GridLayoutManager(this, 1);
        recyclerDepartments.setLayoutManager(glm);
        recyclerDepartments.setVisibility(View.VISIBLE);
        scrollPatients.setVisibility(View.GONE);
        tvTitle.setText("Kết quả PCD");
        recyclerDepartments.setAdapter(pcdAdapter);
        pcdList.clear();
        dbHelper.searchPCD(keyword, records -> runOnUiThread(() -> {
            if (records != null) pcdList.addAll(records);
            pcdAdapter.notifyDataSetChanged();
        }));
    }

    private TextView createCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }

    @Override
    public void onBackPressed() {
        if (!showingDepartments) {
            showDepartments();
        } else {
            super.onBackPressed();
        }
    }
}
