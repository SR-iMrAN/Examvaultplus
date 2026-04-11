package repositories;

import models.StudentModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private static final String FILE = "data/students.txt";
    private static List<StudentModel> students = new ArrayList<>();

    public static void load() {
        students.clear();
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
