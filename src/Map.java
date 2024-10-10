
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
        room8.lockEast(); // Locking the eastern door of room 8

        // Set the starting room
        startingRoom = room1;

        // Create Starter weapon
        MeleeWeapon starterWeapon = new MeleeWeapon("wooden sword", "Wooden Sword", "A basic wooden sword.", 5);
        room1.addItem(starterWeapon); // Add the starter weapon to the starting room


        // Create weapons

        RangedWeapon bow = new RangedWeapon("bow", "Wooden Bow", "A bow that can shoot arrows.", 12, 5); // Bow with 5 arrows
        RangedWeapon wand = new RangedWeapon("wand", "A magic wand that casts spells.", "a wand that cast spells", 18, 20);
        MeleeWeapon ironSword = new MeleeWeapon("iron sword", "Iron Sword", "A strong iron sword.", 15);
        MeleeWeapon goblinDagger = new MeleeWeapon("goblin dagger", "Goblin Dagger", "A small but sharp dagger.", 8);
        RangedWeapon goblinBow = new RangedWeapon("goblin bow", "Goblin Bow", "A crude bow made from branches.", 12, 5);
        MeleeWeapon trollClub = new MeleeWeapon("troll club", "Troll Club", "A massive club made from a tree.", 15);
        RangedWeapon magicWand = new RangedWeapon("magic wand", "Magic Wand", "A wand that casts powerful spells.", 20, 10);
        RangedWeapon dragonBow = new RangedWeapon("dragon bow", "Dragon Bow", "A legendary bow made from dragon bones.", 25, 5);
        MeleeWeapon angelSword = new MeleeWeapon("angel sword", "Angel Sword", "A glowing sword with celestial power.", 30);

        // Add weapons to specific rooms

        room7.addItem(bow);    // Bow in room7
        room2.addItem(wand); // wand in room 2


        // Create enemy with their weapon
        OrcEnemy orcEnemy = new OrcEnemy("Grug The Orc", "An orc warrior.", 15, ironSword); // Orc with an iron sword
        room1.addEnemy(orcEnemy); // Add orc enemy to room1

        GoblinEnemy goblinEnemy = new GoblinEnemy("Sneaky Goblin", "A small green creature with a cunning smile.", 10, goblinDagger);
        room3.addEnemy(goblinEnemy); // Add goblin enemy to room3

        TrollEnemy trollEnemy = new TrollEnemy("Brute Troll", "A massive troll with thick skin.", 20, trollClub);
        room4.addEnemy(trollEnemy); // Add troll enemy to room4

        VampireEnemy vampireEnemy = new VampireEnemy("Bloodthirsty Vampire", "A pale figure with fangs, lurking in the shadows.", 25, magicWand);
        room5.addEnemy(vampireEnemy); // Add vampire enemy to room5

        WerewolfEnemy werewolfEnemy = new WerewolfEnemy("Fierce Werewolf", "A monstrous wolf-like creature.", 30, dragonBow);
        room6.addEnemy(werewolfEnemy); // Add werewolf enemy to room6

        SkeletonEnemy skeletonEnemy = new SkeletonEnemy("Cursed Skeleton", "A bone-chilling skeleton with a sword.", 15, goblinDagger);
        room7.addEnemy(skeletonEnemy); // Add skeleton enemy to room7

        AngelEnemy angelEnemy = new AngelEnemy("Guardian Angel", "A majestic being with wings, protecting the innocent.", 35, angelSword);
        room8.addEnemy(angelEnemy); // Add angel enemy to room8

        // Create the final boss enemy
        DemonBoss demonBoss = new DemonBoss("Malphas", "The Demon Lord of Chaos, a formidable foe.", 50, new MeleeWeapon("demon scythe", "Demon Scythe", "A terrifying scythe that deals heavy damage.", 40)); // weapon put here to know its the boss weapon
        room9.addEnemy(demonBoss); // Add demon boss to room9


        // Create items
        Item magicKey = new Item("Magic Key", "A universel magic key.", "key");


        // Add items to the items list
        itemsList.add(magicKey);

        // Place items in specific rooms
        room1.addItem(magicKey);   // Add rusty key to room1


        // Create food
        Food apple = new Food("apple", "Fresh Apple", "A juicy red apple.", 10, false); // Normal food
        Food bread = new Food("bread", "Loaf of Bread", "A warm loaf of bread.", 15, false); // Normal food
        Food cheese = new Food("cheese", "Cheese Wheel", "A wheel of cheese.", 20, false); // Normal food
        Food poisonousMushroom = new Food("mushroom", "Poisonous Mushroom", "A mushroom that looks delicious but is actually poisonous.", 10, true); // Poisonous food
        Food banana = new Food("banana", "Banana of the Gods", "A ripe banana blessed by the heavens.", 12, false); // Normal food
        Food steak = new Food("steak", "Infernal Steak", "A juicy grilled steak cooked in hellfire.", 25, false); // Normal food
        Food chocolate = new Food("chocolate", "Heavenly Chocolate", "A delicious chocolate bar infused with divine magic.", 15, false); // Normal food
        Food rottonFruit = new Food("rotten fruit", "Cursed Fruit", "A piece of fruit that's gone bad and cursed.", 5, true); // Poisonous food
        Food energyDrink = new Food("energy drink", "Celestial Energy Drink", "A can of energy drink that boosts your stamina.", 20, false); // Normal food
        Food sushi = new Food("sushi", "Demon Sushi", "A roll of fresh sushi made with sea creatures from the abyss.", 30, false); // Normal food
        Food holyBread = new Food("holyBread", "Holy Bread", "Bread blessed by a priest; restores health.", 18, false);
        Food dragonFruit = new Food("dragonFruit", "Dragon Fruit", "A rare fruit that boosts your magical abilities.", 22, false);
        Food phantomPie = new Food("phantomPie", "Phantom Pie", "A pie that appears and disappears; mysterious but safe.", 15, false);
        Food goblinGrapes = new Food("goblinGrapes", "Goblin Grapes", "Small, sweet grapes that increase your agility.", 14, false);
        Food devilishDelight = new Food("devilishDelight", "Devilish Delight", "A dessert that gives a temporary boost in strength.", 20, false);
        Food venomousVegetable = new Food("venomousVegetable", "Venomous Vegetable", "A vegetable that poisons you but increases your attack.", 8, true);
        Food nectar = new Food("nectar", "Nectar of the Gods", "A sweet liquid that restores a large amount of health.", 40, false);
        Food hellfireHotwings = new Food("hellfireHotwings", "Hellfire Hot Wings", "Wings cooked in the flames of the underworld; risky but rewarding.", 30, true);
        Food phoenixFeatherStew = new Food("phoenixFeatherStew", "Phoenix Feather Stew", "A stew that heals and gives temporary invincibility.", 35, false);
        Food shadowStew = new Food("shadowStew", "Shadow Stew", "A dark stew that enhances your stealth abilities.", 25, false);
        Food angelicAlmonds = new Food("angelicAlmonds", "Angelic Almonds", "Nuts that boost your magical defenses.", 15, false);
        Food faerieFruit = new Food("faerieFruit", "Faerie Fruit", "A magical fruit that increases your speed.", 18, false);
        Food cursedCake = new Food("cursedCake", "Cursed Cake", "A cake that looks delicious but is actually poisonous.", 10, true);
        Food heavenlyCider = new Food("heavenlyCider", "Heavenly Cider", "A refreshing drink that restores health and stamina.", 20, false);
        Food demonicDessert = new Food("demonicDessert", "Demonic Dessert", "A dessert that gives you energy but has a side effect.", 15, true);

