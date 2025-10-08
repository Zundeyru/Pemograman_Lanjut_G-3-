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
                int age = Integer.parseInt(input);
                validateAge(age);
                System.out.println("Usia valid: " + age);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Input harus berupa angka bulat. Coba lagi.");
            } catch (InvalidAgeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
