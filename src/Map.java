import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private JPanel mapPanel;
    private Room startingRoom;    // Reference to the starting room
    private List<Room> roomsList; // List to hold all rooms

    public Map() {
        mapPanel = new JPanel(new GridBagLayout());
        roomsList = new ArrayList<>();
        this.setupRooms();  // Setup rooms when initializing map
    }

    // Set up rooms and their connections
    private void setupRooms() {
        // Create and initialize the rooms
        Room room1 = new Room("The Amusement a long time ago", "This is where your journey begins.");
        Room room2 = new Room("Room 2", "A bright room with a wooden floor.");
        Room room3 = new Room("Room 3", "A dark room with a floating broom in the middle.");
        Room room4 = new Room("Room 4", "You come into a bright room with mirrors as walls.");
        Room room5 = new Room("Room 5", "You found the center of the maze; the room is cold.");
        Room room6 = new Room("Room 6", "A pillar of bones lays beneath you; the room is dark with a torch centered in the middle.");
        Room room7 = new Room("Room 7", "This room is all white, and you hear a chime.");
        Room room8 = new Room("Room 8", "You hear a sound; this room is empty.");
        Room room9 = new Room("Room 9", "This is a big room, with blood splashes all around.");

        // Add rooms to list
        roomsList.add(room1);
        roomsList.add(room2);
        roomsList.add(room3);
        roomsList.add(room4);
        roomsList.add(room5);
        roomsList.add(room6);
        roomsList.add(room7);
        roomsList.add(room8);
        roomsList.add(room9);

        // Connect rooms (two-way linking handled in Room class)
        room1.setEast(room2);  // Connect room1 to room2
        room1.setSouth(room4); // Connect room1 to room4
        room2.setEast(room3);  // Connect room2 to room3
        room3.setSouth(room6);  // Connect room3 to room6
        room6.setSouth(room9);  // Connect room6 to room9
        room9.setWest(room8);   // Connect room9 to room8
        room8.setNorth(room5);  // Connect room8 to room5
        room8.setWest(room7);   // Connect room8 to room7
        room7.setNorth(room4);  // Connect room7 to room4

        // Lock the door to the east in room2
        room2.lockEast(); // Locking the eastern door of room2

        // Set the starting room
        startingRoom = room1;
    }

    // New method to handle room transitions
    public Room moveToRoom(Room currentRoom, String direction) throws InterruptedException, IOException {
        Room nextRoom = null;

        switch (direction) {
            case "north":
                nextRoom = currentRoom.getNorth();
                break;
            case "south":
                nextRoom = currentRoom.getSouth();
                break;
            case "east":
                if (currentRoom.isEastLocked()) { // Check if the door is locked
                    return null; // Return null if locked
                }
                nextRoom = currentRoom.getEast();
                break;
            case "west":
                nextRoom = currentRoom.getWest();
                break;
            default:
                return null; // Invalid direction
        }

        // If moving to a valid room
        if (nextRoom != null) {
            // Show loading screen if not moving to the starting room
            if (currentRoom != startingRoom) {
                showLoadingScreen(); // Call to loading screen display
            }
            return nextRoom; // Return the next room
        } else {
            return null; // Handle invalid movement
        }
    }

    // Method to display the loading screen
    private void showLoadingScreen() throws InterruptedException, IOException {
        // Display loading animation
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.displayLoading(); // Assuming this method already handles UI
    }

    // Method to display the map
    public void displayMap() {
        mapPanel.removeAll(); // Clear previous map display

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between panels

        // Display the rooms dynamically in a 3x3 grid
        for (int i = 0; i < roomsList.size(); i++) {
            Room room = roomsList.get(i);

            // Calculate grid position
            int col = i % 3;  // 3 columns
            int row = i / 3;  // Increment row after every 3 rooms

            // Create a panel for the room
            JPanel roomPanel = createRoomPanel(room);
            gbc.gridx = col;
            gbc.gridy = row;

            // Add the room panel to the main map panel
            mapPanel.add(roomPanel, gbc);
        }

        // Revalidate and repaint to update the panel
        mapPanel.revalidate();
        mapPanel.repaint();

        // Display the map in a new window
        showMapWindow();
    }

    // Create a panel for each room
    private JPanel createRoomPanel(Room room) {
        JPanel roomPanel = new JPanel();
        roomPanel.setBorder(BorderFactory.createTitledBorder(room.getName()));
        roomPanel.setLayout(new BorderLayout());

        StringBuilder roomDescription = new StringBuilder(room.getDescription());

        // Check if the eastern door is locked
        if (room.isEastLocked()) {
            roomDescription.append("\n(Warning: The door to the east is locked!)");
        }

        JTextArea descriptionArea = new JTextArea(roomDescription.toString());
        descriptionArea.setEditable(false); // Make the description area non-editable
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        roomPanel.add(descriptionArea, BorderLayout.CENTER);
        roomPanel.setPreferredSize(new Dimension(200, 100)); // Set preferred size

        return roomPanel;
    }

    // Display the map in a JFrame
    private void showMapWindow() {
        JFrame mapFrame = new JFrame("Game Map");
        mapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mapFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> mapFrame.dispose()); // Close the frame when clicked

        // Add components to frame
        mapFrame.add(closeButton, BorderLayout.SOUTH);
        mapFrame.add(mapPanel, BorderLayout.CENTER);

        // Display the frame
        mapFrame.setVisible(true);
    }

    // Return the starting room
    public Room getStartingRoom() {
        return startingRoom;
    }
}
