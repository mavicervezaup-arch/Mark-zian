import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

    public StyledButton(String text) {
        super(text);
        setBackground(AppColors.ACCENT);
        setForeground(AppColors.PRIMARY);
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}