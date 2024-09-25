import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    // Metode til at få input fra brugeren
    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    // Metode til at vise beskeder til brugeren
    public void showMessage(String message) {
        System.out.println(message);
    }

    // Metode til at vise hjælp til spilleren
    public void showHelp() {
        showMessage("Available commands:");
        showMessage("go north, go south, go east, go west - to move between rooms.");
        showMessage("look - to describe the current room.");
        showMessage("exit - to quit the game.");
        showMessage("help - to show this message.");
    }
}
