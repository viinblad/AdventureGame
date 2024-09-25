public class Room {
    private String name;
    private String description;
    private Room north, south, east, west;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter og setter metoder for retninger
    public Room getNorth() { return north; }
    public void setNorth(Room north) { this.north = north; }

    public Room getSouth() { return south; }
    public void setSouth(Room south) { this.south = south; }

    public Room getEast() { return east; }
    public void setEast(Room east) { this.east = east; }

    public Room getWest() { return west; }
    public void setWest(Room west) { this.west = west; }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
