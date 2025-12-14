import javax.swing.*;
import java.awt.*;

public class ChatBotUtils {
    public static JButton createChatBubble() {
        JButton chatBubble = new JButton("💬");
        chatBubble.setFont(new Font("Arial", Font.PLAIN, 30));
        chatBubble.setBounds(10, 460, 50, 50); // Adjust position
        chatBubble.setBorderPainted(false);
        chatBubble.setFocusPainted(false);
        chatBubble.setContentAreaFilled(false);

        chatBubble.addActionListener(e -> new ChatBotFrame());

        return chatBubble;
    }
}
