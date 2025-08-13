import java.util.InputMismatchException;
import java.util.Scanner;

class CGPACalculator {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_PURPLE_BRIGHT = "\u001B[95m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN_BRIGHT = "\u001B[92m";

    private static final String[][] GRADE_TABLE = {
            {"80", "100", "A+", "4.00", "Outstanding"},
            {"75", "79", "A", "3.75", "Excellent"},
            {"70", "74", "A-", "3.50", "Very Good"},
            {"65", "69", "B+", "3.25", "Good"},
            {"60", "64", "B", "3.00", "Satisfactory"},
            {"55", "59", "B-", "2.75", "Above Average"},
            {"50", "54", "C+", "2.50", "Average"},
            {"45", "49", "C", "2.25", "Below Average"},
            {"40", "44", "D", "2.00", "Pass"},
            {"0", "39", "F", "0.00", "Fail"}
    };

    private static String[] getGradeInfo(double marks) {
        for (String[] row : GRADE_TABLE) {
            double min = Double.parseDouble(row[0]);
            double max = Double.parseDouble(row[1]);
            if (marks >= min && marks <= max) {
                return new String[]{row[2], row[3], row[4]};
            }
        }
        return new String[]{"Invalid", "0.00", "N/A"};
    }

    private static String[] getGradeInfoByGrade(String grade) {
        for (String[] row : GRADE_TABLE) {
            if (row[2].equalsIgnoreCase(grade)) {
                return new String[]{row[2], row[3], row[4]};
            }
        }
        return null;
    }

    public static void runGPA(Scanner sc ,String name) {
        while (true) {
            clearScreen();
            clearScrn();
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        " + ANSI_BOLD + "==============================  CGPA Calculator ==============================" + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      1. Marks-based GPA");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      2. Subject-based CGPA");
            System.out.println(ANSI_RED + "                                                                                      3. Return");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                pause(sc, "Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> marksBasedGPA(sc,name);
                case 2 -> subjectBasedCGPA(sc,name);
                case 3 -> { return; }
                default -> pause(sc, "Invalid choice.");
            }
        }
    }

    private static void marksBasedGPA(Scanner sc,String name) {
        try {

            clearScrn();
            double total = 0;
            String[] criteria = {
                    "Class Attendance (0-7)",
                    "Assignment (0-5)",
                    "Presentation (0-8)",
                    "3 Quizzes Total (0-15)",
                    "Midterm Exam (0-25)",
                    "Final Exam (0-40)"
            };

            for (String item : criteria) {
                System.out.print("Enter marks for " + item + ": ");
                double val = sc.nextDouble();
                total += val;
            }
            sc.nextLine();

            if (total < 0 || total > 100) throw new IllegalArgumentException("Total marks must be between 0 and 100.");

            String[] info = getGradeInfo(total);
            String partyPopper = "\uD83C\uDF89";
            System.out.println("\n" + ANSI_GREEN_BRIGHT + "Result:" + ANSI_RESET);
            System.out.printf( "\u2728 "+ANSI_BRIGHT_YELLOW+"Congratulations "+ ANSI_RESET +ANSI_RED+name+ ANSI_RESET +ANSI_BRIGHT_CYAN+", you have done a(n)"+ info[2]+ " result.%n "+ ANSI_RESET +partyPopper);

            System.out.printf( ANSI_PURPLE_BRIGHT+" CGPA: %.2f | Grade: %s | Grade Point: %s |%n", Double.parseDouble(info[1]), info[0], info[1]);


        } catch (Exception e) {
            sc.nextLine();
            pause(sc, "Error: " + e.getMessage());
        }
        pause(sc, null);
    }

    private static void subjectBasedCGPA(Scanner sc, String name) {
        clearScrn();

        double totalCredits = 0, weightedPoints = 0;

        while (true) {
            try {
                System.out.print("Enter Subject Name: ");
                String subject = sc.nextLine().trim();
                if(subject.isEmpty()) throw new IllegalArgumentException("Subject name cannot be empty.");

                double gp = -1;
                String grade = "";
                String remarks = "";


                while (true) {
                    System.out.print("Enter Letter Grade or Grade Point (A+, A, ... or 4.00, 3.75, ...): ");
                    String gradeInput = sc.nextLine().trim().toUpperCase();

                    if (gradeInput.matches("\\d+(\\.\\d+)?")) { // Accepts 3, 3.0, 3.75, etc.
                        gp = Double.parseDouble(gradeInput);
                        String[] info = getGradeInfo(cgpaToMarks(gp));
                        grade = info[0];
                        remarks = info[2];
                        System.out.printf("Grade: %s | Grade Point: %.2f%n", grade, gp);
                        break;
                    } else {
                        String[] info = getGradeInfoByGrade(gradeInput);
                        if (info != null) {
                            grade = info[0];
                            gp = Double.parseDouble(info[1]);
                            remarks = info[2];
                            System.out.printf("Grade: %s | Grade Point: %.2f%n", grade, gp);
                            break;
                        } else {
                            System.out.println("Invalid grade entered. Try again.");
                        }
                    }
                }

                // Loop until valid credits entered
                double credits;
                while (true) {
                    System.out.print("Enter Credit Hours: ");
                    String creditsInput = sc.nextLine().trim();
                    try {
                        credits = Double.parseDouble(creditsInput);
                        if (credits <= 0) {
                            System.out.println("Credit hours must be positive.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Enter numeric value for credit hours.");
                    }
                }

                totalCredits += credits;
                weightedPoints += gp * credits;

                System.out.print("Add more subjects? (y/n): ");
                String more = sc.nextLine().trim();
                if (!more.equalsIgnoreCase("y")) break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }

        double cgpa = weightedPoints / totalCredits;
        String[] info = getGradeInfo(cgpaToMarks(cgpa));

        System.out.println("\n" + ANSI_GREEN_BRIGHT + "Result:" + ANSI_RESET);

        String partyPopper = "\uD83C\uDF89";

//        System.out.printf("\u2728 Congratulations %s, you have done a(n) %s result.%n%s%n",
//                name, info[2], partyPopper);
        System.out.printf( "\u2728 "+ANSI_BRIGHT_YELLOW+"Congratulations "+ ANSI_RESET +ANSI_RED+name+ ANSI_RESET +ANSI_BRIGHT_CYAN+", you have done a(n)"+ info[2]+ " result.%n "+ ANSI_RESET +partyPopper);
        System.out.printf( ANSI_BOLD+ANSI_PURPLE_BRIGHT+"CGPA: %.2f | Grade: %s | Grade Point: %.2f |%n", cgpa, info[0], cgpa);
        pause(sc, null);
    }


    private static double cgpaToMarks(double cgpa) {
        if (cgpa >= 4.00) return 85;
        if (cgpa >= 3.75) return 77;
        if (cgpa >= 3.50) return 72;
        if (cgpa >= 3.25) return 67;
        if (cgpa >= 3.00) return 62;
        if (cgpa >= 2.75) return 57;
        if (cgpa >= 2.50) return 52;
        if (cgpa >= 2.25) return 47;
        if (cgpa >= 2.00) return 42;
        return 35;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private static void clearScrn() {
        for (int i = 0; i < 15; i++) System.out.println();
   }
    private static void pause(Scanner sc, String msg) {
        if (msg != null && !msg.isEmpty()) {
            System.out.println(msg);
        }
        System.out.print("Press Enter to continue...");
        sc.nextLine();
    }
}
