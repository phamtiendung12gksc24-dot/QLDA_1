# 🚀 Lệnh khởi động Server

## Cách 1: Dùng file batch (Dễ nhất) ⭐

### Double-click một trong các file sau:

- **`CHAY_SERVER.bat`** ← Khuyến nghị
- **`KHOI_DONG_SERVER.bat`**
- **`start-server-simple.bat`**

## Cách 2: Dùng PowerShell/CMD

### Bước 1: Vào thư mục server

```powershell
cd MongoDBSever
```

### Bước 2: Khởi động server

```powershell
npm start
```

### Hoặc làm 1 lệnh:

```powershell
cd MongoDBSever; npm start
```

## Cách 3: Từ thư mục gốc (PowerShell)

```powershell
cd C:\Users\FPT\Downloads\QLDA1CLONE\MongoDBSever
npm start
```

---

## ✅ Kiểm tra server đang chạy

### PowerShell:

```powershell
netstat -ano | Select-String ":3000"
```

### CMD:

```cmd
netstat -ano | findstr :3000
```

**Nếu thấy `LISTENING` → Server đang chạy ✓**

---

## 🌐 Test API trong browser

Mở browser và vào:

```
http://localhost:3000/api/products
```

**Nếu thấy JSON data → Server chạy đúng ✓**

---

## 🛑 Dừng server

### Cách 1: Trong cửa sổ đang chạy

Nhấn **`Ctrl + C`**

### Cách 2: Dùng file batch

Double-click: **`DUNG_SERVER.bat`**

### Cách 3: Dùng PowerShell

```powershell
taskkill /F /IM node.exe
```

---

## 📋 Lưu ý

1. **MongoDB phải đang chạy** trước khi start server
   - Kiểm tra: `net start MongoDB` (Windows)
   
2. **Server chạy trên port 3000**
   - Nếu port bị chiếm → Dừng process cũ trước

3. **Server đang chạy sẽ hiển thị:**
   ```
   ✅ connect success
   ```

---

## 🎯 Quick Start

```powershell
# 1. Vào thư mục server
cd MongoDBSever

# 2. Khởi động
npm start

# 3. Kiểm tra trong browser
# http://localhost:3000/api/products
```

