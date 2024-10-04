public class Food extends Item {
    private int healthRestored; // Amount of health restored when consumed
    private boolean isPoison;    // Indicates if this food is poisonous

    // Constructor
    public Food(String shortName, String longName, String description, int healthRestored, boolean isPoison) {
        super(shortName, longName, description); // Call the constructor of the parent class
        this.healthRestored = healthRestored; // Initialize healthRestored
        this.isPoison = isPoison; // Set poison status
    }

    // Get the amount of health restored by this food item
    public int getHealthRestored() {
        return healthRestored;
    }

    // Method to check if food is poisonous
    public boolean isPoisonous() {
        return isPoison;
    }

    @Override
    public String toString() {
        String poisonStatus = isPoison ? " (WARNING: This food is poisonous!)" : "";
        return super.toString() + " (Restores " + healthRestored + " health)" + poisonStatus;
    }
}
