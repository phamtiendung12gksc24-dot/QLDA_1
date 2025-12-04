# 🔌 Hướng dẫn kết nối Server - ĐƠN GIẢN

## ✅ TÌNH TRẠNG HIỆN TẠI

**Server đã đang chạy!** ✓
- Port 3000 đã được sử dụng
- MongoDB đã kết nối thành công
- Process ID: 19208

## 🚀 LỆNH KẾT NỐI SERVER

### Cách 1: Dùng file batch (Double-click)
```
CHAY_SERVER.bat
```
Hoặc trong PowerShell:
```powershell
.\CHAY_SERVER.bat
```

### Cách 2: Chạy lệnh trong PowerShell

**Lệnh đúng cho PowerShell:**
```powershell
cd MongoDBSever
npm start
```

**KHÔNG dùng `&&` trong PowerShell**, dùng `;` thay vào đó:
```powershell
cd MongoDBSever; npm start
```

### Cách 3: Chạy từng bước

**Bước 1: Vào thư mục**
```powershell
cd MongoDBSever
```

**Bước 2: Khởi động server**
```powershell
npm start
```

## ✅ KIỂM TRA SERVER

### 1. Kiểm tra port 3000:
```powershell
netstat -ano | Select-String ":3000"
```

### 2. Test API (mở browser):
```
http://localhost:3000/api/products
```

**LƯU Ý:** Đây là URL để mở trong **browser**, không phải lệnh PowerShell!

### 3. Test bằng PowerShell (nếu có curl):
```powershell
curl http://localhost:3000/api/products
```

Hoặc dùng Invoke-WebRequest:
```powershell
Invoke-WebRequest -Uri "http://localhost:3000/api/products"
```

## 📱 KẾT NỐI TỪ ANDROID

### Android Emulator:
- ✅ IP: `http://10.0.2.2:3000/`
- Đã được cấu hình sẵn trong `ApiServices.java`

### Thiết bị thật:
- IP: `http://192.168.2.44:3000/`
- Cần đổi trong `ApiServices.java`

## ⚠️ LỖI THƯỜNG GẶP

### "Port 3000 is already in use"
→ **Server đã chạy rồi!** Không cần khởi động lại.

Nếu muốn khởi động lại:
```powershell
# Tìm process
netstat -ano | Select-String ":3000"

# Kill process (thay 19208 bằng PID thực tế)
taskkill /PID 19208 /F

# Khởi động lại
cd MongoDBSever
npm start
```

### "Access is denied" khi start MongoDB
→ Cần chạy PowerShell/CMD với quyền Administrator

## 📝 TÓM TẮT

1. **Server đang chạy** - Port 3000 đã được sử dụng
2. **Test API**: Mở browser → `http://localhost:3000/api/products`
3. **Android app**: IP đã cấu hình `10.0.2.2:3000` cho emulator

