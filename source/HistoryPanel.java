import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataManager {
    private static DataManager instance;
    private List<Student> students;
    private List<Room> rooms;
    private List<String[]> allocationHistory; // [studentId, roomNumber, date, action]

    private DataManager() {
        students = new ArrayList<>();
        rooms = new ArrayList<>();
        allocationHistory = new ArrayList<>();
        loadSampleData();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    private void loadSampleData() {
        // --- Rooms: Block A (Male) ---
        rooms.add(new Room("A101", "Single", 1, "1st Floor", "Block A", "Male", 3000));
        rooms.add(new Room("A102", "Double", 2, "1st Floor", "Block A", "Male", 2000));
        rooms.add(new Room("A103", "Triple", 3, "1st Floor", "Block A", "Male", 1500));
        rooms.add(new Room("A201", "Single", 1, "2nd Floor", "Block A", "Male", 3000));
        rooms.add(new Room("A202", "Double", 2, "2nd Floor", "Block A", "Male", 2000));
        rooms.add(new Room("A203", "Triple", 3, "2nd Floor", "Block A", "Male", 1500));
        rooms.add(new Room("A301", "Double", 2, "3rd Floor", "Block A", "Male", 2000));
        rooms.add(new Room("A302", "Triple", 3, "3rd Floor", "Block A", "Male", 1500));

        // --- Rooms: Block B (Female) ---
        rooms.add(new Room("B101", "Single", 1, "1st Floor", "Block B", "Female", 3200));
        rooms.add(new Room("B102", "Double", 2, "1st Floor", "Block B", "Female", 2200));
        rooms.add(new Room("B103", "Triple", 3, "1st Floor", "Block B", "Female", 1700));
        rooms.add(new Room("B201", "Single", 1, "2nd Floor", "Block B", "Female", 3200));
        rooms.add(new Room("B202", "Double", 2, "2nd Floor", "Block B", "Female", 2200));
        rooms.add(new Room("B203", "Triple", 3, "2nd Floor", "Block B", "Female", 1700));
        rooms.add(new Room("B301", "Double", 2, "3rd Floor", "Block B", "Female", 2200));

        // Maintenance room
        Room maint = new Room("A103", "Triple", 3, "1st Floor", "Block A", "Male", 1500);
        maint.setStatus("Maintenance");
        // Already added A103, set it to maintenance
        getRoomByNumber("A103").setStatus("Maintenance");

        // --- Sample Students ---
        Student s1 = new Student("S001", "Arjun Sharma", "arjun@email.com", "9876543210", "B.Tech CSE", 2, "Male");
        Student s2 = new Student("S002", "Vikram Singh", "vikram@email.com", "9876543211", "B.Tech ECE", 1, "Male");
        Student s3 = new Student("S003", "Rohit Kumar", "rohit@email.com", "9876543212", "MCA", 3, "Male");
        Student s4 = new Student("S004", "Priya Patel", "priya@email.com", "9876543213", "B.Tech CSE", 2, "Female");
        Student s5 = new Student("S005", "Anjali Gupta", "anjali@email.com", "9876543214", "MBA", 1, "Female");
        Student s6 = new Student("S006", "Sneha Verma", "sneha@email.com", "9876543215", "B.Sc IT", 3, "Female");
        Student s7 = new Student("S007", "Rahul Mehta", "rahul@email.com", "9876543216", "B.Tech ME", 4, "Male");
        Student s8 = new Student("S008", "Deepak Yadav", "deepak@email.com", "9876543217", "BCA", 2, "Male");

        students.addAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8));

        // Pre-allocate some students
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        allocateRoom("S001", "A101", today);
        allocateRoom("S002", "A102", today);
        allocateRoom("S003", "A102", today);
        allocateRoom("S004", "B101", today);
        allocateRoom("S005", "B202", today);
    }

    // ---- STUDENT OPERATIONS ----
    public List<Student> getStudents() { return students; }

    public Student getStudentById(String id) {
        return students.stream().filter(s -> s.getStudentId().equals(id)).findFirst().orElse(null);
    }

    public boolean addStudent(Student student) {
        if (getStudentById(student.getStudentId()) != null) return false;
        students.add(student);
        return true;
    }

    public boolean updateStudent(Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(updated.getStudentId())) {
                students.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudent(String studentId) {
        Student s = getStudentById(studentId);
        if (s == null) return false;
        if (!s.getRoomNumber().equals("Not Allocated")) {
            deallocateRoom(studentId);
        }
        students.removeIf(st -> st.getStudentId().equals(studentId));
        return true;
    }

    public String generateStudentId() {
        int max = students.stream()
            .mapToInt(s -> {
                try { return Integer.parseInt(s.getStudentId().substring(1)); }
                catch (Exception e) { return 0; }
            }).max().orElse(0);
        return String.format("S%03d", max + 1);
    }

    // ---- ROOM OPERATIONS ----
    public List<Room> getRooms() { return rooms; }

    public Room getRoomByNumber(String roomNumber) {
        return rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);
    }

    public boolean addRoom(Room room) {
        if (getRoomByNumber(room.getRoomNumber()) != null) return false;
        rooms.add(room);
        return true;
    }

    public boolean deleteRoom(String roomNumber) {
        Room r = getRoomByNumber(roomNumber);
        if (r == null || r.getOccupied() > 0) return false;
        rooms.removeIf(rm -> rm.getRoomNumber().equals(roomNumber));
        return true;
    }

    public List<Room> getAvailableRooms(String gender) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable() && r.getGender().equals(gender)) {
                available.add(r);
            }
        }
        return available;
    }

    // ---- ALLOCATION OPERATIONS ----
    public String allocateRoom(String studentId, String roomNumber, String date) {
        Student student = getStudentById(studentId);
        Room room = getRoomByNumber(roomNumber);

        if (student == null) return "Student not found.";
        if (room == null) return "Room not found.";
        if (!student.getRoomNumber().equals("Not Allocated")) return "Student already has a room: " + student.getRoomNumber();
        if (!room.isAvailable()) return "Room is not available.";
        if (!room.getGender().equals(student.getGender())) return "Room gender mismatch.";

        student.setRoomNumber(roomNumber);
        student.setAllocationDate(date);
        student.setStatus("Allocated");
        room.addOccupant(studentId);
        allocationHistory.add(new String[]{studentId, roomNumber, date, "Allocated"});
        return "SUCCESS";
    }

    public String deallocateRoom(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null) return "Student not found.";
        if (student.getRoomNumber().equals("Not Allocated")) return "Student has no room allocated.";

        Room room = getRoomByNumber(student.getRoomNumber());
        if (room != null) room.removeOccupant(studentId);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        allocationHistory.add(new String[]{studentId, student.getRoomNumber(), date, "Vacated"});

        student.setRoomNumber("Not Allocated");
        student.setAllocationDate("-");
        student.setStatus("Pending");
        return "SUCCESS";
    }

    public List<String[]> getAllocationHistory() { return allocationHistory; }

    // ---- STATS ----
    public int getTotalStudents() { return students.size(); }
    public int getAllocatedStudents() { return (int) students.stream().filter(s -> s.getStatus().equals("Allocated")).count(); }
    public int getPendingStudents() { return (int) students.stream().filter(s -> s.getStatus().equals("Pending")).count(); }
    public int getTotalRooms() { return rooms.size(); }
    public int getAvailableRoomsCount() { return (int) rooms.stream().filter(Room::isAvailable).count(); }
    public int getFullRooms() { return (int) rooms.stream().filter(r -> r.getStatus().equals("Full")).count(); }
    public int getMaintenanceRooms() { return (int) rooms.stream().filter(r -> r.getStatus().equals("Maintenance")).count(); }
}
