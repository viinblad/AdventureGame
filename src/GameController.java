import java.io.IOException;

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
        new Thread(() -> {
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
            if (player.getCurrentRoom() != null) {
                ui.displayRoomDescription(player.getCurrentRoom()); // Use Player to get current room
            } else {
                System.err.println("Current room is not initialized.");
            }
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
                String direction = command.substring(3).trim(); // Extract direction
                if (player.canMove(direction)) { // Check if movement is possible
                    // Only show loading screen if we can move
                    new Thread(() -> {
                        LoadingScreen loadingScreen = new LoadingScreen();
                        try {
                            loadingScreen.displayLoading(); // Display loading animation
                            boolean moved = player.move(direction); // Move player
                            if (moved) {
                                ui.displayRoomDescription(player.getCurrentRoom()); // Display new room description
                            } else {
                                ui.showMessage("You cannot go that way."); // Handle invalid movements
                            }
                        } catch (InterruptedException | IOException e) {
                            System.err.println("Loading error: " + e.getMessage());
                        }
                    }).start();
                } else {
                    ui.showMessage("You cannot go that way."); // Inform player about the blocked direction
                }
                break;

            case "look": // Command to look around
                lookAround(); // Describe the current room
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

    // Look around the current room
    private void lookAround() {
        // Display the current room's description
        ui.displayRoomDescription(player.getCurrentRoom()); // Use Player to get current room
        soundManager.playSound("look"); // Play sound when looking around
    }

    // Attempt to unlock a door in the current room
    private void unlockDoor() {
        // Check if the east door is locked and unlock it if possible
        if (player.getCurrentRoom().isEastLocked()) {
            player.getCurrentRoom().unlockEast(); // Unlock the door
            ui.showMessage("The door to the east is now unlocked."); // Inform the user
            soundManager.playSound("unlock"); // Play sound for unlocking
        } else {
            ui.showMessage("There is no locked door here."); // Handle case where no door is locked
        }
    }

    // Show the map of the current room
    private void showMap() {
        // Retrieve room names for the current, east, south, and west rooms
        String currentRoomName = player.getCurrentRoom().getName(); // Get the current room's name
        String eastRoomName = player.getCurrentRoom().getEast() != null ? player.getCurrentRoom().getEast().getName() : "Locked";
        String southRoomName = player.getCurrentRoom().getSouth() != null ? player.getCurrentRoom().getSouth().getName() : "Locked";
        String westRoomName = player.getCurrentRoom().getWest() != null ? player.getCurrentRoom().getWest().getName() : "Locked";

        // Call the showMap method from the UserInterface
        ui.showMap(currentRoomName, eastRoomName, southRoomName, westRoomName);
    }
}
