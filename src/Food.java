public class Food extends Item {
    private int healthRestored; // Amount of health restored when consumed
    private boolean isPoison;

    // Constructor
    public Food(String shortName, String longName, String description, int healthRestored) {
        super(shortName, longName, description); // Call the constructor of the parent class
        this.healthRestored = healthRestored; // Initialize healthRestored
    }

    // Get the amount of health restored by this food item
    public int getHealthRestored() {
        return healthRestored;
    }

    // Method to check if food is poisonous
    public boolean isPoisonous(){
        return isPoison;
    }

    @Override
    public String toString() {
        return super.toString() + " (Restores " + healthRestored + " health)";
    }
}
