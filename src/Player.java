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
        Item starterWeapon = startingRoom.findItem("wooden sword");
        if (starterWeapon instanceof Weapon) {
            if (addItem(starterWeapon)) { // Add the starter weapon to the player's inventory
                // The player can equip it later when desired
                // Remove the starter weapon from the room after picking it up
                startingRoom.removeItem(starterWeapon); // Ensure the starter weapon is removed from the room
            } else {
                printAndShowMessage("Failed to add the starter weapon to the inventory.");
            }
        } else {
            printAndShowMessage("Starter weapon not found in the starting room.");
        }
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
        printAndShowMessage("Health increased by " + amount + ". Current health: " + this.health); // Output health increase
    }

    // Method to decrease health
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }

        // Check if the player is dead
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
        // Check if the weapon exists in the inventory
        if (!inventory.contains(weapon)) {
            if (showInUI) {
                printAndShowMessage("You don't have that weapon.");
            }
            return false; // Weapon not found in inventory
        }

        // If there's already an equipped weapon, put it back in the inventory
        if (equippedWeapon != null) {
            // Only return the previously equipped weapon to inventory if it's not the same as the new weapon
            if (!equippedWeapon.equals(weapon)) {
                // Remove the equipped weapon from the inventory if it's not already there to avoid duplication
                if (!inventory.contains(equippedWeapon)) {
                    inventory.add(equippedWeapon);
                    printAndShowMessage("Your " + equippedWeapon.getLongName() + " has been returned to your inventory.");
                }
            }
        }

        // Equip the new weapon
        equippedWeapon = weapon;

        // Show the equip message
        if (showInUI) {
            System.out.println("You have equipped: " + equippedWeapon.getLongName());
        }

        return true; // Successfully equipped the weapon
    }


    // Method for the player to attack an enemy
    public void attack(String enemyName) {
        if (enemyName == null || enemyName.isEmpty()) {
            printAndShowMessage("You must specify which enemy to attack.");
            return;
        }

        List<Enemy> enemies = currentRoom.getEnemies(); // Retrieve the list of enemies in the current room

        // Check if there are any enemies present
        if (enemies.isEmpty()) {
            printAndShowMessage("There are no enemies to attack.");
            return; // Exit the method if no enemies are found
        }

        // Find the target enemy by name
        Enemy targetEnemy = currentRoom.findEnemy(enemyName);
        if (targetEnemy == null) {
            printAndShowMessage("No enemy by that name here."); // Inform the player if the enemy is not found
            return; // Exit the method if the enemy does not exist
        }

        // Show room description before attack
        ui.clearOutput(); // Clear previous messages
        ui.displayRoomDescription(currentRoom); // Show room description immediately
        printAndShowMessage("-----------------------------------------------------------------"); // Separator line

        // Check if the player has a weapon equipped and it can be used
        if (equippedWeapon != null && equippedWeapon.canUse()) {
            // Calculate the damage dealt to the enemy
            int damageDealt = equippedWeapon.getDamage();
            targetEnemy.takeDamage(damageDealt); // Call the enemy's takeDamage method to reduce health

            // Inform the player of the successful attack
            printAndShowMessage("You attack " + targetEnemy.getName() + " with your " + equippedWeapon.getLongName() + ". " +
                    targetEnemy.getName() + " took " + damageDealt + " damage.");

            // Show the enemy's current health
            printAndShowMessage(targetEnemy.getName() + "'s current health: " + targetEnemy.getHealth() + "/" + targetEnemy.getMaxHealth());

            // Check if the enemy has died as a result of the attack
            if (targetEnemy.isDead()) {
                // Inform that the enemy is defeated and they dropped their weapon
                printAndShowMessage("You have defeated " + targetEnemy.getName() + "! They dropped their weapon for you.");
                currentRoom.removeEnemy(targetEnemy); // Remove the defeated enemy from the room
                return; // Exit since the enemy is dead
            }

            // Enemy counter-attack only if the enemy is still alive
            int enemyDamage = targetEnemy.attack(this); // Call the enemy to attack back

            // Show the enemy's attack message
            printAndShowMessage(targetEnemy.attackMessage()); // Display the attack message

            // Show the player's health after the enemy attack
            printAndShowMessage("You took " + enemyDamage + " damage.");
            ui.showHealth(getHealth(), getMaxHealth());
        } else {
            printAndShowMessage("You have no weapon equipped or your weapon can't be used."); // Notify if the weapon can't be used
        }

        // Check player's health after the enemy's counter-attack
        if (isDead()) {
            printAndShowMessage("You have been defeated.");
            // Call game over handling method here if necessary
        }
    }


    // Method to consume food
    public boolean consumeFood(String foodName) {
        if (foodName == null || foodName.isEmpty()) {
            printAndShowMessage("You must specify which food item to consume.");
            return false;
        }

        // Retrieve the food from the inventory
        Food food = inventory.stream()
                .filter(item -> item instanceof Food && item.getShortName().equalsIgnoreCase(foodName))
                .map(item -> (Food) item)
                .findFirst()
                .orElse(null);

        // Check if the food exists in the inventory
        if (food == null) {
            printAndShowMessage("You don't have that food item.");
            return false; // Exit if the food does not exist
        }

        // Show room description before consuming the food
        ui.clearOutput(); // Clear previous messages
        ui.displayRoomDescription(currentRoom); // Show room description immediately
        printAndShowMessage("-----------------------------------------------------------------"); // Separator line

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

        removeItem(food, false); // Remove food from inventory
        return true; // Return true indicating successful consumption
    }


    // Method for the player to consume a potion
    public boolean consumePotion(String potionName) {
        if (potionName == null || potionName.isEmpty()) {
            printAndShowMessage("You must specify which potion to consume.");
            return false;
        }

        // Retrieve the potion from the inventory
        Potion potion = inventory.stream()
                .filter(item -> item instanceof Potion && item.getShortName().equalsIgnoreCase(potionName))
                .map(item -> (Potion) item)
                .findFirst()
                .orElse(null);

        // Check if the potion exists in the inventory
        if (potion == null) {
            printAndShowMessage("You don't have that potion.");
            return false; // Exit if the potion does not exist
        }

        // Show room description before consuming the potion
        ui.clearOutput(); // Clear previous messages
        ui.displayRoomDescription(currentRoom); // Show room description immediately
        printAndShowMessage("-----------------------------------------------------------------"); // Separator line

        String message;
        if (potion.isPoisonous()) {
            message = "You consumed a poisonous potion! You took " + potion.getHealthRestored() + " damage.";
            takeDamage(potion.getHealthRestored());

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

        removeItem(potion, false); // Remove potion from inventory
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
        // Check if there are any enemies in the current room
        if (!currentRoom.getEnemies().isEmpty()) {
            printAndShowMessage("You cannot move while enemies are present in the room!");
            return false; // Prevent movement if enemies are present
        }

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

    // Method to findItem in inventory
    public Item findItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return item; // Return the item if found
            }
        }
        printAndShowMessage("Item not found in inventory: " + itemName);
        return null; // Return null if not found
    }

    // Method to check if the player has a specific item
    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    // Method to add an item to the inventory (Take item from room)
    public boolean addItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item); // Add the item to inventory
            printAndShowMessage("You have picked up: " + item.getLongName()); // Output to UI and terminal
            return true; // Indicate success
        } else {
            printAndShowMessage("Your inventory is full! Can't add " + item.getLongName()); // Inform player inventory is full
            return false; // Indicate failure
        }
    }

    // Method to remove an item from the inventory (Drop item to room)
    public boolean removeItem(Item item, boolean showDropMessage) {
        if (inventory.contains(item)) {
            inventory.remove(item); // Remove the item from inventory

            // Only show the drop message if we are actually dropping the item
            if (showDropMessage) {
                printAndShowMessage("You dropped " + item.getLongName() + "."); // Notify drop
            }
            return true; // Item removed successfully
        } else {
            printAndShowMessage("Item not found in inventory."); // Inform player if item not found
            return false; // Indicate failure
        }
    }


    // Method to take or drop an item from the current room
    public boolean takeItem(String itemName) {
        Item itemToTake = currentRoom.findItem(itemName);
        if (itemToTake != null) {
            inventory.add(itemToTake); // Add item to inventory
            currentRoom.removeItem(itemToTake); // Remove from room
            return true; // Indicate success
        }
        return false; // Indicate failure
    }


    public boolean dropItem(String itemName) {
        Item itemToDrop = findItemInInventory(itemName);
        if (itemToDrop != null) {
            inventory.remove(itemToDrop); // Remove the item from inventory
            currentRoom.addItem(itemToDrop); // Add the item to the current room
            return true; //success
        } else {
            return false; //failure
        }
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
