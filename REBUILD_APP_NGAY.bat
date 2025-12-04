@echo off
echo ========================================
echo    REBUILD ANDROID APP - FIX IP
echo ========================================
echo.
echo Buoc 1: Clean project...
call gradlew.bat clean
echo.
echo Buoc 2: Rebuild project...
call gradlew.bat build
echo.
echo ========================================
echo    HOAN TAT! Chay app lai tu Android Studio
echo ========================================
pause

