# Script để rebuild Android app
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "   REBUILD ANDROID APP" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra đang ở đúng thư mục
if (-Not (Test-Path "app\build.gradle.kts")) {
    Write-Host "❌ Lỗi: Không tìm thấy app\build.gradle.kts" -ForegroundColor Red
    Write-Host "Vui lòng chạy script này trong thư mục gốc của project" -ForegroundColor Yellow
    exit 1
}

Write-Host "📍 Đang ở thư mục: $(Get-Location)" -ForegroundColor Green
Write-Host ""

# Bước 1: Clean
Write-Host "🧹 Bước 1: Clean project..." -ForegroundColor Yellow
.\gradlew clean
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Clean failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Clean thành công!" -ForegroundColor Green
Write-Host ""

# Bước 2: Build
Write-Host "🔨 Bước 2: Build project..." -ForegroundColor Yellow
.\gradlew assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed!" -ForegroundColor Red
    Write-Host "Vui lòng kiểm tra lỗi ở trên và sửa" -ForegroundColor Yellow
    exit 1
}
Write-Host "✅ Build thành công!" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "✅ HOÀN TẤT!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📱 Tiếp theo:" -ForegroundColor Yellow
Write-Host "   1. Mở Android Studio" -ForegroundColor White
Write-Host "   2. File → Sync Project with Gradle Files" -ForegroundColor White
Write-Host "   3. Chọn emulator/thiết bị" -ForegroundColor White
Write-Host "   4. Click nút Run (▶)" -ForegroundColor White
Write-Host ""


