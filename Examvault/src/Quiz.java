import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    ArrayList<Question> questions = new ArrayList<>();

    public void loadQuestions(String subject) {
        questions.clear();
        String fileName = "data/questions_" + subject.toLowerCase() + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String questionText = parts[0];
                    String[] options = parts[1].split(",");
                    String answer = parts[2];
                    questions.add(new Question(questionText, options, answer));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading questions for subject: " + subject);
        }
    }

    public void startQuiz(Scanner scanner, String studentId, String subject) {
        if (questions.isEmpty()) {
            System.out.println("No questions found for subject: " + subject);
            return;
        }

        int score = 0;
        for (Question q : questions) {
            System.out.println("\n" + q.questionText);
            for (int i = 0; i < q.options.length; i++) {
                System.out.println((i + 1) + ". " + q.options[i]);
            }
            System.out.print("Your answer (1-" + q.options.length + "): ");
            int ans = scanner.nextInt();
            scanner.nextLine();
            if (ans > 0 && ans <= q.options.length) {
                if (q.options[ans - 1].equalsIgnoreCase(q.answer)) {
                    score++;
                }
            }
        }
        System.out.println("\nQuiz finished! Your score: " + score + "/" + questions.size());

        Student.saveResult(studentId, subject, score, questions.size());
    }
}
