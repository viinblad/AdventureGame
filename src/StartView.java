import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        soundManager.startTheme("resources/sounds/theme_mix.wav"); // Start the theme music
        initializeUI(); // Initialize the user interface
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
        startButton.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Set font for buttons
        instructionsButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        exitButton.setFont(new Font("Monospaced", Font.PLAIN, 20));

        // Set button colors
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.WHITE);
        instructionsButton.setBackground(Color.DARK_GRAY);
        instructionsButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);

        // Add action listeners to buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // Start game logic
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInstructions(); // Show instructions logic
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Add buttons to the panel
        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(exitButton);

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom

        // Show the window to the user
        frame.setVisible(true);
    }

    // Method to start the game
    private void startGame() {
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
        SwingUtilities.invokeLater(StartView::new); // Directly create StartView instance
    }
}
