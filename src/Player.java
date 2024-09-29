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
                    return false; // Can't move
                }
                nextRoom = currentRoom.getEast();
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                System.out.println("Invalid direction. Please try again.");
                return false;
        }

        if (nextRoom != null) {
            currentRoom.setVisited(true); // Mark current room as visited
            currentRoom = nextRoom; // Move to the next room
            System.out.println("You moved to " + currentRoom.getName()); // Notify the player of the move
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
                return currentRoom.getNorth() != null; // Check if there is a room to the north
            case "south":
                return currentRoom.getSouth() != null; // Check if there is a room to the south
            case "east":
                return !currentRoom.isEastLocked(); // Check if the eastern door is not locked
            case "west":
                return currentRoom.getWest() != null; // Check if there is a room to the west
            default:
                return false; // Unknown direction
        }
    }

    // Getter for the current room
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Method to add an item to the inventory
    public boolean addItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item);
            System.out.println("You have picked up: " + item.getName());
            return true; // Item added successfully
        } else {
            System.out.println("Your inventory is full! Can't add " + item.getName());
            return false; // Inventory is full
        }
    }

    // Method to remove an item from the inventory
    public boolean removeItem(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            System.out.println("You have removed: " + item.getName());
            return true; // Item removed successfully
        } else {
            System.out.println("Item not found in inventory.");
            return false; // Item not found
        }
    }

    // Method to display the inventory
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : inventory) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }
}
