public class Potion extends Item {
    private int healthRestored; // Amount of health restored by this potion
    private int attackBoost; // Attack boost provided by this potion
    private boolean isPoison; // Indicates if this potion is poisonous

    // Constructor for potions with health restoration, attack boost, and poison status
    public Potion(String shortName, String longName, String description, int healthRestored, int attackBoost, boolean isPoison) {
        super(shortName, longName, description);
        this.healthRestored = healthRestored;
        this.attackBoost = attackBoost;
        this.isPoison = isPoison; // Set poison status
    }

    // Getter for health restored
    public int getHealthRestored() {
        return healthRestored;
    }

    // Getter for attack boost update this later when implemented players stats
    public int getAttackBoost() {
        return attackBoost;
    }

    // Method to check if potion is poisonous
    public boolean isPoisonous() {
        return isPoison;
    }

    @Override
    public String toString() {
        StringBuilder potionInfo = new StringBuilder(super.toString()); // Call the parent class's toString

        if (healthRestored > 0) {
            potionInfo.append(" (Restores ").append(healthRestored).append(" health)");
        }
        if (attackBoost > 0) {
            potionInfo.append(" (Boosts attack by ").append(attackBoost).append(")");
        }
        if (isPoison) {
            potionInfo.append(" (WARNING: This potion is poisonous!)");
        }

        return potionInfo.toString(); // Return the complete potion information
    }
}
