package com.example.hosykien211604449;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.TextView;

/**
 * Adapter để hiển thị danh sách PCDRecord trong RecyclerView
 */
public class PcdRecordAdapter extends RecyclerView.Adapter<PcdRecordAdapter.PcdViewHolder> {
    private final List<PCDRecord> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PCDRecord record);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PcdRecordAdapter(List<PCDRecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PcdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pcd_record, parent, false);
        return new PcdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PcdViewHolder holder, int position) {
        PCDRecord r = items.get(position);
        holder.tvStt.setText(String.valueOf(r.getStt()));
        holder.tvMaPCD.setText(r.getMaPCD());
        holder.tvMaBS_TenBS.setText("BS: " + r.getMaBS() + " - " + r.getTenBS());
        holder.tvMaBN_TenBN.setText("BN: " + r.getMaBN() + " - " + r.getTenBN());
        holder.tvNgayCD.setText("Ngày CD: " + r.getNgayCD());
        holder.tvTongTien.setText("Tổng: " + r.getTongTien());
        holder.tvTrangThai.setText("Trạng thái: " + r.getTrangThai());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PcdViewHolder extends RecyclerView.ViewHolder {
        TextView tvStt;
        TextView tvMaPCD;
        TextView tvMaBS_TenBS;
        TextView tvMaBN_TenBN;
        TextView tvNgayCD;
        TextView tvTongTien;
        TextView tvTrangThai;

        public PcdViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tvStt);
            tvMaPCD = itemView.findViewById(R.id.tvMaPCD);
            tvMaBS_TenBS = itemView.findViewById(R.id.tvMaBS_TenBS);
            tvMaBN_TenBN = itemView.findViewById(R.id.tvMaBN_TenBN);
            tvNgayCD = itemView.findViewById(R.id.tvNgayCD);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
