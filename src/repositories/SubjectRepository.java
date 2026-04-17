package repositories;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.OracleDatabase;

public class SubjectRepository {
    private static final String SUBJECT_DIR = "data/subject/";
    private static final String SUBJECT_LIST_FILE = SUBJECT_DIR + "subjects.txt";
    private static List<String> subjects = new ArrayList<>();

    // ===== LOAD SUBJECTS =====
    public static void load() {
        subjects.clear();
        new File(SUBJECT_DIR).mkdirs();

        File listFile = new File(SUBJECT_LIST_FILE);
        if (!listFile.exists()) {
            try { listFile.createNewFile(); } catch (IOException ignored) {}
        }

        // Load from DB
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT SUBJECT_NAME FROM SUBJECTS ORDER BY SUBJECT_NAME")) {

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String subject = rs.getString("SUBJECT_NAME");
                        if (subject != null && !subject.trim().isEmpty()) {
                            subjects.add(subject.trim());
                        }
                    }
                    return;
                }

            } catch (SQLException e) {
                System.out.println("Subject DB load failed: " + e.getMessage());
            }
        }

        // Load from file fallback
        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECT_LIST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) subjects.add(line.trim());
            }
        } catch (IOException ignored) {}
    }

    // ===== ADD SUBJECT =====
    public static boolean addSubject(String subject, String teacherId) {
        subject = subject.trim();

        // Prevent duplicates
        for (String s : subjects) {
            if (s.equalsIgnoreCase(subject)) return false;
        }

        subjects.add(subject);

        // 🔥 Generate ID automatically
        String subjectId = generateNextSubjectId();

        boolean dbSuccess = false;

        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO SUBJECTS (SUBJECT_ID, SUBJECT_NAME, TEACHER_ID) VALUES (?, ?, ?)")) {

                ps.setString(1, subjectId);
                ps.setString(2, subject);
                ps.setString(3, teacherId);

                ps.executeUpdate();
                dbSuccess = true;

            } catch (SQLException e) {
                System.out.println("Subject DB save failed: " + e.getMessage());
            }
        }

        // Save to file if DB fails
        if (!dbSuccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(SUBJECT_LIST_FILE, true))) {
                bw.write(subject);
                bw.newLine();
            } catch (IOException e) {
                return false;
            }
        }

        // Create subject question file
        String safeFileName = subject.replaceAll("[^a-zA-Z0-9 _-]", "_").toLowerCase() + ".txt";
        File subjectFile = new File(SUBJECT_DIR + safeFileName);
        try { subjectFile.createNewFile(); } catch (IOException ignored) {}

        return true;
    }

    // ===== AUTO GENERATE SUBJECT ID =====
    private static String generateNextSubjectId() {
        int max = 0;

        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT SUBJECT_ID FROM SUBJECTS");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String id = rs.getString("SUBJECT_ID"); // e.g. SUB008
                    if (id != null && id.startsWith("SUB")) {
                        int num = Integer.parseInt(id.substring(3));
                        if (num > max) max = num;
                    }
                }

            } catch (Exception e) {
                System.out.println("ID generation error: " + e.getMessage());
            }
        }

        return String.format("SUB%03d", max + 1); // SUB001, SUB002...
    }

    public static List<String> getAll() {
        return subjects;
    }
}