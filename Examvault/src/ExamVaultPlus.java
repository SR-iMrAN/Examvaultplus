import java.io.Console;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.*;
public class ExamVaultPlus {

    // ANSI colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_PURPLE_BRIGHT = "\u001B[95m";
    public static final String ANSI_GREEN_BRIGHT = "\u001B[92m";

    public static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMessage(String message) {
        System.out.println("\n" + ANSI_BOLD + ANSI_RED + message + ANSI_RESET + "\n");
    }

    public static String readPassword(String prompt, Scanner scanner) {
        Console console = System.console();
        if (console != null) {
            char[] pwdArray = console.readPassword(prompt);
            return new String(pwdArray);
        } else {
            System.out.print(prompt);
            return scanner.nextLine();
        }
    }

    static void pause(Scanner scanner, String message) {
        if (message != null && !message.isEmpty()) {
            System.out.println("\n" + ANSI_YELLOW + message + ANSI_RESET);
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        Student.loadStudents();
        Teacher.loadTeachers();
        SubjectManager.loadSubjects();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearScreen();
            clrscr();
            System.out.println(ANSI_BLUE + "                                                        " + ANSI_BOLD + "======================================================================" + ANSI_RESET);
            System.out.println(ANSI_GREEN_BRIGHT + ANSI_BOLD + "                                                                               ★★★ ExamVaultPlus ★★★" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "                                                         ----------------------------------------------------------------------" + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                       1. Registration");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                       2. Login");
            System.out.println(ANSI_RED + "                                                                                       3. Exit");
            System.out.println(ANSI_BLUE + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> registrationMenu(scanner);
                    case 2 -> loginMenu(scanner);
                    case 3 -> {
                         System.out.println(ANSI_GREEN_BRIGHT+"Thanks For Using"+ANSI_RESET+ANSI_BRIGHT_YELLOW +" ExamVault+"+ANSI_RESET);
                        printMessage("Exiting program. Goodbye!");
                        return;
                    }
                    default -> pause(scanner, "Invalid choice, try again.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input! Please enter number(1-3).");
            }
        }
    }

