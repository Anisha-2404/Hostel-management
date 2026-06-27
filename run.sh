#!/bin/bash
echo "============================================"
echo "  Hostel Room Allocation System - Build"
echo "============================================"
echo ""

# Check Java
if ! command -v javac &> /dev/null; then
    echo "ERROR: javac not found. Install JDK 8+:"
    echo "  Ubuntu/Debian: sudo apt install default-jdk"
    echo "  Mac: brew install openjdk"
    echo "  Or download from: https://adoptium.net/"
    exit 1
fi

echo "[1/3] Compiling source files..."
mkdir -p out
javac -d out src/*.java
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed."
    exit 1
fi

echo "[2/3] Creating JAR file..."
echo "Main-Class: Main" > manifest.txt
jar cfm HostelRoomAllocation.jar manifest.txt -C out .
rm manifest.txt

echo "[3/3] Launching application..."
echo ""
java -jar HostelRoomAllocation.jar
