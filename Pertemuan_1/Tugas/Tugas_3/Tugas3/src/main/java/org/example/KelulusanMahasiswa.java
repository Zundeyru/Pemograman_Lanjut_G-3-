package org.example;

import java.util.Scanner;

public class KelulusanMahasiswa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input nama
        System.out.print("Masukkan nama mahasiswa: ");
        String nama = sc.nextLine().trim();

        // Input nilai (pastikan angka)
        Double nilai = null;
        while (nilai == null) {
            System.out.print("Masukkan nilai ujian akhir (0 - 100): ");
            if (sc.hasNextDouble()) {
                double n = sc.nextDouble();
                // Validasi rentang (opsional, tapi baik untuk kualitas data)
                if (n < 0 || n > 100) {
                    System.out.println("Nilai harus di antara 0 sampai 100. Coba lagi.");
                    continue;
                }
                nilai = n;
            } else {
                // Buang token non-angka dan minta ulang
                System.out.println("Input nilai harus berupa angka. Coba lagi.");
                sc.next();
            }
        }

        String status = (nilai >= 60) ? "Lulus" : "Tidak Lulus";

        System.out.printf("Mahasiswa: %s | Nilai: %.2f | Status: %s%n", nama, nilai, status);

        sc.close();
    }
}

// Deskripsi Formal

//Inisialisasi Scanner untuk membaca input.
//Baca nama menggunakan nextLine() dan lakukan trim().
//Siapkan variabel Double nilai = null.
//Perulangan validasi nilai:
//Tampilkan prompt “Masukkan nilai ujian akhir (0 - 100): ”.
//Jika hasNextDouble() benar → baca ke double n.
//Jika n di luar [0, 100] → tampilkan pesan error dan ulangi.
//Jika valid → set nilai = n untuk keluar dari loop.
//Jika hasNextDouble() salah → tampilkan pesan “Input nilai harus berupa angka”, buang token salah dengan sc.next(), dan ulangi.
//Tentukan status menggunakan operator ternary:
//status = (nilai >= 60) ? "Lulus" : "Tidak Lulus";
//Cetak ringkasan hasil dengan System.out.printf dan tutup Scanner.


// Deskripsi Informal

//Program ini intinya buat nanya nama dan nilai ujian kamu, trus ngecek apakah nilainya bener (angka dan di antara 0 sampai 100).
// Kalau  salah ketik (misal nulis “sembilan puluh” atau 120), programnya bakal ngasih tahu kesalahan dan minta ulang sampai kamu masukin nilai yang valid.
//Begitu nilainya oke, program bakal ngebandingin ke 60:
//Kalau ≥ 60 → “Lulus” 🎉
//Kalau < 60 → “Tidak Lulus”
//Di akhir, dia nyetak ringkasan rapi: nama kamu, nilai (dibulatkan 2 angka di belakang koma), dan status lulus/tidak lulus.
// Simpel, aman dari typo, dan nggak bakal lanjut sebelum input nilainya bener.