    // ----------------- REGISTRATION MENU -----------------
    static void registrationMenu(Scanner scanner) {
        while (true) {
            clrscr();
            clearScreen();
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        " + ANSI_BOLD + "============================== Registration ==============================" + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      1. As Teacher");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      2. As Student");
            System.out.println(ANSI_RED + "                                                                                      3. Return");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    Registration reg = new TeacherRegistration();
                    reg.register();
                    reg.save();
                    pause(scanner, null);
                }
                case 2 -> {
                    Registration reg1 = new StudentRegistration();
                    reg1.register();
                    reg1.save();
                    pause(scanner, null);
                }
                case 3 -> { return; }
                default -> pause(scanner, "Invalid choice.Please Enter number(1-3).");
            }
        }
    }

   // ----------------- LOGIN MENU -----------------
    static void loginMenu(Scanner scanner) {
        while (true) {
            clearScreen();
            clrscr();
            System.out.println(ANSI_BRIGHT_CYAN + "                                                        " + ANSI_BOLD + "============================== Login ==============================" + ANSI_RESET);
            System.out.println(ANSI_GREEN_BRIGHT + "                                                                                      1. Login as Teacher");
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      2. Login as Student");
            System.out.println(ANSI_RED + "                                                                                      3. Return");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");
try {
    int choice = scanner.nextInt();
    scanner.nextLine();


    switch (choice) {
        case 1 -> teacherLogin(scanner);
        case 2 -> studentLogin(scanner);
        case 3 -> {
            return;
        }
        default -> pause(scanner, "Invalid choice.");
    }
}
catch(InputMismatchException e)
{
    pause(scanner, "Invalid input. Please enter a number(1-3).");
    continue;
}



        }
    }

    // ----------------- STUDENT MENU -----------------
    static void studentMenu(Scanner scanner, String studentId ,String studentName) {
        while (true) {
            clearScreen();
            clrscr();
            printMessage(" " + ANSI_RED + "\u2764 " + ANSI_RESET + " Welcome to ExamVault+, " + ANSI_GREEN_BRIGHT + studentName + ANSI_RESET + "!");
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                        " + ANSI_BOLD + "============================== Student Menu ==============================" + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      1. Take Quiz");
            System.out.println(ANSI_GREEN_BRIGHT + "                                                                                      2. View Results");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                                                      3. CGPA Calculator");
            System.out.println(ANSI_BRIGHT_WHITE + "                                                                                      4. Calculator");
            System.out.println(ANSI_RED + "                                                                                      5. Logout");
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> takeQuiz(scanner, studentId);
                    case 2 -> {
                        Student.viewResults(studentId);
                        pause(scanner, null);
                    }
                    case 3 -> {
                        CGPACalculator.runGPA(scanner, studentName);
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
            } catch (InputMismatchException e) {
                {
                    scanner.nextLine();
                    pause(scanner,"Invalid Input.Please Enter number(1-5).");
                }
            }
        }
    }

    // ----------------- TEACHER MENU -----------------
    static void teacherMenu(Scanner scanner, String username , String teachername) {
        while (true) {
            clearScreen();
            clrscr();
            printMessage(" " + ANSI_RED + "\u2764 " + ANSI_RESET + " Welcome to ExamVault+, " + ANSI_GREEN_BRIGHT + teachername + ANSI_RESET + "!");
            System.out.println(ANSI_GREEN_BRIGHT + "                                                        " + ANSI_BOLD + "============================== Teacher Menu ==============================" + ANSI_RESET);
            System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      1. Add Subject");
            System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      2. Add Questions (Manual)");
            System.out.println(ANSI_PURPLE_BRIGHT + "                                                                                      3. Import Questions");
            System.out.println(ANSI_BRIGHT_WHITE + "                                                                                      4. View Student Results");
            System.out.println(ANSI_RED + "                                                                                      5. Logout");
            System.out.println(ANSI_GREEN_BRIGHT + "                                                        ======================================================================" + ANSI_RESET);
            System.out.print("\nEnter choice: ");

            try{
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new subject name: ");
                    String subject = scanner.nextLine();
                    SubjectManager.addSubject(subject);
                    pause(scanner, null);
                }
                case 2 -> addQuestions(scanner);
                case 3 -> importQuestions(scanner);
                case 4 -> viewResultsBySubject(scanner);
                case 5 -> {
                    printMessage("Logging out...");
                    pause(scanner, null);
                    return;
                }
            }
        }
            catch (InputMismatchException e) {
                {
                    scanner.nextLine();
                    pause(scanner,"Invalid Input.Please Enter number(1-5).");
                }
                }

        }
    }

    // ----------------- ADD QUESTIONS MENU -----------------
    static void addQuestions(Scanner scanner) {
        if (SubjectManager.subjects.isEmpty()) {
            pause(scanner, "No Subjects found. Add a subject first.");
            return;
        }
        SubjectManager.listSubjects();
        System.out.print("Select subject by number: ");

        try {
            int subChoice = scanner.nextInt();
            scanner.nextLine();

            if (!SubjectManager.isValidSubject(subChoice)) {
                pause(scanner, "Invalid Subject choice.");
                return;
            }

            String subject = SubjectManager.getSubject(subChoice);

            while (true) {
                clearScreen();
                clrscr();
                System.out.println(ANSI_PURPLE_BRIGHT + "                                                        " + ANSI_BOLD + "============================== Choose Question Type ==============================" + ANSI_RESET);
                System.out.println(ANSI_BRIGHT_YELLOW + "                                                                                      1. MCQ");
                System.out.println(ANSI_BRIGHT_CYAN + "                                                                                      2. CQ (Coming soon)");
                System.out.println(ANSI_GREEN_BRIGHT + "                                                                                      3. MCQ + CQ (Coming soon)");
                System.out.println(ANSI_RED + "                                                                                      4. Return");
                System.out.println(ANSI_PURPLE_BRIGHT + "                                                        ======================================================================" + ANSI_RESET);
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
        }catch (InputMismatchException e) {
            scanner.nextLine();
            pause(scanner, "Invalid Input. Please Enter number .");
        }
    }

    static void importQuestions(Scanner scanner) {
        if (SubjectManager.subjects.isEmpty()) {
            pause(scanner, "No subjects found. Add a subject first.");
            return;
        }
        SubjectManager.listSubjects();
        System.out.print("Select subject by number to import questions: ");

        try {
            int subChoice = scanner.nextInt();
            scanner.nextLine();

            if (!SubjectManager.isValidSubject(subChoice)) {
                pause(scanner, "Invalid subject choice.");
                return;
            }

            String subject = SubjectManager.getSubject(subChoice);
            Teacher.importQuestions(subject, scanner);
            pause(scanner, null);
        }
        catch (InputMismatchException e)
        {
            scanner.nextLine();
            pause(scanner,"Invalid Input. Please enter a number . ");
        }
    }
    
    static void viewResultsBySubject(Scanner scanner) {
        if (SubjectManager.subjects.isEmpty()) {
            pause(scanner, "No subjects found.");
            return;
        }
        SubjectManager.listSubjects();
        System.out.print("Select subject to view results: ");
        int subChoice = scanner.nextInt();
        scanner.nextLine();

        if (!SubjectManager.isValidSubject(subChoice)) {
            pause(scanner, "Invalid subject choice.");
            return;
        }

        String subject = SubjectManager.getSubject(subChoice);
        Teacher.viewResultsBySubject(subject);
        pause(scanner, null);
    }

    // ----------------- LOGIN HELPERS -----------------
    static void teacherLogin(Scanner scanner) {
        System.out.print("Enter Teacher Username: ");
        String username = scanner.nextLine();
        String password = readPassword("Enter Teacher Password: ", scanner);

        Teacher teacher = Teacher.checkLogin(username, password);
        if (teacher != null) {
            printMessage("Login successful! Welcome, " + teacher.getNameT() + "!");
            teacherMenu(scanner, username,teacher.getNameT());
        } else {
            pause(scanner, "Invalid teacher credentials.");
        }
    }

    static void studentLogin(Scanner scanner) {
        System.out.print("Enter your ID: ");
        String id = scanner.nextLine();
        String password = readPassword("Enter your Password: ", scanner);

        Student student = Student.checkLogin(id, password);
        if (student != null) {
            printMessage("Login successful! Welcome, " + student.name + "!");
            studentMenu(scanner, id,student.name);
        } else {
            pause(scanner, "Invalid student credentials.");
        }
    }

    static void takeQuiz(Scanner scanner, String studentId) {
        if (SubjectManager.subjects.isEmpty()) {
            pause(scanner, "No subjects available. Please contact your teacher.");
            return;
        }
        SubjectManager.listSubjects();
        System.out.print("Select subject by number: ");
        int subChoice = scanner.nextInt();
        scanner.nextLine();

        if (!SubjectManager.isValidSubject(subChoice)) {
            pause(scanner, "Invalid subject choice.");
            return;
        }

        String subject = SubjectManager.getSubject(subChoice);

        Quiz quiz = new Quiz();
        quiz.loadQuestions(subject);
        quiz.startQuiz(scanner, studentId, subject);
        pause(scanner, null);
    }
}
