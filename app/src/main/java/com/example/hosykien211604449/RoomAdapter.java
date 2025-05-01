package com.example.hosykien211604449;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    /**
     * Interface để xử lý sự kiện click trên Room item
     */
    public interface OnItemClickListener {
        void onItemClick(Room room);
    }

    private final List<Room> roomList;
    private OnItemClickListener listener;

    public RoomAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    /**
     * Đăng ký listener từ Activity
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvMaPhong.setText(room.getMaPhong());
        holder.tvTenPhong.setText(room.getTenPhong());
        holder.tvViTri.setText(room.getViTri());

        // Gọi listener khi người dùng click vào item, việc clear để Activity xử lý
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    /**
     * ViewHolder cho Room item
     */
    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaPhong, tvTenPhong, tvViTri;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhong  = itemView.findViewById(R.id.tvMaPhong);
            tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
            tvViTri    = itemView.findViewById(R.id.tvViTri);
        }
    }
}
