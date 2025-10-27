package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Order aggregates OrderItem(s) and calculates subtotal, tax, discount, total.
 */
public class Order {
    // KONSTANTA: gunakan autocomplete "psf" di IntelliJ untuk mempermudah
    public static final double TAX_RATE = 0.1;         // 10%
    public static final double DISCOUNT_RATE = 0.05;   // 5%
    public static final double DISCOUNT_MIN = 100_000; // min belanja untuk diskon

    private final List<OrderItem> items = new ArrayList<>();

    /** Tambah item; jika sudah ada menu sama, gabung qty. */
    public void addItem(MenuItem menuItem, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty > 0 required");
        for (OrderItem it : items) {
            if (it.getMenuItem().getName().equalsIgnoreCase(menuItem.getName())) {
                it.setQuantity(it.getQuantity() + qty);
                return;
            }
        }
        items.add(new OrderItem(menuItem, qty));
    }


    /** Hapus item berdasarkan nama menu (return true jika ada & terhapus). */
    public boolean removeItemByName(String menuName) {
        return items.removeIf(i -> i.getMenuItem().getName().equalsIgnoreCase(menuName));
    }

    public List<OrderItem> getItems() { return items; }

    /** @return subtotal dari semua item */
    public double subtotal() {
        double sum = 0;
        for (OrderItem it : items) sum += it.lineSubtotal();
        return sum;
    }

    /** @return diskon (jika subtotal >= DISCOUNT_MIN) */
    public double discount() {
        double sub = subtotal();
        return (sub >= DISCOUNT_MIN) ? (sub * DISCOUNT_RATE) : 0.0;
    }

    /** @return pajak (dihitung dari (subtotal - discount)) */
    public double tax() {
        double base = subtotal() - discount();
        return base * TAX_RATE;
    }

    /** @return total bayar = (subtotal - discount + tax) */
    public double total() {
        return subtotal() - discount() + tax();
    }

    /** Cetak nota sederhana */
    public String printReceipt(String customerName) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== NOTA PEMESANAN RESTORAN ===\n");
        if (customerName != null && !customerName.isBlank()) {
            sb.append("Pelanggan : ").append(customerName).append("\n");
        }
        sb.append("--------------------------------\n");
        for (OrderItem it : items) {
            sb.append(String.format("%-20s x%-3d Rp %,10.0f%n",
                    it.getMenuItem().getName(),
                    it.getQuantity(),
                    it.lineSubtotal()));
        }
        sb.append("--------------------------------\n");
        sb.append(String.format("Subtotal     : Rp %,10.0f%n", subtotal()));
        sb.append(String.format("Diskon       : Rp %,10.0f%n", discount()));
        sb.append(String.format("Pajak (10%%) : Rp %,10.0f%n", tax()));
        sb.append(String.format("TOTAL        : Rp %,10.0f%n", total()));
        sb.append("Terima kasih!\n");
        return sb.toString();
    }
}

