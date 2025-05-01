package com.example.hosykien211604449;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách khoa (Department)
 */
public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private final List<Department> list;
    private OnItemClickListener listener;  // Sử dụng interface nội tại, không phải AdapterView

    public DepartmentAdapter(List<Department> list) {
        this.list = list;
    }

    /**
     * Interface để MainActivity implement
     */
    public interface OnItemClickListener {
        void onItemClick(Department dept);
    }

    /**
     * Gán listener từ MainActivity
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Department dept = list.get(position);
        holder.tvTenKhoa.setText(dept.getTenKhoa());
        holder.tvMaKhoa.setText(dept.getMaKhoa());
        holder.tvVitri.setText(dept.getVitri());
        // Gán sự kiện click lên từng item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(dept);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKhoa, tvMaKhoa, tvVitri;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhoa = itemView.findViewById(R.id.tvTenKhoa);
            tvMaKhoa  = itemView.findViewById(R.id.tvMaKhoa);
            tvVitri   = itemView.findViewById(R.id.tvVitri);
        }
    }
}
