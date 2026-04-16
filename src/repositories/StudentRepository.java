package repositories;

import models.StudentModel;
import utils.OracleDatabase;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private static final String FILE = "data/students.txt";
    private static List<StudentModel> students = new ArrayList<>();

    public static void load() {
        students.clear();
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT s.STUDENT_ID, u.NAME, u.CONTACT, u.PASSWORD FROM STUDENTS s JOIN USERS u ON s.USER_ID = u.USER_ID")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        students.add(new StudentModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("NAME"),
                                rs.getString("CONTACT"),
                                rs.getString("PASSWORD")));
                    }
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Student DB load failed: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(new StudentModel(
                            parts[0].trim(), parts[1].trim(),
                            parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Note: No student file found, starting fresh.");
        }
    }

    public static StudentModel login(String id, String password) {
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT s.STUDENT_ID, u.NAME, u.CONTACT, u.PASSWORD FROM STUDENTS s JOIN USERS u ON s.USER_ID = u.USER_ID WHERE s.STUDENT_ID = ? AND u.PASSWORD = ?")) {
                ps.setString(1, id.trim());
                ps.setString(2, password.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new StudentModel(
                                rs.getString("STUDENT_ID"),
                                rs.getString("NAME"),
                                rs.getString("CONTACT"),
                                rs.getString("PASSWORD"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Student login DB query failed: " + e.getMessage());
            }
        }
        
        // Fallback to in-memory list
        for (StudentModel s : students) {
            if (s.getUsername().equals(id.trim()) && s.getPassword().equals(password.trim()))
                return s;
        }
        return null;
    }

    public static boolean idExists(String id) {
        for (StudentModel s : students) {
            if (s.getUsername().equals(id.trim())) return true;
        }
        return false;
    }

    public static boolean register(String name, String id, String contactNumber) {
        if (idExists(id)) return false;
        StudentModel s = new StudentModel(name, id, contactNumber, id);
        students.add(s);
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection()) {
                // Insert into USERS first
                String userId = "U" + id;  // Generate user_id from student_id
                try (PreparedStatement ps1 = conn.prepareStatement(
                        "INSERT INTO USERS (USER_ID, NAME, CONTACT, PASSWORD, ROLE) VALUES (?, ?, ?, ?, 'STUDENT')")) {
                    ps1.setString(1, userId);
                    ps1.setString(2, name);
                    ps1.setString(3, contactNumber);
                    ps1.setString(4, id);
                    ps1.executeUpdate();
                }
                // Then insert into STUDENTS
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO STUDENTS (STUDENT_ID, USER_ID) VALUES (?, ?)")) {
                    ps2.setString(1, id);
                    ps2.setString(2, userId);
                    ps2.executeUpdate();
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Student DB save failed: " + e.getMessage());
                // Fall through to local file fallback
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(name + "," + id + "," + contactNumber + "," + id);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving student.");
            return false;
        }
        return true;
    }

    public static List<StudentModel> getAll() { return students; }
}
