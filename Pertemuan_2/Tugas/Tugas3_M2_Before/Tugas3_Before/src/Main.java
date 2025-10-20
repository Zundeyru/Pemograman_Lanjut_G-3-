import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- Harga per malam (sengaja angka langsung, bukan konstanta) ---
        int hargaStandard = 300000; // Standard
        int hargaDeluxe   = 500000; // Deluxe
        int hargaSuite    = 800000; // Suite

        System.out.println("=== SISTEM BELI TIKET / BOOKING KAMAR ===");
        System.out.println("Tipe Kamar & Harga per Malam:");
        System.out.println("1) Standard - Rp " + hargaStandard);
        System.out.println("2) Deluxe   - Rp " + hargaDeluxe);
        System.out.println("3) Suite    - Rp " + hargaSuite);
        System.out.println("-----------------------------------------");

        // Input jumlah malam (1x untuk semua kamar)
        System.out.print("Masukkan jumlah malam: ");
        int malam = sc.nextInt();
        if (malam < 1) {
            System.out.println("Jumlah malam minimal 1. Program dihentikan.");
            return;
        }

        // Input kuantitas per tipe kamar (sengaja tanpa array/fungsi)
        System.out.print("Masukkan jumlah kamar Standard yang dibeli: ");
        int qtyStandard = sc.nextInt();
        if (qtyStandard < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        System.out.print("Masukkan jumlah kamar Deluxe yang dibeli  : ");
        int qtyDeluxe = sc.nextInt();
        if (qtyDeluxe < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        System.out.print("Masukkan jumlah kamar Suite yang dibeli   : ");
        int qtySuite = sc.nextInt();
        if (qtySuite < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        // Hitung subtotal per tipe (tanpa fungsi, kode diulang)
        long totalStandard = (long) qtyStandard * hargaStandard * malam;
        long totalDeluxe   = (long) qtyDeluxe   * hargaDeluxe   * malam;
        long totalSuite    = (long) qtySuite    * hargaSuite    * malam;

        long subtotal = totalStandard + totalDeluxe + totalSuite;

        // Pajak & diskon (sengaja angka langsung)
        double pajak = subtotal * 0.10;           // 10% pajak
        double diskon = 0.0;
        if (subtotal >= 2000000) {                // Diskon 5% jika subtotal >= 2 juta
            diskon = subtotal * 0.05;
        }

        double totalBayar = subtotal + pajak - diskon;

        // Tampilkan ringkasan
        System.out.println("\n===== RINCIAN PEMBELIAN =====");
        System.out.println("Malam         : " + malam);
        System.out.println("Standard x " + qtyStandard + " -> Rp " + totalStandard);
        System.out.println("Deluxe   x " + qtyDeluxe   + " -> Rp " + totalDeluxe);
        System.out.println("Suite    x " + qtySuite    + " -> Rp " + totalSuite);
        System.out.println("-----------------------------------------");
        System.out.println("Subtotal      : Rp " + subtotal);
        System.out.println("Pajak 10%     : Rp " + (long)pajak);
        System.out.println("Diskon        : Rp " + (long)diskon);
        System.out.println("TOTAL BAYAR   : Rp " + (long)totalBayar);

        // Pembayaran tunai (sengaja loop sederhana)
        System.out.print("\nMasukkan uang tunai: Rp ");
        long bayar = sc.nextLong();
        while (bayar < totalBayar) {
            System.out.println("Uang kurang Rp " + (long)(totalBayar - bayar));
            System.out.print("Masukkan tambahan: Rp ");
            long tambah = sc.nextLong();
            bayar += tambah;
        }

        long kembalian = (long)Math.round(bayar - totalBayar);

        // Cetak struk (tanpa formatting mewah)
        System.out.println("\n=========== STRUK ===========");
        System.out.println("Standard : " + qtyStandard + " kamar x " + malam + " malam");
        System.out.println("Deluxe   : " + qtyDeluxe   + " kamar x " + malam + " malam");
        System.out.println("Suite    : " + qtySuite    + " kamar x " + malam + " malam");
        System.out.println("-----------------------------------------");
        System.out.println("Subtotal    : Rp " + subtotal);
        System.out.println("Pajak (10%) : Rp " + (long)pajak);
        System.out.println("Diskon      : Rp " + (long)diskon);
        System.out.println("TOTAL       : Rp " + (long)totalBayar);
        System.out.println("Bayar       : Rp " + bayar);
        System.out.println("Kembalian   : Rp " + kembalian);
        System.out.println("Terima kasih!");
    }
}
