package models;

public class TeacherModel extends UserModel {
    private String name;
    private String contactNumber;

    public TeacherModel(String name, String contactNumber, String username, String password) {
        super(username, password, "teacher");
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
