@echo off
echo ============================================
echo   Hostel Room Allocation System - Build
echo ============================================
echo.

REM Check Java installation
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH.
    echo Please install JDK 8 or higher from https://adoptium.net/
    pause
    exit /b 1
)

echo [1/3] Compiling source files...
mkdir out 2>nul
javac -d out src\*.java
if errorlevel 1 (
    echo ERROR: Compilation failed. Check source files.
    pause
    exit /b 1
)

echo [2/3] Creating JAR file...
echo Main-Class: Main > manifest.txt
jar cfm HostelRoomAllocation.jar manifest.txt -C out .
del manifest.txt

echo [3/3] Launching application...
echo.
java -jar HostelRoomAllocation.jar

pause
