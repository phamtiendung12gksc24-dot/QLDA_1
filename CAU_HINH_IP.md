# 📱 Cấu hình IP cho Android App

## ✅ IP hiện tại đã được cập nhật

**File:** `app/src/main/java/com/example/duan1/services/ApiServices.java`

**IP hiện tại:** `http://10.24.28.88:3000/`

## 📋 Hướng dẫn sử dụng

### 1. Nếu dùng thiết bị Android THẬT:

✅ **IP đã được cấu hình đúng:** `10.24.28.88`

**Yêu cầu:**
- ✅ Máy tính và điện thoại phải **cùng mạng WiFi**
- ✅ Server phải đang chạy trên máy tính
- ✅ IP của máy tính phải là `10.24.28.88` (kiểm tra bằng `ipconfig`)

### 2. Nếu dùng Android EMULATOR:

Nếu bạn chuyển sang dùng emulator, cần đổi IP thành:
```java
String Url = "http://10.0.2.2:3000/";  // IP đặc biệt cho emulator
```

## 🔧 Kiểm tra IP máy tính

**Windows:**
```bash
ipconfig
# Tìm "IPv4 Address" trong phần WiFi adapter
```

**Nếu IP khác với 10.24.28.88:**

1. Mở file: `app/src/main/java/com/example/duan1/services/ApiServices.java`
2. Tìm dòng: `String Url = "http://10.24.28.88:3000/";`
3. Thay đổi thành IP mới của bạn

## 🚀 Sau khi thay đổi IP

**PHẢI rebuild app:**

1. **File → Sync Project with Gradle Files**
2. **Build → Clean Project**
3. **Build → Rebuild Project**
4. Chạy lại app

Hoặc dùng script:
```powershell
.\rebuild-app.ps1
```

## ✅ Kiểm tra kết nối

1. **Đảm bảo server đang chạy:**
   ```bash
   cd MongoDBSever
   npm start
   ```

2. **Test server:** Mở browser vào `http://10.24.28.88:3000/api/products`

3. **Chạy app Android** và kiểm tra Logcat

## 🔍 Troubleshooting

### Không kết nối được:

- ✅ Kiểm tra máy tính và điện thoại **cùng mạng WiFi**
- ✅ Kiểm tra server có đang chạy không
- ✅ Kiểm tra IP máy tính có đúng `10.24.28.88` không
- ✅ Tắt firewall tạm thời để test
- ✅ Xem Logcat để biết lỗi cụ thể

### Lỗi "Connection refused":

- Server chưa khởi động hoặc đã dừng
- Khởi động lại: `cd MongoDBSever && npm start`

### Lỗi "Timeout":

- Máy tính và điện thoại không cùng mạng
- Firewall đang chặn port 3000
- Kiểm tra lại IP


