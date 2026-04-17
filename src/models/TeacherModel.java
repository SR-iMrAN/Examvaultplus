package models;

public class TeacherModel extends UserModel {
    private String name;
    private String contact;
private String teacherId;
    public TeacherModel(String username, String password, String name, String teacherId) {
        super(username, password);
        this.name = name;
         this.teacherId = teacherId;
    }

    public TeacherModel(String username, String password) {
        super(username, password);
        this.name = "";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getTeacherId() {
    return teacherId;
}
}
