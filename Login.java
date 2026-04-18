import javax.swing.*;
import java.awt.*;

public class Login {

    private JFrame frame;
    private JTextField usernameField;

    public Login() {
        frame = new JFrame("MATH-INIK");
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.getContentPane().setBackground(AppColors.PRIMARY);

        JLabel label = new JLabel("Enter Username:");
        label.setForeground(AppColors.TEXT);

        usernameField = new JTextField(15);
        usernameField.setBackground(AppColors.SECONDARY);
        usernameField.setForeground(AppColors.TEXT);
        usernameField.setCaretColor(AppColors.TEXT);

        JButton loginBtn = new StyledButton("Enter");
        JButton leaderboardBtn = new StyledButton("Leaderboard");

        frame.add(label);
        frame.add(usernameField);
        frame.add(loginBtn);
        frame.add(leaderboardBtn);

        loginBtn.addActionListener(e -> login());
        leaderboardBtn.addActionListener(e -> new LeaderboardUI());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter username!");
            return;
        }

        if (!PlayerManager.usernameExists(username)) {
            PlayerManager.createPlayer(username);
        }

        frame.dispose();
        new GameUI(username);
    }

    public static void main(String[] args) {
        new Login();
    }
}