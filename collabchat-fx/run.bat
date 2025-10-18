@echo off
echo ========================================
echo CollabChat FX - Run Application
echo ========================================

echo.
echo 🚀 Starting CollabChat FX...
echo.

REM Check if JAR exists
if exist "target\collabchat-fx-1.0.0.jar" (
    echo 📦 Running from JAR file...
    java -jar target\collabchat-fx-1.0.0.jar
) else (
    echo 🔨 JAR not found, running with Maven...
    mvn javafx:run
)

echo.
echo 👋 Application closed.
pause
