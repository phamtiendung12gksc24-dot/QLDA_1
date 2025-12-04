package com.example.duan1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private Context context;
    private OnQuantityChangeListener quantityChangeListener;
    private OnRemoveItemListener removeItemListener;

    public interface OnQuantityChangeListener {
        void onQuantityChange(String cartItemId, int newQuantity);
    }

    public interface OnRemoveItemListener {
        void onRemoveItem(String cartItemId);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public void setOnRemoveItemListener(OnRemoveItemListener listener) {
        this.removeItemListener = listener;
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

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        
        if (cartItem.getProduct() != null) {
            holder.tvProductName.setText(cartItem.getProduct().getName());
            holder.tvProductPrice.setText(cartItem.getProduct().getFormattedPrice());
        }
        
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvSubtotal.setText(cartItem.getFormattedSubtotal());
        
        // Set màu cho icon
        int colorIndex = position % colors.length;
        holder.viewIcon.setBackgroundColor(colors[colorIndex]);

        // Xử lý tăng số lượng
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChange(cartItem.getId(), newQuantity);
            }
        });

        // Xử lý giảm số lượng
        holder.btnMinus.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChange(cartItem.getId(), newQuantity);
                }
            }
        });

        // Xử lý xóa sản phẩm
        holder.btnRemove.setOnClickListener(v -> {
            if (removeItemListener != null) {
                removeItemListener.onRemoveItem(cartItem.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public void updateCartItems(List<CartItem> newList) {
        this.cartItemList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        View viewIcon;
        TextView tvProductName, tvProductPrice, tvQuantity, tvSubtotal;
        Button btnPlus, btnMinus, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            viewIcon = itemView.findViewById(R.id.viewIcon);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}

