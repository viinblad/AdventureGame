
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private JPanel mapPanel;
    private Room startingRoom;    // Reference to the starting room
    private List<Room> roomsList; // List to hold all rooms
    private List<Item> itemsList; // List to hold all items

    public Map() {
        mapPanel = new JPanel(new GridBagLayout());
        roomsList = new ArrayList<>();
        itemsList = new ArrayList<>(); // Initialize items list
        this.setupRooms();  // Setup rooms when initializing map
    }

    // Connect two rooms in a specific direction
    private void connectRooms(Room room1, Room room2, String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                room1.setNorth(room2);
                room2.setSouth(room1);
                break;
            case "south":
                room1.setSouth(room2);
                room2.setNorth(room1);
                break;
            case "east":
                room1.setEast(room2);
                room2.setWest(room1);
                break;
            case "west":
                room1.setWest(room2);
                room2.setEast(room1);
                break;
            default:
                System.out.println("Invalid direction!");
        }
    }

    // Set up rooms, their connections, and the items they contain
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

        // Automatically connect rooms using connectRooms method
        connectRooms(room1, room2, "east");
        connectRooms(room1, room4, "south");
        connectRooms(room2, room3, "east");
        connectRooms(room3, room6, "south");
        connectRooms(room6, room9, "south");
        connectRooms(room9, room8, "west");
        connectRooms(room8, room5, "north");
        connectRooms(room8, room7, "west");
        connectRooms(room7, room4, "north");

        // Lock the door to the east in room2
        room2.lockEast(); // Locking the eastern door of room2

        // Set the starting room
        startingRoom = room1;

        // Create Starter weapon
        MeleeWeapon starterWeapon = new MeleeWeapon("wooden sword", "Wooden Sword", "A basic wooden sword.", 5);
        room1.addItem(starterWeapon); // Add the starter weapon to the starting room




        // Create weapons

        RangedWeapon bow = new RangedWeapon("bow", "Wooden Bow", "A bow that can shoot arrows.", 12, 5); // Bow with 5 arrows
        RangedWeapon wand = new RangedWeapon("wand", "A magic wand that casts spells.", "a wand that cast spells",18,20);

        // Add weapons to specific rooms

        room7.addItem(bow);    // Bow in room7
        room2.addItem(wand); // wand in room 2


        // Create enemy with their weapon
        MeleeWeapon sword = new MeleeWeapon("sword", "Iron Sword", "A strong iron sword.", 15);
        OrcEnemy orcEnemy = new OrcEnemy("Grug", "An orc warrior.", 15, sword); // Orc with a sword
        room1.addEnemy(orcEnemy); // Add orc enemy to room1

        // Create items
        Item rustyKey = new Item("Rusty Key", "A small rusty key.", "key");



        // Add items to the items list
        itemsList.add(rustyKey);

        // Place items in specific rooms
        room1.addItem(rustyKey);   // Add rusty key to room1


        // Create food
        Food apple = new Food("apple", "Fresh Apple", "A juicy red apple.", 10, false); // Normal food
        Food bread = new Food("bread", "Loaf of Bread", "A warm loaf of bread.", 15, false); // Normal food
        Food cheese = new Food("cheese", "Cheese Wheel", "A wheel of cheese.", 20, false); // Normal food
        Food poisonousMushroom = new Food("mushroom", "Poisonous Mushroom", "A mushroom that looks delicious but is actually poisonous.", 10, true); // Poisonous food, health restored but will deduct in the logic


        // Add food to the food list
        itemsList.add(apple);
        itemsList.add(bread);
        itemsList.add(cheese);
        itemsList.add(poisonousMushroom);

        // Place food in specific rooms
        room1.addItem(apple);   // Add apple to room1
        room4.addItem(bread);   // Add bread to room4
        room6.addItem(cheese);   // Add cheese to room6
        room8.addItem(poisonousMushroom);   // Add poisonous mushroom to room8
        room1.addItem(poisonousMushroom);   // Add poisonous mushroom to room1

        // Create potions - HP,ATT Boost, poisonous true or false
        Potion healingPotion = new Potion("healing_potion", "Healing Potion", "A potion that restores health.", 30, 0, false); // healing potion
        Potion attackPotion = new Potion("attack_potion", "Attack Boost Potion", "A potion that boosts your attack.", 0, 10, false);// attack potion
        Potion poisonousPotion = new Potion("poisonous_potion", "Poisonous Potion", "A potion that looks refreshing but is actually poisonous.", 50, 0, true);


        // Add potions to the potion list
        itemsList.add(healingPotion);
        itemsList.add(attackPotion);
        itemsList.add(poisonousPotion);

        // Place potions in specific rooms
        room2.addItem(healingPotion);   // Add healing potion to room2
        room5.addItem(attackPotion);   // Add attack boost potion to room5
        room7.addItem(poisonousPotion);   // Add poisonous potion to room7
    }


    // Method to display the map need to be implementet later
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

    // Optional: Method to retrieve items from the items list
    public List<Item> getItemsList() {
        return itemsList;
    }
}
