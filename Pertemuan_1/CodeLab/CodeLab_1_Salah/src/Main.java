void main(String[] args) {
    String[] nama = {"Adi", "Budi", "Cahyo", "Diana", "Eva"};
    String namaterpanjang = carinamaterpanjang(nama);
    System.out.println("Nama Terpanjang Adalah : " + namaterpanjang);
}

public static String carinamaterpanjang(String[] array) {
    String namaMax = array[0];
    for (String nama : array) {
        if (nama.length() > namaMax.length()) {
            namaMax = nama;
        }
    }
    return namaMax;
}