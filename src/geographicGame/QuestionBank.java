package geographicGame;

import java.io.*;
import java.util.ArrayList;

public class QuestionBank {
    public ArrayList<Question> easyQuestionList = new ArrayList<>();
    public ArrayList<Question> mediumQuestionList = new ArrayList<>();
    public ArrayList<Question> hardQuestionList = new ArrayList<>();

    public QuestionBank() {
        loadQuestions("easy_questions.txt", easyQuestionList, "Easy");
        loadQuestions("medium_questions.txt", mediumQuestionList, "Medium");
        loadQuestions("hard_questions.txt", hardQuestionList, "Hard");
    }

    private void loadQuestions(String fileName, ArrayList<Question> list, String levelName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    list.add(new Question(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            System.out.println(levelName + " Questions Loaded: " + list.size());
        } catch (IOException e) {
            System.err.println("Could not load " + levelName + " questions from " + fileName);
            e.printStackTrace();
        }
    }
}
