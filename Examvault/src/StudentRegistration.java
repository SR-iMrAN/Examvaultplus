import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StudentRegistration extends Registration implements Savable {
  private   static final String STUDENT_FILE = "data/students.txt";
   private String name;
   private String id;
   private String contactNumber;


    @Override
    public void register() {
        System.out.print("Enter Name: ");
        this.name = scanner.nextLine();
        System.out.print("Enter Student ID (will also be password): ");
        this.id = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        this.contactNumber = scanner.nextLine();

        if (Student.addStudent(name, id, contactNumber)) {
            System.out.println("Student registered successfully!");
        } else {
            System.out.println("ID already exists!");
        }
    }
    @Override
    public void save(){
//
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_FILE, true))) {
                bw.write(name + "," + id + "," + contactNumber + "," + id);
                bw.newLine();
            } catch (IOException e) {
                System.out.println("Error saving student.");
            }

        }
    }


//public static boolean addStudent(String name, String id, String contactNumber) {
//            for (Student s : students) {
//                if (s.getUsername().equals(id)) {
//                    return false; // ID already registered
//                }
//            }
//            Student newStudent = new Student(name, id, contactNumber, id); // password = id
//            students.add(newStudent);
//  return true;