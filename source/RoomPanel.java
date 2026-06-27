import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomNumber;
    private String type;       // "Single", "Double", "Triple"
    private int capacity;
    private int occupied;
    private String floor;
    private String block;
    private String gender;     // "Male", "Female"
    private double rentPerMonth;
    private String status;     // "Available", "Full", "Maintenance"
    private List<String> occupants; // Student IDs

    public Room(String roomNumber, String type, int capacity, String floor,
                String block, String gender, double rentPerMonth) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.occupied = 0;
        this.floor = floor;
        this.block = block;
        this.gender = gender;
        this.rentPerMonth = rentPerMonth;
        this.status = "Available";
        this.occupants = new ArrayList<>();
    }

    public boolean isAvailable() {
        return occupied < capacity && !status.equals("Maintenance");
    }

    public int getAvailableBeds() {
        return capacity - occupied;
    }

    public void addOccupant(String studentId) {
        occupants.add(studentId);
        occupied++;
        if (occupied >= capacity) status = "Full";
    }

    public void removeOccupant(String studentId) {
        occupants.remove(studentId);
        occupied--;
        if (!status.equals("Maintenance")) status = "Available";
    }

    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getOccupied() { return occupied; }
    public void setOccupied(int occupied) { this.occupied = occupied; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getRentPerMonth() { return rentPerMonth; }
    public void setRentPerMonth(double rentPerMonth) { this.rentPerMonth = rentPerMonth; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getOccupants() { return occupants; }

    @Override
    public String toString() {
        return roomNumber + " (" + type + " - " + block + ")";
    }
}
