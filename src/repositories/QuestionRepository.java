package repositories;

import models.QuestionModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private static final String DATA_DIR = "data/";

    public static List<QuestionModel> loadForSubject(String subject) {
        List<QuestionModel> questions = new ArrayList<>();
        String fileName = DATA_DIR + "questions_" + subject.toLowerCase() + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String[] options = parts[1].split(",");
                    questions.add(new QuestionModel(parts[0], options, parts[2]));
                }
            }
        } catch (IOException ignored) {}
        return questions;
    }

    public static boolean addQuestion(String subject, String questionText, String[] options, String answer) {
        String fileName = DATA_DIR + "questions_" + subject.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            StringBuilder sb = new StringBuilder(questionText).append(";");
            for (int i = 0; i < options.length; i++) {
                sb.append(options[i]);
                if (i < options.length - 1) sb.append(",");
            }
            sb.append(";").append(answer);
            bw.write(sb.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean importFromFile(String subject, String filePath) {
        String destFile = DATA_DIR + "questions_" + subject.toLowerCase() + ".txt";
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(destFile, true))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[1].contains(",") && !parts[2].trim().isEmpty()) {
                    bw.write(line); bw.newLine(); count++;
                }
            }
        } catch (IOException e) { return false; }
        return count > 0;
    }
}
