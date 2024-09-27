import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private JFrame frame;                     // Main application window
    private JTextArea textArea;               // Area for displaying game messages
    private JTextField inputField;            // Field for user input commands
    private JButton sendButton;                // Button to send user input
    private GameController gameController;     // Reference to the GameController
    private JDialog loadingDialog;             // Loading dialog
    private JTextArea ansiArtArea;            // Area for displaying ANSI art

    public UserInterface() {
        initializeUI(); // Initialize the user interface components
    }

    private void initializeUI() {
        // Create the main frame for the application
        frame = new JFrame("Adventure Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to full-screen
        frame.setLayout(new BorderLayout()); // Use BorderLayout for layout management

        // Create a text area for displaying ANSI art
        ansiArtArea = new JTextArea();
        ansiArtArea.setEditable(false); // Make the text area non-editable
        ansiArtArea.setLineWrap(true); // Enable line wrapping
        ansiArtArea.setWrapStyleWord(true); // Wrap lines at word boundaries
        ansiArtArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set a monospaced font for better readability
        ansiArtArea.setBackground(Color.BLACK); // Set background color
        ansiArtArea.setForeground(Color.WHITE); // Set text color

        // Add ANSI art area to a scroll pane
        JScrollPane ansiScrollPane = new JScrollPane(ansiArtArea);
        ansiScrollPane.setPreferredSize(new Dimension(frame.getWidth(), 1100)); // Set height for the ANSI art area
        frame.add(ansiScrollPane, BorderLayout.NORTH); // Add to the top of the frame

        // Create a text area for displaying messages to the player
        textArea = new JTextArea();
        textArea.setEditable(false); // Make the text area non-editable
        textArea.setLineWrap(true); // Enable line wrapping
        textArea.setWrapStyleWord(true); // Wrap lines at word boundaries
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set a monospaced font for better readability
        textArea.setBackground(Color.BLACK); // Set background color
        textArea.setForeground(Color.WHITE); // Set text color

        // Create a scroll pane for the text area to allow scrolling through long messages
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll bar
        scrollPane.setPreferredSize(new Dimension(frame.getWidth(), 600)); // Set a preferred height for the scroll pane
        frame.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the frame

        // Create input panel containing the input field and the send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField(); // Input field for user commands
        sendButton = new JButton("Send"); // Button to submit commands

        // Style the input field to match the text area
        inputField.setBackground(Color.BLACK); // Set background color
        inputField.setForeground(Color.WHITE); // Set text color
        inputField.setCaretColor(Color.WHITE); // Set caret (cursor) color
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Use the same font

        // Action listener for the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Call handleInput method on button press
            }
        });

        // Add key binding for Enter key
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Call handleInput method on Enter key press
            }
        });

        // Add the input field and send button to the input panel
        inputPanel.add(inputField, BorderLayout.CENTER); // Add input field to center
        inputPanel.add(sendButton, BorderLayout.EAST); // Add send button to the east side
        frame.add(inputPanel, BorderLayout.SOUTH); // Add input panel to the bottom of the frame

        frame.setVisible(true); // Make the frame visible to the user
    }

    // Method to set the GameController
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    // Method to update ANSI art based on the current room
    public void updateAnsiArt(String ansiArt) {
        ansiArtArea.setText(ansiArt); // Set the ANSI art text
    }

    // Handle input from the user
    private void handleInput() {
        String input = inputField.getText().trim(); // Get and trim the user input
        if (!input.isEmpty()) {
            inputField.setText(""); // Clear the input field after submission
            processInput(input); // Process the user input
        }
    }

    // Method to process user input commands
    private void processInput(String input) {
        if (gameController != null) {
            gameController.processCommand(input); // Pass input to the GameController
        } else {
            showMessage("Error: GameController is not set."); // Improved error message
        }
    }

    // Method to show messages to the user in a styled format
    public void showMessage(String message) {
        textArea.setText(""); // Clear the old messages
        textArea.append(message + "\n"); // Append the new message to the text area
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll to the bottom of the text area
    }

    // Method to display room descriptions in an overlay style
    public void displayRoomDescription(Room room) {
        String description = "You are in " + room.getName() + "\n" + room.getDescription(); // Create room description
        showMessage(description); // Show the description in the message area
    }

    // Method to show help instructions to the player
    public void showHelp() {
        String helpMessage = "Available commands:\n" +
                "go north, go south, go east, go west - to move between rooms.\n" +
                "look - to describe the current room.\n" +
                "unlock - to unlock the door.\n" +
                "exit - to quit the game.\n" +
                "help - to show this message.\n" +
                "show map - to display the map."; // Help instructions
        showMessage(helpMessage); // Display the help message
    }

    // Method to display the map of the current room and its connections
    public void showMap(String currentRoomName, String eastRoomName, String southRoomName, String westRoomName) {
        StringBuilder mapDisplay = new StringBuilder();
        mapDisplay.append("Current Room: ").append(currentRoomName).append("\n");
        mapDisplay.append("Adjacent Rooms:\n");
        mapDisplay.append("East: ").append(eastRoomName).append("\n");
        mapDisplay.append("South: ").append(southRoomName).append("\n");
        mapDisplay.append("West: ").append(westRoomName).append("\n");

        // Show the map in a dialog
        JOptionPane.showMessageDialog(frame, mapDisplay.toString(), "Game Map", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to show a loading screen with a message
    public void showLoadingScreen(String message) {
        loadingDialog = new JDialog(frame, "Loading", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.add(new JLabel(message), BorderLayout.CENTER);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(frame);
        loadingDialog.setVisible(true);
    }

    // Method to close the loading screen
    public void closeLoadingScreen() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
            loadingDialog = null; // Clear reference after closing
        }
    }
}
