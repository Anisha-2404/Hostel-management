public class Student {
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String course;
    private int year;
    private String gender;
    private String roomNumber;
    private String allocationDate;
    private String status; // "Allocated", "Pending", "Vacated"

    public Student(String studentId, String name, String email, String phone,
                   String course, int year, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.year = year;
        this.gender = gender;
        this.roomNumber = "Not Allocated";
        this.allocationDate = "-";
        this.status = "Pending";
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getAllocationDate() { return allocationDate; }
    public void setAllocationDate(String allocationDate) { this.allocationDate = allocationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return name + " (" + studentId + ")";
    }
}
