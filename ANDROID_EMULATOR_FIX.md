# 🔧 Sửa lỗi kết nối API với Android Emulator

## ⚠️ Vấn đề

Bạn đang gặp lỗi:
```
Lỗi kết nối: failed to connect to /192.168.0.114 (port 3000) 
from /10.0.2.16 (port 50716) after 10000ms
```

**Nguyên nhân:**
- App đang chạy trên **Android Emulator** (`10.0.2.16` là IP của emulator)
- Emulator không thể kết nối trực tiếp tới IP thật của máy bằng cách thông thường
- IP `192.168.0.114` trong code cũ không còn đúng

## ✅ Giải pháp

### Cách 1: Dùng IP đặc biệt cho Emulator (Đã cập nhật tự động)

Android Emulator có một IP đặc biệt để kết nối tới localhost của máy host:
- **`10.0.2.2`** = localhost của máy chạy emulator

**Đã cập nhật trong `ApiServices.java`:**
```java
String Url = "http://10.0.2.2:3000/";  // IP đặc biệt cho Android Emulator
```

### Cách 2: Dùng thiết bị thật

Nếu muốn test trên thiết bị Android thật:

1. **Kết nối điện thoại và máy tính cùng mạng WiFi**
2. **Lấy IP của máy tính:**
   ```bash
   ipconfig  # Windows
   # Tìm IPv4 Address (ví dụ: 10.24.28.88)
   ```

3. **Cập nhật IP trong `ApiServices.java`:**
   ```java
   String Url = "http://10.24.28.88:3000/";  // IP thật của máy
   ```

## 🚀 Các bước thực hiện

### Bước 1: Rebuild App

Sau khi thay đổi IP, bạn **PHẢI rebuild app**:

**Trong Android Studio:**
1. Build → Clean Project
2. Build → Rebuild Project
3. Chạy lại app (Run)

**Hoặc dùng command line:**
```bash
cd app
./gradlew clean
./gradlew assembleDebug
```

### Bước 2: Khởi động Server

Đảm bảo server đang chạy trên localhost:

```bash
cd MongoDBSever
npm start
```

Server sẽ chạy trên `http://localhost:3000`

### Bước 3: Test kết nối

1. **Kiểm tra server:** Mở browser vào `http://localhost:3000/api/products`
2. **Chạy app Android** và kiểm tra Logcat

## 📝 Lưu ý quan trọng

### Khi dùng Emulator:
- ✅ Dùng `10.0.2.2` để kết nối tới localhost của máy
- ✅ Server phải chạy trên `localhost:3000` (không cần IP thật)
- ✅ Không cần cùng mạng WiFi

### Khi dùng thiết bị thật:
- ✅ Dùng IP thật của máy (lấy từ `ipconfig` hoặc `ifconfig`)
- ✅ Máy tính và điện thoại **PHẢI cùng mạng WiFi**
- ✅ Có thể cần tắt firewall tạm thời

## 🔍 Kiểm tra

Sau khi rebuild và chạy lại app, trong Logcat bạn sẽ thấy:

```
RetrofitClient: RetrofitClient initialized with base URL: http://10.0.2.2:3000/
```

Nếu vẫn lỗi, kiểm tra:
- ✅ Server có đang chạy không? (mở `http://localhost:3000` trong browser)
- ✅ App đã được rebuild chưa?
- ✅ Kiểm tra Logcat để xem request URL chính xác

## 🐛 Troubleshooting

### Vẫn lỗi sau khi rebuild:
1. **Xóa cache và rebuild:**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **Uninstall app cũ và cài lại:**
   - Trong Android Studio: Build → Clean Project
   - Xóa app trên emulator/thiết bị
   - Cài lại app mới

3. **Kiểm tra server:**
   ```bash
   # Kiểm tra server có chạy không
   netstat -ano | findstr :3000  # Windows
   lsof -i :3000  # Linux/Mac
   ```

### Lỗi "Connection refused":
- Server chưa khởi động hoặc đã dừng
- Khởi động lại server: `npm start` trong thư mục `MongoDBSever`

### Lỗi "Timeout":
- Server chậm phản hồi
- Kiểm tra MongoDB có đang chạy không
- Xem logs của server để biết lỗi cụ thể


