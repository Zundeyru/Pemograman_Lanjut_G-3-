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

        // Proses penentuan status
        String status = (nilai >= 60) ? "Lulus" : "Tidak Lulus";

        // Output
        System.out.printf("Mahasiswa: %s | Nilai: %.2f | Status: %s%n", nama, nilai, status);

        sc.close();
    }
}
