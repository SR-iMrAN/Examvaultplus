package models;

public class QuestionModel {
    private String questionText;
    private String[] options;
    private String answer;

    public QuestionModel(String questionText, String[] options, String answer) {
        this.questionText = questionText;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
