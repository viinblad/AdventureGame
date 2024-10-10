import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class LorePresentation extends JFrame {
    private final Color[] colors = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
            Color.CYAN,
            Color.WHITE
    };

    private JLabel titleLabel;
    private JTextArea loreArea;

    public LorePresentation() {
        // Set up the frame
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK); // Set background color of the frame

        // Set up the text area for the lore
        loreArea = new JTextArea();
        loreArea.setEditable(false);
        loreArea.setFont(new Font("Monospaced", Font.PLAIN, 24));
        loreArea.setBackground(Color.BLACK); // Background color
        loreArea.setForeground(Color.WHITE); // Set a default text color
        loreArea.setLineWrap(true); // Enable line wrap to avoid horizontal scrolling
        loreArea.setWrapStyleWord(true); // Ensure words are wrapped properly

        // Set up the title label
        titleLabel = new JLabel("Hell vs Heaven");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to the frame
        setLayout(new BorderLayout()); // Use BorderLayout for the frame
        add(titleLabel, BorderLayout.NORTH); // Add title at the top
        add(loreArea, BorderLayout.CENTER); // Directly add JTextArea

        setVisible(true); // Make the frame visible
    }

    public void displayLore() throws InterruptedException {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                String loreText =
                        "In a realm torn asunder by the eternal battle between good and evil, "
                                + "your soul has been ensnared in the fiery depths of Hell. "
                                + "You find yourself in the cursed domain of Malphas, a formidable DemonBoss known for his cunning and power.\n\n"
                                + "Once a loyal servant of Heaven, Malphas succumbed to the temptations of darkness, "
                                + "turning against his brethren and embracing the abyss. He now rules over this infernal realm with an iron fist, "
                                + "delighting in the suffering of those who dare to challenge him.\n\n"
                                + "To escape the torment of this wretched place, you must face Malphas in a battle of wills and strength. "
                                + "Only by vanquishing this malevolent foe can you reclaim your freedom and return to the light. "
                                + "But beware—the path is fraught with danger, and the shadows whisper of treachery and despair. "
                                + "Will you muster the courage to confront Malphas and escape the clutches of Hell?\n\n"
                                + "Prepare yourself, brave soul, for the ultimate showdown awaits...\n";

                for (String line : loreText.split("\n")) {
                    loreArea.append(line + "\n");
                    Thread.sleep(300); // Adjust delay as needed for effect
                }

                // Change colors in the title label
                for (Color color : colors) {
                    titleLabel.setForeground(color);
                    Thread.sleep(1000); // Change colors every 500ms
                }

                return null; // Return null as a signal of completion
            }

            @Override
            protected void done() {
                try {
                    get(); // Retrieve any exception that occurred during execution
                } catch (InterruptedException e) {
                    // Handle interrupted exception
                    System.err.println("Lore presentation was interrupted: " + e.getMessage());
                    dispose(); // Close the presentation
                    return;
                } catch (ExecutionException e) {
                    // Handle any other exceptions that occurred
                    throw new RuntimeException("Exception during lore presentation: " + e.getCause().getMessage(), e.getCause());
                }

                // Once lore presentation is done, you can close the screen or open the next screen
                dispose(); // Close lore presentation
                // Open your main application or next screen here
                // new MainApplication(); // Uncomment to start the main application
            }
        };

        worker.execute(); // Start the lore presentation process
    }

    // main method for debugging
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LorePresentation lorePresentation = new LorePresentation();
            try {
                lorePresentation.displayLore();
            } catch (InterruptedException e) {
                System.err.println("Error displaying lore presentation: " + e.getMessage());
            }
        });
    }
}
