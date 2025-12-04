# 🔌 LỆNH KẾT NỐI SERVER

## ⚡ Lệnh nhanh nhất

### Trong PowerShell/CMD:
```bash
cd MongoDBSever
npm start
```

### Hoặc double-click file:
**`CHAY_SERVER.bat`** (trong thư mục gốc)

---

## 📋 Các bước chi tiết

### Bước 1: Khởi động MongoDB
```bash
net start MongoDB
```

### Bước 2: Vào thư mục MongoDBSever
```bash
cd MongoDBSever
```

### Bước 3: Khởi động Server
```bash
npm start
```

---

## ✅ Kiểm tra server đang chạy

Mở browser vào:
```
http://localhost:3000/api/products
```

Nếu thấy JSON response → Server đang chạy ✓

---

## 📱 Cấu hình IP cho Android

### Nếu dùng **Android Emulator**:
- IP: `http://10.0.2.2:3000/`
- ✅ Đã được cấu hình sẵn trong `ApiServices.java`

### Nếu dùng **thiết bị thật**:
- IP: `http://192.168.2.44:3000/`
- Cần đổi trong: `app/src/main/java/com/example/duan1/services/ApiServices.java`

---

## 🐛 Lỗi thường gặp

### Lỗi "package.json not found":
→ Bạn đang ở sai thư mục. Cần vào thư mục `MongoDBSever`:
```bash
cd MongoDBSever
```

### Lỗi "MongoDB not running":
→ Khởi động MongoDB:
```bash
net start MongoDB
```

### Lỗi "Port 3000 already in use":
→ Đóng process đang dùng port 3000:
```bash
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

---

## 🚀 Quick Commands

```bash
# 1. Khởi động MongoDB
net start MongoDB

# 2. Khởi động Server (từ thư mục gốc)
cd MongoDBSever && npm start
```

---

## 📝 Checklist

- [ ] MongoDB đang chạy (`net start MongoDB`)
- [ ] Đã vào thư mục `MongoDBSever`
- [ ] Server đang chạy (`npm start`)
- [ ] Test thành công: `http://localhost:3000/api/products`
- [ ] IP đúng trong `ApiServices.java`
