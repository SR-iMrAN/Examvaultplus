import java.util.Scanner;

public class Calculator {
    // ANSI colors codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_GREEN_BRIGHT = "\u001B[92m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE_BRIGHT = "\u001B[95m";
//    public static void clearScreen() {
//        for (int i = 0; i < 15; i++) System.out.println();
//    }
    public static void runCalculator(Scanner scanner) {
        while (true) {
            // Clear screen
            System.out.print("\033[H\033[2J");
            System.out.flush();
//            clearScreen();
            // Calculator banner
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        "
                    + ANSI_BOLD + "============================== Calculator =============================="
                    + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      1. Add (+)");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      2. Subtract (-)");
            System.out.println(ANSI_GREEN_BRIGHT + "                                                                                      3. Multiply (*)");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                                                      4. Divide (/)");
            System.out.println(ANSI_RED + "                                                                                      5. Exit Calculator");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        ======================================================================" + ANSI_RESET);

            System.out.print("\nChoose operation: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println(ANSI_RED + ANSI_BOLD + "\nExiting calculator..." + ANSI_RESET);
                break;
            }
//            clearScreen();
            System.out.print("\nEnter first number: ");
            double num1 = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            scanner.nextLine();

            double result = 0;
            boolean validOperation = true;

            switch (choice) {
                case 1 -> result = num1 + num2;
                case 2 -> result = num1 - num2;
                case 3 -> result = num1 * num2;
                case 4 -> {
                    if (num2 == 0) {
                        System.out.println(ANSI_RED + "Error: Division by zero is not allowed." + ANSI_RESET);
                        validOperation = false;
                    } else {
                        result = num1 / num2;
                    }
                }
                default -> {
                    System.out.println(ANSI_RED + "Invalid choice, try again." + ANSI_RESET);
                    validOperation = false;
                }
            }

            if (validOperation) {
                System.out.printf(ANSI_GREEN_BRIGHT + "\nResult: %.2f\n" + ANSI_RESET, result);
            }

            System.out.println(ANSI_BRIGHT_YELLOW + "\nPress Enter to continue..." + ANSI_RESET);
            scanner.nextLine();
        }
    }
}
