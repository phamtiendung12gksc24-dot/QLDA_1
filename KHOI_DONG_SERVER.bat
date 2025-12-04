@echo off
echo ========================================
echo    KHOI DONG MONGODB SERVER
echo ========================================
echo.

echo [1/3] Dang kiem tra va khoi dong MongoDB...
net start MongoDB 2>nul
if errorlevel 1 (
    echo MongoDB da chay hoac can quyen Administrator
) else (
    echo MongoDB da duoc khoi dong thanh cong!
)

timeout /t 2 /nobreak > nul

echo.
echo [2/3] Dang di chuyen vao thu muc MongoDBSever...
cd /d "%~dp0MongoDBSever"

if not exist "package.json" (
    echo.
    echo [LOI] Khong tim thay package.json!
    echo Vui long kiem tra duong dan: %~dp0MongoDBSever
    pause
    exit /b 1
)

echo.
echo [3/3] Dang khoi dong Node.js Server...
echo.
echo ========================================
echo Server se chay tren: http://localhost:3000
echo Cho emulator: http://10.0.2.2:3000
echo.
echo Nhan Ctrl+C de DUNG server
echo ========================================
echo.

npm start

pause

