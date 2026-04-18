import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameUI {

    private boolean isPaused = false;

    private JFrame frame;
    private String currentUser;
    private int score;
    private String level;
    private int lives = 3;
    private JLabel livesLabel;

    private JLabel questionLabel, scoreLabel, timerLabel;
    private JTextField answerField;
    private Timer timer;
    private int timeLeft = 30;
    private String currentQuestion;

    public GameUI(String username) {
        this.currentUser = username;
        this.score = PlayerManager.getScore(username);
        showLevelSelection();
    }

    private void showLevelSelection() {
        frame = new JFrame("Select Level");
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.getContentPane().setBackground(AppColors.PRIMARY);

        JLabel title = new JLabel("Choose Difficulty");
        title.setForeground(AppColors.TEXT);

        JButton easy = new StyledButton("Easy");
        JButton medium = new StyledButton("Medium");
        JButton hard = new StyledButton("Hard");

        frame.add(title);
        frame.add(easy);
        frame.add(medium);
        frame.add(hard);

        easy.addActionListener(e -> startDoors("EASY"));
        medium.addActionListener(e -> startDoors("MEDIUM"));
        hard.addActionListener(e -> startDoors("HARD"));

        frame.setVisible(true);
    }

    private void startDoors(String lvl) {
        this.level = lvl;
        frame.dispose();

        frame = new JFrame("Pick a Door");
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.getContentPane().setBackground(AppColors.PRIMARY);

        JLabel label = new JLabel("Pick a Door");
        label.setForeground(AppColors.TEXT);

        JButton d1 = new StyledButton("Door 1");
        JButton d2 = new StyledButton("Door 2");
        JButton d3 = new StyledButton("Door 3");
        JButton d4 = new StyledButton("Door 4");

        frame.add(label);
        frame.add(d1);
        frame.add(d2);
        frame.add(d3);
        frame.add(d4);

        ActionListener doorAction = e -> startGame();

        d1.addActionListener(doorAction);
        d2.addActionListener(doorAction);
        d3.addActionListener(doorAction);
        d4.addActionListener(doorAction);

        frame.setVisible(true);
    }

    private void startGame() {
        frame.dispose();

        frame = new JFrame("Game");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.getContentPane().setBackground(AppColors.PRIMARY);

        JLabel playerLabel = new JLabel("Player: " + currentUser);
        playerLabel.setForeground(AppColors.TEXT);

        JLabel levelLabel = new JLabel("Level: " + level);
        levelLabel.setForeground(AppColors.TEXT);

        questionLabel = new JLabel();
        questionLabel.setForeground(AppColors.TEXT);

        answerField = new JTextField(10);
        answerField.setBackground(AppColors.SECONDARY);
        answerField.setForeground(AppColors.TEXT);
        answerField.setCaretColor(AppColors.TEXT);

        JButton submit = new StyledButton("Submit");

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(AppColors.TEXT);

        timerLabel = new JLabel("Time: 30");
        timerLabel.setForeground(AppColors.TEXT);

        // ================= LIVES LABEL (HEARTS ADDED) =================
        livesLabel = new JLabel(getLivesDisplay());
        livesLabel.setForeground(AppColors.TEXT);

        frame.add(playerLabel);
        frame.add(levelLabel);
        frame.add(questionLabel);
        frame.add(answerField);
        frame.add(submit);
        frame.add(scoreLabel);
        frame.add(timerLabel);
        frame.add(livesLabel);

        submit.addActionListener(e -> checkAnswer());

        JButton pauseBtn = new StyledButton("Pause");
        frame.add(pauseBtn);
        pauseBtn.addActionListener(e -> {
            if (!isPaused) {
                pauseGame();
            }
        });

        frame.setVisible(true);

        nextQuestion();
        startTimer();
    }

    private void pauseGame() {
        isPaused = true;
        timer.stop();

        showPauseMenu();
    }

    private void showPauseMenu() {

        int choice = JOptionPane.showOptionDialog(
                frame,
                "Game Paused",
                "Pause Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Resume", "Leaderboard", "Exit"},
                "Resume"
        );

        if (choice == 0) {
            isPaused = false;
            timer.start();

        } else if (choice == 1) {
            openLeaderboardWhilePaused();

        } else {
            saveScore();
            System.exit(0);
        }
    }

    private void openLeaderboardWhilePaused() {
        LeaderboardUI lb = new LeaderboardUI();

        lb.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                showPauseMenu();
            }
        });
    }

    private void nextQuestion() {
        currentQuestion = GameLogic.generateQuestion(level);
        questionLabel.setText(currentQuestion);
        answerField.setText("");
        timeLeft = 30;
        timerLabel.setText("Time: 30");
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft <= 0) {

                loseLife("Time's up!");

                if (lives <= 0) {
                    gameOver();
                } else {
                    nextQuestion();
                }
            }
        });

        timer.start();
    }

    private void checkAnswer() {
        try {
            int userAns = Integer.parseInt(answerField.getText());
            int correct = GameLogic.getAnswer(currentQuestion);

            if (userAns == correct) {
                score += 10;
                scoreLabel.setText("Score: " + score);
                nextQuestion();

            } else {
                loseLife("Wrong!");

                if (lives <= 0) {
                    gameOver();
                } else {
                    nextQuestion();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input!");
        }
    }

    // ================= LIVES SYSTEM =================

    private void loseLife(String msg) {
        lives--;
        livesLabel.setText(getLivesDisplay());
        JOptionPane.showMessageDialog(frame, msg + " -1 Life");
    }

    private String getLivesDisplay() {
        String hearts = "";
        for (int i = 0; i < lives; i++) {
            hearts += "❤️";
        }
        return "Lives: " + hearts;
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(frame, "GAME OVER");

        saveScore();
        replay();
    }

    private void replay() {
        int choice = JOptionPane.showOptionDialog(frame,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Yes", "See Leaderboard", "Exit Game"},
                "Play Again");

        lives = 3;
        score = 0;

        frame.dispose();

        if (choice == 0) {
            showLevelSelection();
        } else if (choice == 1) {
            new LeaderboardUI();
        } else {
            System.exit(0);
        }
    }

    private void saveScore() {
        PlayerManager.updateScore(currentUser, score);
    }
}