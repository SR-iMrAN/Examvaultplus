import java.util.ArrayList;
import java.io.*;

public class SubjectManager {
    private static final String SUBJECT_DIR = "data/subject/"; // folder for subjects
    static ArrayList<String> subjects = new ArrayList<>();

    public static void addSubject(String subject) {
        subject = subject.trim();

        // Add to list if not already ache
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            System.out.println("Subject \"" + subject + "\" added.");
        } else {
            System.out.println("Subject already exists.");
            return;
        }


        File dir = new File(SUBJECT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create the subject file
        String subjectsFile = SUBJECT_DIR + subject.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(subjectsFile, true))) {
            bw.write("");
            System.out.println("File created for subject \"" + subject + "\" at: " + subjectsFile);
        } catch (IOException e) {
            System.out.println("Error creating subject file: " + e.getMessage());
        }
    }

    public static void listSubjects() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects added yet.");
        } else {
            System.out.println("Available subjects:");
            for (int i = 0; i < subjects.size(); i++) {
                System.out.println((i + 1) + ". " + subjects.get(i));
            }
        }
    }

    public static boolean isValidSubject(int index) {
        return index >= 1 && index <= subjects.size();
    }

    public static String getSubject(int index) {
        return subjects.get(index - 1);
    }
}
