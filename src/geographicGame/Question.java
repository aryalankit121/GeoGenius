package geographicGame;

public class Question {
    private String question;
    private String options;
    private String correctOption;
    private String description;

    public Question(String question, String options, String correctOption, String description) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public String getDescription() {
        return description;
    }
}
