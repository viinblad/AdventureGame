public class Item {
    private String name;         // Name of the item
    private String description;  // Description of the item

    // Constructor
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
