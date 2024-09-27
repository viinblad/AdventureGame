public class GUIUserInterface {
    private GameFrame gameFrame; // Reference to the GameFrame

    public GUIUserInterface(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    // Method to show messages to the user in the JTextArea
    public void showMessage(String message) {
        printOverlayedTextBox(message);
    }

    // Method to display room descriptions in the JTextArea
    public void displayRoomDescription(Room room) {
        String description = "You are in " + room.getName() + "\n" + room.getDescription();
        printOverlayedTextBox(description);
    }

    // Method to show help to the player in the JTextArea
    public void showHelp() {
        String helpMessage = "Available commands:\n" +
                "go north, go south, go east, go west - to move between rooms.\n" +
                "look - to describe the current room.\n" +
                "exit - to quit the game.\n" +
                "help - to show this message.";
        printOverlayedTextBox(helpMessage);
    }

    // Helper method to append overlayed text to the JTextArea in GameFrame
    private void printOverlayedTextBox(String message) {
        int width = 60; // Set width for the overlay
        String overlayedMessage = "=".repeat(width) + "\n" + message + "\n" + "=".repeat(width) + "\n";
        gameFrame.getTextArea().append(overlayedMessage); // Append to JTextArea in GameFrame
    }
}
