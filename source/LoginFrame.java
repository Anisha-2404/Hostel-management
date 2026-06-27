import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"Student ID", "Student Name", "Room Number", "Date", "Action"};

    public HistoryPanel() {
        setBackground(AppTheme.BG_MAIN);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(new EmptyBorder(28, 32, 16, 32));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Allocation History");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("Complete log of all room allocations and vacations");
        sub.setFont(AppTheme.FONT_SUBTITLE);
        sub.setForeground(AppTheme.TEXT_SECONDARY);
        titlePanel.add(title);
        titlePanel.add(sub);

        JButton refreshBtn = makeBtn("⟳  Refresh", AppTheme.PRIMARY);
        refreshBtn.setPreferredSize(new Dimension(130, 38));
        refreshBtn.addActionListener(e -> refresh());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 32, 24, 32),
            BorderFactory.createLineBorder(AppTheme.BORDER)));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // Summary bar at bottom
        JPanel summary = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summary.setBackground(AppTheme.BG_MAIN);
        summary.setBorder(new EmptyBorder(0, 32, 10, 32));
        JLabel totalLbl = new JLabel("Total records: 0");
        totalLbl.setFont(AppTheme.FONT_BODY);
        totalLbl.setForeground(AppTheme.TEXT_SECONDARY);
        JLabel allocLbl = new JLabel("✅ Allocated: 0");
        allocLbl.setFont(AppTheme.FONT_BODY);
        allocLbl.setForeground(AppTheme.SUCCESS);
        JLabel vacLbl = new JLabel("🔓 Vacated: 0");
        vacLbl.setFont(AppTheme.FONT_BODY);
        vacLbl.setForeground(AppTheme.DANGER);
        summary.add(totalLbl); summary.add(allocLbl); summary.add(vacLbl);

        // Store refs for refresh
        this.putClientProperty("totalLbl", totalLbl);
        this.putClientProperty("allocLbl", allocLbl);
        this.putClientProperty("vacLbl", vacLbl);

        add(summary, BorderLayout.SOUTH);

        refresh();
    }

    private void styleTable(JTable t) {
        t.setFont(AppTheme.FONT_TABLE_BODY);
        t.setRowHeight(36);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(220, 232, 255));
        t.setFocusable(false);

        JTableHeader header = t.getTableHeader();
        header.setFont(AppTheme.FONT_TABLE_HEAD);
        header.setBackground(AppTheme.BG_TABLE_HEADER);
        header.setForeground(AppTheme.TEXT_TABLE_HEAD);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 40));

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                    boolean focused, int row, int col) {
                super.getTableCellRendererComponent(table, value, selected, focused, row, col);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                if (!selected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : AppTheme.BG_TABLE_ALT);
                    if (col == 4) {
                        String action = String.valueOf(value);
                        boolean alloc = "Allocated".equals(action);
                        setForeground(alloc ? AppTheme.SUCCESS : AppTheme.DANGER);
                        setFont(AppTheme.FONT_TABLE_HEAD);
                        setText((alloc ? "✅  " : "🔓  ") + action);
                    } else {
                        setForeground(AppTheme.TEXT_PRIMARY);
                        setFont(AppTheme.FONT_TABLE_BODY);
                    }
                }
                return this;
            }
        });

        int[] widths = {100, 180, 120, 130, 140};
        for (int i = 0; i < widths.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<String[]> history = DataManager.getInstance().getAllocationHistory();
        int allocated = 0, vacated = 0;

        // Show newest first
        for (int i = history.size() - 1; i >= 0; i--) {
            String[] rec = history.get(i);
            Student s = DataManager.getInstance().getStudentById(rec[0]);
            String sName = (s != null) ? s.getName() : rec[0];
            tableModel.addRow(new Object[]{rec[0], sName, rec[1], rec[2], rec[3]});
            if ("Allocated".equals(rec[3])) allocated++;
            else vacated++;
        }

        JLabel tl = (JLabel) getClientProperty("totalLbl");
        JLabel al = (JLabel) getClientProperty("allocLbl");
        JLabel vl = (JLabel) getClientProperty("vacLbl");
        if (tl != null) tl.setText("Total records: " + history.size());
        if (al != null) al.setText("✅ Allocated: " + allocated);
        if (vl != null) vl.setText("🔓 Vacated: " + vacated);
    }

    private JButton makeBtn(String text, Color color) {
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
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        btn.setFont(AppTheme.FONT_BUTTON);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
