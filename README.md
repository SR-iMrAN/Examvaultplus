# Exam Vault Plus


## Overview
**Exam Vault Plus** is a console-based Java application designed for educational purposes. It allows **students** and **teachers** to manage quizzes, results, and GPA calculations efficiently. The system supports file-based storage, dynamic quiz creation, and interactive features like a built-in calculator and GPA analysis.

---

## Features

### Student Features
- Register and login with unique Student ID.
- Take quizzes in various subjects.
- View past quiz results.
- Students can see the correct answers to questions after finishing the quiz.
- Marks-based and subject-based GPA/CGPA calculations.
- Interactive console calculator during quizzes.

### Teacher Features
- Register and login with unique Teacher ID.
- Add new multiple-choice questions to subjects.
- Import questions from external text files.
- View quiz results by subject.

### System Features
- File-based storage for users, questions, and results.
- Subject management with dynamic subject creation.
- ANSI color-coded console interface for better readability.
- Error handling and input validation for smooth user experience.

---

## Components

| Class | Purpose |
|-------|---------|
| `User` | Base class for Student and Teacher authentication. |
| `Student` | Manages student data, results, and login checks. |
| `Teacher` | Handles teacher data, quiz creation, and result viewing. |
| `Registration` | Abstract class for registration workflow with password confirmation. |
| `StudentRegistration` | Handles student-specific registration and saving. |
| `TeacherRegistration` | Handles teacher-specific registration and saving. |
| `Quiz` | Manages quiz flow, question loading, scoring, and result saving. |
| `Question` | Represents a single quiz question with options and answer. |
| `Calculator` | Console-based arithmetic tool accessible during quizzes. |
| `CGPACalculator` | Marks-based and subject-based GPA calculation system. |
| `SubjectManager` | Manages subjects, subject files, and prevents duplicates. |
| `Savable` | Interface to enforce `save()` implementation in registration classes. |

---

## Folder & File Structure
```text
ExamVaultPlus/
│
├── data/
│ ├── students.txt
│ ├── teachers.txt
│ ├── results.txt
│ ├── subject/
│ │ └── subjects.txt
│ └── questions_<subject>.txt
│
├── src/
│ ├── User.java
│ ├── Student.java
│ ├── Teacher.java
│ ├── Quiz.java
│ ├── Question.java
│ ├── Calculator.java
│ ├── CGPACalculator.java
│ ├── Registration.java
│ ├── StudentRegistration.java
│ ├── TeacherRegistration.java
│ ├── SubjectManager.java
│ └── Savable.java
│
└── ExamVaultPlus.java (Main class with menu and program entry)

In  Exam Vault Plus project, multiple OOP concepts are used. Lets  break them down with examples from  code:

1. Encapsulation
Definition: Wrapping data (variables) and methods that operate on that data together, and controlling access via getters and setters.

Example:


public class User {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
Explanation: username and password are private; access is only allowed through getters and setters.

2. Inheritance
Definition: A class can inherit fields and methods from another class.

Example:


public class Student extends User {
    String name;
    String contactNumber;
}
Explanation: Student inherits username and password from User, so you don’t need to redefine login-related methods.

3. Polymorphism
Definition: The ability of objects to be treated as instances of their parent class or interface.

Example:


public abstract class Registration implements Savable {
    public abstract void register();
}

public class StudentRegistration extends Registration {
    @Override
    public void register() { ... }
}

public class TeacherRegistration extends Registration {
    @Override
    public void register() { ... }
}
Explanation: Both StudentRegistration and TeacherRegistration are treated as Registration types but behave differently (method overriding).

4. Abstraction
Definition: Hiding complex implementation details and showing only the functionality.

Example:

public interface Savable {
    void save();
}
Explanation: Savable only defines that a class should implement save(), without showing how. Classes like StudentRegistration and TeacherRegistration implement it differently.

5. Composition
Definition: A class is made up of other classes, “has-a” relationship.

Example:


public class Quiz {
    ArrayList<Question> questions = new ArrayList<>();
}
Explanation: Quiz contains multiple Question objects. Quiz “has-a” list of questions.

6. Method Overloading
Definition: Multiple methods in the same class with the same name but different parameters.

Example:

public Teacher(String username, String password, String namet) { ... }
public Teacher(String username, String password) { ... }
Explanation: Teacher class has two constructors, demonstrating overloading.

7. Static Members
Definition: Class-level variables and methods that are shared across all instances.

Example:


static ArrayList<Student> students = new ArrayList<>();
public static void loadStudents() { ... }
Explanation: All Student objects share the same students list.

---

## How to Run Locally

1. **Clone the repository**
```bash
git clone https://github.com/SR-iMrAN/Examvaultplus.git
cd Examvaultplus
Compile all Java files


javac src/*.java
Run the main program


java src.ExamVaultPlus
Follow the console menu

Register or login as Student or Teacher.

Navigate quizzes, GPA calculators, and results.

Notes
All data is stored locally in the data folder.

Quizzes and subjects are dynamically managed using text files.

Built with Java SE and requires no additional dependencies.

Contact
™The Rookies +⁠_⁠+
Developer: Sifatur Rahman Imran with
Ratul Hasan Rafi , Md Hasan Al Tarek Palash , Md Abdullah AL Noman and Abu Suffian Hemel


GitHub: SR-iMrAN

  _____                      __     __             _  _         
 | ____|__  __ __ _  _ __ ___\ \   / /__ _  _   _ | || |_   _   
 |  _|  \ \/ // _` || '_ ` _ \\ \ / // _` || | | || || __|_| |_ 
 | |___  >  <| (_| || | | | | |\ V /| (_| || |_| || || |_|_   _|
 |_____|/_/\_\\__,_||_| |_| |_| \_/  \__,_| \__,_||_| \__| |_|  
                                                                





████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗    ██╗   ██╗ ██████╗ ██╗   ██╗
╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝    ╚██╗ ██╔╝██╔═══██╗██║   ██║
   ██║   ███████║███████║██╔██╗ ██║█████╔╝      ╚████╔╝ ██║   ██║██║   ██║
   ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗       ╚██╔╝  ██║   ██║██║   ██║
   ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██╗       ██║   ╚██████╔╝╚██████╔╝
   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝       ╚═╝    ╚═════╝  ╚═════╝ 
                                                                          

