import javax.swing.*;
import java.awt.*;

public class LeaderboardUI {

    private JFrame frame;

    public LeaderboardUI() {
        frame = new JFrame("Leaderboard");
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());

        String[][] data = PlayerManager.getLeaderboard();
        String[] columns = {"Username", "Score"};

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(new JLabel("Leaderboard", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    public JFrame getFrame() {
        return frame;
    }
}