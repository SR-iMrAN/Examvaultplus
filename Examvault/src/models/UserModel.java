package models;

public class UserModel {
    private String username;
    private String password;
    private String userType; // "student" or "teacher"

    public UserModel(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}
