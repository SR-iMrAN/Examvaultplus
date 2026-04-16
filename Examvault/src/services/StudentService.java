package services;

import models.StudentModel;
import models.QuizResultModel;
import repositories.ResultRepository;
import java.util.List;

public class StudentService {
    private ResultRepository resultRepository;
    private static StudentService instance;

    private StudentService() {
        this.resultRepository = new ResultRepository();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public List<QuizResultModel> getStudentResults(String studentId) {
        return resultRepository.getResultsByStudentId(studentId);
    }

    public boolean saveQuizResult(String studentId, String subject, int score, int totalQuestions) {
        return resultRepository.saveResult(studentId, subject, score, totalQuestions);
    }

    public double calculateAverageScore(String studentId) {
        List<QuizResultModel> results = getStudentResults(studentId);
        if (results.isEmpty()) {
            return 0;
        }
        double total = 0;
        for (QuizResultModel result : results) {
            total += result.getPercentage();
        }
        return total / results.size();
    }

    public int getTotalQuizzesTaken(String studentId) {
        return getStudentResults(studentId).size();
    }
}
