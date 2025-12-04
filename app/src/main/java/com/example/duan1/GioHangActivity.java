package com.example.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.model.CartItem;
import com.example.duan1.model.Response;
import com.example.duan1.services.ApiServices;
import com.example.duan1.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class GioHangActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvTotalPrice;
    private Button btnDatHang;
    private LinearLayout layoutEmpty;
    private ApiServices apiServices;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        // Khởi tạo
        apiServices = RetrofitClient.getInstance().getApiServices();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userId = sharedPreferences.getString("id_taikhoan", "");

        // Ánh xạ views
        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnDatHang = findViewById(R.id.btnDatHang);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        // Khởi tạo danh sách
        cartItemList = new ArrayList<>();

        // Setup RecyclerView
        cartAdapter = new CartAdapter(this, cartItemList);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);

        // Xử lý thay đổi số lượng
        cartAdapter.setOnQuantityChangeListener((cartItemId, newQuantity) -> {
            updateCartItemQuantity(cartItemId, newQuantity);
        });

        // Xử lý xóa sản phẩm
        cartAdapter.setOnRemoveItemListener(cartItemId -> {
            removeCartItem(cartItemId);
        });

        // Xử lý đặt hàng
        btnDatHang.setOnClickListener(v -> {
            if (cartItemList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }
            // Chuyển đến màn hình đặt hàng
            Intent intent = new Intent(GioHangActivity.this, DatHangActivity.class);
            startActivity(intent);
        });

        // Load giỏ hàng
        loadCartItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload giỏ hàng khi quay lại màn hình
        loadCartItems();
    }

    private void loadCartItems() {
        if (userId == null || userId.isEmpty()) {
            // Hiển thị giỏ hàng trống nếu chưa đăng nhập
            cartItemList.clear();
            cartAdapter.updateCartItems(cartItemList);
            updateUI();
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        apiServices.getCartItems(userId).enqueue(new Callback<Response<List<CartItem>>>() {
            @Override
            public void onResponse(Call<Response<List<CartItem>>> call, retrofit2.Response<Response<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<List<CartItem>> res = response.body();
                    if (res.isSuccess() && res.getData() != null) {
                        cartItemList.clear();
                        cartItemList.addAll(res.getData());
                        cartAdapter.updateCartItems(cartItemList);
                        updateTotalPrice();
                        updateUI();
                    } else {
                        cartItemList.clear();
                        cartAdapter.updateCartItems(cartItemList);
                        updateUI();
                    }
                } else {
                    Log.e("API Error", "Failed to load cart: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Response<List<CartItem>>> call, Throwable t) {
                Log.e("API Error", t.toString());
                Toast.makeText(GioHangActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItemQuantity(String cartItemId, int newQuantity) {
        Map<String, Integer> body = new HashMap<>();
        body.put("quantity", newQuantity);

        apiServices.updateCartItem(cartItemId, body).enqueue(new Callback<Response<CartItem>>() {
            @Override
            public void onResponse(Call<Response<CartItem>> call, retrofit2.Response<Response<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<CartItem> res = response.body();
                    if (res.isSuccess()) {
                        // Reload giỏ hàng
                        loadCartItems();
                    } else {
                        Toast.makeText(GioHangActivity.this, "Cập nhật thất bại: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<CartItem>> call, Throwable t) {
                Toast.makeText(GioHangActivity.this, "Lỗi cập nhật: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCartItem(String cartItemId) {
        apiServices.removeFromCart(cartItemId).enqueue(new Callback<Response<CartItem>>() {
            @Override
            public void onResponse(Call<Response<CartItem>> call, retrofit2.Response<Response<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<CartItem> res = response.body();
                    if (res.isSuccess()) {
                        Toast.makeText(GioHangActivity.this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        // Reload giỏ hàng
                        loadCartItems();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<CartItem>> call, Throwable t) {
                Toast.makeText(GioHangActivity.this, "Lỗi xóa: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getSubtotal();
        }

        // Format tổng tiền
        long totalLong = (long) total;
        if (totalLong >= 1000) {
            long thousands = totalLong / 1000;
            tvTotalPrice.setText(String.format("%,d.000đ", thousands).replace(",", "."));
        } else {
            tvTotalPrice.setText(String.format("%d.000đ", totalLong));
        }
    }

    private void updateUI() {
        if (cartItemList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvCartItems.setVisibility(View.GONE);
            btnDatHang.setEnabled(false);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvCartItems.setVisibility(View.VISIBLE);
            btnDatHang.setEnabled(true);
        }
    }

    public void onBackClick(View view) {
        finish();
    }
}

