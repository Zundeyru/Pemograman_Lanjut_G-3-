import java.util.Scanner;

class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) { super(message); }
}

public class Main {
    static void validateAge(int age) throws InvalidAgeException {
        if (age <= 0 || age >= 120) {
            throw new InvalidAgeException("Usia tidak valid: " + age + ". Usia harus > 0 dan < 120.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Masukkan usia Anda: ");
            String input = sc.nextLine().trim();

            try {
                int age = Integer.parseInt(input); // jika bukan angka -> NumberFormatException
                validateAge(age);                  // jika tidak valid -> InvalidAgeException
                System.out.println("Usia valid: " + age);
                break;                             // keluar kalau sudah valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Input harus berupa angka bulat. Coba lagi.");
                // loop lanjut otomatis
            } catch (InvalidAgeException e) {
                System.out.println("Error: " + e.getMessage());
                // loop lanjut otomatis
            }
        }

        sc.close();
    }
}
