import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use the SwingUtilities to ensure thread safety for GUI creation
        // Simply create an instance of StartView
        // StartView will handle the setup of UI and GameController
        SwingUtilities.invokeLater(StartView::new);
    }
}
