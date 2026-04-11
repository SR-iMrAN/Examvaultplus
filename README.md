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
