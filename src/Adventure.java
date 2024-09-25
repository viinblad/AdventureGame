public class Adventure {
    private Room currentRoom;
    private UserInterface ui;

    public Adventure(UserInterface ui) {
        this.ui = ui;
        setupRooms();
    }

    private void setupRooms() {
        // Opret rummene
        Room room1 = new Room("Room 1", "This is a small room with stone walls.");
        Room room2 = new Room("Room 2", "A bright room with a wooden floor.");
        Room room3 = new Room("Room 3", "A dunkel room with a floating broom in the middle.");
        Room room4 = new Room ("Room 4", "You come into a bright room with mirrors as walls");
        Room room5 = new Room("Room 5", "You found the center of the maze, the rooms is cold");
        Room room6 = new Room("Room 6", "A pillar of bones lays beneath you feeds the room is dark, a touch is centered in the middle");
        Room room7 = new Room("Room 7", "This room is all white you hear a chime");
        Room room8 = new Room("Room 8", "You hear a sound this room is empty");
        Room room9 = new Room("Room 9", "This is a big room, you see blood splashes all around you ");


        // Forbind rummene
        room1.setEast(room2);
        room2.setWest(room1);

        room2.setEast(room3);  // Forbind room2 til room3 mod øst
        room3.setWest(room2);  // Forbind room3 til room2 mod vest

        room1.setSouth(room4); // Forbind room 1 til room 4 mod syd
        room4.setNorth(room1); // Forbind room 4 til room 1 mod nord

        room3.setSouth(room6);// Forbind room 3 til room 6 mod syd
        room6.setNorth(room3); // Forbind room 6 til room 3 mod nord

        room6.setSouth(room9); // Forbind room 6 til room 9 mod syd
        room9.setNorth(room6); // Forbind room 9 til room 6 mod nord

        room9.setWest(room8); // Forbind room 9 til room 8 mod vest
        room8.setEast(room9); // Forbind room 8 til room 9 mod øst

        room8.setNorth(room5); // Forbind room 9 til room 5 mod nord
        room5.setSouth(room8); // Forbind room 5 til room 9 mod syd

        room8.setWest(room7); // Forbind room 8 til room 7 mod vest
        room7.setEast(room8); // Forbind room 7 til room 8 mod øst

        room7.setNorth(room4); // Forbind room 7 til room 4 mod nord
        room4.setSouth(room7); // Forbind room 4 til room 7 mod syd



        // Start i room1
        currentRoom = room1;
    }

    public void startGame() {
        ui.showMessage("Welcome to the Adventure Game!");
        ui.showMessage("Type 'help' to see the available commands.");
        boolean running = true;

        while (running) {
            String command = ui.getUserInput();

            if (command.startsWith("go")) {
                move(command.substring(3).toLowerCase());
            } else if (command.equals("look")) {
                lookAround();
            } else if (command.equals("help")) {
                ui.showHelp();
            } else if (command.equals("exit")) {
                running = false;
                ui.showMessage("Exiting the game.");
            } else {
                ui.showMessage("Invalid command.");
            }
        }
    }

    private void move(String direction) {
        Room nextRoom = null;

        switch (direction) {
            case "north": nextRoom = currentRoom.getNorth(); break;
            case "south": nextRoom = currentRoom.getSouth(); break;
            case "east": nextRoom = currentRoom.getEast(); break;
            case "west": nextRoom = currentRoom.getWest(); break;
            default: ui.showMessage("Unknown direction: " + direction); return;
        }

        if (nextRoom != null) {
            currentRoom = nextRoom;
            lookAround();
        } else {
            ui.showMessage("You cannot go that way.");
        }
    }

    private void lookAround() {
        ui.showMessage("You are in " + currentRoom.getName());
        ui.showMessage(currentRoom.getDescription());
    }
}
