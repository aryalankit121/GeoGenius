package geographicGame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class GameManagerGUI extends JFrame {
    public GameManagerGUI() {
        setTitle("GeoGenius – The Ultimate Geography Challenge!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(new Color(173, 216, 230));

        JLabel title = new JLabel("GeoGenius – The Ultimate Geography Challenge!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(25, 25, 112));

        JLabel highScoreLabel = new JLabel("High Score: " + GameWindow.loadHighScore(), SwingConstants.CENTER);
        highScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton startBtn = new JButton("Start Game");
        JButton instructionsBtn = new JButton("Instructions");
        JButton exitBtn = new JButton("Exit");

        startBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        instructionsBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 18));

        startBtn.addActionListener(e -> {
            try {
                GameWindow window = new GameWindow();
                window.setVisible(true);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to start game: " + ex.getMessage());
            }
        }); 

        instructionsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "\u2022 Answer geography questions by clicking A, B, or C.\n" +
                        "\u2022 Each correct answer gives you a point.\n" +
                        "\u2022 Each wrong answer costs a life.\n" +
                        "\u2022 Timer: 10 seconds per question.\n" +
                        "\u2022 Score 8/10 to move to Level 2.\n" +
                        "\u2022 Score 9/10 in Level 2 to move to Level 3.\n" +
                        "\u2022 Win by answering all Level 3 questions correctly!",
                "How to Play", JOptionPane.INFORMATION_MESSAGE));

        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(highScoreLabel);
        panel.add(startBtn);
        panel.add(instructionsBtn);
        panel.add(exitBtn);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameManagerGUI().setVisible(true));
    }
}

class GameWindow extends JFrame {
    private QuestionBank qb = new QuestionBank();
    private int level = 1, score = 0, lives = 6, questionIndex = 0, levelScore = 0;
    private List<Question> currentQuestions;
    private Iterator<Question> questionIterator;
    private Clip backgroundClip, correctClip, incorrectClip;
    private boolean isMuted = false;
    private Timer questionTimer;
    private int timeLeft = 10;

    JLabel titleLabel = new JLabel("GeoGenius – Test Your Geography!", SwingConstants.CENTER);
    JLabel scoreLabel = new JLabel();
    JLabel livesLabel = new JLabel();
    JLabel timerLabel = new JLabel();
    JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    JTextArea optionsArea = new JTextArea();
    JButton aButton = new JButton("A"), bButton = new JButton("B"), cButton = new JButton("C");
    JButton muteButton = new JButton("Mute");

    private Question currentQuestion;

