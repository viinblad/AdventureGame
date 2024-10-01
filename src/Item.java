public class Item {
    private String shortName;
    private String longName;       // Full name of the item
    private String description;    // Description of the item

    // Constructor
    public Item(String shortName, String longName, String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    // Get the short name of the item
    public String getShortName() {
        return shortName;
    }

    // Get the full name of the item (renamed to longName)
    public String getLongName() {
        return longName;
    }

    // Get the description of the item
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return longName + " (" + description + ")";
    }
}
