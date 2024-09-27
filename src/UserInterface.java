import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    // Method to get input from the user
    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    // Method to show messages to the user in a styled format
    public void showMessage(String message) {
        printOverlayedTextBox(message);
    }

    // Method to display room descriptions in an overlay style
    public void displayRoomDescription(Room room) {
        String description = "You are in " + room.getName() + "\n" + room.getDescription();
        printOverlayedTextBox(description);
    }

    // Method to show help to the player
    public void showHelp() {
        String helpMessage = "Available commands:\n" +
                "go north, go south, go east, go west - to move between rooms.\n" +
                "look - to describe the current room.\n" +
                "exit - to quit the game.\n" +
                "help - to show this message.";
        printOverlayedTextBox(helpMessage);
    }

    // Optional: Close the scanner to prevent resource leaks
    public void close() {
        scanner.close();
    }

    // Helper method to print messages in an overlay text box style
    private void printOverlayedTextBox(String message) {
        int width = 60; // Set width for the overlay
        System.out.println("=".repeat(width));
        System.out.println(message);
        System.out.println("=".repeat(width));
    }
}
