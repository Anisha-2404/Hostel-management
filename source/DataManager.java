import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel[] cardValues;
    private JPanel recentTable;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(AppTheme.BG_MAIN);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(new EmptyBorder(28, 32, 16, 32));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel sub = new JLabel("Hostel Room Allocation — Admin Panel");
        sub.setFont(AppTheme.FONT_SUBTITLE);
        sub.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(sub);
        header.add(titlePanel, BorderLayout.WEST);

        JButton refreshBtn = makeButton("⟳  Refresh", AppTheme.PRIMARY);
        refreshBtn.addActionListener(e -> refresh());
        header.add(refreshBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Scrollable center
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(AppTheme.BG_MAIN);
        center.setBorder(new EmptyBorder(0, 28, 20, 28));

        // Stats Cards Row 1
        JPanel cardsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsRow.setOpaque(false);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        cardsRow.setPreferredSize(new Dimension(0, 130));

        String[] labels = {"Total Students", "Allocated", "Pending", "Total Rooms"};
        String[] icons  = {"👤", "✅", "⏳", "🚪"};
        Color[] colors  = {AppTheme.PRIMARY, AppTheme.SUCCESS, AppTheme.WARNING, AppTheme.INFO};
        cardValues = new JLabel[7];  // 4 cards in row 1 + 3 cards in row 2

        for (int i = 0; i < labels.length; i++) {
            final int fi = i;
            JPanel card = createStatCard(labels[i], icons[i], colors[i], i);
            cardsRow.add(card);
        }
        center.add(cardsRow);
        center.add(Box.createVerticalStrut(16));

        // Stats Cards Row 2
        JPanel cardsRow2 = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsRow2.setOpaque(false);
        cardsRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cardsRow2.setPreferredSize(new Dimension(0, 120));

        String[] labels2 = {"Available Rooms", "Full Rooms", "Under Maintenance"};
        String[] icons2  = {"🟢", "🔴", "🔧"};
        Color[] colors2  = {AppTheme.SUCCESS, AppTheme.DANGER, AppTheme.GRAY};
        for (int i = 0; i < labels2.length; i++) {
            JPanel card = createStatCard2(labels2[i], icons2[i], colors2[i], i + labels.length);
            cardsRow2.add(card);
        }
        center.add(cardsRow2);
        center.add(Box.createVerticalStrut(20));

        // Quick Actions
        JLabel qaLabel = new JLabel("Quick Actions");
        qaLabel.setFont(AppTheme.FONT_HEADER);
        qaLabel.setForeground(AppTheme.TEXT_PRIMARY);
        qaLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        qaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(qaLabel);

        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsRow.setOpaque(false);
        actionsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        actionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[][] actions = {
            {"➕  Add Student", "1"},
            {"🔑  Allocate Room", "3"},
            {"🚪  Manage Rooms", "2"},
            {"📋  View History", "4"}
        };
        for (String[] a : actions) {
            JButton btn = makeButton(a[0], AppTheme.PRIMARY);
            final int navIdx = Integer.parseInt(a[1]);
            btn.addActionListener(e -> mainFrame.navigateTo(navIdx));
            actionsRow.add(btn);
        }
        center.add(actionsRow);
        center.add(Box.createVerticalStrut(20));

        // Recent Allocations
        JLabel recLabel = new JLabel("Recent Activity");
        recLabel.setFont(AppTheme.FONT_HEADER);
        recLabel.setForeground(AppTheme.TEXT_PRIMARY);
        recLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        recLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(recLabel);

        recentTable = new JPanel();
        recentTable.setLayout(new BoxLayout(recentTable, BoxLayout.Y_AXIS));
        recentTable.setBackground(AppTheme.BG_CARD);
        recentTable.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1, true));
        recentTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(recentTable);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private JPanel createStatCard(String label, String icon, Color color, int valueIdx) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 6, getHeight(), 6, 6);
                g2.fillRect(0, 0, 3, getHeight());
            }
        };
        card.setLayout(new BorderLayout(12, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(18, 22, 18, 18));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel val = new JLabel("0");
        val.setFont(AppTheme.FONT_CARD_VALUE);
        val.setForeground(color);
        cardValues[valueIdx] = val;

        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.FONT_CARD_LABEL);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);

        textPanel.add(val);
        textPanel.add(lbl);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        // Drop shadow effect via border
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            new EmptyBorder(18, 22, 18, 18)));

        return card;
    }

    private JPanel createStatCard2(String label, String icon, Color color, int valueIdx) {
        return createStatCard(label, icon, color, valueIdx);  // valueIdx is already 4, 5, 6
    }

    // Override - need separate storage for row2 values
    // Simple fix: just make all 7 values tracked globally
    JLabel[] allCardValues = new JLabel[7];

    @Override
    public void addNotify() {
        super.addNotify();
    }

    public void refresh() {
        DataManager dm = DataManager.getInstance();
        if (cardValues != null && cardValues.length >= 7) {
            cardValues[0].setText(String.valueOf(dm.getTotalStudents()));
            cardValues[1].setText(String.valueOf(dm.getAllocatedStudents()));
            cardValues[2].setText(String.valueOf(dm.getPendingStudents()));
            cardValues[3].setText(String.valueOf(dm.getTotalRooms()));
            
            // Second row stats
            int totalRooms = dm.getTotalRooms();
            int allocatedRooms = dm.getAllocatedStudents(); // rooms with students
            int availableRooms = totalRooms - allocatedRooms;
            cardValues[4].setText(String.valueOf(availableRooms));
            cardValues[5].setText(String.valueOf(allocatedRooms));
            cardValues[6].setText("0"); // Under maintenance (hardcoded for now)
        }

        // Recent Activity
        if (recentTable != null) {
            recentTable.removeAll();

            // Header row
            JPanel hdr = new JPanel(new GridLayout(1, 4));
            hdr.setBackground(AppTheme.BG_TABLE_HEADER);
            hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            hdr.setMinimumSize(new Dimension(0, 36));
            String[] cols = {"Student ID", "Student Name", "Room", "Action"};
            for (String col : cols) {
                JLabel l = new JLabel(col);
                l.setFont(AppTheme.FONT_TABLE_HEAD);
                l.setForeground(AppTheme.TEXT_TABLE_HEAD);
                l.setBorder(new EmptyBorder(6, 16, 6, 8));
                hdr.add(l);
            }
            recentTable.add(hdr);

            java.util.List<String[]> history = dm.getAllocationHistory();
            int start = Math.max(0, history.size() - 8);
            boolean alt = false;
            for (int i = history.size() - 1; i >= start; i--) {
                String[] rec = history.get(i);
                Student s = dm.getStudentById(rec[0]);
                String sName = (s != null) ? s.getName() : rec[0];
                JPanel row = new JPanel(new GridLayout(1, 4));
                row.setBackground(alt ? AppTheme.BG_TABLE_ALT : Color.WHITE);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
                row.setMinimumSize(new Dimension(0, 34));
                String[] cells = {rec[0], sName, rec[1], rec[3]};
                Color actionColor = rec[3].equals("Allocated") ? AppTheme.SUCCESS : AppTheme.DANGER;
                for (int j = 0; j < cells.length; j++) {
                    JLabel l = new JLabel(cells[j]);
                    l.setFont(AppTheme.FONT_TABLE_BODY);
                    l.setForeground(j == 3 ? actionColor : AppTheme.TEXT_PRIMARY);
                    l.setBorder(new EmptyBorder(4, 16, 4, 8));
                    row.add(l);
                }
                recentTable.add(row);
                alt = !alt;
            }

            if (history.isEmpty()) {
                JLabel empty = new JLabel("No recent activity.");
                empty.setFont(AppTheme.FONT_BODY);
                empty.setForeground(AppTheme.TEXT_MUTED);
                empty.setBorder(new EmptyBorder(20, 20, 20, 20));
                recentTable.add(empty);
            }

            recentTable.revalidate();
            recentTable.repaint();
        }
        repaint();
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setFont(AppTheme.FONT_BUTTON);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        return btn;
    }
}
