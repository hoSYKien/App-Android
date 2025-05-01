package com.example.hosykien211604449;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerDepartments;
    private DepartmentAdapter adapter;
    private List<Department> departmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Thiết lập RecyclerView
        recyclerDepartments = findViewById(R.id.recyclerDepartments);
        recyclerDepartments.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DepartmentAdapter(departmentList);
        recyclerDepartments.setAdapter(adapter);

        // 2. Thiết lập sự kiện click lên từng khoa
        adapter.setOnItemClickListener(dept -> showPasswordDialog(dept));

        // 3. Load dữ liệu từ CSDL
        new DatabaseHelper(this).getDepartmentList(depts -> {
            if (depts != null && !depts.isEmpty()) {
                departmentList.clear();
                departmentList.addAll(depts);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Chưa lấy được dữ liệu từ CSDL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hiển thị hộp thoại yêu cầu nhập mật khẩu khi chọn khoa
     */
    private void showPasswordDialog(Department dept) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập mật khẩu cho " + dept.getTenKhoa());

        // Tạo EditText nhập mật khẩu
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Mật khẩu");
        builder.setView(input);

        // Nút Login
        builder.setPositiveButton("Login", (dialog, which) -> {
            String passInput = input.getText().toString().trim();
            try {
                // Lấy password đã giải mã từ đối tượng dept
                String realPass = dept.getPass();

                if (realPass != null && realPass.equals(passInput)) {
                    Toast.makeText(this,
                            "Đăng nhập thành công vào " + dept.getTenKhoa(),
                            Toast.LENGTH_SHORT).show();

                    // Chuyển sang RoomActivity, truyền maKhoa để load phòng tương ứng
                    Intent it = new Intent(this, RoomActivity.class);
                    it.putExtra("EXTRA_MAKHOA", dept.getMaKhoa());
                    startActivity(it);

                } else {
                    Toast.makeText(this, "Mật khẩu sai!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi kiểm tra mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Cancel
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

}
