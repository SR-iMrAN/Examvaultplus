import java.util.Scanner;

public class GPACalculator {
    public static void runGPA(Scanner scanner) {
        System.out.print("Enter number of subjects: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        double totalPoints = 0;
        for (int i = 1; i <= n; i++) {
            System.out.print("Enter grade point for subject " + i + " (0.0 - 4.0): ");
            double grade = scanner.nextDouble();
            scanner.nextLine();

            if (grade < 0 || grade > 4) {
                System.out.println("Invalid grade point, please enter between 0.0 and 4.0");
                i--;
                continue;
            }
            totalPoints += grade;
        }

        double gpa = totalPoints / n;
        System.out.printf("Your GPA is: %.2f\n", gpa);
    }
}
