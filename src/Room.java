import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String description;
    private Room north, south, east, west;
    private ArrayList<Item> items; // List of items in the room
    private List<Enemy> enemies; // Use List to hold enemies
    private boolean visited = false;  // Mark whether the room has been visited
    private boolean lockedEast = false;  // Marks if the eastern door is locked

    // Constructor
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>(); // Initialize the item list
        this.enemies = new ArrayList<>(); // Initialize the enemy list
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

    // Item functions
    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            System.out.println("Removed item: " + item.getLongName()); // Log successful removal
        } else {
            System.out.println("Failed to remove item: " + item.getLongName()); // Log failure
        }
    }


    public ArrayList<Item> getItems() {
        return items;
    }

    // Method to find an item by its short name
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return item; // Return the item if found
            }
        }
        return null; // Item not found
    }

    // Show items in the room
    public void showItems() {
        if (items.isEmpty()) {
            System.out.println("There are no items in this room.");
        } else {
            System.out.println("You see the following items: ");
            for (Item item : items) {
                System.out.println("- " + item.getLongName()); // Assuming you want to print the long name
            }
        }
    }

    // Methods for enemy management
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies; // Return the list of enemies
    }

    public Enemy findEnemy(String name) {
        for (Enemy enemy : enemies) {
            if (enemy.getName().equalsIgnoreCase(name)) {
                return enemy;
            }
        }
        return null; // Enemy not found
    }
}
