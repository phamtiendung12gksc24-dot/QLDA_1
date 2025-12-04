package com.example.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManchinhAdmin extends AppCompatActivity {
    TextView tvTenKhachHang, tvSuaThongTin;
    Button btnXemSanPham, btnGioHang, btnLichSuDonHang, btnDatHang, btnTaiKhoan, btnLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manchinh_admin);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        tvTenKhachHang = findViewById(R.id.tvTenKhachHang);
        tvSuaThongTin = findViewById(R.id.tvSuaThongTin);
        btnXemSanPham = findViewById(R.id.btnXemSanPham);
        btnGioHang = findViewById(R.id.btnGioHang);
        btnLichSuDonHang = findViewById(R.id.btnLichSuDonHang);
        btnDatHang = findViewById(R.id.btnDatHang);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnLogin = findViewById(R.id.btnLogin);
        
        // Kiểm tra null
        if (btnGioHang == null) {
            Log.e("ManchinhAdmin", "btnGioHang is null!");
        }
        if (btnDatHang == null) {
            Log.e("ManchinhAdmin", "btnDatHang is null!");
        }

        // Lấy thông tin người dùng từ SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String tenKhachHang = sharedPreferences.getString("name", "Khách hàng");
        tvTenKhachHang.setText("Xin chào, " + tenKhachHang);

        // Xử lý sự kiện click các nút
        btnXemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManchinhAdmin.this, XemSanPham.class);
                startActivity(intent);
            }
        });

        if (btnGioHang != null) {
            btnGioHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("ManchinhAdmin", "Button Giỏ hàng clicked");
                        Intent intent = new Intent(ManchinhAdmin.this, GioHangActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("ManchinhAdmin", "Error opening GioHangActivity", e);
                        Toast.makeText(ManchinhAdmin.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Log.e("ManchinhAdmin", "btnGioHang is null, cannot set listener!");
        }

        btnLichSuDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManchinhAdmin.this, "Lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
                // TODO: Chuyển đến màn hình lịch sử đơn hàng
            }
        });

        if (btnDatHang != null) {
            btnDatHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("ManchinhAdmin", "Button Đặt hàng clicked");
                        Intent intent = new Intent(ManchinhAdmin.this, DatHangActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("ManchinhAdmin", "Error opening DatHangActivity", e);
                        Toast.makeText(ManchinhAdmin.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Log.e("ManchinhAdmin", "btnDatHang is null, cannot set listener!");
        }

        btnTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManchinhAdmin.this, "Tài khoản", Toast.LENGTH_SHORT).show();
                // TODO: Chuyển đến màn hình tài khoản
            }
        });

        tvSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManchinhAdmin.this, "Sửa thông tin tài khoản", Toast.LENGTH_SHORT).show();
                // TODO: Chuyển đến màn hình sửa thông tin
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng nhập
                Intent intent = new Intent(ManchinhAdmin.this, Dangnhap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
