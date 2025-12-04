# 🔄 Hướng dẫn Rebuild và Run lại App Android

## ⚠️ Vấn đề: Không run lại được app

Nếu bạn gặp lỗi khi rebuild/run lại app sau khi thay đổi IP, làm theo các bước sau:

## ✅ Bước 1: Sync Gradle Files (QUAN TRỌNG)

1. **Mở Android Studio**
2. **File → Sync Project with Gradle Files**
   - Hoặc click vào thông báo "Sync Now" nếu có
3. Chờ đến khi thấy **"Gradle build finished"** ở thanh trạng thái dưới cùng

## ✅ Bước 2: Clean Project

1. **Build → Clean Project**
2. Chờ hoàn thành (có thể mất 1-2 phút)

## ✅ Bước 3: Rebuild Project

1. **Build → Rebuild Project**
2. Chờ build xong
3. Kiểm tra tab **Build** ở dưới cùng xem có lỗi đỏ không

## ✅ Bước 4: Invalidate Caches (nếu vẫn lỗi)

1. **File → Invalidate Caches...**
2. Chọn **Invalidate and Restart**
3. Chờ Android Studio khởi động lại
4. Sau đó sync Gradle lại (Bước 1)

## ✅ Bước 5: Kiểm tra Run Configuration

1. Ở thanh toolbar, tìm dropdown bên cạnh nút Run (▶)
2. Nếu thấy "No run configurations" hoặc "Edit Configurations...":
   - Click vào dropdown
   - Chọn **Edit Configurations...**
   - Click dấu **+** (Add New Configuration)
   - Chọn **Android App**
   - Đặt tên: `app`
   - Module: chọn `app` từ dropdown
   - Click **Apply** → **OK**

## ✅ Bước 6: Chọn thiết bị/Emulator

1. Ở thanh toolbar, bên trái nút Run, có dropdown chọn thiết bị
2. Nếu chưa có emulator chạy:
   - Click icon **Device Manager** (hình điện thoại) ở bên phải
   - Click nút **Play** (▶) bên cạnh emulator để khởi động
   - Chờ emulator khởi động xong (có thể mất 1-2 phút)

## ✅ Bước 7: Run App

1. Click nút **Run** (▶) màu xanh ở thanh toolbar
2. Hoặc nhấn phím tắt: **Shift + F10** (Windows/Linux) hoặc **Ctrl + R** (Mac)

## 🐛 Nếu vẫn không được

### Lỗi "Gradle sync failed"
- Kiểm tra kết nối internet (cần tải dependencies)
- Kiểm tra file `gradle.properties` có đúng không
- Thử: **File → Invalidate Caches → Invalidate and Restart**

### Lỗi "No run configurations"
- Làm theo Bước 5 ở trên
- Đảm bảo module `app` được chọn

### Lỗi "Device not found"
- Mở Device Manager
- Tạo mới hoặc khởi động lại emulator
- Hoặc kết nối thiết bị thật qua USB và bật USB Debugging

### Lỗi build/compilation
- Xem tab **Build** để biết lỗi cụ thể
- Copy lỗi và tìm kiếm trên Google
- Kiểm tra code có syntax error không

## 📝 Lưu ý quan trọng

- ⚠️ **PHẢI rebuild** sau khi thay đổi IP trong code
- ⚠️ **PHẢI sync Gradle** sau khi thêm dependencies mới
- ⚠️ Đảm bảo emulator/thiết bị đã sẵn sàng trước khi run

## 🚀 Quick Fix (Làm nhanh)

Nếu muốn làm nhanh, chạy lệnh này trong terminal:

**Windows (PowerShell):**
```powershell
cd C:\Users\FPT\Downloads\QLDA1CLONE
.\gradlew clean
.\gradlew assembleDebug
```

Sau đó trong Android Studio:
1. Sync Gradle (File → Sync Project with Gradle Files)
2. Run app (▶)

## 📞 Nếu vẫn không được

Gửi cho tôi:
1. Screenshot lỗi trong tab **Build**
2. Screenshot nút Run (màu xám hay màu xanh)
3. Thông báo lỗi cụ thể (nếu có)