// Add food to the food list
        itemsList.add(apple);
        itemsList.add(bread);
        itemsList.add(cheese);
        itemsList.add(poisonousMushroom);
        itemsList.add(banana);
        itemsList.add(steak);
        itemsList.add(chocolate);
        itemsList.add(rottonFruit);
        itemsList.add(energyDrink);
        itemsList.add(sushi);
        itemsList.add(holyBread);
        itemsList.add(dragonFruit);
        itemsList.add(phantomPie);
        itemsList.add(goblinGrapes);
        itemsList.add(devilishDelight);
        itemsList.add(venomousVegetable);
        itemsList.add(nectar);
        itemsList.add(hellfireHotwings);
        itemsList.add(phoenixFeatherStew);
        itemsList.add(shadowStew);
        itemsList.add(angelicAlmonds);
        itemsList.add(faerieFruit);
        itemsList.add(cursedCake);
        itemsList.add(heavenlyCider);
        itemsList.add(demonicDessert);

// Place food in specific rooms
        room1.addItem(apple);              // Add apple to room1
        room4.addItem(bread);              // Add bread to room4
        room6.addItem(cheese);             // Add cheese to room6
        room8.addItem(poisonousMushroom);  // Add poisonous mushroom to room8
        room2.addItem(banana);             // Add banana to room2
        room3.addItem(steak);              // Add steak to room3
        room5.addItem(chocolate);           // Add chocolate to room5
        room7.addItem(rottonFruit);         // Add rotten fruit to room7
        room9.addItem(energyDrink);         // Add energy drink to room9
        room6.addItem(sushi);               // Add sushi to room6
        room2.addItem(holyBread);          // Add holy bread to room2
        room3.addItem(dragonFruit);         // Add dragon fruit to room3
        room5.addItem(phantomPie);          // Add phantom pie to room5
        room7.addItem(goblinGrapes);        // Add goblin grapes to room7
        room8.addItem(devilishDelight);      // Add devilish delight to room8
        room1.addItem(venomousVegetable);   // Add venomous vegetable to room1
        room9.addItem(nectar);               // Add nectar to room9
        room4.addItem(hellfireHotwings);    // Add hellfire hot wings to room4
        room6.addItem(phoenixFeatherStew);  // Add phoenix feather stew to room6
        room3.addItem(shadowStew);          // Add shadow stew to room3
        room5.addItem(angelicAlmonds);      // Add angelic almonds to room5
        room8.addItem(faerieFruit);          // Add faerie fruit to room8
        room1.addItem(cursedCake);           // Add cursed cake to room1
        room2.addItem(heavenlyCider);       // Add heavenly cider to room2
        room7.addItem(demonicDessert);       // Add demonic dessert to room7

