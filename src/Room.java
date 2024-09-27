public class Room {
    private String name;
    private String description;
    private Room north, south, east, west;
    private boolean visit = false;  // Markerer om rummet er besøgt
    private boolean lockedEast = false;  // Markerer om døren mod øst er låst

    // Constructor
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter og setter metoder for retninger med automatisk tovejskobling
    public Room getNorth() { return north; }
    public void setNorth(Room north) {
        this.north = north;
        if (north.getSouth() != this) {
            north.setSouth(this);  // Automatisk forbinde modsat
        }
    }

    public Room getSouth() { return south; }
    public void setSouth(Room south) {
        this.south = south;
        if (south.getNorth() != this) {
            south.setNorth(this);  // Automatisk forbinde modsat
        }
    }

    public Room getEast() { return east; }
    public void setEast(Room east) {
        this.east = east;
        if (east.getWest() != this) {
            east.setWest(this);  // Automatisk forbinde modsat
        }
    }

    public Room getWest() { return west; }
    public void setWest(Room west) {
        this.west = west;
        if (west.getEast() != this) {
            west.setEast(this);  // Automatisk forbinde modsat
        }
    }

    // Getter og setter for visit-attribut
    public boolean isVisited() {
        return visit;
    }

    public void setVisited(boolean visit) {
        this.visit = visit;
    }

    // Lås og oplåsning af østlige døre
    public void lockEast() {
        this.lockedEast = true;
    }

    public void unlockEast() {
        this.lockedEast = false;
    }

    public boolean isEastLocked() {
        return lockedEast;
    }

    // Getter for navn og beskrivelse. Beskrivelsen ændres baseret på om rummet er besøgt
    public String getName() {
        return name;
    }

    public String getDescription() {
        if (visit) {
            return name + ": You have already been here.";  // Rummets navn og kort besked efter første besøg
        } else {
            return name + ": " + description;  // Fuld beskrivelse første gang
        }
    }
}
