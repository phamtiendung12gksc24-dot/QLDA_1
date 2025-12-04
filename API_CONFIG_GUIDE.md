# Hướng dẫn cấu hình API cho Android App

## 📋 Các bước kết nối API

### 1. Kiểm tra IP của máy chạy server

**Windows:**
```bash
ipconfig
# Tìm "IPv4 Address" trong phần WiFi hoặc Ethernet adapter
```

**Linux/Mac:**
```bash
ifconfig
# hoặc
ip addr
```

### 2. Cập nhật IP trong Android App

1. Mở file: `app/src/main/java/com/example/duan1/services/ApiServices.java`
2. Tìm dòng: `String Url = "http://10.24.28.88:3000/";`
3. Thay đổi IP thành IP của máy bạn (ví dụ: `http://192.168.1.100:3000/`)

### 3. Khởi động MongoDB Server

```bash
cd MongoDBSever
npm install  # (chỉ cần chạy lần đầu)
npm start
# hoặc
node ./bin/www
```

Server sẽ chạy trên port **3000**

### 4. Đảm bảo cùng mạng WiFi

- ✅ Android device và máy chạy server phải **cùng mạng WiFi**
- ✅ Tắt firewall hoặc cho phép port 3000 trong firewall
- ✅ Kiểm tra server đang chạy: Mở trình duyệt và vào `http://localhost:3000`

### 5. Test kết nối

Khi chạy app Android, bạn sẽ thấy logs trong Logcat với tag `RetrofitClient`:
- API requests được gửi đi
- API responses nhận được
- Lỗi kết nối (nếu có)

## 🔧 Troubleshooting

### Lỗi "Connection refused" hoặc "Failed to connect"
- ✅ Kiểm tra server có đang chạy không
- ✅ Kiểm tra IP trong `ApiServices.java` có đúng không
- ✅ Đảm bảo Android và Server cùng mạng WiFi
- ✅ Kiểm tra firewall có chặn port 3000 không

### Lỗi "Timeout"
- ✅ Kiểm tra server có phản hồi không (mở trình duyệt vào `http://YOUR_IP:3000`)
- ✅ Tăng timeout trong `RetrofitClient.java` nếu cần

### Không thấy dữ liệu
- ✅ Kiểm tra Logcat để xem API response
- ✅ Kiểm tra database có dữ liệu không
- ✅ Kiểm tra API endpoints có đúng không

## 📝 Các API đã được cấu hình

### Authentication
- `POST /api/register` - Đăng ký
- `POST /api/login` - Đăng nhập

### Products
- `GET /api/products` - Lấy tất cả sản phẩm
- `GET /api/products/{id}` - Lấy sản phẩm theo ID
- `GET /api/products/search/{keyword}` - Tìm kiếm sản phẩm

### Orders
- `GET /api/orders` - Lấy tất cả đơn hàng
- `GET /api/orders/user/{userId}` - Lấy đơn hàng theo user
- `GET /api/orders/undelivered` - Lấy đơn hàng chưa giao
- `GET /api/orders/{id}` - Lấy chi tiết đơn hàng

### Statistics
- `GET /api/statistics/products/total` - Tổng số sản phẩm
- `GET /api/statistics/products/top5` - Top 5 sản phẩm bán chạy
- `GET /api/statistics/orders/rate` - Tỉ lệ đơn hàng
- `GET /api/statistics/revenue/today` - Doanh thu hôm nay

## 💡 Lưu ý

- IP đã được cấu hình mặc định: `http://10.24.28.88:3000/`
- Nếu thay đổi IP, nhớ cập nhật lại trong `ApiServices.java`
- Logs sẽ hiển thị trong Logcat với tag `RetrofitClient`
- App hỗ trợ HTTP (cleartext traffic) đã được bật trong AndroidManifest


