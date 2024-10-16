import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserInterface {
    private JFrame frame;                     // Main application window
    private JTextArea textArea;               // Area to display game messages
    private JTextField inputField;            // Field for user input
    private JButton sendButton;               // Button to send user input
    private GameController gameController;    // Reference to GameController
    private JTextArea ansiArtArea;            // Area to display ANSI art
    private SoundManager soundManager; // Manages game sounds


    public UserInterface() {
        initializeUI(); // Initialize the user interface
        this.soundManager = new SoundManager(); // Initialize sound manager

    }


    private void initializeUI() {
        // Create the main application window
        frame = new JFrame("Adventure Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application on close
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the window to full screen
        frame.setUndecorated(true); // Remove window borders and title bar
        frame.setLayout(new BorderLayout()); // Use BorderLayout for layout management

        // Create a panel to center the ANSI art area
        JPanel ansiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(60, 120, 120, 120); // Padding around the component
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the content
        ansiPanel.setBackground(Color.BLACK); // Set background color

        // Create a text area to display ANSI art
        ansiArtArea = new JTextArea();
        ansiArtArea.setEditable(false); // Make the text area non-editable
        ansiArtArea.setLineWrap(false); // Disable line wrap for ANSI art
        ansiArtArea.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Set a monospaced font
        ansiArtArea.setBackground(Color.BLACK); // Set background color
        ansiArtArea.setForeground(Color.WHITE); // Set text color

        // Add the ANSI art area to a scroll pane
        JScrollPane ansiScrollPane = new JScrollPane(ansiArtArea);
        ansiScrollPane.setPreferredSize(new Dimension(1400, 600)); // Set a suitable size for ANSI art
        ansiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show the vertical scroll bar

        // Add scroll pane to ANSI panel
        ansiPanel.add(ansiScrollPane, gbc); // Add to the GridBagLayout

        // Add ANSI panel to the window
        frame.add(ansiPanel, BorderLayout.NORTH); // Add to the top of the window

        // Set the title screen ANSI art when the application starts
        updateAnsiArt(ANSIArt.getTitleScreen()); // Display title screen ANSI art centered

        // Create a text area to show messages to the player
        textArea = new JTextArea();
        textArea.setEditable(false); // Make the text area non-editable
        textArea.setLineWrap(true); // Enable line wrap
        textArea.setWrapStyleWord(true); // Wrap lines at word boundaries
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set a monospaced font
        textArea.setBackground(Color.BLACK); // Set background color
        textArea.setForeground(Color.WHITE); // Set text color

        // Create a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show the vertical scroll bar
        frame.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the window

        // Create input panel containing the input field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField(); // Input field for user commands
        sendButton = new JButton("Send"); // Button to submit commands

        // Style the input field to match the text area
        inputField.setBackground(Color.BLACK); // Set background color
        inputField.setForeground(Color.WHITE); // Set text color
        inputField.setCaretColor(Color.WHITE); // Set caret color
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Use the same font

        // Action handling for the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Call handleInput method on button press
            }
        });

        // Add key binding for the Enter key
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Call handleInput method on Enter key press
            }
        });

        // Add the input field and send button to the input panel
        inputPanel.add(inputField, BorderLayout.CENTER); // Add the input field to the center
        inputPanel.add(sendButton, BorderLayout.EAST); // Add the send button to the right

        // Add the input panel to the bottom of the window
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Show the window to the user
        frame.setVisible(true);
    }


    // Method to set the GameController
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    // Method to update ANSI art based on the current room
    public void updateAnsiArt(String ansiArt) {
        String[] lines = ansiArt.split("\n");
        StringBuilder centeredArt = new StringBuilder();
        int maxWidth = ansiArtArea.getVisibleRect().width / ansiArtArea.getFontMetrics(ansiArtArea.getFont()).charWidth('M');

        for (String line : lines) {
            int padding = (maxWidth - line.length()) / 2;
            centeredArt.append(" ".repeat(Math.max(0, padding))).append(line).append("\n");
        }

        ansiArtArea.setText(centeredArt.toString());
        ansiArtArea.setCaretPosition(0); // Optional: scroll to top after update
    }


    // Process user input
    private void handleInput() {
        String input = inputField.getText().trim(); // Get and trim user input
        if (!input.isEmpty()) {
            inputField.setText(""); // Clear the input field after submission
            clearOutput(); // Clear the output area before processing the input
            processInput(input); // Process user input
        }
    }

    // Process user input commands
    private void processInput(String input) {
        if (gameController != null) {
            // Normalize input by trimming and converting to lower case
            String trimmedInput = input.trim().toLowerCase();

            switch (trimmedInput) {
                case "look":
                    Room currentRoom = gameController.getCurrentRoom(); // Get the current room
                    gameController.playSound("resources/sounds/look_sound.wav"); // Play sound for look action
                    displayRoomDescription(currentRoom); // Call the displayRoomDescription method
                    showRoomItemsAndEnemies(currentRoom); // Show items and enemies in the current room
                    break;
                case "inventory":
                    displayInventory(); // Show inventory if the command is "inventory"
                    break;
                case "help":
                    showHelp(); // Show help instructions
                    break;
                case "health":
                    gameController.showHealth(); // Call the GameController to display health
                    break;
                default:
                    if (trimmedInput.startsWith("take ")) {
                        String itemName = trimmedInput.substring(5); // Extract the item name
                        handleTakeItem(itemName); // Handle taking an item
                    } else if (trimmedInput.startsWith("drop ")) {
                        String itemName = trimmedInput.substring(5); // Extract the item name
                        handleDropItem(itemName); // Handle dropping an item
                    } else if (trimmedInput.startsWith("eat ")) {
                        // Handle eating food
                        gameController.handleFoodConsumption(trimmedInput); // Added handling for food consumption
                    } else if (trimmedInput.startsWith("drink ")) {
                        // Handle drinking potions
                        gameController.handlePotionConsumption(trimmedInput); // Added handling for potion consumption
                    } else {
                        gameController.processCommand(trimmedInput); // Handle other commands
                    }
                    break;
            }
        } else {
            showMessage("Error: GameController is not set."); // Improved error message
        }
    }

    // Handle taking an item from the current room
    private void handleTakeItem(String itemName) {
        Item item = gameController.takeItem(itemName); // Attempt to take the item and get the Item object
        if (item != null) { // If the item is successfully taken
            showItemPickedUp(item.getLongName()); // Pass the long name of the Item to showItemPickedUp
        } else {
            showMessage("You couldn't find a " + itemName + " here."); // Notify failure
        }
    }


    // Handle dropping an item from the inventory
    private void handleDropItem(String itemName) {
        Item item = gameController.dropItem(itemName); // Attempt to drop the item and get the Item object
        if (item != null) { // If the item is successfully dropped
            showItemDropped(item.getLongName()); // Pass the long name of the Item to showItemDropped
        } else {
            showMessage("You don't have a " + itemName + " in your inventory."); // Notify failure
        }
    }

    // Method to show item picked up message
    public void showItemPickedUp(String itemLongName) {
        showMessage("You picked up: " + itemLongName + "."); // Notify the user of item pickup
    }

    // Method to show item dropped message
    public void showItemDropped(String itemLongName) {
        showMessage("You dropped the " + itemLongName + "."); // Notify the user of item drop
    }


    // Method to display room descriptions in an overlay style
    public void displayRoomDescription(Room room) {
        String description = "You are in " + room.getName() + "\n" + room.getDescription(); // Create room description
        showMessage(description); // Show the description in the message area
    }

    // Method to show items and enemies in the current room
    public void showRoomItemsAndEnemies(Room room) {
        StringBuilder itemsAndEnemiesMessage = new StringBuilder();
        itemsAndEnemiesMessage.append("Items in this room:\n");

        // Show items
        if (room.getItems().isEmpty()) {
            itemsAndEnemiesMessage.append("There are no items in this room.\n");
        } else {
            for (Item item : room.getItems()) {
                itemsAndEnemiesMessage.append("- ").append(item.getLongName()).append(" (").append(item.getShortName()).append(")\n");
            }
        }

        // Show enemies
        List<Enemy> enemies = room.getEnemies(); // Ensure that the correct List type is imported
        itemsAndEnemiesMessage.append("Enemies in this room:\n");
        if (enemies.isEmpty()) {
            itemsAndEnemiesMessage.append("There are no enemies in this room.\n");
        } else {
            for (Enemy enemy : enemies) {
                itemsAndEnemiesMessage.append("- ").append(enemy.getName()).append("\n");
            }
        }

        showMessage(itemsAndEnemiesMessage.toString()); // Show message with items and enemies
    }

    // Method to display the player's inventory
    private void displayInventory() {
        StringBuilder inventoryMessage = new StringBuilder("Your Inventory:\n");
        if (gameController.getPlayerInventory().isEmpty()) {
            inventoryMessage.append("Your inventory is empty.");
        } else {
            for (Item item : gameController.getPlayerInventory()) {
                String equippedMessage = ""; // Initialize the equipped message
                // Check if the item is the equipped weapon and display accordingly
                if (item instanceof Weapon && item.equals(gameController.getEquippedWeapon())) {
                    equippedMessage = " (equipped)"; // Set equipped message
                }
                inventoryMessage.append("- ").append(item.getLongName()).append(equippedMessage).append("\n");
            }
        }
        showMessage(inventoryMessage.toString()); // Show the inventory in the message area
    }

    // Method to display the player's health
    public void showHealth(int currentHealth, int maxHealth) {
        String healthMessage = "Your health: " + currentHealth + "/" + maxHealth;

        String healthStatus;
        // Determine health status message
        if (currentHealth >= 75) {
            healthStatus = "You are in great shape!";
        } else if (currentHealth >= 50) {
            healthStatus = "You are feeling fine.";
        } else if (currentHealth >= 25) {
            healthStatus = "You are hurt, be careful!";
        } else {
            healthStatus = "You are in critical condition! Find something to heal.";
        }

        // Show both health message and status
        showMessage(healthMessage); // Display the health message
        showMessage(healthStatus); // Display the health status
    }

    // Method to clear the output text area
    public void clearOutput() {
        textArea.setText(""); // Clear the text area
    }

    // Method to show messages to the user in a styled format
    public void showMessage(String message) {
        textArea.append(message + "\n"); // Append the new message to the text area
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll to the bottom of the text area
    }

    // Method to show help instructions to the player
    public void showHelp() {
        // Create a StringBuilder to build the help message with a grid layout
        StringBuilder helpMessage = new StringBuilder("Available commands:\n\n");

        // Define the commands in an array
        String[][] commands = {
                {"Move Commands:", "Status commands:", "Action command:"},
                {"go north", "health", "inventory", "drink [potion]"},
                {"go west", "look", "take [item]", "attack[enemy name]"},
                {"go east", "show map", "drop [item]", "exit" + ": close the game"},
                {"go south", "unlock", "eat [food]"},
        };

        // Create a formatted string with columns
        for (String[] row : commands) {
            for (String command : row) {
                if (!command.isEmpty()) {
                    helpMessage.append(String.format("%-20s", command)); // Format each command
                } else {
                    helpMessage.append(String.format("%-20s", " ")); // Fill empty spaces
                }
            }
            helpMessage.append("\n"); // New line after each row
        }

        // Show the formatted help message
        showMessage(helpMessage.toString());
    }

    // Method to display the map of the current room and its connections
    public void showMap(String currentRoomName, String eastRoomName, String southRoomName, String westRoomName, String northRoomName, String enemies) {
        StringBuilder mapDisplay = new StringBuilder();
        mapDisplay.append("Current Room: ").append(currentRoomName).append("\n");
        mapDisplay.append("Adjacent Rooms:\n");
        mapDisplay.append("North: ").append(northRoomName).append("\n");
        mapDisplay.append("South: ").append(southRoomName).append("\n");
        mapDisplay.append("West: ").append(westRoomName).append("\n");
        mapDisplay.append("East: ").append(eastRoomName).append("\n");
        mapDisplay.append("Enemies in the room: ").append(enemies).append("\n"); // Display enemies

        // Show the map in a dialog
        JOptionPane.showMessageDialog(frame, mapDisplay.toString(), "Game Map", JOptionPane.INFORMATION_MESSAGE);
    }
}
