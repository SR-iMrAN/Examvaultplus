import java.io.Console;
import java.util.Scanner;

public class ExamVaultPlus {

    private static final int BOX_WIDTH = 100; // width for menu box

    public static void main(String[] args) {
        Student.loadStudents();
        Teacher.loadTeachers();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            clrscr();
            printBox("ExamVaultPlus", "1. Registration", "2. Login", "3. Exit");
            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registrationMenu(scanner);
                case 2 -> loginMenu(scanner);
                case 3 -> {
                    printMessage("Exiting program. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> pause(scanner, "Invalid choice, try again.");
            }
        }
    }

    // Centered menu box
    public static void printBox(String title, String... options) {
        String border = "=".repeat(BOX_WIDTH);
        System.out.println(border);
        System.out.println(centerText("=== " + title + " ===", BOX_WIDTH));
        System.out.println(border);
        for (String opt : options) {
            System.out.println(centerText(opt, BOX_WIDTH));
        }
        System.out.println(border);
    }

    public static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        if (padding <= 0) return text;
        return " ".repeat(padding) + text;
    }

    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMessage(String message) {
        System.out.println("\n" + message + "\n");
    }

    // Password masking (works in terminal, fallback for IDE)
    public static String readPassword(String prompt, Scanner scanner) {
        Console console = System.console();
        if (console != null) {
            char[] pwdArray = console.readPassword(prompt);
            return new String(pwdArray);
        } else {
            // Fallback for IDE (password visible)
            System.out.print(prompt);
            return scanner.nextLine();
        }
    }

    static void registrationMenu(Scanner scanner) {
        while (true) {
            clrscr();
            printBox("Registration", "1. As Teacher", "2. As Student", "3. Return");
            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    Registration reg = new TeacherRegistration();
                    reg.register                                   ();
                    reg.save();
                    pause(scanner, null);
                }
                case 2 -> {
                    Registration reg1 = new StudentRegistration();
                    reg1.register();
                    reg1.save();
                    pause(scanner, null);
                }
//                case 2 -> {
//                    System.out.print("Enter your Name: ");
//                    String name = scanner.nextLine();
//                    System.out.print("Enter your ID: ");
//                    String id = scanner.nextLine();
//                    System.out.print("Enter your Contact Number: ");
//                    String contact = scanner.nextLine();
//
//                    boolean success = Student.addStudent(name, id, contact);
//                    if (success)
//                        printMessage("Registration successful! Your ID is also your password.");
//                    else
//                        printMessage("ID already registered.");
//                    pause(scanner, null);
//                }
                case 3 -> { return; }
                default -> pause(scanner, "Invalid choice.");
            }
        }
    }

    static void loginMenu(Scanner scanner) {
        while (true) {
            clrscr();
            printBox("Login", "1. Login as Teacher", "2. Login as Student", "3. Return");
            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Teacher Username: ");
                    String username = scanner.nextLine();
                    String password = readPassword("Enter Teacher Password: ", scanner);

                    if (Teacher.checkLogin(username, password)) {
                        teacherMenu(scanner, username);
                    } else {
                        pause(scanner, "Invalid teacher credentials.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter your ID: ");
                    String id = scanner.nextLine();
                    String password = readPassword("Enter your Password: ", scanner);

                    if (Student.checkLogin(id, password)) {
                        studentMenu(scanner, id);
                    } else {
                        pause(scanner, "Invalid student credentials.");
                    }
                }
                case 3 -> { return; }
                default -> pause(scanner, "Invalid choice.");
            }
        }
    }

    static void studentMenu(Scanner scanner, String studentId) {
        while (true) {
            clrscr();
            printBox("Student Menu",
                    "1. Take Quiz",
                    "2. View Results",
                    "3. GPA Calculator",
                    "4. Calculator",
                    "5. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        pause(scanner, "No subjects available. Please contact your teacher.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject by number: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        pause(scanner, "Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);

                    Quiz quiz = new Quiz();
                    quiz.loadQuestions(subject);
                    quiz.startQuiz(scanner, studentId, subject);
                    pause(scanner, null);
                }
                case 2 -> {
                    Student.viewResults(studentId);
                    pause(scanner, null);
                }
                case 3 -> {
                    GPACalculator.runGPA(scanner);
                    pause(scanner, null);
                }
                case 4 -> {
                    Calculator.runCalculator(scanner);
                    pause(scanner, null);
                }
                case 5 -> {
                    printMessage("Logging out...");
                    pause(scanner, null);
                    return;
                }
                default -> pause(scanner, "Invalid choice, try again.");
            }
        }
    }

    static void teacherMenu(Scanner scanner, String username) {
        while (true) {
            clrscr();
            printBox("Teacher Menu",
                    "1. Add Subject",
                    "2. Add Questions (Manual)",
                    "3. Import Questions",
                    "4. View Student Results",
                    "5. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new subject name: ");
                    String subject = scanner.nextLine();
                    SubjectManager.addSubject(subject);
                    pause(scanner, null);
                }
                case 2 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        pause(scanner, "No subjects found. Add a subject first.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject by number: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        pause(scanner, "Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);
                    while (true) {
                        printBox("Choose Question Type",
                                "1. MCQ",
                                "2. CQ (Coming soon)",
                                "3. MCQ + CQ (Coming soon)",
                                "4. Return");
                        System.out.print("Enter choice: ");

                        int qChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (qChoice == 4) break;

                        switch (qChoice) {
                            case 1 -> Teacher.addMCQQuestion(subject, scanner);
                            case 2 -> printMessage("CQ feature coming soon.");
                            case 3 -> printMessage("MCQ + CQ feature coming soon.");
                            default -> printMessage("Invalid choice.");
                        }
                    }
                }
                case 3 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        pause(scanner, "No subjects found. Add a subject first.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject by number to import questions: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        pause(scanner, "Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);
                    Teacher.importQuestions(subject, scanner);
                    pause(scanner, null);
                }
                case 4 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        pause(scanner, "No subjects found.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject to view results: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        pause(scanner, "Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);
                    Teacher.viewResultsBySubject(subject);
                    pause(scanner, null);
                }
                case 5 -> {
                    printMessage("Logging out...");
                    pause(scanner, null);
                    return;
                }
                default -> pause(scanner, "Invalid choice, try again.");
            }
        }
    }

    static void pause(Scanner scanner, String message) {
        if (message != null && !message.isEmpty()) {
            System.out.println("\n" + message);
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
