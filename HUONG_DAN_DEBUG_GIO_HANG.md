# 🔍 Hướng dẫn Debug - Không thêm được vào giỏ hàng

## ✅ Các bước kiểm tra

### 1. Kiểm tra đăng nhập
- **Vấn đề**: Nếu chưa đăng nhập, không thể thêm vào giỏ hàng
- **Cách kiểm tra**: 
  - Mở app → Đăng nhập
  - Kiểm tra trong Logcat: `Cart Debug` → Xem `UserId` có giá trị không

### 2. Kiểm tra Server đang chạy
- **Vấn đề**: Server không chạy hoặc không kết nối được
- **Cách kiểm tra**:
  ```powershell
  # Kiểm tra port 3000
  netstat -ano | findstr :3000
  ```
- **Test API trong browser**:
  ```
  http://localhost:3000/api/products
  ```
  Nếu thấy JSON response → Server đang chạy ✓

### 3. Kiểm tra IP kết nối
- **Android Emulator**: IP phải là `http://10.0.2.2:3000/`
- **Thiết bị thật**: IP phải là IP thật của máy (ví dụ: `http://192.168.2.44:3000/`)
- **File cấu hình**: `app/src/main/java/com/example/duan1/services/ApiServices.java`
  ```java
  String Url = "http://10.0.2.2:3000/";  // Cho emulator
  ```

### 4. Kiểm tra Log trong Android Studio
- Mở **Logcat** trong Android Studio
- Lọc theo tag: `Cart Debug` hoặc `Cart Error`
- Khi nhấn "Thêm vào giỏ", xem log:
  ```
  Cart Debug: Adding to cart - UserId: xxx, ProductId: xxx
  Cart Debug: Response success: true/false, Message: xxx
  ```

### 5. Kiểm tra MongoDB
- **Vấn đề**: MongoDB không chạy
- **Cách kiểm tra**:
  ```powershell
  net start MongoDB
  ```
- Nếu lỗi, khởi động lại MongoDB

### 6. Test API trực tiếp
- **Dùng Postman hoặc curl**:
  ```bash
  POST http://localhost:3000/api/cart/add
  Content-Type: application/json
  
  {
    "user_id": "YOUR_USER_ID",
    "product_id": "YOUR_PRODUCT_ID",
    "quantity": 1
  }
  ```
- Nếu API trả về lỗi → Vấn đề ở server
- Nếu API thành công → Vấn đề ở Android app

## 🐛 Các lỗi thường gặp

### Lỗi 1: "Vui lòng đăng nhập để thêm vào giỏ hàng"
- **Nguyên nhân**: `userId` rỗng hoặc null
- **Giải pháp**: 
  1. Đăng nhập lại
  2. Kiểm tra SharedPreferences có lưu `id_taikhoan` không
  3. Xem log: `Cart Error: UserId is empty or null`

### Lỗi 2: "Lỗi kết nối"
- **Nguyên nhân**: Không kết nối được server
- **Giải pháp**:
  1. Kiểm tra server đang chạy: `netstat -ano | findstr :3000`
  2. Kiểm tra IP trong `ApiServices.java`
  3. Kiểm tra Firewall có chặn port 3000 không
  4. Xem log: `Cart Error: Network error`

### Lỗi 3: "Thêm vào giỏ hàng thất bại: Thiếu dữ liệu"
- **Nguyên nhân**: Server không nhận được đủ dữ liệu
- **Giải pháp**:
  1. Kiểm tra log: `Cart Debug: Adding to cart - UserId: xxx, ProductId: xxx`
  2. Đảm bảo `product.getId()` không null
  3. Kiểm tra request body trong log

### Lỗi 4: "Thêm vào giỏ hàng thất bại: Không tìm thấy sản phẩm"
- **Nguyên nhân**: `product_id` không tồn tại trong database
- **Giải pháp**:
  1. Kiểm tra sản phẩm có trong database không
  2. Kiểm tra `product.getId()` có đúng format MongoDB ObjectId không

## 🔧 Cách sửa nhanh

### Sửa 1: Rebuild app
```powershell
.\REBUILD_APP_NGAY.bat
```

### Sửa 2: Khởi động lại server
```powershell
# Dừng server (Ctrl+C)
# Khởi động lại
cd MongoDBSever
npm start
```

### Sửa 3: Clear cache và rebuild
1. Trong Android Studio: **Build** → **Clean Project**
2. Sau đó: **Build** → **Rebuild Project**

## 📝 Checklist Debug

- [ ] Đã đăng nhập thành công
- [ ] Server đang chạy (port 3000)
- [ ] MongoDB đang chạy
- [ ] IP đúng trong `ApiServices.java`
- [ ] Test API thành công trong browser/Postman
- [ ] Xem log trong Logcat khi thêm vào giỏ
- [ ] `userId` không rỗng
- [ ] `product.getId()` không null

## 🚀 Test nhanh

1. **Mở app** → Đăng nhập
2. **Vào màn hình sản phẩm**
3. **Nhấn "Thêm vào giỏ"** trên một sản phẩm
4. **Xem Logcat**:
   - Nếu thấy `Cart Debug: Successfully added to cart` → Thành công ✓
   - Nếu thấy `Cart Error` → Xem thông báo lỗi cụ thể

## 📞 Thông tin Debug

Sau khi thêm vào giỏ, log sẽ hiển thị:
- `Cart Debug: Adding to cart - UserId: xxx, ProductId: xxx`
- `Cart Debug: Response success: true/false, Message: xxx`
- `Cart Error: ...` (nếu có lỗi)

**Lưu ý**: Luôn kiểm tra Logcat để biết lỗi cụ thể!

