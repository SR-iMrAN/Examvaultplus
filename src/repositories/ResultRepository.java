package repositories;

import models.ResultModel;
import utils.OracleDatabase;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository {
    private static final String FILE = "data/results.txt";

    public static void save(String studentId, String subject, int score, int total) {
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO RESULTS (STUDENT_ID, SUBJECT, SCORE, TOTAL) VALUES (?, ?, ?, ?)") ) {
                ps.setString(1, studentId);
                ps.setString(2, subject);
                ps.setInt(3, score);
                ps.setInt(4, total);
                ps.executeUpdate();
                return;
            } catch (SQLException e) {
                System.out.println("Result DB save failed: " + e.getMessage());
                // Fall through to local file fallback
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(studentId + "," + subject + "," + score + "," + total);
            bw.newLine();
        } catch (IOException ignored) {}
    }

    public static List<ResultModel> getByStudent(String studentId) {
        List<ResultModel> results = new ArrayList<>();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT STUDENT_ID, SUBJECT, SCORE, TOTAL FROM RESULTS WHERE STUDENT_ID = ? ORDER BY SUBJECT")) {
                ps.setString(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL")));
                    }
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
                 PreparedStatement ps = conn.prepareStatement("SELECT STUDENT_ID, SUBJECT, SCORE, TOTAL FROM RESULTS WHERE SUBJECT = ? ORDER BY STUDENT_ID")) {
                ps.setString(1, subject);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL")));
                    }
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
                 PreparedStatement ps = conn.prepareStatement("SELECT STUDENT_ID, SUBJECT, SCORE, TOTAL FROM RESULTS ORDER BY STUDENT_ID, SUBJECT")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new ResultModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("SUBJECT"),
                                rs.getInt("SCORE"),
                                rs.getInt("TOTAL")));
                    }
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
}
