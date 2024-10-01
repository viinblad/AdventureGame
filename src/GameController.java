import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class GameController {
    private Player player; // Instance of Player to manage player state
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

    // Initialize the game by setting the starting room and creating a Player
    private void initializeGame() {
        Room startingRoom = gameMap.getStartingRoom(); // Get the starting room from the game map
        player = new Player(startingRoom); // Initialize Player with the starting room
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
                    ui.displayRoomDescription(player.getCurrentRoom()); // Use Player to get current room
                } else {
                    System.err.println("Current room is not initialized.");
                }

            });
        }).start(); // Start the loading thread
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

            case String cmd when cmd.startsWith("take "): // Take an item
                handleItemTake(command);
                break;

            case String cmd when cmd.startsWith("drop "): // Drop an item
                handleItemDrop(command);
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
                        } else {
                            ui.showMessage("You cannot go that way."); // Handle invalid movements
                        }
                    });
                } catch (InterruptedException | IOException e) {
                    System.err.println("Loading error: " + e.getMessage());
                }
            }).start();
        } else {
            ui.showMessage("You cannot go that way."); // Inform player about the blocked direction
        }
    }

    // Handle taking an item
    private void handleItemTake(String command) {
        String itemToTake = command.substring(5).trim(); // Extract the item name
        if (!itemToTake.isEmpty()) {
            boolean success = takeItem(itemToTake); // Call takeItem method
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
            boolean success = dropItem(itemToDrop); // Call dropItem method
            if (success) {
                ui.showItemDropped(itemToDrop); // Notify UI about item drop
            } else {
                ui.showMessage("You don't have that item.");
            }
        } else {
            ui.showMessage("Drop what?");
        }
    }

    // Look around the current room
    private void lookAround() {
        // Display the current room's description
        ui.displayRoomDescription(player.getCurrentRoom()); // Use Player to get current room
        soundManager.playSound("look"); // Play sound when looking around

        // Display items in the current room
        ui.showRoomItems(player.getCurrentRoom()); // Show items in the room
    }

    // Attempt to unlock a door in the current room
    private void unlockDoor() {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        // Check if the east door is locked and unlock it if possible
        if (currentRoom.isEastLocked()) {
            currentRoom.unlockEast(); // Unlock the door
            ui.showMessage("The door to the east is now unlocked."); // Inform the user
            soundManager.playSound("unlock"); // Play sound for unlocking
        } else {
            ui.showMessage("There is no locked door here."); // Handle case where no door is locked
        }
    }

    // Show the map of the current room
    private void showMap() {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        // Retrieve room names for the current, east, south, and west rooms
        String currentRoomName = currentRoom.getName(); // Get the current room's name
        String eastRoomName = currentRoom.getEast() != null ? currentRoom.getEast().getName() : "Locked";
        String southRoomName = currentRoom.getSouth() != null ? currentRoom.getSouth().getName() : "Locked";
        String westRoomName = currentRoom.getWest() != null ? currentRoom.getWest().getName() : "Locked";
        String northRoomName = currentRoom.getNorth() != null ? currentRoom.getNorth().getName() : "Locked";

        // Call the showMap method from the UserInterface
        ui.showMap(currentRoomName, eastRoomName, southRoomName, westRoomName, northRoomName);
    }

    // Player class getter for current room
    public Room getCurrentRoom() {
        return player.getCurrentRoom();
    }

    // Player class getter for PlayerInventory
    public List<Item> getPlayerInventory() {
        return player.getInventory(); // Assuming player has a method to get inventory
    }

    // Take an item from the current room
    boolean takeItem(String itemName) {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        Item itemToTake = currentRoom.findItem(itemName); // Use findItem to get the Item
        if (itemToTake != null) {
            boolean success = player.takeItem(itemToTake.getShortName()); // Use short name to take the item
            return success; // Return the success status
        } else {
            ui.showMessage("There is no " + itemName + " here."); // Inform the user if the item is not found
            return false; // Item taking failed
        }
    }

    // Drop an item to the current room
    boolean dropItem(String itemName) {
        Room currentRoom = player.getCurrentRoom(); // Get the current room
        Item itemToDrop = player.findItemInInventory(itemName); // Corrected method name
        if (itemToDrop != null) { // Check if the item exists in inventory
            boolean success = player.dropItem(itemToDrop.getShortName()); // Call the dropItem method on Player
            if (success) {
                currentRoom.addItem(itemToDrop); // Add the item back to the room after dropping it
            }
            return success;
        } else {
            ui.showMessage("You don't have " + itemName + " in your inventory."); // Handle case where item is not found
            return false; // Item was not found to drop
        }
    }
}
