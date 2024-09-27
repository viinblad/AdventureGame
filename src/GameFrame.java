import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    private JTextArea textArea; // To display messages
    private JTextField inputField; // To get user input
    private Adventure adventure; // Reference to the Adventure game

    public GameFrame() {
        // Set up the JFrame properties
        setTitle("Adventure Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set the initial size of the frame
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false); // Prevent resizing

        // Set layout
        setLayout(new BorderLayout());

        // Create a JTextArea to display messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Monospaced font for better readability
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setText(getASCIIArt()); // Display ASCII art at the start
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER); // Add the text area to the center

        // Create an input field for user commands
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Same font as textArea
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String userInput = inputField.getText(); // Capture input
                    inputField.setText(""); // Clear input field

                    // Pass the user input to the adventure to handle commands
                    processUserInput(userInput);
                }
            }
        });
        add(inputField, BorderLayout.SOUTH); // Add the input field at the bottom

        setVisible(true); // Make the frame visible
    }

    // Initialize the game by linking GameFrame with the Adventure game
    public void startGame(Adventure adventure) {
        this.adventure = adventure;
        adventure.startGame(); // Start the adventure game
    }

    // Process user input and forward it to the Adventure game
    private void processUserInput(String input) {
        if (adventure != null) {
            adventure.processCommand(input); // Forward input to the Adventure game for processing
        } else {
            textArea.append("\nAdventure game not initialized.");
        }
    }

    // Method to append text to the JTextArea
    public void appendText(String text) {
        textArea.append(text + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll to the bottom
    }

    // Method to get ASCII art as a string
    private String getASCIIArt() {
        return
                " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@@  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@@@@  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@   @@  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@  @  @@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   @@  @@@  @@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@@  @@@@@  @@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@              @  @@@           @@  @             @@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@  @                                           @  @@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@  @  @@@@@@@@@@.    @@@@@@     @@@@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@  @  @@@@@@@@@.@@@ @@@@@@@@@@ @@@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@  @ #@@@@@@@@.    @@@@@@     @@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@  @ @@@@@@@@@@@ @ @@@@@@@@@@@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@  @@  @@@@@@@@@ @ @@   %@@@@@@@@@@@  @ @ @@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@  @  @   @@@***@      @@%   @%@%@@@  @@   @@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@  @  @@ @  @@*@*@@ @@@@@   @ @%@%@@  @ @@@  @@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@ @  @@ @ @  @***@@    @ @  @ @@@@@  @ @  @@  @@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@   @@   @ @  @@@@@@@@     @  @@@@  @  %%  @@  @ %@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@  @@  @@@  @  @@@@@@@ @@@   @@@@  @  @@@@  @@  @%@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@:  @@  @@@@@  @  @@@@     @   @@@  @  @@@@@@  @@  %@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@  @@@  @@@@@@@  @  @@@ @ @@@ @ @@  @  @@@@@@@@  @@  @@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@  @@              @  @@   @@@   @  @              @@   @@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@  @@@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@                        @@@@@@@  @                     @@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ @  @@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @  @@@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @  @  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @   @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @ @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n" +
                        " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n";
    }

    // Getter for JTextArea (optional, in case you want to interact directly with the text area)
    public JTextArea getTextArea() {
        return textArea; // Provide access to JTextArea
    }
}
