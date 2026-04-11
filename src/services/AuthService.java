package services;

import models.StudentModel;
import models.TeacherModel;
import repositories.StudentRepository;
import repositories.TeacherRepository;

public class AuthService {
    private static StudentModel currentStudent = null;
    private static TeacherModel currentTeacher = null;

    public static StudentModel loginStudent(String id, String password) {
        StudentModel s = StudentRepository.login(id, password);
        currentStudent = s;
        return s;
    }

    public static TeacherModel loginTeacher(String username, String password) {
        TeacherModel t = TeacherRepository.login(username, password);
        currentTeacher = t;
        return t;
    }

    public static boolean registerStudent(String name, String id, String contact) {
        return StudentRepository.register(name, id, contact);
    }

    public static boolean registerTeacher(String name, String id, String contact, String password) {
        return TeacherRepository.register(name, id, contact, password);
    }

    public static StudentModel getCurrentStudent() { return currentStudent; }
    public static TeacherModel getCurrentTeacher() { return currentTeacher; }

    public static void logout() {
        currentStudent = null;
        currentTeacher = null;
    }
}
