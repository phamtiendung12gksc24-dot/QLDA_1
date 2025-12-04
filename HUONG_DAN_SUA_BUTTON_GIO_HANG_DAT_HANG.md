# 🔧 Hướng dẫn sửa button "Giỏ hàng" và "Đặt hàng"

## ✅ Đã sửa

### 1. Button "Giỏ hàng"
- Đã có listener chuyển đến `GioHangActivity`
- Đã thêm error handling và log

### 2. Button "Đặt hàng"  
- Đã sửa để chuyển đến `DatHangActivity` (trước đó chỉ Toast)
- Đã thêm error handling và log

### 3. Error Handling
- Thêm try-catch để bắt lỗi
- Thêm log để debug
- Thêm kiểm tra null pointer

## ⚠️ QUAN TRỌNG: PHẢI REBUILD APP!

Sau khi sửa code, **PHẢI REBUILD APP**:

### Cách 1: Từ Android Studio
1. **Build → Clean Project** (đợi xong)
2. **Build → Rebuild Project** (đợi xong)
3. **Run → Run 'app'**

### Cách 2: Dùng file batch
Double-click: **`REBUILD_APP_NGAY.bat`**

### Cách 3: Xóa app cũ và cài lại
1. Xóa app trên emulator/thiết bị
2. Chạy lại từ Android Studio

## 🔍 Kiểm tra lỗi

### Nếu button vẫn không hoạt động:

1. **Mở Logcat** trong Android Studio
2. **Tìm lỗi** khi click button:
   - Tìm tag: `ManchinhAdmin`
   - Xem có log: "Button Giỏ hàng clicked" hoặc "Button Đặt hàng clicked"
   - Xem có lỗi nào không

3. **Kiểm tra Activities đã khai báo trong AndroidManifest:**
   - `GioHangActivity` ✓
   - `DatHangActivity` ✓

4. **Kiểm tra layout files tồn tại:**
   - `activity_gio_hang.xml` ✓
   - `activity_dat_hang.xml` ✓

## 📋 Code đã sửa

File: `app/src/main/java/com/example/duan1/ManchinhAdmin.java`

### Button "Giỏ hàng":
```java
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
            }
        }
    });
}
```

### Button "Đặt hàng":
```java
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
            }
        }
    });
}
```

## ✅ Checklist

- [x] Code đã sửa ✓
- [x] Error handling đã thêm ✓
- [x] Log đã thêm ✓
- [ ] **REBUILD APP** ← **LÀM NGAY!**
- [ ] Test lại các button
- [ ] Kiểm tra Logcat nếu có lỗi

## 🚀 Sau khi rebuild

1. Chạy app lại
2. Click button "Giỏ hàng" → Sẽ chuyển đến màn hình giỏ hàng
3. Click button "Đặt hàng" → Sẽ chuyển đến màn hình đặt hàng
4. Nếu có lỗi, xem Logcat để biết chi tiết

