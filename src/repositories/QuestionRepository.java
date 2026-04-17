package repositories;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.QuestionModel;
import utils.OracleDatabase;

public class QuestionRepository {
    private static final String DATA_DIR = "data/";

    public static List<QuestionModel> loadForSubject(String subject) {
        List<QuestionModel> questions = new ArrayList<>();
        
        if (OracleDatabase.isAvailable()) {
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT q.QUESTION_ID, q.QUESTION_TEXT, q.CORRECT_OPTION_ID, " +
                     "LISTAGG(o.OPTION_TEXT, ',') WITHIN GROUP (ORDER BY o.OPTION_LABEL) as OPTION_LIST, " +
                     "co.OPTION_TEXT as CORRECT_OPTION_TEXT " +
                     "FROM QUESTIONS q " +
                     "LEFT JOIN OPTIONS o ON q.QUESTION_ID = o.QUESTION_ID " +
                     "LEFT JOIN OPTIONS co ON q.CORRECT_OPTION_ID = co.OPTION_ID " +
                     "WHERE q.SUBJECT_ID = (SELECT SUBJECT_ID FROM SUBJECTS WHERE SUBJECT_NAME = ?) " +
                     "GROUP BY q.QUESTION_ID, q.QUESTION_TEXT, q.CORRECT_OPTION_ID, co.OPTION_TEXT")) {
                ps.setString(1, subject);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String[] options = rs.getString("OPTION_LIST") != null ? 
                            rs.getString("OPTION_LIST").split(",") : new String[]{};
                        String correctOptionText = rs.getString("CORRECT_OPTION_TEXT");
                        questions.add(new QuestionModel(
                                rs.getString("QUESTION_TEXT"),
                                options,
                                correctOptionText != null ? correctOptionText.trim() : ""));
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
        if (OracleDatabase.isAvailable()) {
            String subjectId = null;
            try (Connection conn = OracleDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT SUBJECT_ID FROM SUBJECTS WHERE SUBJECT_NAME = ?")) {
                ps.setString(1, subject.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        subjectId = rs.getString("SUBJECT_ID");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Question DB get subject failed: " + e.getMessage());
            }
            if (subjectId != null) {
                String questionId = nextQuestionId();
                String correctLabel = null;
                Map<String, String> optionMap = new HashMap<>();
                for (int i = 0; i < options.length; i++) {
                    String label = String.valueOf((char)('A' + i));
                    optionMap.put(label, options[i]);
                    if (options[i].trim().equalsIgnoreCase(answer.trim())) {
                        correctLabel = label;
                    }
                }
                if (correctLabel == null) {
                    System.out.println("Question DB save failed: correct answer does not match any option");
                } else {
                    String correctOptionId = null;
                    Connection conn = null;
                    try {
                        conn = OracleDatabase.getConnection();
                        conn.setAutoCommit(false);

                        try (CallableStatement cs = conn.prepareCall("{call ADD_QUESTION(?, ?, ?)}")) {
                            cs.setString(1, questionId);
                            cs.setString(2, subjectId);
                            cs.setString(3, questionText);
                            cs.execute();
                        }

                        for (Map.Entry<String, String> entry : optionMap.entrySet()) {
                            String optionLabel = entry.getKey();
                            String optionText = entry.getValue();
                            String optionId = nextOptionId();
                            try (CallableStatement cs = conn.prepareCall("{call ADD_OPTION(?, ?, ?, ?)}")) {
                                cs.setString(1, optionId);
                                cs.setString(2, questionId);
                                cs.setString(3, optionLabel);
                                cs.setString(4, optionText);
                                cs.execute();
                            }
                            if (optionLabel.equals(correctLabel)) {
                                correctOptionId = optionId;
                            }
                        }

                        if (correctOptionId != null) {
                            try (CallableStatement cs = conn.prepareCall("{call SET_CORRECT_OPTION(?, ?)}")) {
                                cs.setString(1, questionId);
                                cs.setString(2, correctOptionId);
                                cs.execute();
                            }
                        }
                        conn.commit();
                        return true;
                    } catch (SQLException e) {
                        System.out.println("Question DB save failed: " + e.getMessage());
                        if (conn != null) {
                            try {
                                conn.rollback();
                            } catch (SQLException ignored) {}
                        }
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException ignored) {}
                        }
                    }
                }
            }
        }

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

    private static String nextQuestionId() {
        return "Q" + String.format("%09d", System.nanoTime() % 1000000000L);
    }

    private static String nextOptionId() {
        return "O" + String.format("%09d", System.nanoTime() % 1000000000L);
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
