package models;

public class ResultModel {
    private String studentId;
    private String subject;
    private int score;
    private int total;

    public ResultModel(String studentId, String subject, int score, int total) {
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.total = total;
    }

    public String getStudentId() { return studentId; }
    public String getSubject() { return subject; }
    public int getScore() { return score; }
    public int getTotal() { return total; }
    public double getPercentage() { return total > 0 ? (score * 100.0 / total) : 0; }
}
