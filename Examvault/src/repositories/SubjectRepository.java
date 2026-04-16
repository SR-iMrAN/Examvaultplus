package repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectRepository {
    private static final String SUBJECTS_FILE = "data/subject/subjects.txt";
    private Set<String> subjects = new HashSet<>();

    public SubjectRepository() {
        loadSubjects();
    }

    private void loadSubjects() {
        subjects.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                subjects.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading subjects: " + e.getMessage());
        }
    }

    public boolean addSubject(String subjectName) {
        if (subjects.contains(subjectName)) {
            return false; // Subject already exists
        }
        subjects.add(subjectName);
        return saveSubject(subjectName);
    }

    private boolean saveSubject(String subjectName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SUBJECTS_FILE, true))) {
            bw.write(subjectName);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving subject: " + e.getMessage());
            return false;
        }
    }

    public List<String> getAllSubjects() {
        return new ArrayList<>(subjects);
    }

    public boolean subjectExists(String subjectName) {
        return subjects.contains(subjectName);
    }

    public void reload() {
        loadSubjects();
    }
}
