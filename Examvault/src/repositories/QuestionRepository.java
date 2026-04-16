package repositories;

import models.QuestionModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private static final String QUESTIONS_DIR = "data/";

    public List<QuestionModel> loadQuestionsBySubject(String subject) {
        List<QuestionModel> questions = new ArrayList<>();
        String fileName = QUESTIONS_DIR + "questions_" + subject.toLowerCase() + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String questionText = parts[0];
                    String[] options = parts[1].split(",");
                    String answer = parts[2];
                    questions.add(new QuestionModel(questionText, options, answer));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading questions for subject " + subject + ": " + e.getMessage());
        }

        return questions;
    }

    public boolean addQuestion(String subject, String questionText, String[] options, String answer) {
        String questionsFile = QUESTIONS_DIR + "questions_" + subject.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(questionsFile, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(questionText).append(";");
            for (String opt : options) {
                sb.append(opt).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(";").append(answer);

            bw.write(sb.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving question: " + e.getMessage());
            return false;
        }
    }

    public List<String> getQuestionsBySubject(String subject) {
        return new ArrayList<>();
    }
}
