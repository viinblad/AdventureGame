import java.util.ArrayList;
import java.util.List;

public class Player {
    private Room currentRoom; // Reference to the player's current room
    private List<Item> inventory; // Player's inventory
    private final int MAX_INVENTORY_SIZE = 10; // Maximum inventory size
    private Weapon equippedWeapon; // Currently equipped weapon

    private int health; // Player's health
    private final int MAX_HEALTH = 100; // Maximum health
    private UserInterface ui; // Reference to the UI

    // Constructor
    public Player(Room startingRoom, UserInterface ui) {
        this.currentRoom = startingRoom; // Set the current room
        this.ui = ui; // Set UI instance
        this.inventory = new ArrayList<>(); // Initialize inventory
        this.health = MAX_HEALTH; // Set health to maximum
        this.equippedWeapon = null; // Initialize equipped weapon

        // Attempt to take the starter weapon from the starting room
        Item starterWeapon = startingRoom.findItem("wooden_sword");
        if (starterWeapon instanceof Weapon) {
            addItem(starterWeapon); // Add the starter weapon to the player's inventory

            // Change the short name of the starter weapon so it can be equip again after other weapon is equipped
            ((Weapon) starterWeapon).setShortName("sword");

            // Equip the weapon without UI message
            equipWeapon((Weapon) starterWeapon, false);
        }
        System.out.println("Player starts with a Wooden Sword."); // Only show in terminal
    }

    // Helper method to print to both terminal and UI
    private void printAndShowMessage(String message) {
        System.out.println(message); // Output to terminal
        ui.showMessage(message); // Output to UI
    }

    // Method to increase health
    public void increaseHealth(int amount) {
        this.health += amount;
        if (this.health > MAX_HEALTH) {
            this.health = MAX_HEALTH;
        }
    }

