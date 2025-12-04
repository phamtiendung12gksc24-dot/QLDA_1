@echo off
echo ========================================
echo    KHOI DONG LAI SERVER
echo ========================================
echo.

echo [1/3] Dang dung server cu...
taskkill /F /IM node.exe 2>nul
if errorlevel 1 (
    echo Khong tim thay server nao dang chay
) else (
    echo Da dung server cu
)

timeout /t 2 /nobreak > nul

echo.
echo [2/3] Dang di chuyen vao thu muc MongoDBSever...
cd /d "%~dp0MongoDBSever"

echo.
echo [3/3] Dang khoi dong server moi...
echo.
npm start

pause

