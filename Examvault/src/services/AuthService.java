package services;

import models.StudentModel;
import models.TeacherModel;
import repositories.StudentRepository;
import repositories.TeacherRepository;

public class AuthService {
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private static AuthService instance;

    private StudentModel currentStudent;
    private TeacherModel currentTeacher;

    private AuthService() {
        this.studentRepository = new StudentRepository();
        this.teacherRepository = new TeacherRepository();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public StudentModel loginStudent(String username, String password) {
        currentStudent = studentRepository.findByUsernameAndPassword(username, password);
        currentTeacher = null;
        return currentStudent;
    }

    public TeacherModel loginTeacher(String username, String password) {
        currentTeacher = teacherRepository.findByUsernameAndPassword(username, password);
        currentStudent = null;
        return currentTeacher;
    }

    public boolean registerStudent(String name, String username, String contactNumber, String password) {
        if (studentRepository.findByUsername(username) != null) {
            return false;
        }
        StudentModel student = new StudentModel(name, username, contactNumber, password);
        return studentRepository.registerStudent(student);
    }

    public boolean registerTeacher(String name, String contactNumber, String username, String password) {
        if (teacherRepository.findByUsername(username) != null) {
            return false;
        }
        TeacherModel teacher = new TeacherModel(name, contactNumber, username, password);
        return teacherRepository.registerTeacher(teacher);
    }

    public StudentModel getCurrentStudent() {
        return currentStudent;
    }

    public TeacherModel getCurrentTeacher() {
        return currentTeacher;
    }

    public boolean isStudentLoggedIn() {
        return currentStudent != null;
    }

    public boolean isTeacherLoggedIn() {
        return currentTeacher != null;
    }

    public void logout() {
        currentStudent = null;
        currentTeacher = null;
    }

    public StudentRepository getStudentRepository() {
        return studentRepository;
    }

    public TeacherRepository getTeacherRepository() {
        return teacherRepository;
    }
}
