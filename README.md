# ExamVaultPlus

A professional academic management system with a full JavaFX GUI — dark, minimal, and built for students and teachers.

---

## What's Inside

**For Students**
- Register and log in with your student ID
- Browse available quiz subjects and take MCQ quizzes
- Navigate questions with Previous / Next controls
- See a live progress bar and answered-question counter during the quiz
- After submitting, view your score, grade, percentage, and an answer-by-answer review
- Check your full quiz history in the Results tab
- Use the built-in Calculator and CGPA Calculator at any time

**For Teachers**
- Register and log in with a contact number and custom password
- Add subjects from the Subjects tab (each subject gets its own question file automatically)
- Add MCQ questions one at a time with a clean form, or bulk-import from a `.txt` file
- View all student quiz results, filterable by subject, in a sortable table
- See an overview dashboard with total quizzes taken and average scores

---

## Project Structure

```
ExamVaultPlus/
├── src/
│   ├── ExamVaultPlus.java          ← main entry point
│   ├── models/
│   │   ├── UserModel.java
│   │   ├── StudentModel.java
│   │   ├── TeacherModel.java
│   │   ├── QuestionModel.java
│   │   └── ResultModel.java
│   ├── repositories/
│   │   ├── StudentRepository.java
│   │   ├── TeacherRepository.java
│   │   ├── SubjectRepository.java
│   │   ├── QuestionRepository.java
│   │   └── ResultRepository.java
│   ├── services/
│   │   ├── AuthService.java
│   │   └── CGPAService.java
│   ├── gui/
│   │   ├── MainApplication.java    ← JavaFX Application subclass
│   │   ├── SceneManager.java       ← navigation between screens
│   │   ├── LoginView.java          ← login + register for both roles
│   │   ├── StudentDashboard.java   ← quiz browser, results, tools
│   │   ├── TeacherDashboard.java   ← subjects, questions, results
│   │   ├── QuizView.java           ← interactive quiz session
│   │   ├── CalculatorView.java     ← calculator tool
│   │   └── CGPACalculatorView.java ← marks-based and subject-based GPA
│   └── css/
│       └── style.css               ← dark professional theme
├── data/
│   ├── students.txt
│   ├── teachers.txt
│   ├── results.txt
│   └── subject/
│       ├── subjects.txt
│       └── <subject>.txt           ← per-subject question files
├── run.bat                         ← one-click Windows build + run
└── run.ps1                         ← PowerShell alternative
```

---

## Running the App

### Prerequisites

| Requirement | Location |
|-------------|----------|
| JDK 26      | `C:\Program Files\Java\jdk-26` |
| JavaFX SDK  | `C:\Program Files\Java\javafx` |

### Option 1 — Double-click (Windows)

Run `run.bat` from the project root. It compiles everything and launches the app.

### Option 2 — PowerShell

```powershell
cd ExamVaultPlus
.\run.ps1
```

### Option 3 — IDE (IntelliJ / Eclipse)

1. Open the `ExamVaultPlus` folder as a project.
2. Add `C:\Program Files\Java\javafx\lib\*.jar` to your classpath / module path.
3. Add VM options:
   ```
   --module-path "C:\Program Files\Java\javafx\lib" --add-modules javafx.controls,javafx.fxml
   ```
4. Set the main class to `ExamVaultPlus` and run.

### Option 4 — Manual compile + run

```bat
javac --module-path "C:\Program Files\Java\javafx\lib" --add-modules javafx.controls,javafx.fxml ^
  -d out ^
  src\models\*.java src\repositories\*.java src\services\*.java src\gui\*.java src\ExamVaultPlus.java

xcopy /Y src\css out\css\

java --enable-native-access=javafx.graphics ^
  --module-path "C:\Program Files\Java\javafx\lib" --add-modules javafx.controls,javafx.fxml ^
  -cp out ExamVaultPlus
```

---

## File Format Reference

### students.txt
```
Name,StudentID,ContactNumber,Password
```
Password defaults to the Student ID on registration.

### teachers.txt
```
Name,TeacherID,ContactNumber,Password
```
Login username is the ContactNumber.

### results.txt
```
StudentID,Subject,Score,Total
```

### Question files — `data/questions_<subject>.txt`
One question per line, semicolon-separated:
```
Question text;Option1,Option2,Option3,Option4;CorrectOptionExactText
```
Example:
```
What is 2 + 2?;3,4,5,6;4
Which planet is closest to the Sun?;Venus,Mercury,Earth,Mars;Mercury
```

---

## Grade Scale (CGPA Calculator)

| Marks | Grade | Points | Remark        |
|-------|-------|--------|---------------|
| 80–100 | A+   | 4.00   | Outstanding   |
| 75–79  | A    | 3.75   | Excellent     |
| 70–74  | A-   | 3.50   | Very Good     |
| 65–69  | B+   | 3.25   | Good          |
| 60–64  | B    | 3.00   | Satisfactory  |
| 55–59  | B-   | 2.75   | Above Average |
| 50–54  | C+   | 2.50   | Average       |
| 45–49  | C    | 2.25   | Below Average |
| 40–44  | D    | 2.00   | Pass          |
| 0–39   | F    | 0.00   | Fail          |

---

## Developer

**Sifatur Rahman Imran**  
Contributors: Ratul Hasan Rafi, Md Hasan Al Tarek Palash, Md Abdullah AL Noman, Abu Suffian Hemel  
GitHub: [SR-iMrAN](https://github.com/SR-iMrAN)
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
                                                                          

