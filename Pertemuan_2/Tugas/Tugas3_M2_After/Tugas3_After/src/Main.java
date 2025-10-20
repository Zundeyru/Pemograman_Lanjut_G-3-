import java.util.Scanner;

public class Main {

    public static final double PAJAK = 0.10;
    public static final int Syarat_Diskon = 2000000;
    public static final double DiskonSpesial = 0.05;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- Harga per malam (sengaja angka langsung, bukan konstanta) ---
        Result RoomType = getRoomType();

        System.out.println("=== SISTEM BELI TIKET / BOOKING KAMAR ===");
        System.out.println("Tipe Kamar & Harga per Malam:");
        System.out.println("1) Standard - Rp " + RoomType.hargaStandard());
        System.out.println("2) Deluxe   - Rp " + RoomType.hargaDeluxe());
        System.out.println("3) Suite    - Rp " + RoomType.hargaSuite());
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
        int JumlahKamarStandard = sc.nextInt();
        if (JumlahKamarStandard < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        System.out.print("Masukkan jumlah kamar Deluxe yang dibeli  : ");
        int JumlahKamarDeluxe = sc.nextInt();
        if (JumlahKamarDeluxe < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        System.out.print("Masukkan jumlah kamar Suite yang dibeli   : ");
        int JumlahKamarSuite = sc.nextInt();
        if (JumlahKamarSuite < 0) {
            System.out.println("Jumlah tidak boleh negatif. Program dihentikan.");
            return;
        }

        // Hitung subtotal per tipe (tanpa fungsi, kode diulang)
        long totalStandard = (long) JumlahKamarStandard * RoomType.hargaStandard() * malam;
        long totalDeluxe   = (long) JumlahKamarDeluxe   * RoomType.hargaDeluxe() * malam;
        long totalSuite    = (long) JumlahKamarSuite    * RoomType.hargaSuite() * malam;

        long subtotal = totalStandard + totalDeluxe + totalSuite;

        // Pajak & diskon (sengaja angka langsung)
        double pajak = subtotal * PAJAK;           // 10% pajak
        double diskon = 0.0;
        if (subtotal >= Syarat_Diskon) {                // Diskon 5% jika subtotal >= 2 juta
            diskon = subtotal * DiskonSpesial;
        }

        double totalBayar = subtotal + pajak - diskon;

        // Tampilkan ringkasan
        DisplayInfo(malam, JumlahKamarStandard, totalStandard, JumlahKamarDeluxe, totalDeluxe, JumlahKamarSuite, totalSuite, subtotal, (long) pajak, (long) diskon, (long) totalBayar);

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
        StrukPembayaran(JumlahKamarStandard, malam, JumlahKamarDeluxe, JumlahKamarSuite, subtotal, (long) pajak, (long) diskon, (long) totalBayar, bayar, kembalian);
    }

    private static void StrukPembayaran(int JumlahKamarStandard, int malam, int JumlahKamarDeluxe, int JumlahKamarSuite, long subtotal, long pajak, long diskon, long totalBayar, long bayar, long kembalian) {
        System.out.println("\n=========== STRUK ===========");
        System.out.println("Standard : " + JumlahKamarStandard + " kamar x " + malam + " malam");
        System.out.println("Deluxe   : " + JumlahKamarDeluxe + " kamar x " + malam + " malam");
        System.out.println("Suite    : " + JumlahKamarSuite + " kamar x " + malam + " malam");
        System.out.println("-----------------------------------------");
        System.out.println("Subtotal    : Rp " + subtotal);
        System.out.println("Pajak (10%) : Rp " + pajak);
        System.out.println("Diskon      : Rp " + diskon);
        System.out.println("TOTAL       : Rp " + totalBayar);
        System.out.println("Bayar       : Rp " + bayar);
        System.out.println("Kembalian   : Rp " + kembalian);
        System.out.println("Terima kasih!");
    }

    private static void DisplayInfo(int malam, int JumlahKamarStandard, long totalStandard, int JumlahKamarDeluxe, long totalDeluxe, int JumlahKamarSuite, long totalSuite, long subtotal, long pajak, long diskon, long totalBayar) {
        System.out.println("\n===== RINCIAN PEMBELIAN =====");
        System.out.println("Malam         : " + malam);
        System.out.println("Standard x " + JumlahKamarStandard + " -> Rp " + totalStandard);
        System.out.println("Deluxe   x " + JumlahKamarDeluxe + " -> Rp " + totalDeluxe);
        System.out.println("Suite    x " + JumlahKamarSuite + " -> Rp " + totalSuite);
        System.out.println("-----------------------------------------");
        System.out.println("Subtotal      : Rp " + subtotal);
        System.out.println("Pajak 10%     : Rp " + pajak);
        System.out.println("Diskon        : Rp " + diskon);
        System.out.println("TOTAL BAYAR   : Rp " + totalBayar);
    }

    private static Result getRoomType() {
        int hargaStandard = 300000; // Standard
        int hargaDeluxe   = 500000; // Deluxe
        int hargaSuite    = 800000; // Suite
        Result RoomType = new Result(hargaStandard, hargaDeluxe, hargaSuite);
        return RoomType;
    }

    private record Result(int hargaStandard, int hargaDeluxe, int hargaSuite) {
    }
}


