package repositories;

import models.TeacherModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    private static final String FILE = "data/teachers.txt";
    private static List<TeacherModel> teachers = new ArrayList<>();

    public static void load() {
        teachers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    teachers.add(new TeacherModel(
                            parts[2].trim(), parts[3].trim(), parts[0].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Note: No teacher file found, starting fresh.");
        }
    }

    public static TeacherModel login(String username, String password) {
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
        TeacherModel t = new TeacherModel(contact, password, name);
        teachers.add(t);
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
