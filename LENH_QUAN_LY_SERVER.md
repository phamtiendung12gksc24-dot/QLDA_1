# 🔧 Lệnh Quản Lý Server

## ✅ Tình trạng hiện tại

**Server đang chạy!** Port 3000 đã được sử dụng.

## 📋 Các lệnh quản lý

### 1. Kiểm tra server có đang chạy:

```powershell
netstat -ano | Select-String ":3000"
```

### 2. Dừng server:

**Cách 1: Dùng file batch**
```
DUNG_SERVER.bat
```

**Cách 2: Dùng lệnh PowerShell**
```powershell
# Tìm process ID
$process = netstat -ano | Select-String ":3000" | Select-String "LISTENING"
$pid = ($process -split '\s+')[-1]
taskkill /PID $pid /F
```

**Cách 3: Dùng lệnh đơn giản**
```powershell
Get-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess | Stop-Process -Force
```

### 3. Khởi động server:

```powershell
cd MongoDBSever
npm start
```

### 4. Khởi động lại server:

**Cách 1: Dùng file batch**
```
QUAN_LY_SERVER.bat
# Chọn option [4] Khởi động lại server
```

**Cách 2: Dừng rồi khởi động lại**
```powershell
# Dừng server
Get-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess | Stop-Process -Force

# Đợi 2 giây
Start-Sleep -Seconds 2

# Khởi động lại
cd MongoDBSever
npm start
```

## 🎯 Menu quản lý (Dùng file batch)

Double-click: **`QUAN_LY_SERVER.bat`**

Menu sẽ hiển thị:
- [1] Kiểm tra server có đang chạy không
- [2] Dừng server
- [3] Khởi động server
- [4] Khởi động lại server
- [5] Thoát

## 📝 Lưu ý

- **Port đã được sử dụng** = Server đang chạy ✓
- Không cần khởi động lại nếu server đang chạy
- Nếu muốn khởi động lại, dừng server trước

## ✅ Checklist

- [ ] Server đang chạy (port 3000)
- [ ] MongoDB đã kết nối
- [ ] IP đúng trong ApiServices.java
- [ ] Test API: `http://localhost:3000/api/products`

