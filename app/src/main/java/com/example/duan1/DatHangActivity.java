package com.example.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.model.CartItem;
import com.example.duan1.model.Response;
import com.example.duan1.services.ApiServices;
import com.example.duan1.utils.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class DatHangActivity extends AppCompatActivity {
    private TextInputEditText edtReceiverName, edtReceiverPhone, edtReceiverAddress;
    private TextView tvTotalPrice;
    private Button btnConfirmOrder;
    private ApiServices apiServices;
    private SharedPreferences sharedPreferences;
    private String userId;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);

        apiServices = RetrofitClient.getInstance().getApiServices();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userId = sharedPreferences.getString("id_taikhoan", "");

        // Ánh xạ views
        edtReceiverName = findViewById(R.id.edtReceiverName);
        edtReceiverPhone = findViewById(R.id.edtReceiverPhone);
        edtReceiverAddress = findViewById(R.id.edtReceiverAddress);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Load thông tin user nếu có
        String userName = sharedPreferences.getString("name", "");
        String userPhone = sharedPreferences.getString("sdt", "");
        
        if (!userName.isEmpty()) {
            edtReceiverName.setText(userName);
        }
        if (!userPhone.isEmpty()) {
            edtReceiverPhone.setText(userPhone);
        }

        // Tính tổng tiền từ giỏ hàng
        loadCartAndCalculateTotal();

        // Xử lý đặt hàng
        btnConfirmOrder.setOnClickListener(v -> {
            if (validateInput()) {
                createOrder();
            }
        });
    }

    private void loadCartAndCalculateTotal() {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt hàng", Toast.LENGTH_SHORT).show();
            tvTotalPrice.setText("0đ");
            return;
        }

        apiServices.getCartItems(userId).enqueue(new Callback<Response<List<CartItem>>>() {
            @Override
            public void onResponse(Call<Response<List<CartItem>>> call, retrofit2.Response<Response<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<List<CartItem>> res = response.body();
                    if (res.isSuccess() && res.getData() != null) {
                        List<CartItem> cartItems = res.getData();
                        totalPrice = 0;
                        for (CartItem item : cartItems) {
                            totalPrice += item.getSubtotal();
                        }
                        updateTotalPriceDisplay();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<CartItem>>> call, Throwable t) {
                Log.e("API Error", t.toString());
            }
        });
    }

    private void updateTotalPriceDisplay() {
        long totalLong = (long) totalPrice;
        if (totalLong >= 1000) {
            long thousands = totalLong / 1000;
            tvTotalPrice.setText(String.format("%,d.000đ", thousands).replace(",", "."));
        } else {
            tvTotalPrice.setText(String.format("%d.000đ", totalLong));
        }
    }

    private boolean validateInput() {
        String name = edtReceiverName.getText().toString().trim();
        String phone = edtReceiverPhone.getText().toString().trim();
        String address = edtReceiverAddress.getText().toString().trim();

        if (name.isEmpty()) {
            edtReceiverName.setError("Vui lòng nhập họ tên");
            return false;
        }

        if (phone.isEmpty()) {
            edtReceiverPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        if (address.isEmpty()) {
            edtReceiverAddress.setError("Vui lòng nhập địa chỉ");
            return false;
        }

        return true;
    }

    private void createOrder() {
        String receiverName = edtReceiverName.getText().toString().trim();
        String receiverPhone = edtReceiverPhone.getText().toString().trim();
        String receiverAddress = edtReceiverAddress.getText().toString().trim();

        Map<String, String> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("receiver_name", receiverName);
        body.put("receiver_phone", receiverPhone);
        body.put("receiver_address", receiverAddress);

        btnConfirmOrder.setEnabled(false);
        btnConfirmOrder.setText("Đang xử lý...");

        apiServices.createOrder(body).enqueue(new Callback<Response<com.example.duan1.model.Order>>() {
            @Override
            public void onResponse(Call<Response<com.example.duan1.model.Order>> call, retrofit2.Response<Response<com.example.duan1.model.Order>> response) {
                btnConfirmOrder.setEnabled(true);
                btnConfirmOrder.setText("Xác nhận đặt hàng");

                if (response.isSuccessful() && response.body() != null) {
                    Response<com.example.duan1.model.Order> res = response.body();
                    if (res.isSuccess()) {
                        Toast.makeText(DatHangActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        
                        // Chuyển về màn hình chính hoặc giỏ hàng
                        Intent intent = new Intent(DatHangActivity.this, ManchinhAdmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DatHangActivity.this, "Đặt hàng thất bại: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DatHangActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<com.example.duan1.model.Order>> call, Throwable t) {
                btnConfirmOrder.setEnabled(true);
                btnConfirmOrder.setText("Xác nhận đặt hàng");
                Toast.makeText(DatHangActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.toString());
            }
        });
    }

    public void onBackClick(View view) {
        finish();
    }
}

