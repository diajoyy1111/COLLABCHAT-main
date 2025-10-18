@echo off
echo ========================================
echo CollabChat FX - Build Script
echo ========================================

echo.
echo [1/4] Cleaning previous build...
call mvn clean

echo.
echo [2/4] Compiling JavaFX application...
call mvn compile

if %ERRORLEVEL% neq 0 (
    echo.
    echo ❌ Compilation failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo [3/4] Creating executable JAR...
call mvn package

if %ERRORLEVEL% neq 0 (
    echo.
    echo ❌ Packaging failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo [4/4] ✅ Build completed successfully!
echo.
echo 📁 JAR file created: target\collabchat-fx-1.0.0.jar
echo.
echo 🚀 To run the application:
echo    java -jar target\collabchat-fx-1.0.0.jar
echo.
echo Or use Maven:
echo    mvn javafx:run
echo.
pause
