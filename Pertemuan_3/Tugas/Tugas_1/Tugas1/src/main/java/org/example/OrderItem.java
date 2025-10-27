package org.example;

/**
 * One line in the order: which menu and how many qty.
 */
public class OrderItem {
    private final MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        if (menuItem == null) throw new IllegalArgumentException("menuItem required");
        if (quantity <= 0) throw new IllegalArgumentException("qty must be > 0");
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("qty must be > 0");
        this.quantity = quantity;
    }

    /** @return line subtotal = price * qty */
    public double lineSubtotal() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return menuItem.getName() + " x" + quantity + " = Rp " +
                String.format("%,.0f", lineSubtotal());
    }
}

