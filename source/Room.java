import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton[] navButtons;
    private String[] navNames = {"Dashboard", "Students", "Rooms", "Allocate Room", "History"};
    private String[] navIcons = {"⊞", "👤", "🚪", "🔑", "📋"};

    private DashboardPanel dashboardPanel;
    private StudentPanel studentPanel;
    private RoomPanel roomPanel;
    private AllocationPanel allocationPanel;
    private HistoryPanel historyPanel;

    public MainFrame() {
        setTitle("Hostel Room Allocation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 740);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // SIDEBAR
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.BG_SIDEBAR, 0, getHeight(), new Color(10, 28, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);

        // Logo Area - LPU Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        logoPanel.setMaximumSize(new Dimension(220, 100));

        JLabel logoIcon = new JLabel("🎓");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("LPU");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoSub = new JLabel("Lovely Professional University");
        logoSub.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        logoSub.setForeground(new Color(255, 255, 200));
        logoSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(logoText);
        logoPanel.add(logoSub);
        sidebar.add(logoPanel);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 165, 0));
        sep.setMaximumSize(new Dimension(180, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(12));

        // Nav Section Label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(new Color(100, 130, 180));
        navLabel.setBorder(new EmptyBorder(0, 20, 6, 0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navLabel);

        // Nav Buttons
        navButtons = new JButton[navNames.length];
        for (int i = 0; i < navNames.length; i++) {
            final int idx = i;
            JButton btn = createNavButton(navIcons[i], navNames[i]);
            navButtons[i] = btn;
            btn.addActionListener(e -> selectNav(idx));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        // Bottom: Logout
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(10, 12, 20, 12));
        bottomPanel.setMaximumSize(new Dimension(220, 90));

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(60, 90, 140));
        bottomPanel.add(sep2);
        bottomPanel.add(Box.createVerticalStrut(10));

        JLabel adminLabel = new JLabel("👤  Administrator");
        adminLabel.setFont(AppTheme.FONT_SMALL);
        adminLabel.setForeground(new Color(160, 185, 225));
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(adminLabel);
        bottomPanel.add(Box.createVerticalStrut(6));

        JButton logoutBtn = new JButton("⇦  Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutBtn.setForeground(new Color(200, 210, 235));
        logoutBtn.setOpaque(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);
        sidebar.add(bottomPanel);

        // CONTENT
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.BG_MAIN);

        dashboardPanel  = new DashboardPanel(this);
        studentPanel    = new StudentPanel(this);
        roomPanel       = new RoomPanel(this);
        allocationPanel = new AllocationPanel(this);
        historyPanel    = new HistoryPanel();

        contentPanel.add(dashboardPanel,  "Dashboard");
        contentPanel.add(studentPanel,    "Students");
        contentPanel.add(roomPanel,       "Rooms");
        contentPanel.add(allocationPanel, "Allocate Room");
        contentPanel.add(historyPanel,    "History");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        selectNav(0);
    }

    private JButton createNavButton(String icon, String text) {
        JButton btn = new JButton(icon + "   " + text) {
            boolean selected = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // White background
                g2.setColor(Color.WHITE);
                g2.fillRect(8, 2, getWidth() - 16, getHeight() - 4);
                
                if (getClientProperty("selected") != null && (boolean) getClientProperty("selected")) {
                    // Orange highlight for selected
                    g2.setColor(AppTheme.PRIMARY);
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    g2.setColor(AppTheme.ACCENT);
                    g2.fillRoundRect(0, 6, 4, getHeight() - 12, 4, 4);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 240, 200));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                }
                g2.setFont(getFont());
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 28, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setFont(AppTheme.FONT_NAV);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setMinimumSize(new Dimension(220, 44));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    public void selectNav(int idx) {
        for (int i = 0; i < navButtons.length; i++) {
            navButtons[i].putClientProperty("selected", i == idx);
            navButtons[i].repaint();
        }
        cardLayout.show(contentPanel, navNames[idx]);
        if (idx == 0) dashboardPanel.refresh();
        if (idx == 1) studentPanel.refresh();
        if (idx == 2) roomPanel.refresh();
        if (idx == 3) allocationPanel.refresh();
        if (idx == 4) historyPanel.refresh();
    }

    public void navigateTo(int idx) { selectNav(idx); }
}
