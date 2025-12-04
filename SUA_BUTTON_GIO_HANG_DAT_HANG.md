# ✅ Đã sửa button "Giỏ hàng" và "Đặt hàng"

## 🔧 Đã sửa

### Button "Đặt hàng"
- **Trước:** Chỉ hiển thị Toast
- **Sau:** Chuyển đến màn hình `DatHangActivity` ✓

### Button "Giỏ hàng"
- **Đã đúng:** Chuyển đến màn hình `GioHangActivity` ✓

## ⚠️ QUAN TRỌNG: Phải rebuild app!

Sau khi sửa code, **phải rebuild app** để áp dụng thay đổi:

### Cách 1: Từ Android Studio
1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. **Run → Run 'app'**

### Cách 2: Dùng file batch
Double-click: **`REBUILD_APP_NGAY.bat`**

### Cách 3: Xóa app cũ và cài lại
1. Xóa app trên emulator/thiết bị
2. Chạy lại từ Android Studio

## 📋 Code đã sửa

File: `app/src/main/java/com/example/duan1/ManchinhAdmin.java`

```java
// Button "Đặt hàng"
btnDatHang.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ManchinhAdmin.this, DatHangActivity.class);
        startActivity(intent);
    }
});

// Button "Giỏ hàng" - đã đúng từ trước
btnGioHang.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ManchinhAdmin.this, GioHangActivity.class);
        startActivity(intent);
    }
});
```

## ✅ Sau khi rebuild

1. Chạy app lại
2. Click button "Giỏ hàng" → Sẽ chuyển đến màn hình giỏ hàng
3. Click button "Đặt hàng" → Sẽ chuyển đến màn hình đặt hàng

## 🔍 Nếu vẫn không hoạt động

1. Kiểm tra Logcat xem có lỗi gì không
2. Đảm bảo đã rebuild app
3. Kiểm tra AndroidManifest.xml có khai báo Activities:
   - `GioHangActivity` ✓
   - `DatHangActivity` ✓

