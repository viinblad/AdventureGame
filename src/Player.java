public class Player {
    private Room currentRoom;

    // Constructor
    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
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
            return true; // Move successful
        } else {
            System.out.println("You can't go that way!");
            return false; // Move unsuccessful
        }
    }

    // Getter for the current room
    public Room getCurrentRoom() {
        return currentRoom;
    }
}
