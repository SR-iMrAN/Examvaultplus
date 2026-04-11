package models;

public class StudentModel extends UserModel {
    private String name;
    private String contactNumber;

    public StudentModel(String name, String id, String contactNumber, String password) {
        super(id, password);
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
