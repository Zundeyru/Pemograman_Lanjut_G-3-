package org.example;

public class MenuItem {
    private final String name;
    private final double price;

    public MenuItem(String name, double price) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (price < 0) throw new IllegalArgumentException("Price must be >= 0");
        this.name = name;
        this.price = price;
    }

    /** @return item name */
    public String getName() { return name; }

    /** @return unit price */
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " (Rp " + String.format("%,.0f", price) + ")";
    }
}
