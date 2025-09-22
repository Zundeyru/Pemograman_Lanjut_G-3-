package org.example;

import java.util.Scanner;

class InvalidNumberException extends Exception {
    public InvalidNumberException(String message) {
        super(message);
    }
}

public class Main {
    // Validasi: harus angka positif (> 0) dan bukan NaN/Infinity
    static void validatePositive(double value) throws InvalidNumberException {
        if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
            throw new InvalidNumberException(
                    "Angka tidak valid: " + value + ". Harus berupa angka positif (> 0)."
            );
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Masukkan angka positif: ");
            String input = sc.nextLine().trim();

            try {
                double number = Double.parseDouble(input);  // jika bukan angka -> NumberFormatException
                validatePositive(number);                   // jika ≤0/NaN/∞ -> InvalidNumberException
                System.out.println("Valid ✔ Angka positif: " + number);
                break;                                      // keluar jika sudah valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Input harus berupa angka (contoh: 12 atau 3.14). Coba lagi.");
            } catch (InvalidNumberException e) {
                System.out.println("Error: " + e.getMessage() + " Coba lagi.");
            }
        }

        sc.close();
    }
}
