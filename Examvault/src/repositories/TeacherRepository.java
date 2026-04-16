package repositories;

import models.TeacherModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    private static final String TEACHERS_FILE = "data/teachers.txt";
    private List<TeacherModel> teachers = new ArrayList<>();

    public TeacherRepository() {
        loadTeachers();
    }

    private void loadTeachers() {
        teachers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    teachers.add(new TeacherModel(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading teachers: " + e.getMessage());
        }
    }

    public TeacherModel findByUsernameAndPassword(String username, String password) {
        username = username.trim();
        password = password.trim();
        for (TeacherModel t : teachers) {
            if (t.getUsername().equalsIgnoreCase(username) && t.getPassword().equals(password)) {
                return t;
            }
        }
        return null;
    }

    public TeacherModel findByUsername(String username) {
        username = username.trim();
        for (TeacherModel t : teachers) {
            if (t.getUsername().equalsIgnoreCase(username)) {
                return t;
            }
        }
        return null;
    }

    public boolean registerTeacher(TeacherModel teacher) {
        if (findByUsername(teacher.getUsername()) != null) {
            return false; // Teacher ID already exists
        }
        teachers.add(teacher);
        return saveTeacher(teacher);
    }

    private boolean saveTeacher(TeacherModel teacher) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEACHERS_FILE, true))) {
            bw.write(teacher.getName() + "," + teacher.getContactNumber() + "," +
                    teacher.getUsername() + "," + teacher.getPassword());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving teacher: " + e.getMessage());
            return false;
        }
    }

    public List<TeacherModel> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    public void reload() {
        loadTeachers();
    }
}
