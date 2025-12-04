package com.example.duan1.AdapterAdmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.DanhMuc;
import com.example.duan1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    private List<Product> productList;
    private List<Product> productListFull;
    private List<DanhMuc> danhMucList;
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

    // Màu sắc cho các icon sản phẩm
    private int[] colors = {
            Color.parseColor("#FFD700"), // Vàng
            Color.parseColor("#9370DB"), // Tím
            Color.parseColor("#87CEEB"), // Xanh nhạt
            Color.parseColor("#FFA500"), // Cam
            Color.parseColor("#FF69B4"), // Hồng
            Color.parseColor("#32CD32"), // Xanh lá
    };

    public SanPhamAdapter(Context context, List<Product> productList, List<DanhMuc> danhMucList) {
        this.context = context;
        this.productList = productList != null ? productList : new ArrayList<>();
        this.productListFull = new ArrayList<>(this.productList);
        this.danhMucList = danhMucList != null ? danhMucList : new ArrayList<>();
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        Product product = productList.get(position);
        
        holder.tvProductName.setText(product.getName() != null ? product.getName() : "");
        holder.tvProductPrice.setText(product.getFormattedPrice());
        
        // Hiển thị danh mục
        String categoryName = "Chưa có danh mục";
        if (product.getCategory() != null && product.getCategory().getName() != null) {
            categoryName = product.getCategory().getName();
        } else if (product.getCategoryId() != null && !product.getCategoryId().isEmpty()) {
            // Tìm danh mục từ danhMucList
            for (DanhMuc danhMuc : danhMucList) {
                if (danhMuc.getId() != null && danhMuc.getId().equals(product.getCategoryId())) {
                    categoryName = danhMuc.getName();
                    break;
                }
            }
        }
        holder.tvCategory.setText(categoryName);

        // Load hình ảnh nếu có
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            String imageUrl = product.getImage();
            if (!imageUrl.startsWith("http")) {
                // Nếu là đường dẫn tương đối, thêm base URL
                imageUrl = "http://10.0.2.2:3000/" + imageUrl;
            }
            try {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.imgProduct);
            } catch (Exception e) {
                holder.imgProduct.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            // Set màu cho icon nếu không có hình ảnh
            int colorIndex = position % colors.length;
            holder.imgProduct.setBackgroundColor(colors[colorIndex]);
            holder.imgProduct.setImageResource(android.R.color.transparent);
        }

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
        return productList != null ? productList.size() : 0;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void updateList(List<Product> newList) {
        this.productList = newList != null ? newList : new ArrayList<>();
        this.productListFull = new ArrayList<>(this.productList);
        notifyDataSetChanged();
    }

    public void setDanhMucList(List<DanhMuc> danhMucList) {
        this.danhMucList = danhMucList != null ? danhMucList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void filterList(String text) {
        productList.clear();
        if (text == null || text.trim().isEmpty()) {
            productList.addAll(productListFull);
        } else {
            text = text.toLowerCase().trim();
            for (Product product : productListFull) {
                if (product.getName() != null && product.getName().toLowerCase().contains(text)) {
                    productList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvCategory;
        ImageButton btnEdit, btnDelete;
        android.widget.ImageView imgProduct;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}

