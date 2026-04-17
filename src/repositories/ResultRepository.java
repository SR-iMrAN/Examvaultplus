package repositories;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.ResultModel;
import utils.OracleDatabase;

public class ResultRepository {
    private static final String FILE = "data/results.txt";

    public static void save(String studentId, String subject, int score, int total) {
        System.out.println("[DEBUG] Result save requested: studentId=" + studentId + ", subject=" + subject + ", score=" + score + "/" + total);
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection()) {
                // Get subject_id from subject name
                String getSubjectIdSql = "SELECT SUBJECT_ID FROM SUBJECTS WHERE SUBJECT_NAME = ?";
                String subjectId = null;
                try (PreparedStatement ps = conn.prepareStatement(getSubjectIdSql)) {
                    ps.setString(1, subject.trim());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            subjectId = rs.getString("SUBJECT_ID");
                            if (subjectId != null) {
                                subjectId = subjectId.trim();
                            }
                            System.out.println("[DEBUG] Found subjectId: " + subjectId + " for subject: " + subject);
                        } else {
                            System.out.println("[DEBUG] No subject found with name: '" + subject + "'");
                        }
                    }
                }
                
                if (subjectId == null) {
                    System.out.println("[ERROR] Subject not found in DB: " + subject);
                    System.out.println("[DEBUG] Saving to local file instead.");
                    throw new SQLException("Subject not found");
                }
                
                // Generate RESULT_ID and QUIZ_ID (simplified)
                String resultId = "R" + String.format("%09d", System.nanoTime() % 1000000000L);
                String quizId = "QZ" + String.format("%08d", System.nanoTime() % 100000000L);
                
                // Insert into QUIZZES first
                String insertQuizSql = "INSERT INTO QUIZZES (QUIZ_ID, STUDENT_ID, SUBJECT_ID, SCORE, TOTAL_MARKS, QUIZ_DATE) " +
                                       "VALUES (?, ?, ?, ?, ?, SYSDATE)";
                try (PreparedStatement psQuiz = conn.prepareStatement(insertQuizSql)) {
                    psQuiz.setString(1, quizId);
                    psQuiz.setString(2, studentId);
                    psQuiz.setString(3, subjectId);
                    psQuiz.setInt(4, score);
                    psQuiz.setInt(5, total);
                    psQuiz.executeUpdate();
                    System.out.println("[DEBUG] Quiz inserted: " + quizId);
                }
                
                // Insert into RESULTS
                String insertSql = "INSERT INTO RESULTS (RESULT_ID, STUDENT_ID, SUBJECT_ID, QUIZ_ID, SCORE, TOTAL_MARKS, RESULT_DATE) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, resultId);
                    ps.setString(2, studentId);
                    ps.setString(3, subjectId);
                    ps.setString(4, quizId);
                    ps.setInt(5, score);
                    ps.setInt(6, total);
                    int rows = ps.executeUpdate();
                    System.out.println("[SUCCESS] Result saved to DB: " + rows + " rows affected. Result ID: " + resultId + ", Quiz ID: " + quizId);
                    return;
                }
            } catch (SQLException e) {
                System.out.println("[ERROR] Result DB save failed: " + e.getMessage());
                e.printStackTrace();
                System.out.println("[DEBUG] Falling back to local file.");
            }
        } else {
            System.out.println("[ERROR] Oracle Database not available. Saving to local file.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(studentId + "," + subject + "," + score + "," + total);
            bw.newLine();
            System.out.println("[LOCAL] Result saved to file: " + FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save quiz result to history file: " + e.getMessage());
        }
    }

    public static List<ResultModel> getByStudent(String studentId) {
        List<ResultModel> results = new ArrayList<>();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT r.STUDENT_ID, s.SUBJECT_NAME, r.SCORE, r.TOTAL_MARKS FROM RESULTS r " +
                     "JOIN SUBJECTS s ON r.SUBJECT_ID = s.SUBJECT_ID " +
                     "WHERE r.STUDENT_ID = ? ORDER BY s.SUBJECT_NAME")) {
                ps.setString(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT_NAME"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL_MARKS")));
                    }
                }
                if (!results.isEmpty()) {
                    return results;
                }
            } catch (SQLException e) {
                System.out.println("Result DB query failed: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4 && p[0].equals(studentId)) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }

    public static List<ResultModel> getBySubject(String subject) {
        List<ResultModel> results = new ArrayList<>();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT r.STUDENT_ID, s.SUBJECT_NAME, r.SCORE, r.TOTAL_MARKS FROM RESULTS r " +
                     "JOIN SUBJECTS s ON r.SUBJECT_ID = s.SUBJECT_ID " +
                     "WHERE s.SUBJECT_NAME = ? ORDER BY r.STUDENT_ID")) {
                ps.setString(1, subject);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT_NAME"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL_MARKS")));
                    }
                }
                if (!results.isEmpty()) {
                    return results;
                }
            } catch (SQLException e) {
                System.out.println("Result DB query failed: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4 && p[1].equalsIgnoreCase(subject)) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }

    public static List<ResultModel> getAll() {
        List<ResultModel> results = new ArrayList<>();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT r.STUDENT_ID, s.SUBJECT_NAME, r.SCORE, r.TOTAL_MARKS FROM RESULTS r " +
                     "JOIN SUBJECTS s ON r.SUBJECT_ID = s.SUBJECT_ID " +
                     "ORDER BY r.STUDENT_ID, s.SUBJECT_NAME")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT_NAME"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL_MARKS")));
                    }
                }
                if (!results.isEmpty()) {
                    return results;
                }
            } catch (SQLException e) {
                System.out.println("Result DB query failed: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }

    // ===== GET RESULTS BY TEACHER =====
    public static List<ResultModel> getByTeacher(String teacherId) {
        List<ResultModel> results = new ArrayList<>();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT r.STUDENT_ID, s.SUBJECT_NAME, r.SCORE, r.TOTAL_MARKS FROM RESULTS r " +
                     "JOIN SUBJECTS s ON r.SUBJECT_ID = s.SUBJECT_ID " +
                     "WHERE s.TEACHER_ID = ? " +
                     "ORDER BY r.STUDENT_ID, s.SUBJECT_NAME")) {
                ps.setString(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT_NAME"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL_MARKS")));
                    }
                }
                if (!results.isEmpty()) {
                    return results;
                }
            } catch (SQLException e) {
                System.out.println("Teacher results DB query failed: " + e.getMessage());
            }
        }
        return results;
    }
}
