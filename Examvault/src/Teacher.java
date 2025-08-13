import java.io.*;
import java.util.*;

public class Teacher extends User {
    private String namet;
    static ArrayList<Teacher> teachers = new ArrayList<>();

    // Constructor that sets both username/password and teacher's name
    public Teacher(String username, String password, String namet) {
        super(username, password);
        this.namet = namet;
    }

    // Overloaded constructor without name
    public Teacher(String username, String password) {
        super(username, password);
    }

    public String getNameT() {
        return namet;
    }

    public static void loadTeachers() {
        teachers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("data/teachers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    String contact = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();
                    teachers.add(new Teacher(username, password, name));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading teachers.");
        }
    }


    public static Teacher checkLogin(String username, String password) {
        username = username.trim();
        password = password.trim();

        for (Teacher t : teachers) {
            if (t.getUsername().equalsIgnoreCase(username) && t.getPassword().equals(password)) {
                return t;
            }
        }
        return null;
    }


    public static void addQuestion(String subject, String questionText, String[] options, String answer) {
        String questionsFile = "data/questions_" + subject.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFile, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(questionText).append(";");
            for (String opt : options) {
                sb.append(opt).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
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

    public static void importQuestions(String subject, Scanner scanner) {
        System.out.print("Enter full path to the .txt file to import: ");
        String filePath = scanner.nextLine();

        File importFile = new File(filePath);
        if (!importFile.exists() || !importFile.isFile()) {
            System.out.println("File does not exist or is not a valid file.");
            return;
        }

        String questionsFile = "data/questions_" + subject.toLowerCase() + ".txt";
        int importedCount = 0;
        try (
                BufferedReader br = new BufferedReader(new FileReader(importFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFile, true))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[1].contains(",") && !parts[2].trim().isEmpty()) {
                    bw.write(line);
                    bw.newLine();
                    importedCount++;
                } else {
                    System.out.println("Skipped invalid format line: " + line);
                }
            }
            System.out.println("Import finished! " + importedCount + " questions added to subject \"" + subject + "\".");
        } catch (IOException e) {
            System.out.println("Error during import: " + e.getMessage());
        }
    }
}
