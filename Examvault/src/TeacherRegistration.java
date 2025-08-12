import java.io.*;

public class TeacherRegistration extends Registration implements Savable {
    private static final String TEACHER_FILE = "data/teachers.txt";
    private String name;
    private String id;
    private String contact;
    private String password;
    @Override
    public void register() {
        System.out.print("Enter Name: ");
        this.name = scanner.nextLine();
        System.out.print("Enter Teacher ID: ");
        this.id = scanner.nextLine();
        System.out.print("Enter Contact Number (username for login): ");
        this.contact = scanner.nextLine();

        this.password = confirmPassword();
    }
    @Override
     public  void save(){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEACHER_FILE, true))) {
                bw.write(name + "," + id + "," + contact + "," + password);
                bw.newLine();
                System.out.println("Teacher registered successfully!");
            } catch (IOException e) {
                System.out.println("Error saving teacher data.");
            }
        }

}
