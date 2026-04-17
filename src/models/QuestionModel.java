package models;

public class QuestionModel {
    private String id;
    private String questionText;
    private String[] options;
    private String answer;

    public QuestionModel(String id, String questionText, String[] options, String answer) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.answer = answer;
    }

    public QuestionModel(String questionText, String[] options, String answer) {
        this(null, questionText, options, answer);
    }

    public String getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public String getAnswer() { return answer; }
}
