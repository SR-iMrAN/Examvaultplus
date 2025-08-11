import java.util.ArrayList;

public class SubjectManager {
    static ArrayList<String> subjects = new ArrayList<>();

    public static void addSubject(String subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            System.out.println("Subject \"" + subject + "\" added.");
        } else {
            System.out.println("Subject already exists.");
        }
    }

    public static void listSubjects() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects added yet.");
        } else {
            System.out.println("Available subjects:");
            for (int i = 0; i < subjects.size(); i++) {
                System.out.println((i + 1) + ". " + subjects.get(i));
            }
        }
    }

    public static boolean isValidSubject(int index) {
        return index >= 1 && index <= subjects.size();
    }

    public static String getSubject(int index) {
        return subjects.get(index - 1);
    }
}
