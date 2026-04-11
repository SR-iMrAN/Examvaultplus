package repositories;

import models.ResultModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository {
    private static final String FILE = "data/results.txt";

    public static void save(String studentId, String subject, int score, int total) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(studentId + "," + subject + "," + score + "," + total);
            bw.newLine();
        } catch (IOException ignored) {}
    }

    public static List<ResultModel> getByStudent(String studentId) {
        List<ResultModel> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4 && p[0].equals(studentId)) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }

    public static List<ResultModel> getBySubject(String subject) {
        List<ResultModel> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4 && p[1].equalsIgnoreCase(subject)) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }

    public static List<ResultModel> getAll() {
        List<ResultModel> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    results.add(new ResultModel(p[0], p[1], Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
                }
            }
        } catch (IOException ignored) {}
        return results;
    }
}
