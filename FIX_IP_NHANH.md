# ⚡ Sửa lỗi IP - Hướng dẫn nhanh

## ❌ Vấn đề

App đang kết nối tới IP cũ: **`10.24.28.88:3000`**  
→ Lỗi: `failed to connect`

## ✅ Đã sửa

IP mới trong code: **`10.0.2.2:3000`** ✓ (đúng cho emulator)

## 🚀 Cần làm ngay: REBUILD APP

### ⚠️ QUAN TRỌNG: Phải rebuild app!

App chưa rebuild nên vẫn dùng IP cũ. 

### Cách 1: File batch (NHANH NHẤT)

1. Double-click: **`REBUILD_APP_NGAY.bat`**
2. Đợi xong
3. Chạy app lại từ Android Studio

### Cách 2: Android Studio

1. **Build → Clean Project**
2. **Build → Rebuild Project**  
3. **Run → Run 'app'**

### Cách 3: Xóa app cũ và cài lại

1. Xóa app trên emulator
2. Chạy lại từ Android Studio

---

## ✅ Sau khi rebuild

Mở **Logcat** và tìm:
```
RetrofitClient: RetrofitClient initialized with base URL: http://10.0.2.2:3000/
```

Nếu thấy IP `10.0.2.2:3000` → **ĐÃ ĐÚNG!** ✓

---

## 📋 Checklist

- [x] IP đã đổi trong code (`10.0.2.2:3000`) ✓
- [x] Server đang chạy (port 3000) ✓
- [ ] **REBUILD APP** ← **LÀM NGAY!**
- [ ] Test kết nối lại

