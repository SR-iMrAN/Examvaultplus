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
            boolean answered = false;

            while (!answered) {

                System.out.println("\n" + q.questionText);
                for (int i = 0; i < q.options.length; i++) {
                    System.out.println((i + 1) + ". " + q.options[i]);
                }

                System.out.print("Your answer (1-" + q.options.length + ") or 'C' for Calculator: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("C")) {
                    Calculator.runCalculator(scanner);
                    // After calculator exits, loop will repeat and question will show again
                    continue;
                }

                int ans;
                try {
                    ans = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + q.options.length + " or 'C' to open calculator.");
                    continue;
                }

                if (ans < 1 || ans > q.options.length) {
                    System.out.println("Please select a valid option between 1 and " + q.options.length + ".");
                    continue;
                }

                if (q.options[ans - 1].equalsIgnoreCase(q.answer)) {
                    score++;
                }

                answered = true;
            }
        }

        System.out.println("\nQuiz finished! Your score: " + score + "/" + questions.size());
        Student.saveResult(studentId, subject, score, questions.size());

         System.out.print("\nDo you want to review the correct answers? (y/n): ");
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("y")) {
            System.out.println("\n--- Review Correct Answers ---");
            int questionNumber = 1;
            for (Question q : questions) {
                System.out.println("\n" + questionNumber + ". " + q.questionText);
                System.out.println( "Correct Answer: " + q.answer);
                questionNumber++;
            }
        }
    }
}
