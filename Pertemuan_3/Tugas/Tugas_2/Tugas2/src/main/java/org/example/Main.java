package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * <h1>Sistem Beli Tiket / Booking Kamar (Console)</h1>
 * <p>
 * Aplikasi konsol untuk memproses pemesanan kamar: menghitung subtotal (kamar + add-on),
 * diskon berlapis (diskon standar, membership, voucher), pajak, pembulatan,
 * dan mencetak struk (opsional simpan ke file).
 * </p>
 *
 * <h2>Fitur Utama</h2>
 * <ul>
 *   <li>Harga default atau input manual.</li>
 *   <li>Add-on per kamar per malam: sarapan &amp; parkir.</li>
 *   <li>Multiplier high/peak season.</li>
 *   <li>Membership (NONE/SILVER/GOLD) → diskon bertingkat.</li>
 *   <li>Voucher: {@value #VOUCHER_HEMAT50K} (flat) dan {@value #VOUCHER_PROMO10} (persentase, ada cap).</li>
 *   <li>Format Rupiah &amp; pembulatan ke kelipatan Rp100.</li>
 *   <li>Struk dapat disimpan ke file .txt.</li>
 * </ul>
 *
 * <h2>Urutan Perhitungan</h2>
 * <pre>
 * subtotalKamar = Σ(qty * harga * multiplier) * malam
 * subtotalAddon = (sarapanPerMalam + parkirPerMalam) * (qtyTotalKamar) * malam
 * subtotal      = subtotalKamar + subtotalAddon
 *
 * diskonStandar (5% bila subtotal ≥ 2.000.000)
 * diskonMember  (SILVER 2% | GOLD 4%) [setelah diskon standar]
 * diskonVoucher (HEMAT50K / PROMO10)  [setelah diskon member]
 *
 * dasarPajak = subtotal - (diskonStandar + diskonMember + diskonVoucher)
 * pajak      = dasarPajak * 10%
 *
 * total      = dasarPajak + pajak
 * totalBulat = pembulatan ke Rp100 terdekat
 * </pre>
 *
 * <p><b>Persyaratan:</b> Java 17+ direkomendasikan.</p>
 */
public class Main {

    /** Persentase pajak (10%). */
    public static final double PAJAK = 0.10;

    /** Ambang minimal subtotal untuk mendapatkan diskon spesial (Rp2.000.000). */
    public static final int SYARAT_DISKON = 2_000_000;

    /** Besaran diskon spesial (5%). */
    public static final double DISKON_SPESIAL = 0.05;

    /** Harga add-on sarapan per kamar per malam (Rp50.000). */
    public static final int SARAPAN_PER_MALAM = 50_000;

    /** Harga add-on parkir per kamar per malam (Rp20.000). */
    public static final int PARKIR_PER_MALAM  = 20_000;

    /** Kode voucher flat Rp50.000 (minimal base Rp1.000.000). */
    public static final String VOUCHER_HEMAT50K = "HEMAT50K";

    /** Kode voucher 10% dengan batas maksimum (cap) Rp200.000. */
    public static final String VOUCHER_PROMO10  = "PROMO10";

    /** Minimal dasar untuk {@link #VOUCHER_HEMAT50K}. */
    public static final int MIN_HEMAT50K = 1_000_000;

    /** Batas maksimum potongan untuk {@link #VOUCHER_PROMO10}. */
    public static final int PROMO10_CAP  = 200_000;

    /**
     * Tingkat membership untuk diskon tambahan.
     * <ul>
     *   <li>{@link #NONE}  : tanpa diskon</li>
     *   <li>{@link #SILVER}: 2%</li>
     *   <li>{@link #GOLD}  : 4%</li>
     * </ul>
     */
    enum Membership { NONE, SILVER, GOLD }

    /** Diskon membership SILVER (2%). */
    public static final double DISC_SILVER = 0.02;

    /** Diskon membership GOLD (4%). */
    public static final double DISC_GOLD   = 0.04;

    /** Scanner tunggal untuk input. */
    private static final Scanner sc = new Scanner(System.in);

    /** Formatter Rupiah (ID). */
    private static final NumberFormat RP = NumberFormat.getCurrencyInstance(new Locale("in","ID"));

    /**
     * Titik masuk program.
     * <p>Menjalankan alur input → hitung → rincian → pembayaran → (opsional) simpan struk.</p>
     *
     * @param args argumen baris perintah (tidak digunakan)
     */
    public static void main(String[] args) {
        // 1) Harga per malam
        Prices prices = askPricesOrUseDefault();

        // Info awal
        System.out.println("\n=== SISTEM BELI TIKET / BOOKING KAMAR (FITUR LANJUT) ===");
        System.out.printf("1) Standard - %s%n", RP.format(prices.standard()));
        System.out.printf("2) Deluxe   - %s%n", RP.format(prices.deluxe()));
        System.out.printf("3) Suite    - %s%n", RP.format(prices.suite()));
        System.out.println("-----------------------------------------");

        // 2) Input jumlah malam & qty
        int malam = askInt("Masukkan jumlah malam", v -> v >= 1, "Minimal 1 malam.");
        int qtyStd = askInt("Masukkan jumlah kamar Standard", v -> v >= 0, "Tidak boleh negatif.");
        int qtyDlx = askInt("Masukkan jumlah kamar Deluxe  ", v -> v >= 0, "Tidak boleh negatif.");
        int qtySte = askInt("Masukkan jumlah kamar Suite   ", v -> v >= 0, "Tidak boleh negatif.");

        // 3) Add-on
        boolean pakaiSarapan = askYesNo("Tambahkan sarapan? (y/n)");
        boolean pakaiParkir  = askYesNo("Tambahkan parkir?  (y/n)");
        int sarapanPerMalam = pakaiSarapan ? SARAPAN_PER_MALAM : 0;
        int parkirPerMalam  = pakaiParkir  ? PARKIR_PER_MALAM  : 0;

        // 4) Season & membership & voucher
        double multiplier = askDouble("Multiplier season (mis. 1.0 normal, 1.2 peak)", v -> v >= 1.0, "Minimal 1.0.");
        Membership member = askMembership();
        String voucher = askLine("Kode voucher (kosongkan jika tidak ada)").trim().toUpperCase();

        // 5) Subtotal kamar
        long totalStd = Math.round(qtyStd * prices.standard() * multiplier) * (long) malam;
        long totalDlx = Math.round(qtyDlx * prices.deluxe()   * multiplier) * (long) malam;
        long totalSte = Math.round(qtySte * prices.suite()    * multiplier) * (long) malam;
        long subtotalKamar = totalStd + totalDlx + totalSte;

        // 6) Subtotal add-on
        int totalKamar = qtyStd + qtyDlx + qtySte;
        long subtotalAddon = (long) (sarapanPerMalam + parkirPerMalam) * totalKamar * malam;

        // 7) Diskon
        long subtotal = subtotalKamar + subtotalAddon;
        long diskonStandar = subtotal >= SYARAT_DISKON ? Math.round(subtotal * DISKON_SPESIAL) : 0;
        long afterStandar = subtotal - diskonStandar;

        double memberRate = switch (member) {
            case NONE -> 0.0;
            case SILVER -> DISC_SILVER;
            case GOLD -> DISC_GOLD;
        };
        long diskonMember = Math.round(afterStandar * memberRate);
        long afterMember = afterStandar - diskonMember;

        long diskonVoucher = hitungDiskonVoucher(voucher, afterMember);
        long dasarPajak = Math.max(0, afterMember - diskonVoucher);

        // 8) Pajak & total
        long pajak = Math.round(dasarPajak * PAJAK);
        long totalBayar = dasarPajak + pajak;
        long totalPembulatan = bulatkanKeRatusanTerdekat(totalBayar);

        // 9) Rincian
        printRincian(
                malam, prices, qtyStd, qtyDlx, qtySte,
                multiplier, sarapanPerMalam, parkirPerMalam,
                subtotalKamar, subtotalAddon, subtotal,
                diskonStandar, diskonMember, diskonVoucher,
                pajak, totalBayar, totalPembulatan, member, voucher
        );

        // 10) Pembayaran
        long bayar = askLong("Masukkan uang tunai (Rp)", v -> v >= 0, "Tidak boleh negatif.");
        while (bayar < totalPembulatan) {
            System.out.printf("Uang kurang %s%n", RP.format(totalPembulatan - bayar));
            bayar += askLong("Masukkan tambahan (Rp)", v -> v > 0, "Harus > 0.");
        }
        long kembalian = bayar - totalPembulatan;

        // 11) Struk
        String customer = askLine("Nama pelanggan (opsional)");
        String receipt = buildReceipt(
                customer, malam, prices, qtyStd, qtyDlx, qtySte,
                multiplier, sarapanPerMalam, parkirPerMalam,
                subtotalKamar, subtotalAddon, subtotal,
                diskonStandar, diskonMember, diskonVoucher,
                pajak, totalPembulatan, bayar, kembalian, member, voucher
        );
        System.out.println(receipt);

        // 12) Simpan struk (opsional)
        if (askYesNo("Simpan struk ke file .txt? (y/n)")) {
            String fn = "struk-" + System.currentTimeMillis() + ".txt";
            try (FileWriter fw = new FileWriter(fn)) {
                fw.write(receipt);
                System.out.println("Struk tersimpan: " + fn);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan struk: " + e.getMessage());
            }
        }

        System.out.println("Terima kasih!");
    }

    // ================== LOGIKA & UTIL ==================

    /**
     * Menghitung diskon berdasarkan kode voucher.
     *
     * @param voucher kode voucher (case-insensitive, bisa kosong)
     * @param base    nilai dasar setelah diskon standar & membership
     * @return besar potongan (rupiah). Jika voucher tidak valid/ tidak memenuhi syarat → 0.
     */
    private static long hitungDiskonVoucher(String voucher, long base) {
        if (voucher == null || voucher.isBlank()) return 0;
        if (voucher.equals(VOUCHER_HEMAT50K)) {
            return (base >= MIN_HEMAT50K) ? 50_000 : 0;
        }
        if (voucher.equals(VOUCHER_PROMO10)) {
            long pot = Math.round(base * 0.10);
            return Math.min(pot, PROMO10_CAP);
        }
        return 0;
    }

    /**
     * Membulatkan nilai ke kelipatan Rp100 terdekat.
     * <ul>
     *   <li>Sisa &ge; 50 → dibulatkan naik</li>
     *   <li>Sisa &lt; 50 → dibulatkan turun</li>
     * </ul>
     *
     * @param nilai nilai awal
     * @return nilai hasil pembulatan ke Rp100
     */
    private static long bulatkanKeRatusanTerdekat(long nilai) {
        long sisa = nilai % 100;
        if (sisa >= 50) return nilai + (100 - sisa);
        return nilai - sisa;
    }

    /**
     * Mencetak rincian pembelian ke konsol.
     *
     * @param malam jumlah malam
     * @param prices struktur harga per malam
     * @param qtyStd qty Standard
     * @param qtyDlx qty Deluxe
     * @param qtySte qty Suite
     * @param multiplier faktor season
     * @param sarapanPerMalam biaya sarapan / kamar / malam
     * @param parkirPerMalam  biaya parkir / kamar / malam
     * @param subtotalKamar subtotal semua kamar
     * @param subtotalAddon subtotal add-on
     * @param subtotal subtotalKamar + subtotalAddon
     * @param discStandar diskon 5% jika syarat terpenuhi
     * @param discMember diskon membership
     * @param discVoucher diskon voucher
     * @param pajak pajak 10%
     * @param totalBayar total sebelum pembulatan
     * @param totalBulat total sesudah pembulatan Rp100
     * @param member jenis membership
     * @param voucher kode voucher (asli yang diinput)
     */
    private static void printRincian(
            int malam, Prices prices, int qtyStd, int qtyDlx, int qtySte,
            double multiplier, int sarapanPerMalam, int parkirPerMalam,
            long subtotalKamar, long subtotalAddon, long subtotal,
            long discStandar, long discMember, long discVoucher,
            long pajak, long totalBayar, long totalBulat, Membership member, String voucher
    ) {
        System.out.println("\n===== RINCIAN PEMBELIAN =====");
        System.out.printf("Malam         : %d%n", malam);
        System.out.printf("Multiplier    : x%.2f%n", multiplier);
        System.out.println("-----------------------------------------");
        if (qtyStd > 0)
            System.out.printf("Standard x %-3d -> %s%n", qtyStd, RP.format(subtotalPerTipe(qtyStd, prices.standard(), multiplier, malam)));
        if (qtyDlx > 0)
            System.out.printf("Deluxe   x %-3d -> %s%n", qtyDlx, RP.format(subtotalPerTipe(qtyDlx, prices.deluxe(),   multiplier, malam)));
        if (qtySte > 0)
            System.out.printf("Suite    x %-3d -> %s%n", qtySte, RP.format(subtotalPerTipe(qtySte, prices.suite(),    multiplier, malam)));

        System.out.println("-----------------------------------------");
        System.out.printf("Subtotal Kamar : %s%n", RP.format(subtotalKamar));

        if (sarapanPerMalam > 0 || parkirPerMalam > 0) {
            System.out.printf("Add-on/malam   : Sarapan %s | Parkir %s%n",
                    RP.format(sarapanPerMalam), RP.format(parkirPerMalam));
            System.out.printf("Subtotal Add-on: %s%n", RP.format(subtotalAddon));
        }

        System.out.println("-----------------------------------------");
        System.out.printf("Subtotal       : %s%n", RP.format(subtotal));
        System.out.printf("Diskon 5%%      : %s%n", RP.format(discStandar));
        System.out.printf("Diskon Member  : %s (%s)%n", RP.format(discMember), member);
        System.out.printf("Diskon Voucher : %s (%s)%n", RP.format(discVoucher), (voucher == null || voucher.isBlank()) ? "-" : voucher);
        long dasarPajak = Math.max(0, subtotal - discStandar - discMember - discVoucher);
        System.out.printf("Dasar Pajak    : %s%n", RP.format(dasarPajak));
        System.out.printf("Pajak (10%%)    : %s%n", RP.format(pajak));
        System.out.printf("TOTAL          : %s%n", RP.format(totalBayar));
        System.out.printf("Pembulatan     : %s%n", RP.format(totalBulat));
    }

    /**
     * Menghitung subtotal per tipe kamar (qty * harga * multiplier * malam).
     *
     * @param qty qty kamar tipe itu
     * @param harga harga per malam
     * @param mult multiplier season
     * @param malam jumlah malam
     * @return subtotal tipe kamar
     */
    private static long subtotalPerTipe(int qty, int harga, double mult, int malam) {
        return Math.round(qty * harga * mult) * (long) malam;
    }

    /**
     * Menyusun teks struk (siap ditampilkan atau disimpan).
     *
     * @param customer       nama pelanggan (opsional)
     * @param malam          jumlah malam
     * @param prices         struktur harga
     * @param qtyStd         qty Standard
     * @param qtyDlx         qty Deluxe
     * @param qtySte         qty Suite
     * @param multiplier     faktor season
     * @param sarapanPerMalam biaya sarapan per malam
     * @param parkirPerMalam  biaya parkir per malam
     * @param subtotalKamar  subtotal kamar
     * @param subtotalAddon  subtotal add-on
     * @param subtotal       subtotalKamar + subtotalAddon
     * @param discStandar    diskon standar (5%)
     * @param discMember     diskon membership
     * @param discVoucher    diskon voucher
     * @param pajak          pajak 10%
     * @param totalBulat     total setelah pembulatan
     * @param bayar          nominal pembayaran
     * @param kembalian      kembalian
     * @param member         membership
     * @param voucher        kode voucher
     * @return string struk siap cetak/simpan
     */
    private static String buildReceipt(
            String customer, int malam, Prices prices, int qtyStd, int qtyDlx, int qtySte,
            double multiplier, int sarapanPerMalam, int parkirPerMalam,
            long subtotalKamar, long subtotalAddon, long subtotal,
            long discStandar, long discMember, long discVoucher,
            long pajak, long totalBulat, long bayar, long kembalian,
            Membership member, String voucher
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=========== STRUK ==========\n");
        if (customer != null && !customer.isBlank()) {
            sb.append("Pelanggan    : ").append(customer).append("\n");
        }
        sb.append(String.format("Malam        : %d%n", malam));
        sb.append(String.format("Multiplier   : x%.2f%n", multiplier));
        sb.append("-----------------------------------------\n");
        if (qtyStd > 0)
            sb.append(String.format("Standard x %-3d @%s%n", qtyStd, RP.format(prices.standard())));
        if (qtyDlx > 0)
            sb.append(String.format("Deluxe   x %-3d @%s%n", qtyDlx, RP.format(prices.deluxe())));
        if (qtySte > 0)
            sb.append(String.format("Suite    x %-3d @%s%n", qtySte, RP.format(prices.suite())));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Subtotal Kamar : %s%n", RP.format(subtotalKamar)));
        if (sarapanPerMalam > 0 || parkirPerMalam > 0) {
            sb.append(String.format("Add-on/malam   : Sarapan %s | Parkir %s%n", RP.format(sarapanPerMalam), RP.format(parkirPerMalam)));
            sb.append(String.format("Subtotal Add-on: %s%n", RP.format(subtotalAddon)));
        }
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Subtotal       : %s%n", RP.format(subtotal)));
        sb.append(String.format("Diskon 5%%      : %s%n", RP.format(discStandar)));
        sb.append(String.format("Diskon Member  : %s (%s)%n", RP.format(discMember), member));
        sb.append(String.format("Diskon Voucher : %s (%s)%n", RP.format(discVoucher), (voucher == null || voucher.isBlank()) ? "-" : voucher));
        long dasarPajak = Math.max(0, subtotal - discStandar - discMember - discVoucher);
        sb.append(String.format("Dasar Pajak    : %s%n", RP.format(dasarPajak)));
        sb.append(String.format("Pajak (10%%)    : %s%n", RP.format(pajak)));
        sb.append(String.format("TOTAL (bulat)  : %s%n", RP.format(totalBulat)));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Bayar          : %s%n", RP.format(bayar)));
        sb.append(String.format("Kembalian      : %s%n", RP.format(kembalian)));
        sb.append("Terima kasih!\n");
        return sb.toString();
    }

    // ====== INPUT HELPERS ======

    /**
     * Membaca bilangan bulat dari input dengan validasi kustom.
     *
     * @param prompt  teks prompt
     * @param ok      predicate penerimaan nilai
     * @param msgIfFail pesan jika gagal validasi
     * @return nilai int yang lolos validasi
     */
    private static int askInt(String prompt, java.util.function.IntPredicate ok, String msgIfFail) {
        while (true) {
            System.out.print(prompt + " : ");
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (ok.test(v)) return v;
                System.out.println(msgIfFail);
            } catch (NumberFormatException e) {
                System.out.println("Harus angka bulat.");
            }
        }
    }

    /**
     * Membaca bilangan long dari input dengan validasi kustom.
     *
     * @param prompt  teks prompt
     * @param ok      predicate penerimaan nilai
     * @param msgIfFail pesan jika gagal validasi
     * @return nilai long yang lolos validasi
     */
    private static long askLong(String prompt, java.util.function.LongPredicate ok, String msgIfFail) {
        while (true) {
            System.out.print(prompt + " : ");
            String s = sc.nextLine().trim();
            try {
                long v = Long.parseLong(s);
                if (ok.test(v)) return v;
                System.out.println(msgIfFail);
            } catch (NumberFormatException e) {
                System.out.println("Harus angka bulat.");
            }
        }
    }

    /**
     * Membaca bilangan double dari input dengan validasi kustom.
     *
     * @param prompt  teks prompt
     * @param ok      predicate penerimaan nilai
     * @param msgIfFail pesan jika gagal validasi
     * @return nilai double yang lolos validasi
     */
    private static double askDouble(String prompt, java.util.function.DoublePredicate ok, String msgIfFail) {
        while (true) {
            System.out.print(prompt + " : ");
            String s = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(s);
                if (ok.test(v)) return v;
                System.out.println(msgIfFail);
            } catch (NumberFormatException e) {
                System.out.println("Harus angka (mis. 1.2).");
            }
        }
    }

    /**
     * Pertanyaan ya/tidak.
     *
     * @param prompt teks prompt
     * @return true jika jawaban "y"/"ya", false jika "n"/"no"
     */
    private static boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("ya")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Jawab y/n.");
        }
    }

    /**
     * Membaca satu baris string dari input.
     *
     * @param prompt teks prompt
     * @return string (boleh kosong)
     */
    private static String askLine(String prompt) {
        System.out.print(prompt + " : ");
        return sc.nextLine();
    }

    /**
     * Memilih membership.
     *
     * @return {@link Membership} yang valid
     */
    private static Membership askMembership() {
        while (true) {
            System.out.print("Membership (NONE/SILVER/GOLD): ");
            String s = sc.nextLine().trim().toUpperCase();
            try {
                return Membership.valueOf(s.isBlank() ? "NONE" : s);
            } catch (IllegalArgumentException e) {
                System.out.println("Pilihan tidak valid.");
                System.out.println("kamu belum makan");
            }
        }
    }

    // ====== HARGA (default atau input user) ======

    /**
     * Struktur harga per malam untuk tiap tipe kamar.
     *
     * @param standard harga Standard
     * @param deluxe   harga Deluxe
     * @param suite    harga Suite
     */
    private record Prices(int standard, int deluxe, int suite) {}

    /**
     * Meminta konfirmasi penggunaan harga default, atau input manual dari pengguna.
     *
     * @return {@link Prices} sesuai pilihan
     */
    private static Prices askPricesOrUseDefault() {
        System.out.print("Gunakan harga default? (y/n) ");
        String s = sc.nextLine().trim().toLowerCase();
        if (s.equals("y") || s.equals("ya") || s.isBlank()) {
            return new Prices(300_000, 500_000, 800_000);
        }
        int std = askInt("Harga Standard per malam", v -> v >= 0, "Tidak boleh negatif.");
        int dlx = askInt("Harga Deluxe   per malam", v -> v >= 0, "Tidak boleh negatif.");
        int ste = askInt("Harga Suite    per malam", v -> v >= 0, "Tidak boleh negatif.");
        return new Prices(std, dlx, ste);
    }
}
