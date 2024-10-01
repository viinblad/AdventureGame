import java.util.ArrayList;
import java.util.List;

public class Player {
    private Room currentRoom;               // The player's current room
    private List<Item> inventory;           // List to store items in the player's inventory
    private final int MAX_INVENTORY_SIZE = 10; // Maximum number of items in inventory

    // Constructor
    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>(); // Initialize the inventory
    }

    // Move in the specified direction
    public boolean move(String direction) {
        Room nextRoom = null;

        switch (direction.toLowerCase()) {
            case "north":
                nextRoom = currentRoom.getNorth();
                break;
            case "south":
                nextRoom = currentRoom.getSouth();
                break;
            case "east":
                if (currentRoom.isEastLocked()) {
                    System.out.println("The eastern door is locked!");
                    return false; // Can't move east if locked
                }
                nextRoom = currentRoom.getEast();
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                System.out.println("Invalid direction. Please try north, south, east, or west.");
                return false;
        }

        // Move to the next room if possible
        if (nextRoom != null) {
            currentRoom.setVisited(true); // Mark current room as visited
            currentRoom = nextRoom; // Move to the next room
            System.out.println("You moved to " + currentRoom.getName() + ".");
            return true; // Move successful
        } else {
            System.out.println("You can't go that way!");
            return false; // Move unsuccessful
        }
    }

    // Check if the player can move in the specified direction
    public boolean canMove(String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                return currentRoom.getNorth() != null;
            case "south":
                return currentRoom.getSouth() != null;
            case "east":
                return !currentRoom.isEastLocked() && currentRoom.getEast() != null; // Check if east is unlocked and available
            case "west":
                return currentRoom.getWest() != null;
            default:
                return false; // Invalid direction
        }
    }

    // Getter for the current room
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Getter for the inventory
    public List<Item> getInventory() {
        return inventory; // Return the inventory list
    }

    // Method to add an item to the inventory (Take item from room)
    public boolean addItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item);
            System.out.println("You have picked up: " + item.getLongName());
            return true; // Item added successfully
        } else {
            System.out.println("Your inventory is full! Can't add " + item.getLongName());
            return false; // Inventory full
        }
    }

    // Method to remove an item from the inventory (Drop item to room)
    public boolean removeItem(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            System.out.println("You have dropped: " + item.getLongName());
            return true; // Item removed successfully
        } else {
            System.out.println("Item not found in inventory.");
            return false; // Item not found
        }
    }

    // Method to find an item in the inventory by its short name
    public Item findItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null; // Item not found
    }

    // Method to take an item from the current room
    public boolean takeItem(String itemName) {
        Item item = currentRoom.findItem(itemName); // Find item in the current room
        if (item != null) {
            if (addItem(item)) { // Add item to inventory
                currentRoom.removeItem(item); // Remove from room
                return true; // Item successfully taken
            }
        } else {
            System.out.println("There is no " + itemName + " here.");
        }
        return false; // Item taking failed
    }

    // Method to drop an item into the current room
    public boolean dropItem(String itemName) {
        Item item = findItemInInventory(itemName); // Find item in the inventory
        if (item != null) {
            currentRoom.addItem(item); // Add it to the current room
            removeItem(item); // Remove from inventory
            return true; // Item successfully dropped
        } else {
            System.out.println("You don't have " + itemName + " in your inventory.");
        }
        return false; // Item dropping failed
    }

    // Method to display the inventory
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : inventory) {
                System.out.println("- " + item.getLongName() + ": " + item.getDescription());
            }
        }
    }
}
