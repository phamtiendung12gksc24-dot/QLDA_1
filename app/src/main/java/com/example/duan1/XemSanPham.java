package com.example.duan1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.duan1.model.Product;
import com.example.duan1.model.Response;
import com.example.duan1.services.ApiServices;
import com.example.duan1.utils.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class XemSanPham extends AppCompatActivity {
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextInputEditText edtSearch;
    private TextView tvQuantity;
    private Button btnMinus, btnPlus;
    private int quantity = 0;
    private LinearLayout navTrangChu, navBoSuuTap, navTaiKhoan, navDonHang, navMenu;
    private ApiServices apiServices;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_san_pham);

        // Khởi tạo Retrofit
        apiServices = RetrofitClient.getInstance().getApiServices();
        
        // Lấy user ID
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userId = sharedPreferences.getString("id_taikhoan", "");
        
        // Ánh xạ các view
        rvProducts = findViewById(R.id.rvProducts);
        edtSearch = findViewById(R.id.edtSearch);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        
        // Bottom navigation
        navTrangChu = findViewById(R.id.navTrangChu);
        navBoSuuTap = findViewById(R.id.navBoSuuTap);
        navTaiKhoan = findViewById(R.id.navTaiKhoan);
        navDonHang = findViewById(R.id.navDonHang);
        navMenu = findViewById(R.id.navMenu);

        // Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();

        // Setup RecyclerView
        productAdapter = new ProductAdapter(this, productList);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(productAdapter);

        // Load sản phẩm từ API
        loadProducts();

        // Xử lý tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    loadProducts();
                } else {
                    searchProducts(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Xử lý thêm vào giỏ hàng
        productAdapter.setOnAddToCartClickListener(product -> {
            addToCart(product);
        });

        // Xử lý bottom navigation
        navTrangChu.setOnClickListener(v -> {
            finish();
        });

        navBoSuuTap.setOnClickListener(v -> {
            Toast.makeText(this, "Bộ sưu tập", Toast.LENGTH_SHORT).show();
        });

        navTaiKhoan.setOnClickListener(v -> {
            Toast.makeText(this, "Tài khoản", Toast.LENGTH_SHORT).show();
        });

        navDonHang.setOnClickListener(v -> {
            Toast.makeText(this, "Đơn hàng", Toast.LENGTH_SHORT).show();
        });

        navMenu.setOnClickListener(v -> {
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProducts() {
        apiServices.getProducts().enqueue(new Callback<Response<List<Product>>>() {
            @Override
            public void onResponse(Call<Response<List<Product>>> call, retrofit2.Response<Response<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<List<Product>> res = response.body();
                    if (res.isSuccess() && res.getData() != null) {
                        productList.clear();
                        productList.addAll(res.getData());
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(XemSanPham.this, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(XemSanPham.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Response<List<Product>>> call, Throwable t) {
                Toast.makeText(XemSanPham.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.toString());
            }
        });
    }

    private void searchProducts(String keyword) {
        apiServices.searchProducts(keyword).enqueue(new Callback<Response<List<Product>>>() {
            @Override
            public void onResponse(Call<Response<List<Product>>> call, retrofit2.Response<Response<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<List<Product>> res = response.body();
                    if (res.isSuccess() && res.getData() != null) {
                        productList.clear();
                        productList.addAll(res.getData());
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<Product>>> call, Throwable t) {
                Log.e("API Error", t.toString());
            }
        });
    }

    private void addToCart(Product product) {
        // Kiểm tra userId
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            Log.e("Cart Error", "UserId is empty or null");
            return;
        }

        // Kiểm tra product ID
        if (product == null || product.getId() == null || product.getId().isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            Log.e("Cart Error", "Product ID is empty or null");
            return;
        }

        Log.d("Cart Debug", "Adding to cart - UserId: " + userId + ", ProductId: " + product.getId());

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("product_id", product.getId());
        body.put("quantity", 1); // Mặc định thêm 1 sản phẩm

        apiServices.addToCart(body).enqueue(new Callback<Response<CartItem>>() {
            @Override
            public void onResponse(Call<Response<CartItem>> call, retrofit2.Response<Response<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<CartItem> res = response.body();
                    Log.d("Cart Debug", "Response success: " + res.isSuccess() + ", Message: " + res.getMessage());
                    
                    if (res.isSuccess()) {
                        Toast.makeText(XemSanPham.this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        Log.d("Cart Debug", "Successfully added to cart");
                    } else {
                        String errorMsg = res.getMessage() != null ? res.getMessage() : "Không xác định";
                        Toast.makeText(XemSanPham.this, "Thêm vào giỏ hàng thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("Cart Error", "Add to cart failed: " + errorMsg);
                    }
                } else {
                    String errorMsg = "Lỗi từ server";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                            Log.e("Cart Error", "Error body: " + errorMsg);
                        } catch (Exception e) {
                            Log.e("Cart Error", "Cannot read error body: " + e.getMessage());
                        }
                    }
                    Toast.makeText(XemSanPham.this, "Thêm vào giỏ hàng thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("Cart Error", "Response not successful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Response<CartItem>> call, Throwable t) {
                String errorMsg = t.getMessage() != null ? t.getMessage() : "Lỗi kết nối";
                Toast.makeText(XemSanPham.this, "Lỗi kết nối: " + errorMsg, Toast.LENGTH_LONG).show();
                Log.e("Cart Error", "Network error: " + t.toString());
                t.printStackTrace();
            }
        });
    }
}
