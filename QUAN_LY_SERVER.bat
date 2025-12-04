@echo off
echo ========================================
echo    QUAN LY MONGODB SERVER
echo ========================================
echo.

:menu
echo.
echo Chon hanh dong:
echo [1] Kiem tra server co dang chay khong
echo [2] Dung server (kill process port 3000)
echo [3] Khoi dong server
echo [4] Khoi dong lai server
echo [5] Thoat
echo.
set /p choice="Nhap lua chon (1-5): "

if "%choice%"=="1" goto check
if "%choice%"=="2" goto stop
if "%choice%"=="3" goto start
if "%choice%"=="4" goto restart
if "%choice%"=="5" goto end
goto menu

:check
echo.
echo Dang kiem tra port 3000...
netstat -ano | findstr :3000
if errorlevel 1 (
    echo Server KHONG dang chay
) else (
    echo Server DANG CHAY
)
pause
goto menu

:stop
echo.
echo Dang tim process dung port 3000...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (
    echo Dang dung process %%a
    taskkill /PID %%a /F
    echo Da dung server
)
pause
goto menu

:start
echo.
echo Dang khoi dong server...
cd /d "%~dp0MongoDBSever"
npm start
goto menu

:restart
echo.
echo Dang dung server cu...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (
    taskkill /PID %%a /F
)
timeout /t 2 /nobreak > nul
echo Dang khoi dong server moi...
cd /d "%~dp0MongoDBSever"
npm start
goto menu

:end
exit

