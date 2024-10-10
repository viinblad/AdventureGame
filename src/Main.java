import javax.swing.*;

public static void main(String[] args) {
    // Use the SwingUtilities to ensure thread safety for GUI creation
    // Simply create an instance of StartView
    // Start view will handle the setup of UI and GameController
    SwingUtilities.invokeLater(StartView::new);
}
