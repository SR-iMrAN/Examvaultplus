package repositories;

import models.StudentModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private static final String STUDENTS_FILE = "data/students.txt";
    private List<StudentModel> students = new ArrayList<>();

    public StudentRepository() {
        loadStudents();
    }

    private void loadStudents() {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(new StudentModel(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public StudentModel findByUsernameAndPassword(String username, String password) {
        username = username.trim();
        password = password.trim();
        for (StudentModel s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                return s;
            }
        }
        return null;
    }

    public StudentModel findByUsername(String username) {
        username = username.trim();
        for (StudentModel s : students) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }
        return null;
    }

    public boolean registerStudent(StudentModel student) {
        if (findByUsername(student.getUsername()) != null) {
            return false; // Student ID already exists
        }
        students.add(student);
        return saveStudent(student);
    }

    private boolean saveStudent(StudentModel student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENTS_FILE, true))) {
            bw.write(student.getName() + "," + student.getUsername() + "," +
                    student.getContactNumber() + "," + student.getPassword());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving student: " + e.getMessage());
            return false;
        }
    }

    public List<StudentModel> getAllStudents() {
        return new ArrayList<>(students);
    }

    public void reload() {
        loadStudents();
    }
}
