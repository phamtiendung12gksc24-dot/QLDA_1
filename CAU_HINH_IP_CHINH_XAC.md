# 📱 Cấu hình IP chính xác cho Android App

## 🔍 IP hiện tại của máy

Theo hình ảnh WiFi settings:
- **IPv4 address:** `192.168.2.44`
- **Gateway:** `192.168.2.1`

## ⚙️ Cấu hình IP trong App

File cần sửa: `app/src/main/java/com/example/duan1/services/ApiServices.java`

### Nếu dùng Android EMULATOR:
```java
String Url = "http://10.0.2.2:3000/";  // IP đặc biệt cho emulator
```

### Nếu dùng thiết bị THẬT:
```java
String Url = "http://192.168.2.44:3000/";  // IP thật của máy
```

## ✅ IP hiện tại trong code

- **Hiện tại:** `http://10.0.2.2:3000/` (cho emulator)
- **IP máy:** `192.168.2.44`

## 🔄 Cách đổi IP

1. Mở file: `app/src/main/java/com/example/duan1/services/ApiServices.java`
2. Tìm dòng: `String Url = "http://10.0.2.2:3000/";`
3. Thay đổi thành IP phù hợp:
   - Emulator: `http://10.0.2.2:3000/`
   - Thiết bị thật: `http://192.168.2.44:3000/`
4. Rebuild app sau khi đổi

## 📝 Lưu ý

- **Emulator:** Luôn dùng `10.0.2.2` (không đổi)
- **Thiết bị thật:** Dùng IP thật `192.168.2.44` và đảm bảo cùng mạng WiFi

