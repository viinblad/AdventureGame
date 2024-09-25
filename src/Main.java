public class Main {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        Adventure adventure = new Adventure(ui);
        adventure.startGame();
    }
}
