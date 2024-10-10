import javax.swing.*;
import java.awt.*;

public class StartView {
    private JFrame frame; // Main application window
    private JTextArea ansiArtArea; // Area to display ANSI art
    private SoundManager soundManager; // Sound manager instance
    private GameController gameController; // Reference to GameController
    private UserInterface ui; // Reference to UserInterface

    // Constructor to set up the full-screen window
    public StartView() {
        ui = new UserInterface(); // Create UserInterface instance
        gameController = new GameController(ui); // Initialize GameController with UserInterface
        ui.setGameController(gameController); // Set GameController in UserInterface
        soundManager = new SoundManager();

        initializeUI(); // Initialize the user interface
        soundManager.startTheme(); // Start the theme music with lower volume
    }

    // Static method to start the StartView
    public static void start() {
        SwingUtilities.invokeLater(StartView::new); // Create StartView instance
    }

    // Method to initialize the user interface
    private void initializeUI() {
        // Create the main application window
        frame = new JFrame("Adventure Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application on close
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the window to full screen
        frame.setUndecorated(true); // Remove borders and title bar
        frame.setLayout(new BorderLayout()); // Use BorderLayout for layout management

        // Create an area for ANSI art
        ansiArtArea = new JTextArea();
        ansiArtArea.setEditable(false); // Make the text area non-editable
        ansiArtArea.setLineWrap(false); // Disable line wrap for ANSI art
        ansiArtArea.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Use a monospaced font
        ansiArtArea.setBackground(Color.BLACK); // Set background color
        ansiArtArea.setForeground(Color.WHITE); // Set text color
        ansiArtArea.setFocusable(false); // Prevent focus for the text area
        ansiArtArea.setText(ANSIArt.getStartScreenArt()); // Set ANSI art as the text
        ansiArtArea.setOpaque(true); // Ensure it is opaque

        // Set the ANSI art area to fill the frame
        frame.add(ansiArtArea, BorderLayout.CENTER); // Add ANSI art area to the center

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(); // Panel to hold buttons
        buttonPanel.setBackground(Color.BLACK); // Set background color
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for button alignment

        // Create buttons
        JButton startButton = new JButton("Start Game");
        JButton instructionsButton = new JButton("Instructions");
        JButton exitButton = new JButton("Exit");

        // Customize buttons
        customizeButton(startButton);
        customizeButton(instructionsButton);
        customizeButton(exitButton);

        // Add action listeners to buttons
        startButton.addActionListener(e -> startGame()); // Start game logic
        instructionsButton.addActionListener(e -> showInstructions()); // Show instructions logic
        exitButton.addActionListener(e -> System.exit(0)); // Exit the application

        // Add buttons to the panel
        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(exitButton);

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom

        // Show the window to the user
        frame.setVisible(true);
    }

    // Method to customize button appearance
    private void customizeButton(JButton button) {
        button.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Set font for buttons
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
    }

    // Method to start the game
    private void startGame() {
        // Play the start sound effect
        soundManager.playSoundEffect("start");

        // Stop the theme music when the game starts
        soundManager.stopTheme();

        // Proceed with the game
        frame.dispose(); // Close the start screen
        gameController.startGame(); // Call the method to start the game
    }

    // Method to show instructions
    private void showInstructions() {
        // Logic to show game instructions goes here
        JOptionPane.showMessageDialog(frame,
                "Game Instructions:\n" +
                        "1. Navigate through the rooms.\n" +
                        "2. Collect items and avoid enemies.\n" +
                        "3. Use commands to interact with the environment.",
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE); // Show instructions in a dialog
    }

    public static void main(String[] args) {
        start(); // Start the game by creating StartView instance
    }
}
