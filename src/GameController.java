import java.io.IOException;

public class GameController {
    private Room currentRoom; // The room the player is currently in
    private UserInterface ui; // User interface for displaying messages and getting input
    private SoundManager soundManager; // Manages game sounds
    private Map gameMap; // Instance of the Map class for displaying the game map

    public GameController(UserInterface ui) {
        this.ui = ui; // Set the user interface
        this.soundManager = new SoundManager(); // Initialize sound manager
        this.gameMap = new Map(); // Initialize the Map instance
        initializeGame(); // Call initializeGame to set the starting room
        soundManager.startTheme("resources/sounds/theme_mix.wav"); // Start background theme music
    }

    public void initializeGame() {
        currentRoom = gameMap.getStartingRoom(); // Set the starting room from the game map
    }

    public void startGame() {
        // Start a new thread for loading screen to avoid blocking the main thread
        new Thread(() -> {
            // Display loading screen while game assets are being loaded
            LoadingScreen loadingScreen = new LoadingScreen();
            try {
                loadingScreen.displayLoading(); // Display loading animation
            } catch (InterruptedException | IOException e) {
                System.err.println("Loading error: " + e.getMessage());
                return; // Exit if loading fails
            }

            // After loading, update the UI on the main thread
            ui.showMessage("Welcome to the Adventure Game!");
            ui.showMessage("Type 'help' to see the available commands.");
            soundManager.playSound("start"); // Play sound for starting the game

            // Display the initial room description
            if (currentRoom != null) {
                ui.displayRoomDescription(currentRoom);
            } else {
                System.err.println("Current room is not initialized.");
            }
        }).start(); // Start the loading thread
    }

    public void processCommand(String command) {
        command = command.trim().toLowerCase(); // Normalize input

        // Clear the output area before processing the command
        ui.clearOutput();  // Clear previous text/output

        // Command processing
        switch (command) {
            case String cmd when cmd.startsWith("go "):
                // Start loading screen in a new thread before moving
                String finalCommand = command;
                new Thread(() -> {
                    LoadingScreen loadingScreen = new LoadingScreen();
                    try {
                        loadingScreen.displayLoading(); // Display loading animation
                        move(finalCommand.substring(3).trim()); // Extract and move in the specified direction
                    } catch (InterruptedException | IOException e) {
                        System.err.println("Loading error: " + e.getMessage());
                    }
                }).start();
                break;
            case "look":
                lookAround(); // Describe the current room
                break;
            case "help":
                ui.showHelp(); // Display help instructions
                break;
            case "unlock":
                unlockDoor(); // Attempt to unlock the door
                break;
            case "show map":
                showMap(); // Show the map when the user types 'show map'
                break;
            case "exit":
                ui.showMessage("Exiting the game.");
                soundManager.close(); // Close SoundManager when exiting the game
                System.exit(0); // Exit the application
                break;
            default:
                ui.showMessage("Invalid command."); // Handle unknown commands
                break;
        }
    }

    private void move(String direction) {
        // Determine the next room based on the direction the player wants to move
        Room nextRoom = null;

        switch (direction) {
            case "north":
                nextRoom = currentRoom.getNorth();
                break;
            case "south":
                nextRoom = currentRoom.getSouth();
                break;
            case "east":
                if (currentRoom.isEastLocked()) { // Check if the door is locked
                    ui.showMessage("The door to the east is locked.");
                    return; // Exit if locked
                }
                nextRoom = currentRoom.getEast(); // Get the next room if it's not locked
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                ui.showMessage("Unknown direction: " + direction); // Handle unknown directions
                return;
        }

        // Update the current room if movement is possible
        if (nextRoom != null) {
            currentRoom = nextRoom; // Move to the next room
            ui.displayRoomDescription(currentRoom); // Display the new room's description
            currentRoom.setVisited(true); // Mark room as visited after displaying description
        } else {
            ui.showMessage("You cannot go that way."); // Handle invalid movements
        }
    }


    private void lookAround() {
        // Display the current room's description
        ui.displayRoomDescription(currentRoom);
        soundManager.playSound("look"); // Play sound when looking around
    }

    private void unlockDoor() {
        // Attempt to unlock the door in the current room
        if (currentRoom.isEastLocked()) { // Check if the east door is locked
            currentRoom.unlockEast(); // Unlock the door
            ui.showMessage("The door to the east is now unlocked."); // Inform the user
            soundManager.playSound("unlock"); // Play sound for unlocking
        } else {
            ui.showMessage("There is no locked door here."); // Handle case where no door is locked
        }
    }

    private void showMap() {
        // Retrieve room names for the current, east, south, and west rooms
        String currentRoomName = currentRoom.getName();
        String eastRoomName = currentRoom.getEast() != null ? currentRoom.getEast().getName() : "Locked";
        String southRoomName = currentRoom.getSouth() != null ? currentRoom.getSouth().getName() : "Locked";
        String westRoomName = currentRoom.getWest() != null ? currentRoom.getWest().getName() : "Locked";

        // Call the showMap method from the UserInterface
        ui.showMap(currentRoomName, eastRoomName, southRoomName, westRoomName);
    }
}
