import java.util.Scanner;

public class ExamVaultPlus {
    public static void main(String[] args) {
        Student.loadStudents();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== ExamVaultPlus ===");
            System.out.println("1. Student Registration");
            System.out.println("2. Student Login");
            System.out.println("3. Teacher Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter your Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter your ID (e.g., 241-15-423 or 24115423): ");
                    String id = scanner.nextLine();

                    System.out.print("Enter your Contact Number: ");
                    String contact = scanner.nextLine();

                    boolean success = Student.addStudent(name, id, contact);
                    if (success) System.out.println("Registration successful! Your ID is also your password.");
                    else System.out.println("ID already registered.");
                }
                case 2 -> {
                    System.out.print("Enter your ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter your Password (your ID): ");
                    String password = scanner.nextLine();

                    if (Student.checkLogin(id, password)) {
                        System.out.println("Student login successful! Welcome " + id);
                        studentMenu(scanner, id);
                    } else {
                        System.out.println("Invalid student credentials.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Teacher Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Teacher Password: ");
                    String password = scanner.nextLine();

                    if (Teacher.checkLogin(username, password)) {
                        System.out.println("Teacher login successful! Welcome " + username);
                        teacherMenu(scanner, username);
                    } else {
                        System.out.println("Invalid teacher credentials.");
                    }
                }
                case 4 -> {
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    static void studentMenu(Scanner scanner, String studentId) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Take Quiz");
            System.out.println("2. View Results");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                if (SubjectManager.subjects.isEmpty()) {
                    System.out.println("No subjects available. Please contact your teacher.");
                    continue;
                }
                SubjectManager.listSubjects();
                System.out.print("Select subject by number: ");
                int subChoice = scanner.nextInt();
                scanner.nextLine();

                if (!SubjectManager.isValidSubject(subChoice)) {
                    System.out.println("Invalid subject choice.");
                    continue;
                }

                String subject = SubjectManager.getSubject(subChoice);

                Quiz quiz = new Quiz();
                quiz.loadQuestions(subject);
                quiz.startQuiz(scanner, studentId, subject);
            } else if (choice == 2) {
                Student.viewResults(studentId);
            } else if (choice == 3) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
    }

    static void teacherMenu(Scanner scanner, String username) {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. Add Subject");
            System.out.println("2. Add Questions (Manual)");
            System.out.println("3. Import Questions (coming soon)");
            System.out.println("4. View Student Results");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new subject name: ");
                    String subject = scanner.nextLine();
                    SubjectManager.addSubject(subject);
                }
                case 2 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        System.out.println("No subjects found. Add a subject first.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject by number: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        System.out.println("Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);
                    System.out.println("Selected subject: " + subject);

                    while (true) {
                        System.out.println("\nChoose question type:");
                        System.out.println("1. MCQ");
                        System.out.println("2. CQ (Coming soon)");
                        System.out.println("3. MCQ + CQ (Coming soon)");
                        System.out.println("4. Return to Teacher Menu");
                        System.out.print("Enter choice: ");

                        int qChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (qChoice == 4) break;

                        switch (qChoice) {
                            case 1 -> Teacher.addMCQQuestion(subject, scanner);
                            case 2 -> System.out.println("CQ feature coming soon.");
                            case 3 -> System.out.println("MCQ + CQ feature coming soon.");
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                }
                case 3 -> System.out.println("Import questions feature coming soon.");
                case 4 -> {
                    if (SubjectManager.subjects.isEmpty()) {
                        System.out.println("No subjects found.");
                        continue;
                    }
                    SubjectManager.listSubjects();
                    System.out.print("Select subject to view results: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (!SubjectManager.isValidSubject(subChoice)) {
                        System.out.println("Invalid subject choice.");
                        continue;
                    }

                    String subject = SubjectManager.getSubject(subChoice);
                    Teacher.viewResultsBySubject(subject);
                }
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }
}
