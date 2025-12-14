import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class ChatBotFrame extends JFrame {
    private final JTextPane chatPane;
    private JTextField inputField;
    private final StyledDocument doc;

    public ChatBotFrame() {
        setTitle("🤖 شات بوت شفاء");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // إعداد شات بان الحديثة
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Arial", Font.PLAIN, 16));
        chatPane.setBackground(new Color(250, 250, 250));

        doc = chatPane.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // تنسيقات النصوص
        Style userStyle = doc.addStyle("UserStyle", null);
        StyleConstants.setForeground(userStyle, new Color(33, 150, 243));
        StyleConstants.setBold(userStyle, true);

        Style botStyle = doc.addStyle("BotStyle", null);
        StyleConstants.setForeground(botStyle, new Color(76, 175, 80));
        StyleConstants.setBold(botStyle, true);

        Style welcomeStyle = doc.addStyle("WelcomeStyle", null);
        StyleConstants.setForeground(welcomeStyle, new Color(0, 0, 0));
        StyleConstants.setFontSize(welcomeStyle, 18);
        StyleConstants.setBold(welcomeStyle, true);

        appendToPane("🤖 اهلا بك في شات بوت شفاء.\n", welcomeStyle);

        // حقل الإدخال
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputField.setBackground(new Color(240, 240, 240));
        inputField.setForeground(Color.BLACK);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField.addActionListener(e -> {
            String userInput = inputField.getText().trim();
            if (!userInput.isEmpty()) {
                appendToPane("\n👤 أنت: " + userInput + "\n", userStyle);

                String response = ChatBotClient.getResponse(userInput).trim();
                appendToPane("\n🤖 شفاء:\n", botStyle);
                appendToPane(response.replace("\\n", "\n") + "\n", botStyle);


                inputField.setText("");
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        // شكل عصري للإطار
        getContentPane().setBackground(new Color(230, 245, 255));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void appendToPane(String message, Style style) {
        try {
            doc.insertString(doc.getLength(), message, style);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatBotFrame::new);
    }
}
