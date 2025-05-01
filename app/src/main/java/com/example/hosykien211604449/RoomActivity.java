package com.example.hosykien211604449;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
    public static final String EXTRA_MAKHOA = "EXTRA_MAKHOA";
    private RecyclerView recyclerRooms;
    private RoomAdapter adapter;
    private List<Room> roomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        String maKhoa = getIntent().getStringExtra(EXTRA_MAKHOA);
        if (maKhoa == null) {
            Toast.makeText(this, "Không có mã khoa!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerRooms = findViewById(R.id.recyclerRooms);
        recyclerRooms.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RoomAdapter(roomList);
        recyclerRooms.setAdapter(adapter);

        // Load phòng theo khoa
        new DatabaseHelper(this).getRoomList(maKhoa, rooms -> {
            Log.i("RoomActivity", "Callback rooms.size = " + rooms.size());
            Toast.makeText(this, "Loaded rooms: " + rooms.size(), Toast.LENGTH_SHORT).show();
            if (rooms != null && !rooms.isEmpty()) {
                roomList.clear();
                roomList.addAll(rooms);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Chưa có phòng cho khoa này", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
