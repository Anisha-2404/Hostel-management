import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JLabel statusLabel;

    // Hardcoded credentials
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    public LoginFrame() {
        setTitle("Hostel Room Allocation System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // LEFT: Branding Panel
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.PRIMARY, getWidth(), getHeight(), new Color(10, 30, 70));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(-60, -60, 260, 260);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 220, 220);
                g2.setColor(new Color(255, 160, 30, 40));
                g2.fillOval(20, getHeight() - 180, 200, 200);
            }
        };
        left.setLayout(new GridBagLayout());
        left.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));

        // Icon
        JLabel iconLabel = new JLabel("🏨", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Hostel Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title2 = new JLabel("Allocation System");
        title2.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title2.setForeground(AppTheme.ACCENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("<html><center>Efficiently manage hostel rooms,<br>students, and allocations</center></html>");
        sub.setFont(AppTheme.FONT_SUBTITLE);
        sub.setForeground(new Color(180, 200, 235));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(new EmptyBorder(16, 0, 0, 0));

        // Features list
        String[] features = {"📋  Student Management", "🚪  Room Allocation", "📊  Dashboard Analytics", "📜  Allocation History"};
        leftContent.add(iconLabel);
        leftContent.add(Box.createVerticalStrut(12));
        leftContent.add(title);
        leftContent.add(title2);
        leftContent.add(sub);
        leftContent.add(Box.createVerticalStrut(30));
        for (String f : features) {
            JLabel fl = new JLabel(f);
            fl.setFont(AppTheme.FONT_BODY);
            fl.setForeground(new Color(200, 220, 255));
            fl.setAlignmentX(Component.CENTER_ALIGNMENT);
            fl.setBorder(new EmptyBorder(4, 0, 4, 0));
            leftContent.add(fl);
        }

        left.add(leftContent);

        // RIGHT: Login Panel
        JPanel right = new JPanel();
        right.setBackground(AppTheme.BG_MAIN);
        right.setLayout(new GridBagLayout());
        right.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(350, 400));

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        loginTitle.setForeground(AppTheme.PRIMARY);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSub = new JLabel("Sign in to your admin account");
        loginSub.setFont(AppTheme.FONT_SUBTITLE);
        loginSub.setForeground(AppTheme.TEXT_SECONDARY);
        loginSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(loginTitle);
        form.add(Box.createVerticalStrut(4));
        form.add(loginSub);
        form.add(Box.createVerticalStrut(30));

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(AppTheme.FONT_HEADER);
        userLabel.setForeground(AppTheme.TEXT_PRIMARY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(AppTheme.FONT_BODY);
        usernameField.setPreferredSize(new Dimension(300, 42));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setText("admin");

        form.add(userLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(usernameField);
        form.add(Box.createVerticalStrut(16));

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppTheme.FONT_HEADER);
        passLabel.setForeground(AppTheme.TEXT_PRIMARY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(AppTheme.FONT_BODY);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setText("admin123");

        form.add(passLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));

        JLabel hint = new JLabel("Default: admin / admin123");
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);
        form.add(Box.createVerticalStrut(24));

        // Login Button
        loginBtn = new JButton("Sign In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.PRIMARY_LIGHT, getWidth(), getHeight(), AppTheme.PRIMARY);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(AppTheme.FONT_BUTTON);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        form.add(loginBtn);
        form.add(Box.createVerticalStrut(12));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(AppTheme.FONT_SMALL);
        statusLabel.setForeground(AppTheme.DANGER);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(statusLabel);

        right.add(form);

        // Actions
        loginBtn.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());

        root.add(left);
        root.add(right);
        setContentPane(root);
    }

    private void attemptLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            statusLabel.setText(" ");
            dispose();
            new MainFrame().setVisible(true);
        } else {
            statusLabel.setText("❌ Invalid username or password.");
            passwordField.setText("");
        }
    }
}
