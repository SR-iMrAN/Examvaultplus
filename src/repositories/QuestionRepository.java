package repositories;

import models.QuestionModel;
import utils.OracleDatabase;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionRepository {
    private static final String DATA_DIR = "data/";

    public static List<QuestionModel> loadForSubject(String subject) {
        List<QuestionModel> questions = new ArrayList<>();
        
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT q.QUESTION_ID, q.QUESTION_TEXT, q.CORRECT_OPTION_ID, " +
                     "LISTAGG(o.OPTION_TEXT, ',') WITHIN GROUP (ORDER BY o.OPTION_LABEL) as OPTION_LIST " +
                     "FROM QUESTIONS q LEFT JOIN OPTIONS o ON q.QUESTION_ID = o.QUESTION_ID " +
                     "WHERE q.SUBJECT_ID = (SELECT SUBJECT_ID FROM SUBJECTS WHERE SUBJECT_NAME = ?) " +
                     "GROUP BY q.QUESTION_ID, q.QUESTION_TEXT, q.CORRECT_OPTION_ID")) {
                ps.setString(1, subject);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String[] options = rs.getString("OPTION_LIST").split(",");
                        questions.add(new QuestionModel(
                                rs.getString("QUESTION_TEXT"),
                                options,
                                rs.getString("CORRECT_OPTION_ID")));
                    }
                    if (!questions.isEmpty()) return questions;
                }
            } catch (SQLException e) {
                System.out.println("Question DB load failed: " + e.getMessage());
            }
        }
        
        // Fallback to local file
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
