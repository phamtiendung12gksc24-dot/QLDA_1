# 🔧 Sửa lỗi kết nối IP

## ❌ Lỗi hiện tại

```
Lỗi kết nối: failed to connect to /10.24.28.88 (port 3000)
```

**Nguyên nhân:** App đang dùng IP cũ, chưa rebuild sau khi đổi IP.

## ✅ Đã sửa IP trong code

IP mới: `http://10.0.2.2:3000/` ✓ (đúng cho Android Emulator)

## 🚀 BƯỚC TIẾP THEO - REBUILD APP

### Cách 1: Dùng file batch (Dễ nhất)

Double-click file: **`REBUILD_APP_NGAY.bat`**

### Cách 2: Từ Android Studio

1. **File → Sync Project with Gradle Files**
2. **Build → Clean Project** (đợi xong)
3. **Build → Rebuild Project** (đợi xong)
4. **Run → Run 'app'** (hoặc nhấn Shift+F10)

### Cách 3: Từ Command Line

```powershell
# Clean
.\gradlew.bat clean

# Build lại
.\gradlew.bat build

# Rồi chạy từ Android Studio
```

## ⚠️ QUAN TRỌNG

- **Phải rebuild** sau khi đổi IP
- Nếu không rebuild, app vẫn dùng IP cũ
- Có thể uninstall app cũ trên emulator và cài lại

## 🔍 Kiểm tra sau khi rebuild

1. Mở **Logcat** trong Android Studio
2. Tìm dòng:
   ```
   RetrofitClient: RetrofitClient initialized with base URL: http://10.0.2.2:3000/
   ```
3. Nếu thấy IP `10.0.2.2:3000` → Đã đúng ✓

## 📱 IP Configuration

- **Emulator:** `http://10.0.2.2:3000/`
- **Thiết bị thật:** `http://192.168.2.44:3000/` (hoặc IP WiFi của máy)

## ✅ Checklist

- [ ] IP đã đổi thành `10.0.2.2:3000` trong code ✓
- [ ] Server đang chạy (port 3000) ✓
- [ ] **REBUILD APP** ← QUAN TRỌNG NHẤT!
- [ ] Kiểm tra Logcat xem IP mới chưa
- [ ] Test kết nối lại