// Create potions - HP, ATT Boost, poisonous true or false
        Potion healingPotion = new Potion("healing potion", "Healing Potion", "A potion that restores health.", 30, 0, false); // healing potion
        Potion attackPotion = new Potion("attack_potion", "Attack Boost Potion", "A potion that boosts your attack.", 0, 10, false); // attack potion
        Potion poisonousPotion = new Potion("poisonous_potion", "Poisonous Potion", "A potion that looks refreshing but is actually poisonous.", 50, 0, true); // poisonous potion

// New potion items
        Potion manaPotion = new Potion("mana_potion", "Mana Potion", "Restores your magical energy.", 0, 0, false); // Mana restoration
        Potion speedPotion = new Potion("speed_potion", "Speed Potion", "Increases your speed for a short time.", 0, 5, false); // Speed boost
        Potion strengthPotion = new Potion("strength_potion", "Strength Potion", "Temporarily increases your strength.", 0, 15, false); // Strength boost
        Potion invisibilityPotion = new Potion("invisibility_potion", "Invisibility Potion", "Makes you invisible for a short period.", 0, 0, false); // Invisibility
        Potion poisonResistancePotion = new Potion("poison_resistance_potion", "Poison Resistance Potion", "Grants resistance to poisons.", 0, 0, false); // Poison resistance
        Potion holyWater = new Potion("holy_water", "Holy Water", "A blessed water that heals and repels evil.", 20, 0, false); // Healing
        Potion demonBlood = new Potion("demon_blood", "Demon Blood", "A potion that increases attack power but causes damage.", 10, 5, true); // Attack boost with damage
        Potion divineElixir = new Potion("divine_elixir", "Divine Elixir", "A rare elixir that restores a significant amount of health.", 100, 0, false); // Major health restoration
        Potion timeWarpPotion = new Potion("time_warp_potion", "Time Warp Potion", "Slows down time for everything except you.", 0, 0, false); // Time manipulation
        Potion berserkerPotion = new Potion("berserker_potion", "Berserker Potion", "Temporarily increases your attack but lowers defense.", 0, 20, true); // Attack boost with defense decrease
        Potion revivalPotion = new Potion("revival_potion", "Revival Potion", "Revives you upon death.", 0, 0, false); // Revive
        Potion potionOfLuck = new Potion("potion_of_luck", "Potion of Luck", "Increases your luck for finding items.", 0, 0, false); // Luck increase
        Potion elixirOfLife = new Potion("elixir_of_life", "Elixir of Life", "Grants temporary immortality.", 50, 0, false); // Immortality for a short time
        Potion cursedElixir = new Potion("cursed_elixir", "Cursed Elixir", "A potion that gives great power but has a dark curse.", 80, 10, true); // Powerful but cursed

// Add potions to the potion list
        itemsList.add(healingPotion);
        itemsList.add(attackPotion);
        itemsList.add(poisonousPotion);
        itemsList.add(manaPotion);
        itemsList.add(speedPotion);
        itemsList.add(strengthPotion);
        itemsList.add(invisibilityPotion);
        itemsList.add(poisonResistancePotion);
        itemsList.add(holyWater);
        itemsList.add(demonBlood);
        itemsList.add(divineElixir);
        itemsList.add(timeWarpPotion);
        itemsList.add(berserkerPotion);
        itemsList.add(revivalPotion);
        itemsList.add(potionOfLuck);
        itemsList.add(elixirOfLife);
        itemsList.add(cursedElixir);

// Place potions in specific rooms
        room2.addItem(healingPotion);      // Add healing potion to room2
        room1.addItem(healingPotion);       // Add another healing potion to room1
        room5.addItem(attackPotion);        // Add attack boost potion to room5
        room7.addItem(poisonousPotion);     // Add poisonous potion to room7
        room3.addItem(manaPotion);          // Add mana potion to room3
        room4.addItem(speedPotion);         // Add speed potion to room4
        room9.addItem(strengthPotion);      // Add strength potion to room9
        room6.addItem(invisibilityPotion);   // Add invisibility potion to room6
        room8.addItem(holyWater);           // Add holy water to room8
        room3.addItem(demonBlood);          // Add demon blood to room3
        room5.addItem(divineElixir);        // Add divine elixir to room5
        room1.addItem(timeWarpPotion);      // Add time warp potion to room1
        room7.addItem(berserkerPotion);     // Add berserker potion to room7
        room2.addItem(revivalPotion);       // Add revival potion to room2
        room4.addItem(potionOfLuck);        // Add potion of luck to room4
        room8.addItem(elixirOfLife);        // Add elixir of life to room8
        room6.addItem(cursedElixir);        // Add cursed elixir to room6

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
