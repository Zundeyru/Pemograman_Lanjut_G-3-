void main() {
    String[] nama = {"Adi", "Budi", "Cahyo", "Diana", "Eva"};
    String namaterpanjang = carinamaterpanjang(nama);
    System.out.println("Nama Terpanjang Adalah : " + namaterpanjang);
}

public static String carinamaterpanjang(String[] array) {
    String namaMax = array[0];
    int maxLen = namaMax.length();                  // (+) tambah: simpan panjang saat ini

    for (String nama : array) {
        if (nama.length() > maxLen) {               // (±) ganti pembanding ke maxLen
            namaMax = nama;
            maxLen = nama.length();                 // (+) update panjang maksimum
        } else if (nama.length() == maxLen) {       // (+) jika sama panjang, gabungkan
            if (!namaMax.equals(nama)) {
                namaMax += ", " + nama;
            }
        }
    }
    return namaMax;
}