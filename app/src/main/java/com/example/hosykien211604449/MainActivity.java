package com.example.hosykien211604449;

import android.os.Bundle;
import android.widget.EditText;
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
    private TextView tvTitle;

    private DepartmentAdapter deptAdapter;
    private RoomAdapter roomAdapter;
    private List<Department> departmentList = new ArrayList<>();
    private List<Room> roomList           = new ArrayList<>();

    // state để biết đang hiển khoa hay phòng
    private boolean showingDepartments = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // layout có headerLayout và tvTitle :contentReference[oaicite:1]{index=1}&#8203;:contentReference[oaicite:2]{index=2}

        recyclerDepartments = findViewById(R.id.recyclerDepartments);
        tvTitle             = findViewById(R.id.tvTitle);

        recyclerDepartments.setLayoutManager(new GridLayoutManager(this, 2));

        // bắt đầu bằng việc load danh sách khoa
        loadDepartments();
    }

    private void loadDepartments() {
        showingDepartments = true;
        tvTitle.setText("HOSPITAL");
        // 1. Thiết lập adapter khoa
        deptAdapter = new DepartmentAdapter(departmentList);
        recyclerDepartments.setAdapter(deptAdapter);

        // 2. Bắt sự kiện click khoa
        deptAdapter.setOnItemClickListener(dept -> {
            // hiển hộp thoại nhập mật khẩu
            EditText input = new EditText(this);
            input.setHint("Nhập mật khẩu");
            new AlertDialog.Builder(this)
                    .setTitle(dept.getTenKhoa())
                    .setView(input)
                    .setPositiveButton("OK", (d, w) -> {
                        try {
                            if (dept.validatePassword(input.getText().toString().trim())) {
                                // đúng → load phòng
                                loadRooms(dept.getMaKhoa());
                            } else {
                                Toast.makeText(this, "Mật khẩu sai", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Lỗi kiểm tra mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // 3. Load dữ liệu khoa từ DB
        new DatabaseHelper(this).getDepartmentList(depts -> runOnUiThread(() -> {
            departmentList.clear();
            if (depts != null) departmentList.addAll(depts);
            deptAdapter.notifyDataSetChanged();
        }));
    }

    private void loadRooms(String maKhoa) {
        showingDepartments = false;
        tvTitle.setText("Phòng của " + maKhoa);

        // 1. Thiết lập adapter phòng
        roomAdapter = new RoomAdapter(roomList);
        recyclerDepartments.setAdapter(roomAdapter);

        // 2. Load dữ liệu phòng từ DB
        new DatabaseHelper(this).getRoomList(maKhoa, rooms -> runOnUiThread(() -> {
            roomList.clear();
            if (rooms != null) roomList.addAll(rooms);
            roomAdapter.notifyDataSetChanged();
        }));
    }

    @Override
    public void onBackPressed() {
        // nếu đang hiển phòng, bấm back sẽ quay lại danh sách khoa
        if (!showingDepartments) {
            loadDepartments();
        } else {
            super.onBackPressed();
        }
    }
}
