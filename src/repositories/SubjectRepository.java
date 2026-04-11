package repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {
    private static final String SUBJECT_DIR = "data/subject/";
    private static final String SUBJECT_LIST_FILE = SUBJECT_DIR + "subjects.txt";
    private static List<String> subjects = new ArrayList<>();

    public static void load() {
        subjects.clear();
        new File(SUBJECT_DIR).mkdirs();
        File listFile = new File(SUBJECT_LIST_FILE);
        if (!listFile.exists()) {
            try { listFile.createNewFile(); } catch (IOException ignored) {}
        }
        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECT_LIST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) subjects.add(line.trim());
            }
        } catch (IOException ignored) {}
    }

    public static boolean addSubject(String subject) {
        subject = subject.trim();
        for (String s : subjects) {
            if (s.equalsIgnoreCase(subject)) return false;
        }
        subjects.add(subject);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SUBJECT_LIST_FILE, true))) {
            bw.write(subject); bw.newLine();
        } catch (IOException e) { return false; }
        // Create subject question file
        String safeFileName = subject.replaceAll("[^a-zA-Z0-9 _-]", "_").toLowerCase() + ".txt";
        File subjectFile = new File(SUBJECT_DIR + safeFileName);
        try { subjectFile.createNewFile(); } catch (IOException ignored) {}
        return true;
    }

    public static List<String> getAll() { return subjects; }
}
