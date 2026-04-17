package repositories;

import models.TeacherModel;
import utils.OracleDatabase;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    private static final String FILE = "data/teachers.txt";
    private static List<TeacherModel> teachers = new ArrayList<>();

    public static void load() {
        teachers.clear();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT t.TEACHER_ID, u.CONTACT as USERNAME, u.PASSWORD, u.NAME FROM TEACHERS t JOIN USERS u ON t.USER_ID = u.USER_ID")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        teachers.add(new TeacherModel(
                                rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                    rs.getString("TEACHER_ID")));
                    }
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Teacher DB load failed: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                 String teacherId = "T" + parts[2].replaceAll("[^0-9]", "");

teachers.add(new TeacherModel(
    parts[2].trim(),
    parts[3].trim(),
    parts[0].trim(),
    teacherId
));
                }
            }
        } catch (IOException e) {
            System.out.println("Note: No teacher file found, starting fresh.");
        }
    }

    public static TeacherModel login(String username, String password) {
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT t.TEACHER_ID, u.CONTACT as USERNAME, u.PASSWORD, u.NAME FROM TEACHERS t JOIN USERS u ON t.USER_ID = u.USER_ID WHERE u.CONTACT = ? AND u.PASSWORD = ?")) {
                ps.setString(1, username.trim());
                ps.setString(2, password.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new TeacherModel(
                                rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NAME"),
                                 rs.getString("TEACHER_ID"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Teacher login DB query failed: " + e.getMessage());
            }
        }
        
        // Fallback to in-memory list
        for (TeacherModel t : teachers) {
            if (t.getUsername().equalsIgnoreCase(username.trim()) && t.getPassword().equals(password.trim()))
                return t;
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        for (TeacherModel t : teachers) {
            if (t.getUsername().equalsIgnoreCase(username.trim())) return true;
        }
        return false;
    }

    public static boolean register(String name, String id, String contact, String password) {
        if (usernameExists(contact)) return false;
       String teacherId = "T" + contact.replaceAll("[^0-9]", "");

TeacherModel t = new TeacherModel(contact, password, name, teacherId);
        teachers.add(t);
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection()) {
                // Insert into USERS first
                String userId = "U" + contact.replaceAll("[^0-9]", "");  // Generate user_id from contact
                try (PreparedStatement ps1 = conn.prepareStatement(
                        "INSERT INTO USERS (USER_ID, NAME, CONTACT, PASSWORD, ROLE) VALUES (?, ?, ?, ?, 'TEACHER')")) {
                    ps1.setString(1, userId);
                    ps1.setString(2, name);
                    ps1.setString(3, contact);
                    ps1.setString(4, password);
                    ps1.executeUpdate();
                }
                // Then insert into TEACHERS
              
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO TEACHERS (TEACHER_ID, USER_ID) VALUES (?, ?)")) {
                    ps2.setString(1, teacherId);
                    ps2.setString(2, userId);
                    ps2.executeUpdate();
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Teacher DB save failed: " + e.getMessage());
                // Fall through to local file fallback
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(name + "," + id + "," + contact + "," + password);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving teacher.");
            return false;
        }
        return true;
    }

    public static List<TeacherModel> getAll() { return teachers; }
}
