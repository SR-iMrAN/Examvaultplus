import java.io.*;
import java.util.ArrayList;

public class Student extends User {
    String name;

    String contactNumber;


    static ArrayList<Student> students = new ArrayList<>();


    public Student(String name, String id, String contactNumber, String password) {
        super(id,password);
        this.name = name;

        this.contactNumber = contactNumber;

    }

    public static void loadStudents() {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("data/students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students.");
        }
    }

//    public static boolean addStudent(String name, String id, String contactNumber) {
//        for (Student s : students) {
//            if (s.getUsername().equals(id)) {
//                return false; // ID already registered
//            }
//        }
//        Student newStudent = new Student(name, id, contactNumber, id); // password = id
//        students.add(newStudent);
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/students.txt", true))) {
//            bw.write(name + "," + id + "," + contactNumber + "," + id);
//            bw.newLine();
//        } catch (IOException e) {
//            System.out.println("Error saving student.");
//        }
//        return true;
//    }
public Student(String username, String password) {
    super(username, password);
}
    public static boolean checkLogin(String id, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(id) && s.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    public static void viewResults(String id) {
        String resultsFile = "data/results.txt";
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(resultsFile))) {
            String line;
            System.out.println("\nYour past quiz results:");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // parts format: studentId,subject,score,total
                if (parts.length == 4 && parts[0].equals(id)) {
                    System.out.println("Subject: " + parts[1] + " | Score: " + parts[2] + " out of " + parts[3]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No results found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading results.");
        }
    }

    public static void saveResult(String id, String subject, int score, int total) {
        String resultsFile = "data/results.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(resultsFile, true))) {
            bw.write(id + "," + subject + "," + score + "," + total);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving result.");
        }
    }
}
