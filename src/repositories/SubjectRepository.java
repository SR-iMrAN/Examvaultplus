package repositories;

import utils.OracleDatabase;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT SUBJECT_NAME FROM SUBJECTS ORDER BY SUBJECT_NAME")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String subject = rs.getString("SUBJECT_NAME");
                        if (subject != null && !subject.trim().isEmpty()) subjects.add(subject.trim());
                    }
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Subject DB load failed: " + e.getMessage());
            }
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

        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO SUBJECTS (SUBJECT_NAME) VALUES (?)")) {
                ps.setString(1, subject);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Subject DB save failed: " + e.getMessage());
                // Fall back to local file storage below.
            }
        }

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
