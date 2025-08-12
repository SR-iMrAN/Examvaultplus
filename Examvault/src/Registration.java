import java.util.Scanner;

public abstract class Registration implements Savable {
    protected Scanner scanner = new Scanner(System.in);

    public abstract void register();

    @Override
    public void save() {

    }

    protected String confirmPassword() {
        while (true) {
            System.out.print("Enter password: ");
            String pass1 = scanner.nextLine();
            System.out.print("Confirm password: ");
            String pass2 = scanner.nextLine();

            if (pass1.equals(pass2)) {
                return pass1;
            } else {
                System.out.println("Passwords do not match! Try again.");
            }
        }
    }
}
