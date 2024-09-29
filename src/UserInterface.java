import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private JFrame frame;                     // Hovedprogramvinduet
    private JTextArea textArea;               // Område til at vise spilmeddelelser
    private JTextField inputField;            // Felt til brugerinput
    private JButton sendButton;               // Knap til at sende brugerinput
    private GameController gameController;    // Reference til GameController
    private JTextArea ansiArtArea;            // Område til at vise ANSI kunst

    public UserInterface() {
        initializeUI(); // Initialiser brugergrænsefladen
    }

    private void initializeUI() {
        // Opret hovedvinduet for applikationen
        frame = new JFrame("Adventure Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Afslut applikationen ved lukning
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Sæt vinduet til fuld skærm
        frame.setLayout(new BorderLayout()); // Brug BorderLayout til layoutstyring

        // Opret et panel til at centrere ANSI kunstområdet
        JPanel ansiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(60, 120, 120, 120); // Padding around the component
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the content
        ansiPanel.setBackground(Color.BLACK); // set background color

        // Opret et tekstområde til at vise ANSI kunst
        ansiArtArea = new JTextArea();
        ansiArtArea.setEditable(false); // Gør tekstområdet ikke-redigerbart
        ansiArtArea.setLineWrap(false); // Deaktiver linjeskift for ANSI kunst
        ansiArtArea.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Sæt en monospaced skrifttype
        ansiArtArea.setBackground(Color.BLACK); // Sæt baggrundsfarve
        ansiArtArea.setForeground(Color.WHITE); // Sæt tekstfarve

        // Tilføj ANSI kunstområde til en rullepanel
        JScrollPane ansiScrollPane = new JScrollPane(ansiArtArea);
        ansiScrollPane.setPreferredSize(new Dimension(1400, 600)); // Sæt en passende størrelse for ANSI kunst
        ansiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Vis altid den lodrette rullebjælke

        // Tilføj scrollpane til ANSI panel
        ansiPanel.add(ansiScrollPane, gbc); // Add to the GridBagLayout

        // Tilføj ANSI panel til vinduet
        frame.add(ansiPanel, BorderLayout.NORTH); // Tilføj til toppen af vinduet

        // Sæt titelskærmens ANSI kunst, når applikationen starter
        updateAnsiArt(ANSIArt.getTitleScreen()); // Vis titelskærmens ANSI kunst centreret


        // Opret et tekstområde til at vise meddelelser til spilleren
        textArea = new JTextArea();
        textArea.setEditable(false); // Gør tekstområdet ikke-redigerbart
        textArea.setLineWrap(true); // Aktiver linjeskift
        textArea.setWrapStyleWord(true); // Wrap linjer ved ordgrænser
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Sæt en monospaced skrifttype
        textArea.setBackground(Color.BLACK); // Sæt baggrundsfarve
        textArea.setForeground(Color.WHITE); // Sæt tekstfarve

        // Opret en rullepanel for tekstområdet
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Vis altid den lodrette rullebjælke
        frame.add(scrollPane, BorderLayout.CENTER); // Tilføj rullepanelet til midten af vinduet

        // Opret inputpanel, der indeholder inputfeltet og sendeknappen
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField(); // Inputfelt til brugerens kommandoer
        sendButton = new JButton("Send"); // Knap til at indsende kommandoer

        // Stil inputfeltet til at matche tekstområdet
        inputField.setBackground(Color.BLACK); // Sæt baggrundsfarve
        inputField.setForeground(Color.WHITE); // Sæt tekstfarve
        inputField.setCaretColor(Color.WHITE); // Sæt markørfarven
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Brug samme skrifttype

        // Handling for sendeknappen
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Kald handleInput-metoden ved knaptryk
            }
        });

        // Tilføj key binding for Enter-tasten
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(); // Kald handleInput-metoden ved Enter-tastetryk
            }
        });

        // Tilføj inputfeltet og sendeknappen til inputpanelet
        inputPanel.add(inputField, BorderLayout.CENTER); // Tilføj inputfeltet til midten
        inputPanel.add(sendButton, BorderLayout.EAST); // Tilføj sendeknappen til højre

        // Tilføj inputpanelet til bunden af vinduet
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Vis vinduet for brugeren
        frame.setVisible(true);
    }

    // Metode til at indstille GameController
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    // Metode til at opdatere ANSI kunst baseret på det aktuelle rum
    public void updateAnsiArt(String ansiArt) {
        String[] lines = ansiArt.split("\n"); // Del ANSI kunsten i individuelle linjer
        StringBuilder centeredArt = new StringBuilder();
        int maxWidth = ansiArtArea.getVisibleRect().width / ansiArtArea.getFontMetrics(ansiArtArea.getFont()).charWidth('M'); // Estimate maximum width


        for (String line : lines) {
            int padding = (maxWidth - line.length()) / 2; // Beregn padding for centering
            if (padding > 0) {
                centeredArt.append(" ".repeat(Math.max(0, padding))); // Tilføj mellemrum for centering
            }
            centeredArt.append(line).append("\n"); // Vedhæft den originale linje
        }

        ansiArtArea.setText(centeredArt.toString()); // Sæt den centrerede tekst
    }

    // Behandl input fra brugeren
    private void handleInput() {
        String input = inputField.getText().trim(); // Få og trim brugerinput
        if (!input.isEmpty()) {
            inputField.setText(""); // Ryd inputfeltet efter indsendelse
            processInput(input); // Behandl brugerinput
        }
    }

    // Metode til at behandle brugerinputkommandoer
    private void processInput(String input) {
        if (gameController != null) {
            gameController.processCommand(input); // Send input til GameController
        } else {
            showMessage("Error: GameController is not set."); // Forbedret fejlmeddelelse
        }
    }

    // Metode til at rydde outputtekstområdet
    public void clearOutput() {
        textArea.setText(""); // Ryd tekstområdet
    }

    // Metode til at vise meddelelser til brugeren i et stiliseret format
    public void showMessage(String message) {
        textArea.append(message + "\n"); // Vedhæft meddelelsen til tekstområdet
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll til bunden af tekstområdet
    }

    // Metode til at vise rummets beskrivelser i en overlay-stil
    public void displayRoomDescription(Room room) {
        String description = "You are in " + room.getName() + "\n" + room.getDescription(); // Opret rumbeskrivelse
        showMessage(description); // Vis beskrivelsen i meddelelsesområdet
    }

    // Metode til at vise hjælpeinstruktioner til spilleren
    public void showHelp() {
        String helpMessage = "Available commands:\n" +
                "go north, go south, go east, go west - to move between rooms.\n" +
                "look - to describe the current room.\n" +
                "unlock - to unlock the door.\n" +
                "exit - to quit the game.\n" +
                "help - to show this message.\n" +
                "show map - to display the map."; // Hjælpeinstruktioner
        showMessage(helpMessage); // Vis hjælpebeskeden
    }

    // Metode til at vise kortet over det aktuelle rum og dets forbindelser
    public void showMap(String currentRoomName, String eastRoomName, String southRoomName, String westRoomName) {
        StringBuilder mapDisplay = new StringBuilder();
        mapDisplay.append("Current Room: ").append(currentRoomName).append("\n");
        mapDisplay.append("Adjacent Rooms:\n");
        mapDisplay.append("East: ").append(eastRoomName).append("\n");
        mapDisplay.append("South: ").append(southRoomName).append("\n");
        mapDisplay.append("West: ").append(westRoomName).append("\n");

        // Vis kortet i en dialog
        JOptionPane.showMessageDialog(frame, mapDisplay.toString(), "Game Map", JOptionPane.INFORMATION_MESSAGE);
    }
}
