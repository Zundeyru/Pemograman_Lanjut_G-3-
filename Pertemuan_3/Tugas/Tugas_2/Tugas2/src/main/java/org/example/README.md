# Sistem Beli Tiket / Booking Kamar (Console, Java)

Aplikasi konsol untuk menghitung biaya pemesanan kamar dengan fitur lanjutan: add-on per malam, membership, voucher, multiplier high/peak season, pajak 10%, pembulatan ke Rp100, dan cetak struk ke file.

## Fitur
- Harga default atau input manual.
- Add-on: **sarapan** dan **parkir** per kamar per malam.
- Multiplier **season** (mis. 1.2 saat peak).
- **Membership**: NONE (0%), SILVER (2%), GOLD (4%).
- **Voucher**:
    - `HEMAT50K` → potongan Rp50.000 jika dasar ≥ Rp1.000.000.
    - `PROMO10` → 10% dengan cap Rp200.000.
- Pajak **10%** dihitung dari (subtotal − semua diskon).
- **Pembulatan** total ke kelipatan **Rp100**.
- Cetak struk dan **simpan ke file .txt** (opsional).
- JavaDoc lengkap untuk kelas, konstanta, enum, record, dan metode.


## Struktur Sumber
Satu berkas:
```
src/
└─ org/
   └─ example/
      └─ Main.java
```

## Cara Menjalankan
```bash
# Kompilasi
javac -d out src/org/example/Main.java

# Jalankan
java -cp out org.example.Main
```

## Alur Input Singkat
1. Pilih harga default / input harga manual.
2. Isi jumlah malam & qty tiap tipe kamar.
3. Pilih add-on sarapan/parkir.
4. Masukkan multiplier (1.0 normal, 1.2 peak).
5. Pilih membership (NONE/SILVER/GOLD).
6. (Opsional) masukkan kode voucher.
7. Lihat rincian → lakukan pembayaran → cetak struk → simpan (opsional).

## Urutan Perhitungan
```
subtotalKamar = Σ(qty * harga * multiplier) * malam
subtotalAddon = (sarapanPerMalam + parkirPerMalam) * (qtyTotalKamar) * malam
subtotal      = subtotalKamar + subtotalAddon

diskonStandar (5% bila subtotal ≥ 2.000.000)
diskonMember  (SILVER 2% | GOLD 4%) [setelah diskon standar]
diskonVoucher (HEMAT50K / PROMO10)  [setelah diskon member]

dasarPajak = subtotal - (diskonStandar + diskonMember + diskonVoucher)
pajak      = dasarPajak * 10%

total      = dasarPajak + pajak
totalBulat = pembulatan ke Rp100 terdekat
```

## Konstanta Penting
- `PAJAK = 0.10`
- `SYARAT_DISKON = 2_000_000`
- `DISKON_SPESIAL = 0.05`
- `SARAPAN_PER_MALAM = 50_000`
- `PARKIR_PER_MALAM  = 20_000`
- Voucher: `VOUCHER_HEMAT50K`, `VOUCHER_PROMO10`; `MIN_HEMAT50K = 1_000_000`, `PROMO10_CAP = 200_000`.

## Generate JavaDoc (CLI)
```bash
# Dari root proyek (folder yang berisi src/)
javadoc -d docs -sourcepath src -subpackages org.example
# Buka docs/index.html di browser
```

## Generate JavaDoc (IntelliJ IDEA)
1. **Tools → Generate JavaDoc...**
2. Scope: pilih **Project** (atau src/org/example/Main.java saja).
3. Output directory: `docs`
4. OK → buka `docs/index.html` di browser.

## Catatan Implementasi
- Format Rupiah menggunakan `NumberFormat` locale `in-ID`.
- `record Prices` menyimpan harga per malam untuk Standard/Deluxe/Suite.
- Urutan diskon → pajak → pembulatan dijaga konsisten.
- Struk dapat disimpan ke berkas teks, penamaan otomatis.

## Lisensi
Bebas digunakan untuk pembelajaran.
