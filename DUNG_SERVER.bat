@echo off
echo ========================================
echo    DUNG MONGODB SERVER
echo ========================================
echo.

echo Dang tim process dung port 3000...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (
    echo Tim thay process: %%a
    echo Dang dung process...
    taskkill /PID %%a /F
    if errorlevel 1 (
        echo Khong the dung process. Co the can quyen Administrator.
    ) else (
        echo Da dung server thanh cong!
    )
)

pause

