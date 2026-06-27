# 🏨 Hostel Room Allocation System
### A Complete Java Swing Desktop Application

---

## 📋 Project Overview

A fully functional **Hostel Room Allocation System** built in **pure Java** using **Swing GUI**. This application allows hostel administrators to manage students, rooms, and room allocations from a clean, modern desktop interface.

---

## 🚀 How to Run

### Prerequisites
- **Java JDK 8 or higher** must be installed
  - Download: https://adoptium.net/
  - Or: `sudo apt install default-jdk` (Ubuntu/Debian)

### Windows
```
Double-click run.bat
```
Or in Command Prompt:
```cmd
cd HostelRoomAllocation
run.bat
```

### Linux / macOS
```bash
chmod +x run.sh
./run.sh
```

### Manual Compile & Run
```bash
mkdir out
javac -d out src/*.java
java -cp out Main
```

---

## 🔑 Login Credentials
| Field    | Value     |
|----------|-----------|
| Username | `admin`   |
| Password | `admin123`|

---

## 📁 Project Structure

```
HostelRoomAllocation/
├── src/
│   ├── Main.java              # Application entry point
│   ├── AppTheme.java          # Color & font constants
│   ├── DataManager.java       # Central data store (singleton)
│   ├── Student.java           # Student model
│   ├── Room.java              # Room model
│   ├── LoginFrame.java        # Login screen
│   ├── MainFrame.java         # Main window with sidebar navigation
│   ├── DashboardPanel.java    # Dashboard with statistics
│   ├── StudentPanel.java      # Student management
│   ├── RoomPanel.java         # Room management
│   ├── AllocationPanel.java   # Room allocation
│   └── HistoryPanel.java      # Allocation history log
├── run.bat                    # Windows build & run script
├── run.sh                     # Linux/Mac build & run script
└── README.md                  # This file
```

---

## ✨ Features

### 🔐 Login System
- Secure admin login with credential validation
- Modern split-screen design

### 📊 Dashboard
- Live statistics: Total students, Allocated, Pending, Rooms
- Available/Full/Maintenance room counts
- Recent activity log (last 8 events)
- Quick action buttons to navigate anywhere

### 👤 Student Management
- View all students in a sortable table
- **Add** new students with auto-generated IDs
- **Edit** student details
- **Delete** students (with automatic room deallocation)
- **Deallocate** room from a student
- **Search/Filter** by name, ID, course, gender
- Color-coded status (Allocated / Pending)

### 🚪 Room Management
- View all rooms across blocks
- **Add** new rooms (type, floor, block, gender, rent)
- **Toggle Maintenance** status
- **Delete** empty rooms
- **Filter** by gender, status
- Color-coded status (Available / Full / Maintenance)

### 🔑 Room Allocation
- Smart dropdowns: students filtered by "Pending", rooms by gender match
- Real-time info display (available beds, rent, block)
- Prevents gender mismatch allocations
- Visual room cards overview (available rooms grid)
- Instant success/error feedback

### 📋 Allocation History
- Complete log of all allocations and vacations
- Newest first ordering
- Summary counts at bottom
- Color-coded actions (✅ Allocated / 🔓 Vacated)

---

## 🏗 Architecture

| Pattern        | Usage                                      |
|----------------|--------------------------------------------|
| **Singleton**  | `DataManager` — single source of truth     |
| **MVC**        | Models (Student/Room), Views (Panels), Controller (DataManager) |
| **Observer**   | Panels call `refresh()` on navigation      |
| **Factory**    | Button/card creation helper methods        |

---

## 📦 Sample Data

The system loads with sample data on startup:
- **15 Rooms**: Block A (Male) and Block B (Female), floors 1-3
- **8 Students**: Mixed genders, courses, years
- **5 Pre-allocations**: Showing real usage

---

## 🛠 Technical Details

- **Language**: Pure Java (JDK 8+)
- **GUI Framework**: Java Swing
- **Custom Rendering**: All buttons, tables, and cards use custom `paintComponent()`
- **No external dependencies**: Zero external libraries needed
- **Data Storage**: In-memory (no database — data resets on restart)

---

## 📌 Limitations & Future Enhancements

- [ ] Add database persistence (MySQL / SQLite)
- [ ] Export reports to PDF/Excel
- [ ] Fee management module
- [ ] Email notifications
- [ ] Multi-admin support with roles
- [ ] Room swap functionality

---

*Built with ❤️ using Java Swing — Hostel Room Allocation System*
