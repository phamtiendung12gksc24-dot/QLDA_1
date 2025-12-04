# 🛒 Hướng dẫn sử dụng Giỏ hàng và Đặt hàng

## ✅ Các chức năng đã có

### 1. Thêm sản phẩm vào giỏ hàng
- **Màn hình:** Xem sản phẩm (`XemSanPham`)
- **Cách dùng:** Click button "Thêm vào giỏ" trên mỗi sản phẩm
- **Chức năng:** 
  - Gọi API để thêm sản phẩm vào giỏ hàng
  - Hiển thị thông báo thành công/thất bại
  - Cần đăng nhập để thêm vào giỏ hàng

### 2. Xem giỏ hàng
- **Màn hình:** Giỏ hàng (`GioHangActivity`)
- **Cách vào:** Click button "Giỏ hàng" từ màn hình Admin
- **Chức năng:**
  - Hiển thị danh sách sản phẩm trong giỏ hàng
  - Tăng/giảm số lượng sản phẩm
  - Xóa sản phẩm khỏi giỏ hàng
  - Hiển thị tổng tiền
  - Button "Đặt hàng" để chuyển đến màn hình đặt hàng

### 3. Đặt hàng
- **Màn hình:** Đặt hàng (`DatHangActivity`)
- **Cách vào:** 
  - Click button "Đặt hàng" từ màn hình Admin
  - Hoặc click button "Đặt hàng" từ màn hình Giỏ hàng
- **Chức năng:**
  - Nhập thông tin người nhận (tên, SĐT, địa chỉ)
  - Hiển thị tổng tiền từ giỏ hàng
  - Xác nhận đặt hàng → Tạo đơn hàng từ giỏ hàng
  - Sau khi đặt hàng thành công → Chuyển về màn hình chính

## 📋 Quy trình đầy đủ

### Bước 1: Đăng nhập
1. Mở app
2. Đăng nhập với email và password
3. Sau khi đăng nhập thành công → Vào màn hình Admin

### Bước 2: Xem sản phẩm
1. Click "Xem sản phẩm" từ màn hình Admin
2. Xem danh sách sản phẩm
3. (Tùy chọn) Tìm kiếm sản phẩm

### Bước 3: Thêm vào giỏ hàng
1. Click button "Thêm vào giỏ" trên sản phẩm muốn mua
2. Thấy thông báo "Đã thêm [tên sản phẩm] vào giỏ hàng"
3. Lặp lại cho các sản phẩm khác

### Bước 4: Xem giỏ hàng
1. Click "Giỏ hàng" từ màn hình Admin
2. Xem danh sách sản phẩm đã thêm
3. (Tùy chọn) Tăng/giảm số lượng hoặc xóa sản phẩm

### Bước 5: Đặt hàng
1. Từ giỏ hàng, click "Đặt hàng"
2. Nhập thông tin:
   - Họ và tên người nhận
   - Số điện thoại
   - Địa chỉ nhận hàng
3. Kiểm tra tổng tiền
4. Click "Xác nhận đặt hàng"
5. Thấy thông báo "Đặt hàng thành công!"
6. Tự động chuyển về màn hình chính

## 🔧 Kiểm tra hoạt động

### 1. Kiểm tra API
- Server phải đang chạy trên port 3000
- Kiểm tra: `http://localhost:3000/api/products`

### 2. Kiểm tra đăng nhập
- Phải đăng nhập trước khi thêm vào giỏ hàng
- User ID được lưu trong SharedPreferences với key `id_taikhoan`

### 3. Kiểm tra kết nối
- IP trong `ApiServices.java` phải đúng:
  - Emulator: `http://10.0.2.2:3000/`
  - Thiết bị thật: IP WiFi của máy

## ⚠️ Lưu ý

1. **Phải đăng nhập** trước khi sử dụng giỏ hàng
2. **Server phải chạy** để các API hoạt động
3. **Rebuild app** sau khi thay đổi code

## 📱 Files liên quan

- `XemSanPham.java` - Màn hình xem sản phẩm và thêm vào giỏ
- `GioHangActivity.java` - Màn hình giỏ hàng
- `DatHangActivity.java` - Màn hình đặt hàng
- `CartAdapter.java` - Adapter cho RecyclerView giỏ hàng
- `ProductAdapter.java` - Adapter cho RecyclerView sản phẩm

## 🚀 API Endpoints

- `GET /api/cart/{userId}` - Lấy giỏ hàng
- `POST /api/cart/add` - Thêm vào giỏ hàng
- `PUT /api/cart/update/{cartItemId}` - Cập nhật số lượng
- `DELETE /api/cart/remove/{cartItemId}` - Xóa khỏi giỏ hàng
- `POST /api/orders/create` - Tạo đơn hàng

