import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoadingScreen extends JFrame {
    // Define color codes for ANSI art
    private final Color[] colors = {
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.BLUE,
            Color.MAGENTA,
            Color.CYAN,
            Color.WHITE
    };
    // Method to get a random color
    public Color getRandomColor() {
        Random random = new Random();
        int index = random.nextInt(colors.length); // Generate a random index
        return colors[index]; // Return the color at the random index
    }

    private JLabel loadingLabel;
    private JTextArea textArea;

    public LoadingScreen() {
        // Set up the frame
        setUndecorated(true); // Remove title bar and borders
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLocationRelativeTo(null); // Center the frame

        // Set up the text area for the ANSI art
        textArea = new JTextArea();
        textArea.setEditable(false); // Make it non-editable
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18)); // Use a monospaced font
        textArea.setBackground(Color.BLACK); // Background color
        textArea.setForeground(getRandomColor()); // Text color
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Use a scroll pane for the text area to allow scrolling if needed
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(1920, 1080)); // Set preferred size for the scroll pane

        // Set up the loading label
        loadingLabel = new JLabel("Loading...   ");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Use a larger font for loading text
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the loading label

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER); // Add the text area in the center
        add(loadingLabel, BorderLayout.SOUTH); // Add the loading label at the bottom

        setVisible(true); // Make the frame visible
    }

    public void displayLoading() throws InterruptedException, IOException {
        String ansiArt =
                "::::::::::::::::::::.........                              .        .     #%%                                                                             \n" +
                        "::::::::::::::::::::........                         =  . %:  .# +.  :#%%%%@=           +                                                                 \n" +
                        ":::::::::::::::::::........                          # .:#@ ..@@*%@@%*%%%%@@@#*=     *++   .                                                              \n" +
                        "::::::::::::::::::........                           %+%%@%#+@+##%*#*@@@@@@%%@%@@%%%%#*: .                                                                \n" +
                        "::::::::::::::::::........                          *%@@%@%##**%@%@#%@%%@%#%#%%%%%@@@@%%## ..- +*.                                                        \n" +
                        ":::::::::::::::::.......                           +@%%@@%@@%@@@@@@%@@@@@@@@@@%@*%%@%*%%##***-##.                  .                                      \n" +
                        "::::::::::::::::........                          .#@%%@%#@@@@@@@@%@@@@%%%%@@@@@@@@@%%%@%#######=                 .=                                      \n" +
                        "::::::::::::::::.......                            #%%%@%@@@@@%%%-###=-::+**%#@@@@@@@@@@@@%**#### :               *           *                          \n" +
                        ":::::::::::::::........                            %%@@@@@@@@#=-=.::++=::***#*%%@@@@@%%@@@@%*.=.:                *.         =%                           \n" +
                        "::::::::::::::........                             :@@@@@@@=.....-:-++++-+#*##=*#%@@@@@@@#@+%-.:....            **         #%.                           \n" +
                        "::::::::::::::........                               .@@@#=:::=-::++=-=-#%#%%%+=+%@@@@@@@@-=%:                .+#         %%#                            \n" +
                        ":::::::::::::........                                .+*#+-:-.:..:-=*##=#*#%%%%.@@-%@@@@@@* :@   .     .    .-+#-        #%#                              \n" +
                        ":::::::::::::.......                                 =%*==+.=-......:-+##-*#+:-@@@@+@@@@@@--=  . -         :*+#*        *##.           .                 \n" +
                        ":::::::::::::.......                                 #++-:...=%@%%##*-:+=+=%%@@@#*#+@%@@@@@*               *%%%#      +####          .@                  \n" +
                        "::::::::::::.......                                 :**:=..-@@@@@@@@%-..=#***-@@@%*=@@@##* #+              #%@:      .%%%#*         %%.                  \n" +
                        "::::::::::::.......                                #=:-+#=@@@@*@@%+=.:+*%@%%++#@@%=@%*%##%% #-            -%%+  + =  *%%%%#       ##%-                   \n" +
                        "::::::::::::......                                  %%%@@@@@@@@##*+-##@%@@@%@###-+@%==#==+*+     .#%## *#%%%%.      -##%       .##%%+                    \n" +
                        "::::::::::::......                                #  +@+:===+-=*#@@%@%@@@%@%#%%*#@@%+##=+-=+=     *%%.  :#%%%%  *****%%       #%%%%=                     \n" +
                        "::::::::::::.....                               ::   %-..:.*#%-.:*@@%@#%*##%%#%@@@@*-#+:=-=---==:-%%:   #*%*   #+%#%%#=    .. %%@%#.      %.             \n" +
                        "::::::::::::.....                                     +=-@@%:.+:-%%@*+##+*#=:%%@@@*==%%:.:+-:::-=*#@@  -%@%   . %@@@@%@%%    #%%*       *%#                \n" +
                        "::::::::::::.....                                      +@@+-=:.:*=*:%*%*##+*+%%+:*==@*=--=+=**%%%%%%%%%%%%   .-####**%@@ +@%*%%=      :%@@                 \n" +
                        "::::::::::::.....                                      +%##*++-:---+@#%%@@@%%%@@%@*+#+@-*+@#*==*++*####@@ #%%**#**+**   .%%@%%      #%@%                \n" +
                        "::::::::::::.....                                      =@@@@%@@@@@@*%@%%:@@@@@@#%+##*++*%##+++*%@**###*####%@@@%%%%#++. :%%@@%%=   +%%@%    @@@            \n" +
                        "::::::::::::.....                                       #@@@@@%%@@%#%%%@@%@@@@@@@@#%+#%#+==*#+=**-#@@@@@@@@@%#%@%%%#   #@@@@@   #@#%@@*      @=            \n" +
                        "::::::::::::.....                                        @@@@%@@@@@@%@@@@@@@@@@@@@%*%@%*+%=+=*-@@=+--:::-=-=:=%%#@%# %#%*#@*-  .@@%%%#                   \n" +
                        "::::::::::::.....                                        #@#**#+*#@@@@@@@@@@@@@@@*%%%#*%=**=@@--=++==*=:::-+-+##+-=@@@@@%%##  *@@@@%@:  ..               \n" +
                        "::::::::::::.....                                        @@@@@%@@@@@@@@@@@@@@@@%#@%#+**#*#@:*=-#=-=::*#+::::-=++-=:+*+@@%%@@@%%@@*                         \n" +
                        "::::::::::::.....                                        @@@@@@@@@@@@@@@@@@@@*#%@%#%@#**@==+-#+#%+++=:::::+*==+=####***+*@#@@@@@@:                         \n" +
                        "::::::::::::.....                                      =  .+%@@@@@@@@@@@%@@@%%@@+++%#*@@***%*++-%=--:::::::=++*++*::*#%@*#*@@@@@@@@#      @@             \n" +
                        "::::::::::::......                                   :..#%@@@@@%@@@@@@@@@@%##@%#@#%#+@%@*#%*#=%==-=--------+%====+**#-=+###%@@@@@@@      @@@.            \n" +
                        "::::::::::::....                                 .*:. *@%@@@@@@@@@@@@@@@%@+@@@%*@##*@%%*+@**%%*+**++=#=--*%+**++++#@@##%##%%%@@@@@@@     *@@@            \n" +
                        "::::::::::::.....                              ..= -+%@@@%@@@@@@@@@@@@@%@@%@+%@%##%@@#%%+#%=++#*-+--+=:%#%+*+::+%%%#@@@@*@@@@@:@@@      :@@@#  @=        \n" +
                        "::::::::::::......                             **-:#%@@%%@@@@@@@@@@@%@@%@%%#*#%**@@@+#%%#%#**+@#**=-@#*%+%*#%%###%##%@@@@@@@@@@@%%%+     @@@@  @@@@@     \n" +
                        "::::::::::::......                          .#   *@@@@@@@@@@@@@@@@@@#@@@@@@@@@@@@@@%%%###%*%*@@##%@@*@@@@@@@###%@@###%@@@%@@@@@#@@@#@@#     @@@@@ @@@@@    \n" +
                        "::::::::::::......                        * %*     +@@@%@@@@%%@@@%@@%#%%@@#@@@@@###@@@%@@###*%%%#@@@@@%%%###@@@@@@%@@@@@@@@@@@@%%@@%#@@#@       @@@@@@@@  \n" +
                        "::::::::::::....                       *+   *@@@@#@@@%@@@#%%@@#@@%@%%@@%@@#%%@@@@@@%%%@####@##@@@@%@@@@@@@@@@@###@#@@%@@@@@@@@@@@%@@@@@@   @@@@@@@  @@#@   \n" +
                        "::::::::::::...                    .= .      *%@@#%%@@@@@%@#%#%@@@@@%%@%@%@#@@@@%%@@@%#@@@@@@@@@@@%@##@@%@@@@@###%@@@@@@@@@@###@@%%%@@%@@#@@@#@@@@@@@@@@@  \n" +
                        ":::::::::::::..                  -    *-      =#@@@@@@@@@@#@@@@@@@@@@#@@#%@@@@@@#@@%@@@@#@@@@@%%@@#@@#@@@@@%@@@@@%@@#@@%@#@@@#@@@@@###@@@@@@@@@@         \n" +
                        ":::::::::::::..                 * + @@@  =  *#@@@@@@@@@@@@@@@@@@@@@@#@@@@@@%#@#@@@%%@@@@@@%#%@@@@@@@@@@@@@#@%%@@@@@%@@@##@@@@@#@@@@@@@@@@@@@@@@@@@@@@    \n" +
                        ":::::::::::::..               *% @@@@@#%@###@@#@@@@#@@@@@@%@#@@#@@%@%@@@@@#@@#@%@#@@#@@#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       %@@@@@@@      \n" +
                        ":::::::::::::..            .*  @@@@@@@@@@@@#@@%@@@@%@@@@@@@@@@@%@@@@@@%@@#%@@@@@@#%@@@@@%%@@@@%@@@#@@%@%@%@@#@@#@@@@@@@@@@@@@       =@@@@@@@@@     \n" +
                        ":::::::::::::..          -+ =  =@@@@@@@@@@@@%@@@@@@@@@@@@@@@%@@@%@%@%#@@@#@@%@@@@@@#@@@@@@@@@@#@@@@@@@@@@@@#@@@@@@@@@@@@@@        .@@@@@@@@@@   \n" +
                        ":::::::::::::..         %    .@@%@@@@@@@@@@%@%@@@#@@@#@@@@@@@%@@@@@@%@@#@@#@@#@%@@@@@@@#@@#@@@@@@@@@@@@@@@@@@@@#@@@@@@@@         .@@@@@@@@@@@     \n" +
                        ":::::::::::::..      .     =    =@@@@#@@@@@@%@@@@@@@@@@@@@@@@@@@@@#@@@@@@@@@@@@@@@@@%@@@@@@#@@@@@@@@@@@@@@@@@@@@@@@@@       @@@@@@@@@@@@@   \n" +
                        ":::::::::::::..     .+    *      =@#@@@@@@@@@%@@@@@@%@%@@%@%@@#@@@@@@@@@@@@@@@%@%@@@@@@@@@@%@@@@@%@@@@@@@@@@@@@@@@@@@@@      *@@@@@@@@@@@@@@ \n" +
                        ":::::::::::::..    -  %    +      =@%@@@%%@@#@@#@@@@@@%@%@@@@%@@@@@@@@@@@@@@@@@@@@@@@@@@#@@@@@@#@@#@@%@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@@@   \n" +
                        ":::::::::::::..     -   %      =@@@%@@@@@%@@@@@@#@@@@@@@@@@@@@@#@@@@@@@%@%@@@@@%@@@@@@#@@@@%@@@@@#@@@@@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@  \n" +
                        ":::::::::::::..    -   *      =@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%@#@@@@@@@@@@@@@@@@@@@%@%@%@%@@@@@@#@@@@@@@@@@@@@@@@@@@@@@@@@@@%@@@@@@@@@  \n" +
                        ":::::::::::::..   -         %@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#@@@@@@%@@@@@@@@@@@@@@@#@@@@@%@@@@#@@@@@@@@@@@@@@@@@@@@@@      \n";

        // Set the ANSI art to the text area
        textArea.setText(ansiArt);
        textArea.setCaretPosition(0); // Scroll to the top

        for (int i = 0; i < 10; i++) {
            loadingLabel.setForeground(colors[i % colors.length]); // Change loading message color
            loadingLabel.setText("Enjoy from Hell to Heaven" + ".".repeat(i % 4)); // Update loading message
            TimeUnit.MILLISECONDS.sleep(500); // Pause for 500 milliseconds
        }

        // Wait for user input (simulated here by a sleep)
        Thread.sleep(1000);
        System.out.println("Game starting...");

        // Close the loading screen
        dispose();
        // Here you can start your actual game logic or GUI
    }

    public static void main(String[] args) {
        LoadingScreen loadingScreen = new LoadingScreen();
        try {
            loadingScreen.displayLoading();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
