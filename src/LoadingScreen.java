import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoadingScreen extends JFrame {
    private final Color[] colors = {
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.BLUE,
            Color.MAGENTA,
            Color.CYAN,
            Color.WHITE
    };

    private JLabel loadingLabel;
    private JTextArea textArea;

    public LoadingScreen() {
        // Set up the frame
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK); // Set background color of the frame

        // Set up the text area for the ANSI art
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 28));
        textArea.setBackground(Color.BLACK); // Background color
        textArea.setForeground(Color.WHITE); // Set a default text color
        textArea.setLineWrap(false); // Disable line wrap to maintain ANSI art formatting
        textArea.setWrapStyleWord(false); // Ensure no word wrap occurs
        textArea.setCaretPosition(0); // Ensure the caret is at the start

        // Use a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(1920, 1080));

        // Set up the loading label
        loadingLabel = new JLabel("Loading...   ");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 40));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(loadingLabel, BorderLayout.SOUTH);

        setVisible(true); // Make the frame visible
    }

    public void displayLoading() throws InterruptedException, IOException {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
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

                for (String line : ansiArt.split("\n")) {
                    textArea.append(line + "\n");
                    Thread.sleep(50); // Adjust delay as needed
                }

                // Change colors in the loading label
                for (Color color : colors) {
                    loadingLabel.setForeground(color);
                    Thread.sleep(500); // Change colors every 500ms
                }

                return null; // Return null as a signal of completion
            }

            @Override
            protected void done() {
                try {
                    get(); // Retrieve any exception that occurred during execution
                } catch (InterruptedException e) {
                    // Handle interrupted exception
                    System.err.println("Loading was interrupted: " + e.getMessage());
                    dispose(); // Close the loading screen
                    return;
                } catch (ExecutionException e) {
                    // Handle any other exceptions that occurred
                    throw new RuntimeException("Exception during loading: " + e.getCause().getMessage(), e.getCause());
                }

                // Once loading is done, you can close the loading screen or open the main application
                dispose(); // Close loading screen
                // Open your main application here
                // new MainApplication(); // Uncomment to start the main application
            }
        };

        worker.execute(); // Start the loading process
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingScreen loadingScreen = new LoadingScreen();
            try {
                loadingScreen.displayLoading();
            } catch (InterruptedException | IOException e) {
                System.err.println("Error displaying loading screen: " + e.getMessage());
            }
        });
    }
}