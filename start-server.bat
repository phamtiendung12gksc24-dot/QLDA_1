@echo off
echo ========================================
echo    KHOI DONG MONGODB SERVER
echo ========================================
echo.

echo [1/3] Dang kiem tra MongoDB...
net start MongoDB 2>nul
if errorlevel 1 (
    echo MongoDB da chay hoac khong the khoi dong
) else (
    echo MongoDB da duoc khoi dong
)

timeout /t 2 /nobreak > nul

echo.
echo [2/3] Dang di chuyen vao thu muc MongoDBSever...
cd /d "%~dp0MongoDBSever"

if not exist "package.json" (
    echo LOI: Khong tim thay package.json trong thu muc MongoDBSever!
    pause
    exit /b 1
)

echo.
echo [3/3] Dang khoi dong Node.js Server...
echo Server se chay tren: http://localhost:3000
echo.
echo Nhan Ctrl+C de dung server
echo.
echo ========================================
echo.

npm start

pause

