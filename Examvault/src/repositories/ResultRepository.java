package repositories;

import models.QuizResultModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository {
    private static final String RESULTS_FILE = "data/results.txt";

    public boolean saveResult(String studentId, String subject, int score, int totalQuestions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESULTS_FILE, true))) {
            bw.write(studentId + "," + subject + "," + score + "," + totalQuestions);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving result: " + e.getMessage());
            return false;
        }
    }

    public List<QuizResultModel> getResultsByStudentId(String studentId) {
        List<QuizResultModel> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].trim().equals(studentId)) {
                    results.add(new QuizResultModel(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            Integer.parseInt(parts[3].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading results: " + e.getMessage());
        }
        return results;
    }

    public List<QuizResultModel> getResultsBySubject(String subject) {
        List<QuizResultModel> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[1].trim().equals(subject)) {
                    results.add(new QuizResultModel(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            Integer.parseInt(parts[3].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading results: " + e.getMessage());
        }
        return results;
    }
}
