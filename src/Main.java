import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface(); // Create UserInterface instance
            GameController gameController = new GameController(ui); // Pass UI to GameController
            ui.setGameController(gameController); // Set GameController in UserInterface

            // Start the game
            gameController.startGame();
        });
    }
}
