import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
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
        soundManager.startTheme(); // Start background theme music
    }

    // Start the game and display the loading screen
    public void startGame() {
        System.out.println("Starting the game...");

        // Start a new thread for the game start process
        new Thread(() -> {
            // Show the lore presentation first
            LorePresentation lorePresentation = new LorePresentation();
            try {
                lorePresentation.displayLore(); // Display the lore presentation
            } catch (InterruptedException e) {
                System.err.println("Lore presentation error: " + e.getMessage());
                return; // Exit if lore presentation fails
            }

            // After the lore presentation, show the loading screen
            LoadingScreen loadingScreen = new LoadingScreen();
            try {
                loadingScreen.displayLoading(); // Display loading animation
            } catch (InterruptedException | IOException e) {
                System.err.println("Loading error: " + e.getMessage());
                return; // Exit if loading fails
            }

            // Continue with the game UI after loading is complete
            SwingUtilities.invokeLater(() -> {
                if (ui == null) {
                    System.err.println("UI is not initialized.");
                    return;
                }

                ui.showMessage("Welcome to the Adventure Game!");
                ui.showMessage("Type 'help' to see the available commands.");
                // soundManager.playSound("start"); // Uncomment to play sound for starting the game

                if (player.getCurrentRoom() != null) {
                    ui.displayRoomDescription(player.getCurrentRoom()); // Display current room
                } else {
                    System.err.println("Current room is not initialized.");
                }
            });
        }).start(); // Start the game start process thread
    }

    // Method to ensure audio
    public void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions for debugging
        }
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
                unlockDoor();// Attempt to unlock the door
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
        // Check player's health after attack
        if (player.isDead()) {
            handleGameOver(); // Call game over handling if player is dead
        }
    }

    // Handle game over scenario
    private void handleGameOver() {
        ui.showMessage("You have died - GAME OVER."); // Show death message

        // Use a new thread to wait for a while before closing the game
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Sleep for 3000 milliseconds (3 seconds)
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle interruption
            }
            SwingUtilities.invokeLater(this::closeGameAndStartNewView); // Close the game on the EDT
        }).start();
    }

    // Method to close the current game and start the StartView
    private void closeGameAndStartNewView() {
        soundManager.close(); // Stop any sounds
        // Assuming StartView has a static method to initialize and show it
        // Launch StartView
        SwingUtilities.invokeLater(StartView::start);
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
        // Ensure the command is at least 5 characters long to avoid IndexOutOfBoundsException
        if (command.length() < 5) {
            ui.showMessage("Eat what?");
            return;
        }

        String foodName = command.substring(4).trim(); // Extract the food name
        if (!foodName.isEmpty()) {
            // Attempt to consume the food using the player's method
            if (player.consumeFood(foodName)) {
                // If the food was successfully consumed, print the consumption message
                System.out.println("You consumed " + foodName + "."); // Confirmation message for console
            }
            // No need to show a message here if the food is not found; it will be handled in consumeFood
        } else {
            ui.showMessage("Eat what?");
        }
    }


    // Handle player consuming a potion
    void handlePotionConsumption(String command) {
        // Ensure the command is at least 7 characters long to avoid IndexOutOfBoundsException
        if (command.length() < 7) {
            ui.showMessage("Drink what?");
            return;
        }

        String potionName = command.substring(6).trim(); // Extract the potion name
        if (!potionName.isEmpty()) {
            // Attempt to consume the potion using the player's method
            if (player.consumePotion(potionName)) {
                // If the potion was successfully consumed, print the consumption message
                System.out.println("You consumed " + potionName + "."); // Confirmation message for console
            } else {
                // This message will already be printed in consumePotion if the potion is not found
                // ui.showMessage("You don't have that potion item."); // No need for this line
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
                            soundManager.playSoundEffect("move"); // Play move sound effect
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
            Item item = player.getCurrentRoom().findItem(itemToTake); // Find the item in the room
            if (item != null && player.takeItem(item.getShortName())) { // Use short name for taking
                ui.showItemPickedUp(item.getLongName()); // Pass the long name of the item to showItemPickedUp
            } else {
                ui.showMessage("Item not found or can't be picked up."); // Handle failure
            }
        } else {
            ui.showMessage("Take what?");
        }
    }

    // Handle dropping an item
    private void handleItemDrop(String command) {
        String itemToDrop = command.substring(5).trim(); // Extract the item name
        if (!itemToDrop.isEmpty()) {
            Item item = player.findItemInInventory(itemToDrop); // Find the item in the player's inventory
            if (item != null && player.dropItem(item.getShortName())) { // Use short name for dropping
                player.getCurrentRoom().addItem(item); // Add the item back to the room
                ui.showItemDropped(item.getLongName()); // Pass the long name of the item to showItemDropped
            } else {
                ui.showMessage("You don't have " + itemToDrop + " in your inventory."); // Handle failure
            }
        } else {
            ui.showMessage("Drop what?");
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        ui.displayRoomDescription(currentRoom); // Show the room description
        System.out.println("Attempting to play 'look' sound effect..."); // Debug line
        soundManager.playSoundEffect("look"); // Play sound when looking around
        ui.showRoomItemsAndEnemies(currentRoom); // Call the UI method to display items and enemies
    }


    // Unlock a door in the current room
    private void unlockDoor() {
        Room currentRoom = player.getCurrentRoom();

        // Check if the player has the magic key
        if (player.hasItem("Magic Key")) {
            if (currentRoom.isEastLocked()) {
                currentRoom.unlockEast();
                ui.showMessage("The door to the east is now unlocked.");
                soundManager.playSoundEffect("unlock");
            } else {
                ui.showMessage("There is no locked door here.");
            }
        } else {
            ui.showMessage("You need a Magic Key to unlock this door.");
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
    public Item takeItem(String itemName) {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        Item itemToTake = currentRoom.findItem(itemName); // Find the item in the room
        if (itemToTake != null && player.takeItem(itemToTake.getShortName())) { // Check if the item is successfully taken
            return itemToTake; // Return the Item object if successful
        }
        return null; // Return null if the item cannot be taken
    }


    // Drop an item from the inventory
    public Item dropItem(String itemName) {
        Item itemToDrop = player.findItemInInventory(itemName); // Find the item in the player's inventory
        if (itemToDrop != null && player.dropItem(itemToDrop.getShortName())) { // Check if the item is successfully dropped
            player.getCurrentRoom().addItem(itemToDrop); // Add the dropped item to the room
            return itemToDrop; // Return the Item object if successful
        }
        return null; // Return null if the item cannot be dropped
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
