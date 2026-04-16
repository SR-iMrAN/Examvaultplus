package services;

import models.QuestionModel;
import repositories.QuestionRepository;
import java.util.List;

public class QuizService {
    private QuestionRepository questionRepository;
    private static QuizService instance;

    private QuizService() {
        this.questionRepository = new QuestionRepository();
    }

    public static QuizService getInstance() {
        if (instance == null) {
            instance = new QuizService();
        }
        return instance;
    }

    public List<QuestionModel> getQuestionsBySubject(String subject) {
        return questionRepository.loadQuestionsBySubject(subject);
    }

    public int calculateScore(List<QuestionModel> questions, List<Integer> userAnswers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionModel question = questions.get(i);
            if (userAnswers.get(i) >= 1 && userAnswers.get(i) <= question.getOptions().length) {
                String selectedOption = question.getOptions()[userAnswers.get(i) - 1];
                if (selectedOption.equalsIgnoreCase(question.getAnswer())) {
                    score++;
                }
            }
        }
        return score;
    }

    public boolean isAnswerCorrect(QuestionModel question, int answerIndex) {
        if (answerIndex >= 1 && answerIndex <= question.getOptions().length) {
            return question.getOptions()[answerIndex - 1].equalsIgnoreCase(question.getAnswer());
        }
        return false;
    }
}
