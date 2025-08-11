import java.util.Scanner;

public class Calculator {
    public static void runCalculator(Scanner scanner) {
        while (true) {
            System.out.println("\nCalculator Menu:");
            System.out.println("1. Add (+)");
            System.out.println("2. Subtract (-)");
            System.out.println("3. Multiply (*)");
            System.out.println("4. Divide (/)");
            System.out.println("5. Exit Calculator");
            System.out.print("Choose operation: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println("Exiting calculator.");
                break;
            }

            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            scanner.nextLine();

            double result = 0;
            boolean validOperation = true;

            switch (choice) {
                case 1: result = num1 + num2; break;
                case 2: result = num1 - num2; break;
                case 3: result = num1 * num2; break;
                case 4:
                    if (num2 == 0) {
                        System.out.println("Error: Division by zero is not allowed.");
                        validOperation = false;
                    } else {
                        result = num1 / num2;
                    }
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
                    validOperation = false;
            }

            if (validOperation) {
                System.out.printf("Result: %.2f\n", result);
            }
        }
    }
}
