import java.util.*;
import java.io.*;

public class SubjectManager {
    private static final String SUBJECT_DIR = "data/subject/";  // folder for subject files
    private static final String SUBJECT_LIST_FILE = SUBJECT_DIR + "subjects.txt"; // master list file
    static ArrayList<String> subjects = new ArrayList<>();

    // Load subjects from subjects.txt at start
    public static void loadSubjects() {
        subjects.clear();

        File dir = new File(SUBJECT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File listFile = new File(SUBJECT_LIST_FILE);
        if (!listFile.exists()) {
            try {
                listFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating subjects list file: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECT_LIST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    subjects.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading subjects: " + e.getMessage());
        }
    }

    // Add new subject
    public static void addSubject(String subject) {
        subject = subject.trim();

        // Avoid duplicates (case-insensitive)
        for (String s : subjects) {
            if (s.equalsIgnoreCase(subject)) {
                System.out.println("Subject already exists.");
                return;
            }
        }

        subjects.add(subject);

        // Save to subjects.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SUBJECT_LIST_FILE, true))) {
            bw.write(subject);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving subject to list: " + e.getMessage());
        }

        // Create subject file
        String safeFileName = subject.replaceAll("[^a-zA-Z0-9 _-]", "_").toLowerCase() + ".txt";
        File subjectFile = new File(SUBJECT_DIR + safeFileName);

        try {
            if (subjectFile.createNewFile()) {
                System.out.println("Subject \"" + subject + "\" added and file created: " + subjectFile.getPath());
            } else {
                System.out.println("Subject file already exists: " + subjectFile.getPath());
            }
        } catch (IOException e) {
            System.out.println("Error creating subject file: " + e.getMessage());
        }
    }

    // List subjects
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

    // Validate index
    public static boolean isValidSubject(int index) {
        return index >= 1 && index <= subjects.size();
    }

    // Get subject by index
    public static String getSubject(int index) {
        return subjects.get(index - 1);
    }
}
