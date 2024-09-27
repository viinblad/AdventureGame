public class Room {
    private String name;
    private String description;
    private Room north, south, east, west;
    private boolean visited = false;  // Mark whether the room has been visited
    private boolean lockedEast = false;  // Marks if the eastern door is locked

    // Constructor
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter and setter methods for directions with automatic bidirectional linking
    public Room getNorth() { return north; }
    public void setNorth(Room north) {
        this.north = north;
        if (north != null && north.getSouth() != this) {
            north.setSouth(this);
        }
    }

    public Room getSouth() { return south; }
    public void setSouth(Room south) {
        this.south = south;
        if (south != null && south.getNorth() != this) {
            south.setNorth(this);
        }
    }

    public Room getEast() { return lockedEast ? null : east; } // Prevent movement if locked
    public void setEast(Room east) {
        this.east = east;
        if (east != null && east.getWest() != this) {
            east.setWest(this);
        }
    }

    public Room getWest() { return west; }
    public void setWest(Room west) {
        this.west = west;
        if (west != null && west.getEast() != this) {
            west.setEast(this);
        }
    }

    // Mark the room as visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    // Getter for room name
    public String getName() {
        return name;
    }

    // Getter for room description
    public String getDescription() {
        return description;
    }

    // Lock and unlock the eastern door
    public void lockEast() {
        this.lockedEast = true;
    }

    public void unlockEast() {
        this.lockedEast = false;
    }

    public boolean isEastLocked() {
        return lockedEast;
    }
}