    // Method to decrease health
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        printAndShowMessage("You took " + damage + " damage. Current health: " + this.health);
        if (isDead()) {
            printAndShowMessage("You have died.");
        }
    }

    // Method to check if the player is dead
    public boolean isDead() {
        return health == 0;
    }

    // Getter for current health
    public int getHealth() {
        return health;
    }

    // Getter for maximum health
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    // Getter for showing equipped weapon in the inventory UI
    public Weapon getEquippedWeapon() {
        return equippedWeapon; // Return the currently equipped weapon
    }

    // Getter for the current room
    public Room getCurrentRoom() {
        return currentRoom; // Return the current room
    }

    // Getter for the inventory
    public List<Item> getInventory() {
        return inventory; // Return the inventory
    }

    // Method to equip a weapon from the inventory
    public boolean equipWeapon(Weapon weapon) {
        return equipWeapon(weapon, true);
    }

    // Overloaded method to allow suppressing the UI message
    public boolean equipWeapon(Weapon weapon, boolean showInUI) {
        System.out.println("Trying to equip weapon: " + weapon.getLongName() + " with short name: " + weapon.getShortName());

        // Check if the weapon exists in the inventory based on its unique properties
        for (Item item : inventory) {
            // Check using shortName first
            if (item instanceof Weapon && item.getShortName().equals(weapon.getShortName())) {
                this.equippedWeapon = (Weapon) item; // Equip the weapon
                if (showInUI) {
                    printAndShowMessage("You have equipped: ");
                }
                return true;
            }
            // Additionally check using longName
            if (item instanceof Weapon && item.getLongName().equals(weapon.getLongName())) {
                this.equippedWeapon = (Weapon) item; // Equip the weapon
                if (showInUI) {
                    printAndShowMessage("You have equipped: " + equippedWeapon.getLongName());
                }
                return true;
            }
        }

        printAndShowMessage("You don't have that weapon.");
        return false;
    }

    // Method to attack with the equipped weapon
    public void attack() {
        if (equippedWeapon == null) {
            printAndShowMessage("You have no weapon equipped.");
        } else if (equippedWeapon.canUse()) {
            equippedWeapon.useWeapon();
            printAndShowMessage("You attacked with " + equippedWeapon.getLongName());
        } else {
            printAndShowMessage("Your weapon cannot be used.");
        }
    }

    // Method to consume food
    public boolean consumeFood(Food food) {
        String message;
        if (food.isPoisonous()) {
            takeDamage(food.getHealthRestored());
            message = "You consumed a poisonous food item! You took " + food.getHealthRestored() + " damage.";
        } else {
            increaseHealth(food.getHealthRestored());
            message = "You consumed " + food.getLongName() + " and restored " + food.getHealthRestored() + " health.";
        }

        printAndShowMessage(message); // Show the consumption message
        ui.showHealth(getHealth(), getMaxHealth()); // Update health display

        // Check health status
        if (getHealth() > 0 && getHealth() < 50) {
            printAndShowMessage("You are in great shape!");
        }

        removeItem(food); // Remove food from inventory
        return true;
    }

    // Method to consume potion
    public boolean consumePotion(Potion potion) {
        String message;
        if (potion.isPoisonous()) {
            takeDamage(potion.getHealthRestored());
            message = "You consumed a poisonous potion! You took " + potion.getHealthRestored() + " damage.";
        } else {
            increaseHealth(potion.getHealthRestored());
            message = "You consumed " + potion.getLongName() + " and restored " + potion.getHealthRestored() + " health.";
        }

        printAndShowMessage(message); // Show the consumption message
        ui.showHealth(getHealth(), getMaxHealth()); // Update health display

        // Check health status
        if (getHealth() > 0 && getHealth() < 50) {
            printAndShowMessage("You are in great shape!");
        }

        removeItem(potion); // Remove potion from inventory
        return true;
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
                    printAndShowMessage("The eastern door is locked!");
                    return false;
                }
                nextRoom = currentRoom.getEast();
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                printAndShowMessage("Invalid direction. Please try north, south, east, or west.");
                return false;
        }

        if (nextRoom != null) {
            currentRoom.setVisited(true);
            currentRoom = nextRoom;
            printAndShowMessage("You moved to " + currentRoom.getName() + ".");
            return true;
        } else {
            printAndShowMessage("You can't go that way!");
            return false;
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
                return !currentRoom.isEastLocked() && currentRoom.getEast() != null;
            case "west":
                return currentRoom.getWest() != null;
            default:
                return false;
        }
    }

    // Method to add an item to the inventory (Take item from room)
    public boolean addItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item);
            System.out.println("Added item to inventory: " + item.getLongName()); // Debugging output
            // Only print the pickup message if the item is not the starter weapon
            if (!(item instanceof Weapon && "wooden_sword".equals(item.getShortName()))) {
                printAndShowMessage("You have picked up: ");
            }
            return true;
        } else {
            printAndShowMessage("Your inventory is full! Can't add " + item.getLongName());
            return false;
        }
    }


    // Method to remove an item from the inventory (Drop item to room)
    public boolean removeItem(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            return true; // Dropping item doesn't print message
        } else {
            printAndShowMessage("Item not found in inventory.");
            return false;
        }
    }

    // Method to findItem in inventory
    public Item findItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                System.out.println("Found item in inventory: " + item.getLongName()); // Debug output
                return item;
            }
        }
        System.out.println("Item not found in inventory: " + itemName); // Debug output
        return null; // Return null if not found
    }


    // Method to take an item from the current room
    public boolean takeItem(String itemName) {
        Item item = currentRoom.findItem(itemName);
        if (item != null) {
            if (addItem(item)) {
                currentRoom.removeItem(item);
                return true;
            }
        } else {
            printAndShowMessage("There is no " + itemName + " here.");
        }
        return false;
    }

    // Method to drop an item into the current room
    public boolean dropItem(String itemName) {
        Item item = findItemInInventory(itemName);
        if (item != null) {
            currentRoom.addItem(item);
            removeItem(item);
            return true;
        } else {
            printAndShowMessage("You don't have " + itemName + " in your inventory.");
        }
        return false;
    }

    // Method to display the inventory
    public void displayInventory() {
        if (inventory.isEmpty()) {
            printAndShowMessage("Your inventory is empty.");
        } else {
            printAndShowMessage("Your Inventory:");
            for (Item item : inventory) {
                // Check if the item is the equipped weapon and display accordingly
                String equippedMessage = (equippedWeapon != null && equippedWeapon.equals(item)) ? " (equipped)" : "";
                printAndShowMessage("- " + item.getLongName() + equippedMessage + ": " + item.getDescription());
            }
        }
    }
}
