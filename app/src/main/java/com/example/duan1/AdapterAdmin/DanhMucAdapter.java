package com.example.duan1.AdapterAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.DanhMuc;

import java.util.ArrayList;
import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.DanhMucViewHolder> {
    private List<DanhMuc> danhMucList;
    private List<DanhMuc> danhMucListFull;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DanhMucAdapter(Context context, List<DanhMuc> danhMucList) {
        this.context = context;
        this.danhMucList = danhMucList != null ? danhMucList : new ArrayList<>();
        this.danhMucListFull = new ArrayList<>(this.danhMucList);
    }

    @NonNull
    @Override
    public DanhMucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new DanhMucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhMucViewHolder holder, int position) {
        DanhMuc danhMuc = danhMucList.get(position);
        
        holder.tvTenDanhMuc.setText(danhMuc.getName() != null ? danhMuc.getName() : "");
        holder.tvMoTa.setText(danhMuc.getDescription() != null && !danhMuc.getDescription().isEmpty() 
                ? danhMuc.getDescription() : "Chưa có mô tả");

        // Xử lý click item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });

        // Xử lý nút Edit
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(holder.getAdapterPosition());
            }
        });

        // Xử lý nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhMucList != null ? danhMucList.size() : 0;
    }

    public void updateList(List<DanhMuc> newList) {
        this.danhMucList = newList != null ? newList : new ArrayList<>();
        this.danhMucListFull = new ArrayList<>(this.danhMucList);
        notifyDataSetChanged();
    }

    public void filterList(String text) {
        danhMucList.clear();
        if (text == null || text.trim().isEmpty()) {
            danhMucList.addAll(danhMucListFull);
        } else {
            text = text.toLowerCase().trim();
            for (DanhMuc danhMuc : danhMucListFull) {
                if (danhMuc.getName() != null && danhMuc.getName().toLowerCase().contains(text)) {
                    danhMucList.add(danhMuc);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class DanhMucViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMuc, tvMoTa;
        ImageButton btnEdit, btnDelete;

        public DanhMucViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

