import java.io.IOException;

public class Adventure {
    private Room currentRoom;
    private UserInterface ui;
    private SoundManager soundManager;

    public Adventure(UserInterface ui) {
        this.ui = ui;
        this.soundManager = new SoundManager();
        setupRooms();
        loadSounds();
        soundManager.startTheme("resources/sounds/theme_mix.wav"); // Start theme music
    }

    private void setupRooms() {
        // Create rooms
        Room room1 = new Room("The Amusement a long time ago", "This is where your journey begins.");
        Room room2 = new Room("Room 2", "A bright room with a wooden floor.");
        Room room3 = new Room("Room 3", "A dark room with a floating broom in the middle.");
        Room room4 = new Room("Room 4", "You come into a bright room with mirrors as walls.");
        Room room5 = new Room("Room 5", "You found the center of the maze; the room is cold.");
        Room room6 = new Room("Room 6", "A pillar of bones lays beneath you; the room is dark with a torch centered in the middle.");
        Room room7 = new Room("Room 7", "This room is all white, and you hear a chime.");
        Room room8 = new Room("Room 8", "You hear a sound; this room is empty.");
        Room room9 = new Room("Room 9", "This is a big room, with blood splashes all around.");

        // Lock the door to the east in room2
        room2.lockEast();

        // Connect rooms (automatic two-way linking in Room class)
        room1.setEast(room2);
        room1.setSouth(room4);
        room2.setEast(room3);
        room3.setSouth(room6);
        room6.setSouth(room9);
        room9.setWest(room8);
        room8.setNorth(room5);
        room8.setWest(room7);
        room7.setNorth(room4);

        // Start in room1
        currentRoom = room1;
    }

    private void loadSounds() {
        // Load sounds in SoundManager (replace with your actual file paths)
        try {
            soundManager.loadSound("start", "resources/sounds/start_sound.wav");
            soundManager.loadSound("move", "resources/sounds/move_sound.wav");
            soundManager.loadSound("unlock", "resources/sounds/unlock_sound.wav");
            soundManager.loadSound("look", "resources/sounds/look_sound.wav");
            soundManager.loadSound("win", "resources/sounds/win_sound.wav"); // Extra sound for winning
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    public void startGame() {
        try {
            LoadingScreen loadingScreen = new LoadingScreen();
            loadingScreen.displayLoading(); // Display loading animation
        } catch (InterruptedException e) {
            System.err.println("Loading interrupted: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ui.showMessage("Welcome to the Adventure Game!");
        ui.showMessage("Type 'help' to see the available commands.");
        soundManager.playSound("start"); // Play start sound

        // Display the initial room description
        ui.displayRoomDescription(currentRoom);

        boolean running = true;

        while (running) {
            String command = ui.getUserInput().trim().toLowerCase(); // Normalize input

            if (command.startsWith("go ")) {
                move(command.substring(3).trim());
            } else if (command.equals("look")) {
                lookAround();
            } else if (command.equals("help")) {
                ui.showHelp();
            } else if (command.equals("unlock")) {
                unlockDoor();
            } else if (command.equals("exit")) {
                running = false;
                ui.showMessage("Exiting the game.");
            } else {
                ui.showMessage("Invalid command.");
            }
        }
        soundManager.close(); // Close SoundManager on game exit
    }

    private void move(String direction) {
        Room nextRoom = null;

        switch (direction) {
            case "north":
                nextRoom = currentRoom.getNorth();
                break;
            case "south":
                nextRoom = currentRoom.getSouth();
                break;
            case "east":
                if (currentRoom.isEastLocked()) {
                    ui.showMessage("The door to the east is locked.");
                    return;
                }
                nextRoom = currentRoom.getEast();
                soundManager.lowerThemeVolume(); // Lower theme music
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                ui.showMessage("Unknown direction: " + direction);
                return;
        }

        if (nextRoom != null) {
            currentRoom = nextRoom;
            ui.displayRoomDescription(currentRoom);  // Display the room description immediately
            currentRoom.setVisited(true);  // Mark room as visited after displaying description
        } else {
            ui.showMessage("You cannot go that way.");
        }
    }

    private void lookAround() {
        ui.displayRoomDescription(currentRoom); // Reuse the room description method
        soundManager.playSound("look"); // Play sound for looking around
    }

    private void unlockDoor() {
        if (currentRoom.isEastLocked()) {
            currentRoom.unlockEast();
            ui.showMessage("The door to the east is now unlocked.");
            soundManager.playSound("unlock"); // Play sound for unlocking
        } else {
            ui.showMessage("There is no locked door here.");
        }
    }

    // Placeholder for any additional command processing logic
    public void processCommand(String input) {
        // This method can be implemented if needed for future commands
    }
}
