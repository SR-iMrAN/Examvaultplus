package models;

public class QuizResultModel {
    private String studentId;
    private String subject;
    private int score;
    private int totalQuestions;

    public QuizResultModel(String studentId, String subject, int score, int totalQuestions) {
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSubject() {
        return subject;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public double getPercentage() {
        return (double) score / totalQuestions * 100;
    }
}
