import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class Teacher {
    String username;
    String password;

    static ArrayList<Teacher> teachers = new ArrayList<>();

    // Static block to add fixed teachers
    static {
        teachers.add(new Teacher("sphrn", "12345"));
        teachers.add(new Teacher("teacher1", "pass123"));
        // Add more fixed teachers here if needed
    }

    public Teacher(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean checkLogin(String username, String password) {
        for (Teacher t : teachers) {
            if (t.username.equals(username) && t.password.equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void addQuestion(String subject, String questionText, String[] options, String answer) {
        String questionsFile = "data/questions_" + subject.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFile, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(questionText).append(";");
            for (String opt : options) {
                sb.append(opt).append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // remove last comma
            sb.append(";").append(answer);

            bw.write(sb.toString());
            bw.newLine();
            System.out.println("Question added successfully to subject \"" + subject + "\"!");
        } catch (IOException e) {
            System.out.println("Error saving question.");
        }
    }

    public static void addMCQQuestion(String subject, Scanner scanner) {
        System.out.print("How many MCQ questions do you want to add? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= count; i++) {
            System.out.println("Enter question " + i + ":");
            String questionText = scanner.nextLine();

            String[] options = new String[4];
            for (int j = 0; j < 4; j++) {
                System.out.print("Enter option " + (j + 1) + ": ");
                options[j] = scanner.nextLine();
            }

            System.out.print("Enter correct answer (exact option text): ");
            String answer = scanner.nextLine();

            addQuestion(subject, questionText, options, answer);
        }
    }

    public static void viewResultsBySubject(String subject) {
        String resultsFile = "data/results.txt";
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(resultsFile))) {
            System.out.println("\nResults for subject: " + subject);
            System.out.println("Student ID | Score | Total");

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // parts format: studentId,subject,score,total
                if (parts.length == 4 && parts[1].equalsIgnoreCase(subject)) {
                    System.out.println(parts[0] + " | " + parts[2] + " | " + parts[3]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No results found for this subject.");
            }
        } catch (IOException e) {
            System.out.println("Error reading results.");
        }
    }
}
