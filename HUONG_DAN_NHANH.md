# ✅ Hướng Dẫn Nhanh - Server Đang Chạy

## 🎯 Tình trạng: SERVER ĐÃ CHẠY!

**"Port 3000 is already in use"** = Server đang chạy rồi! ✓

**KHÔNG CẦN** khởi động lại, server đã sẵn sàng!

---

## 📝 Nếu muốn khởi động lại server

### Bước 1: Dừng server cũ

**Cách 1: Dùng file batch**
```
Double-click: DUNG_SERVER.bat
```

**Cách 2: Dùng PowerShell**
```powershell
Get-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess | Stop-Process -Force
```

**Cách 3: Dùng lệnh đơn giản**
```powershell
taskkill /PID 19208 /F
```

### Bước 2: Khởi động lại server
```powershell
cd MongoDBSever
npm start
```

---

## ✅ Kiểm tra server

### Server đang chạy nếu thấy:
```
Port 3000 is already in use
```

### Test server trong browser:
```
http://localhost:3000/api/products
```

---

## 🚀 Lệnh nhanh nhất

### Khởi động lại server (all-in-one):
```powershell
# Dừng server cũ
taskkill /F /IM node.exe

# Đợi 1 giây
Start-Sleep -Seconds 1

# Khởi động lại
cd MongoDBSever
npm start
```

---

## 📱 Kết nối từ Android

- **IP cho emulator:** `http://10.0.2.2:3000/` (đã cấu hình)
- Server đang chạy → App có thể kết nối ngay!

---

## 💡 Lưu ý

- **KHÔNG CẦN** khởi động lại nếu server đã chạy
- Port 3000 đã được sử dụng = Server OK ✓
- Chỉ khởi động lại khi có lỗi hoặc thay đổi code backend

