package com.example.duan1;

import android.content.Intent;
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
        Log.d("Cart Debug", "UserId loaded in onCreate: " + (userId != null && !userId.isEmpty() ? userId : "EMPTY"));
        
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

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại userId mỗi khi activity được resume (để cập nhật sau khi đăng nhập)
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        }
        userId = sharedPreferences.getString("id_taikhoan", "");
        Log.d("Cart Debug", "UserId reloaded in onResume: " + (userId != null && !userId.isEmpty() ? userId : "EMPTY"));
        
        // Debug: Kiểm tra tất cả các key trong SharedPreferences
        if (userId == null || userId.isEmpty()) {
            Log.e("Cart Debug", "All SharedPreferences keys: " + sharedPreferences.getAll().toString());
        }
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
        // Load lại userId để đảm bảo có giá trị mới nhất
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        }
        userId = sharedPreferences.getString("id_taikhoan", "");
        
        // Debug: Log tất cả SharedPreferences để kiểm tra
        Log.d("Cart Debug", "Current userId: " + (userId != null ? userId : "NULL"));
        Log.d("Cart Debug", "All SharedPreferences: " + sharedPreferences.getAll().toString());
        
        // Kiểm tra userId
        if (userId == null || userId.isEmpty() || userId.trim().isEmpty()) {
            Log.e("Cart Error", "UserId is empty or null - User needs to login");
            Log.e("Cart Error", "Available keys in SharedPreferences: " + sharedPreferences.getAll().keySet().toString());
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            // Mở màn hình đăng nhập
            try {
                Intent intent = new Intent(this, Dangnhap.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("Cart Error", "Cannot open login screen: " + e.getMessage());
            }
            return;
        }
        
        // Trim userId để loại bỏ khoảng trắng
        userId = userId.trim();
        Log.d("Cart Debug", "Using userId (trimmed): " + userId);

        // Kiểm tra product ID
        if (product == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            Log.e("Cart Error", "Product is null");
            return;
        }
        
        String productId = product.getId();
        if (productId == null || productId.isEmpty() || productId.trim().isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID sản phẩm", Toast.LENGTH_SHORT).show();
            Log.e("Cart Error", "Product ID is empty or null");
            return;
        }

        Log.d("Cart Debug", "Adding to cart - UserId: " + userId + ", ProductId: " + productId + ", ProductName: " + product.getName());

        // Tạo request body
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId.trim());
        body.put("product_id", productId.trim());
        body.put("quantity", 1); // Mặc định thêm 1 sản phẩm

        Log.d("Cart Debug", "Request body: " + body.toString());

        apiServices.addToCart(body).enqueue(new Callback<Response<CartItem>>() {
            @Override
            public void onResponse(Call<Response<CartItem>> call, retrofit2.Response<Response<CartItem>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Response<CartItem> res = response.body();
                        Log.d("Cart Debug", "Response success: " + res.isSuccess() + ", Message: " + res.getMessage());
                        
                        if (res.isSuccess()) {
                            String successMsg = "Đã thêm " + product.getName() + " vào giỏ hàng";
                            Toast.makeText(XemSanPham.this, successMsg, Toast.LENGTH_SHORT).show();
                            Log.d("Cart Debug", "Successfully added to cart: " + successMsg);
                        } else {
                            String errorMsg = res.getMessage() != null ? res.getMessage() : (res.getMessenger() != null ? res.getMessenger() : "Không xác định");
                            Toast.makeText(XemSanPham.this, "Thêm vào giỏ hàng thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                            Log.e("Cart Error", "Add to cart failed - Success: " + res.isSuccess() + ", Message: " + errorMsg);
                        }
                    } else {
                        Toast.makeText(XemSanPham.this, "Thêm vào giỏ hàng thất bại: Response body is null", Toast.LENGTH_LONG).show();
                        Log.e("Cart Error", "Response body is null");
                    }
                } else {
                    String errorMsg = "Lỗi từ server (Code: " + response.code() + ")";
                    if (response.errorBody() != null) {
                        try {
                            String errorBodyStr = response.errorBody().string();
                            Log.e("Cart Error", "Error body: " + errorBodyStr);
                            errorMsg = errorBodyStr.length() > 100 ? errorBodyStr.substring(0, 100) + "..." : errorBodyStr;
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
                
                // Xử lý lỗi JSON parsing
                if (t instanceof IllegalStateException && errorMsg.contains("Not a JSON Object")) {
                    errorMsg = "Lỗi định dạng dữ liệu từ server. Vui lòng thử lại.";
                    Log.e("Cart Error", "JSON parsing error: " + t.getMessage());
                }
                
                Toast.makeText(XemSanPham.this, "Lỗi kết nối: " + errorMsg, Toast.LENGTH_LONG).show();
                Log.e("Cart Error", "Network error: " + t.toString());
                t.printStackTrace();
            }
        });
    }
}