    public GameWindow() {
        setTitle("GeoGenius - Level " + level);
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        topPanel.setBackground(new Color(224, 255, 255));

        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 51, 102));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(34, 139, 34));
        livesLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        livesLabel.setForeground(new Color(178, 34, 34));
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setForeground(new Color(0, 0, 128));

        topPanel.add(titleLabel);
        topPanel.add(scoreLabel);
        topPanel.add(livesLabel);
        topPanel.add(timerLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 248, 255));

        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        optionsArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        optionsArea.setEditable(false);
        optionsArea.setOpaque(false);
        optionsArea.setLineWrap(true);
        optionsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(optionsArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerPanel.add(questionLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        aButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        bButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        cButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        muteButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        aButton.addActionListener(e -> checkAnswer("A"));
        bButton.addActionListener(e -> checkAnswer("B"));
        cButton.addActionListener(e -> checkAnswer("C"));
        muteButton.addActionListener(e -> toggleMusic());

        buttonPanel.add(aButton);
        buttonPanel.add(bButton);
        buttonPanel.add(cButton);
        buttonPanel.add(muteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadSounds();
        setQuestionList();
        playBackgroundMusic();
        loadNextQuestion();
    }

    private void loadSounds() {
        try {
            correctClip = AudioSystem.getClip();
            incorrectClip = AudioSystem.getClip();
            correctClip.open(AudioSystem.getAudioInputStream(new File("src/geographicGame/correct.wav")));
            incorrectClip.open(AudioSystem.getAudioInputStream(new File("src/geographicGame/incorrect.wav")));
        } catch (Exception e) {
            System.err.println("Error loading sound effects: " + e.getMessage());
        }
    }

    private void playBackgroundMusic() {
        try {
            File soundFile = new File("src/geographicGame/background.wav");
            if (!soundFile.exists()) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Background music error: " + e.getMessage());
        }
    }

    private void toggleMusic() {
        if (backgroundClip != null) {
            if (isMuted) {
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                muteButton.setText("Mute");
            } else {
                backgroundClip.stop();
                muteButton.setText("Unmute");
            }
            isMuted = !isMuted;
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    private void setQuestionList() {
        List<Question> baseList = level == 1 ? qb.easyQuestionList
                : level == 2 ? qb.mediumQuestionList
                : qb.hardQuestionList;
        currentQuestions = new ArrayList<>(baseList);
        Collections.shuffle(currentQuestions);
        questionIterator = currentQuestions.iterator();
        levelScore = 0;
        questionIndex = 0;
    }

    private void loadNextQuestion() {
        if (lives <= 0) {
            endGame("Game Over! Lives: 0\nFinal Score: " + score);
            return;
        }

        if (!questionIterator.hasNext() || questionIndex >= 10) {
            boolean advance = (level == 1 && levelScore >= 8) ||
                              (level == 2 && levelScore >= 9) ||
                              (level == 3 && levelScore == 10);

            if (advance) {
                if (level == 3) {
                    endGame("Game Completed! You are a Geo-Genius!\nFinal Score: " + score);
                    return;
                }

                level++;
                setTitle("GeoGenius - Level " + level);
                JOptionPane.showMessageDialog(this,
                        "🎉 Great job! You've advanced to Level " + level + "!",
                        "Level Up", JOptionPane.INFORMATION_MESSAGE);
                setQuestionList();
            } else {
                endGame("You didn’t score enough to move to next level.\nFinal Score: " + score);
                return;
            }
        }

        if (!questionIterator.hasNext()) {
            questionIterator = currentQuestions.iterator();
        }

        currentQuestion = questionIterator.next();
        questionLabel.setText("Q" + (questionIndex + 1) + ": " + currentQuestion.getQuestion());
        String formattedOptions = currentQuestion.getOptions()
                .replace("\\n", "\n")
                .replaceAll("(?=[ABC]\\))", "\n");
        optionsArea.setText(formattedOptions.trim());

        startTimer();
        updateStatus();
    }


    private void checkAnswer(String selected) {
        if (questionTimer != null) questionTimer.stop();

        String correct = currentQuestion.getCorrectOption();
        boolean isCorrect = selected.equalsIgnoreCase(correct);

        if (isCorrect) {
            score++;
            levelScore++;
            if (!isMuted && correctClip != null) {
                correctClip.setFramePosition(0);
                correctClip.start();
            }
        } else {
            lives--;
            if (!isMuted && incorrectClip != null) {
                incorrectClip.setFramePosition(0);
                incorrectClip.start();
            }
        }

        JOptionPane.showMessageDialog(this,
                (isCorrect ? "Correct!" : "Incorrect!") + "\n\n" + currentQuestion.getDescription(),
                "Result", JOptionPane.INFORMATION_MESSAGE);

        questionIndex++;
        loadNextQuestion();
    }

    private void startTimer() {
        if (questionTimer != null) questionTimer.stop();

        timeLeft = 10;
        timerLabel.setText("Time Left: " + timeLeft + " seconds");

        questionTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + " seconds");

            if (timeLeft <= 0) {
                questionTimer.stop();
                JOptionPane.showMessageDialog(this, "Time's up!");
                lives--;
                questionIndex++;
                loadNextQuestion();
            }
        });

        questionTimer.start();
    }

    private void updateStatus() {
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives Remaining: " + lives);
    }

    private void endGame(String message) {
        stopBackgroundMusic();
        if (questionTimer != null) questionTimer.stop();
        int highScore = loadHighScore();
        if (score > highScore) {
            saveHighScore(score);
            message += "\nNEW HIGH SCORE!";
        } else {
            message += "\nHigh Score: " + highScore;
        }
        JOptionPane.showMessageDialog(this, message);
        dispose();
    }

    public static void saveHighScore(int newScore) {
        try (FileWriter writer = new FileWriter("highscore.txt")) {
            writer.write(String.valueOf(newScore));
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }

    public static int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }
}
