package services;

import models.QuizResultModel;
import repositories.SubjectRepository;
import repositories.QuestionRepository;
import repositories.ResultRepository;
import java.util.List;

public class TeacherService {
    private SubjectRepository subjectRepository;
    private QuestionRepository questionRepository;
    private ResultRepository resultRepository;
    private static TeacherService instance;

    private TeacherService() {
        this.subjectRepository = new SubjectRepository();
        this.questionRepository = new QuestionRepository();
        this.resultRepository = new ResultRepository();
    }

    public static TeacherService getInstance() {
        if (instance == null) {
            instance = new TeacherService();
        }
        return instance;
    }

    public List<String> getAllSubjects() {
        return subjectRepository.getAllSubjects();
    }

    public boolean addSubject(String subjectName) {
        return subjectRepository.addSubject(subjectName);
    }

    public boolean addQuestion(String subject, String questionText, String[] options, String answer) {
        return questionRepository.addQuestion(subject, questionText, options, answer);
    }

    public List<QuizResultModel> getResultsBySubject(String subject) {
        return resultRepository.getResultsBySubject(subject);
    }

    public SubjectRepository getSubjectRepository() {
        return subjectRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public ResultRepository getResultRepository() {
        return resultRepository;
    }
}
