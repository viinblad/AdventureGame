import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class GameController {
    private Player player; // Instance of Player to manage player state
    private Enemy enemy; // Instance of the current enemy in the room
    private UserInterface ui; // User interface for displaying messages and getting input
    private SoundManager soundManager; // Manages game sounds
    private Map gameMap; // Instance of the Map class for displaying the game map

    // Constructor that initializes the UserInterface, SoundManager, Map, and Player
    public GameController(UserInterface ui) {
        this.ui = ui; // Set the user interface
        this.soundManager = new SoundManager(); // Initialize sound manager
        this.gameMap = new Map(); // Initialize the Map instance
        initializeGame(); // Initialize the game and create the Player
        soundManager.startTheme("resources/sounds/theme_mix.wav"); // Start background theme music
    }
    // Start the game and display the loading screen
    public void startGame() {
        System.out.println("Starting the game...");
        new Thread(() -> {
            LoadingScreen loadingScreen = new LoadingScreen();
            try {
                loadingScreen.displayLoading(); // Display loading animation
            } catch (InterruptedException | IOException e) {
                System.err.println("Loading error: " + e.getMessage());
                return; // Exit if loading fails
            }

            SwingUtilities.invokeLater(() -> {
                if (ui == null) {
                    System.err.println("UI is not initialized.");
                    return;
                }

                ui.showMessage("Welcome to the Adventure Game!");
                ui.showMessage("Type 'help' to see the available commands.");
                soundManager.playSound("start"); // Play sound for starting the game

                if (player.getCurrentRoom() != null) {
                    ui.displayRoomDescription(player.getCurrentRoom()); // Display current room
                } else {
                    System.err.println("Current room is not initialized.");
                }
            });
        }).start(); // Start the loading thread
    }

    // Initialize the game by setting the starting room and creating a Player
    private void initializeGame() {
        Room startingRoom = gameMap.getStartingRoom(); // Get the starting room from the game map
        player = new Player(startingRoom, ui); // Initialize Player with the starting room
        updateCurrentEnemy(); // Check for enemies in the starting room
    }

    // Update the current enemy based on the player's current room
    private void updateCurrentEnemy() {
        Room currentRoom = player.getCurrentRoom();
        List<Enemy> enemies = currentRoom.getEnemies();
        enemy = (enemies.isEmpty()) ? null : enemies.get(0); // Set the enemy to the first one found
    }



    // Process user commands
    public void processCommand(String command) {
        command = command.trim().toLowerCase(); // Normalize input

        // Clear the output area before processing the command
        ui.clearOutput(); // Clear previous text/output

        // Command processing
        switch (command) {
            case String cmd when cmd.startsWith("go "): // Check for movement commands
                handleMovement(command);
                break;
            case "look": // Command to look around
                lookAround(); // Describe the current room and show items
                break;
            case "help": // Command to show help
                ui.showHelp(); // Display help instructions
                break;
            case "unlock": // Command to unlock a door
                unlockDoor(); // Attempt to unlock the door
                break;
            case "show map": // Command to show the map
                showMap(); // Show the map when the user types 'show map'
                break;
            case "health": // Command to show player's health
                showHealth(); // Show the player's health
                break;
            case String cmd when cmd.startsWith("eat "): // Eat food
                handleFoodConsumption(command);
                break;
            case String cmd when cmd.startsWith("drink "): // Drink potion
                handlePotionConsumption(command);
                break;
            case String cmd when cmd.startsWith("take "): // Take an item
                handleItemTake(command);
                break;
            case String cmd when cmd.startsWith("drop "): // Drop an item
                handleItemDrop(command);
                break;
            case String cmd when cmd.startsWith("equip "): // Equip a weapon
                handleEquipWeapon(command);
                break;
            case String cmd when cmd.startsWith("attack"): // Command to attack
                String enemyName = command.substring(7).trim(); // Extract enemy name
                handleAttack(enemyName);
                break;
            case "inventory": // Command to show inventory
                player.displayInventory(); // Show the player's inventory
                break;
            case "exit": // Command to exit the game
                ui.showMessage("Exiting the game.");
                soundManager.close(); // Close SoundManager when exiting the game
                System.exit(0); // Exit the application
                break;
            default: // Handle unknown commands
                ui.showMessage("Invalid command.");
                break;
        }
    }


    //method to handle player attack and fight with enemy
    public void handleAttack(String enemyName) {
        updateCurrentEnemy(); // Ensure the current enemy is up to date

        if (enemy == null) {
            ui.showMessage("There are no enemies to attack.");
            return;
        }

        // Clear output before displaying the attack
        ui.clearOutput();
        player.attack(enemyName); // Call the player's attack method

        // After the attack, check if the enemy is dead
        if (enemy.isDead()) {
            enemy.dropWeapon(player.getCurrentRoom()); // Drop the enemy's weapon into the room
            player.getCurrentRoom().removeEnemy(enemy); // Remove the enemy from the room
            enemy = null; // Reset current enemy since it's defeated
            updateCurrentEnemy(); // Update the enemy after removing
        }
    }

    // Handle player equipping a weapon
    public void handleEquipWeapon(String command) {
        String weaponName = command.substring(6).trim(); // Extract the weapon name
        if (!weaponName.isEmpty()) {
            Weapon weapon = (Weapon) player.findItemInInventory(weaponName);
            if (weapon != null) {
                if (player.equipWeapon(weapon)) {
                    ui.showMessage("You have equipped " + weapon.getLongName() + ".");
                } else {
                    ui.showMessage("You don't have that weapon.");
                }
            } else {
                ui.showMessage("You don't have that weapon.");
            }
        } else {
            ui.showMessage("Equip what?");
        }
    }

    // Handle player consuming food
    void handleFoodConsumption(String command) {
        String foodName = command.substring(4).trim(); // Extract the food name
        if (!foodName.isEmpty()) {
            Food food = (Food) player.findItemInInventory(foodName);
            if (food != null) {
                player.consumeFood(food); // Consume food
            } else {
                ui.showMessage("You don't have that food item.");
            }
        } else {
            ui.showMessage("Eat what?");
        }
    }

    // Handle player consuming a potion
    void handlePotionConsumption(String command) {
        String potionName = command.substring(6).trim(); // Extract the potion name
        if (!potionName.isEmpty()) {
            Potion potion = (Potion) player.findItemInInventory(potionName);
            if (potion != null) {
                player.consumePotion(potion); // Consume potion
            } else {
                ui.showMessage("You don't have that potion item.");
            }
        } else {
            ui.showMessage("Drink what?");
        }
    }

    // Show the player's current health
    public void showHealth() {
        ui.showHealth(player.getHealth(), player.getMaxHealth()); // Update health in the UI
    }

    // Handle player movement
    private void handleMovement(String command) {
        String direction = command.substring(3).trim(); // Extract direction
        if (player.canMove(direction)) { // Check if movement is possible
            new Thread(() -> {
                LoadingScreen loadingScreen = new LoadingScreen();
                try {
                    loadingScreen.displayLoading(); // Display loading animation
                    boolean moved = player.move(direction); // Move player
                    SwingUtilities.invokeLater(() -> {
                        if (moved) {
                            ui.displayRoomDescription(player.getCurrentRoom()); // Display new room description
                            ui.showHealth(player.getHealth(), player.getMaxHealth()); // Update health display
                            updateCurrentEnemy(); // Update current enemy after moving
                        } else {
                            ui.showMessage("You cannot go that way.");
                        }
                    });
                } catch (InterruptedException | IOException e) {
                    System.err.println("Loading error: " + e.getMessage());
                }
            }).start();
        } else {
            ui.showMessage("You cannot go that way.");
        }
    }

    // Handle taking an item
    private void handleItemTake(String command) {
        String itemToTake = command.substring(5).trim(); // Extract the item name
        if (!itemToTake.isEmpty()) {
            boolean success = takeItem(itemToTake);
            if (success) {
                ui.showItemPickedUp(itemToTake); // Notify UI about item pickup
            } else {
                ui.showMessage("Item not found or can't be picked up.");
            }
        } else {
            ui.showMessage("Take what?");
        }
    }

    // Handle dropping an item
    private void handleItemDrop(String command) {
        String itemToDrop = command.substring(5).trim(); // Extract the item name
        if (!itemToDrop.isEmpty()) {
            boolean success = dropItem(itemToDrop);
            if (success) {
                ui.showItemDropped(itemToDrop); // Notify the user of item drop
            } else {
                ui.showMessage("You don't have that item.");
            }
        } else {
            ui.showMessage("Drop what?");
        }
    }

    // Look around the current room
    private void lookAround() {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        ui.displayRoomDescription(currentRoom); // Show the room description
        soundManager.playSound("look"); // Play sound when looking around

        // Display both items and enemies in the room
        ui.showRoomItemsAndEnemies(currentRoom); // Call the UI method to display items and enemies


    }


    // Unlock a door in the current room
    private void unlockDoor() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.isEastLocked()) {
            currentRoom.unlockEast();
            ui.showMessage("The door to the east is now unlocked.");
            soundManager.playSound("unlock");
        } else {
            ui.showMessage("There is no locked door here.");
        }
    }

    // Show the map of the current room
    private void showMap() {
        Room currentRoom = player.getCurrentRoom();
        String eastRoomName = currentRoom.getEast() != null ? currentRoom.getEast().getName() : "Locked";
        String southRoomName = currentRoom.getSouth() != null ? currentRoom.getSouth().getName() : "Locked";
        String westRoomName = currentRoom.getWest() != null ? currentRoom.getWest().getName() : "Locked";
        String northRoomName = currentRoom.getNorth() != null ? currentRoom.getNorth().getName() : "Locked";

        // Get the enemies in the current room
        List<Enemy> enemies = currentRoom.getEnemies();
        StringBuilder enemyNames = new StringBuilder();
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                enemyNames.append(enemy.getName()).append(", "); // Append enemy names
            }
            // Remove the last comma and space
            enemyNames.setLength(enemyNames.length() - 2);
        } else {
            enemyNames.append("None"); // No enemies present
        }

        // Show the map with the enemies
        ui.showMap(currentRoom.getName(), eastRoomName, southRoomName, westRoomName, northRoomName, enemyNames.toString());
    }

    // Take an item from the current room
    boolean takeItem(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item itemToTake = currentRoom.findItem(itemName);
        if (itemToTake != null) {
            boolean success = player.takeItem(itemToTake.getShortName());
            return success;
        } else {
            ui.showMessage("There is no " + itemName + " here.");
            return false;
        }
    }

    // Drop an item into the current room
    boolean dropItem(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item itemToDrop = player.findItemInInventory(itemName);
        if (itemToDrop != null) {
            boolean success = player.dropItem(itemToDrop.getShortName());
            if (success) {
                currentRoom.addItem(itemToDrop);
            }
            return success;
        } else {
            ui.showMessage("You don't have " + itemName + " in your inventory.");
            return false;
        }
    }

    // New methods for UserInterface to avoid errors
    public Room getCurrentRoom() {
        return player.getCurrentRoom();
    }

    public List<Item> getPlayerInventory() {
        return player.getInventory(); // Return the player's inventory
    }

    public Weapon getEquippedWeapon() {
        return player.getEquippedWeapon(); // Return the currently equipped weapon


    }
}
