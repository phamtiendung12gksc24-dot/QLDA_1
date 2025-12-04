# 🔧 Sửa lỗi kết nối - IP không đúng

## ❌ Vấn đề

App đang cố kết nối tới IP cũ: `10.24.28.88:3000`
- App đang chạy trên **Android Emulator** (IP: `10.0.2.16`)
- Server đang chạy trên `localhost:3000`

## ✅ Giải pháp

### 1. Đổi IP trong code (Đã làm)

File: `app/src/main/java/com/example/duan1/services/ApiServices.java`

IP hiện tại: `http://10.0.2.2:3000/` ✓ (đúng cho emulator)

### 2. REBUILD APP (QUAN TRỌNG!)

**Phải rebuild app** để áp dụng thay đổi IP:

1. **File → Sync Project with Gradle Files**
2. **Build → Clean Project**
3. **Build → Rebuild Project**
4. **Run app lại**

### 3. Hoặc uninstall app cũ và cài lại

- Xóa app trên emulator
- Chạy lại app từ Android Studio

## 📱 IP cho emulator

- **IP đúng:** `http://10.0.2.2:3000/`
- Đây là IP đặc biệt của Android Emulator để kết nối tới localhost

## 🔍 Kiểm tra

Sau khi rebuild, mở Logcat và tìm:
```
RetrofitClient: RetrofitClient initialized with base URL: http://10.0.2.2:3000/
```

Nếu thấy IP `10.0.2.2:3000` → Đã đúng ✓

