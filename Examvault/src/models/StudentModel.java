package models;

public class StudentModel extends UserModel {
    private String name;
    private String contactNumber;

    public StudentModel(String name, String username, String contactNumber, String password) {
        super(username, password, "student");
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
