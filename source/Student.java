import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class RoomPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterBox;

    private static final String[] COLUMNS = {
        "Room No.", "Block", "Floor", "Type", "Capacity", "Occupied", "Available", "Gender", "Rent/Month", "Status"
    };

    public RoomPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(AppTheme.BG_MAIN);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(new EmptyBorder(28, 32, 16, 32));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Room Management");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("View and manage all hostel rooms");
        sub.setFont(AppTheme.FONT_SUBTITLE);
        sub.setForeground(AppTheme.TEXT_SECONDARY);
        titlePanel.add(title);
        titlePanel.add(sub);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        JButton addBtn  = makeBtn("➕  Add Room", AppTheme.SUCCESS);
        JButton maintBtn = makeBtn("🔧  Toggle Maintenance", AppTheme.WARNING);
        JButton delBtn  = makeBtn("🗑  Delete Room", AppTheme.DANGER);

        addBtn.addActionListener(e -> showRoomDialog());
        maintBtn.addActionListener(e -> toggleMaintenance());
        delBtn.addActionListener(e -> deleteRoom());

        btnPanel.add(delBtn); btnPanel.add(maintBtn); btnPanel.add(addBtn);
        header.add(titlePanel, BorderLayout.WEST);
        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBar.setBackground(AppTheme.BG_MAIN);
        filterBar.setBorder(new EmptyBorder(0, 32, 10, 32));
        filterBar.add(new JLabel("Filter by:"));
        filterBox = new JComboBox<>(new String[]{"All", "Male", "Female", "Available", "Full", "Maintenance"});
        filterBox.setFont(AppTheme.FONT_BODY);
        filterBox.setPreferredSize(new Dimension(160, 34));
        filterBox.addActionListener(e -> refresh());
        filterBar.add(filterBox);
        add(filterBar, BorderLayout.AFTER_LAST_LINE);

        // Table
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

        refresh();
    }

    private void styleTable(JTable t) {
        t.setFont(AppTheme.FONT_TABLE_BODY);
        t.setRowHeight(34);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(220, 232, 255));
        t.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        t.setFocusable(false);

        JTableHeader header = t.getTableHeader();
        header.setFont(AppTheme.FONT_TABLE_HEAD);
        header.setBackground(AppTheme.BG_TABLE_HEADER);
        header.setForeground(AppTheme.TEXT_TABLE_HEAD);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 38));

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                    boolean focused, int row, int col) {
                super.getTableCellRendererComponent(table, value, selected, focused, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 8));
                if (!selected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : AppTheme.BG_TABLE_ALT);
                    if (col == 9) { // Status column
                        String status = String.valueOf(value);
                        setForeground("Available".equals(status) ? AppTheme.SUCCESS :
                                      "Full".equals(status) ? AppTheme.DANGER : AppTheme.WARNING);
                        setFont(AppTheme.FONT_TABLE_HEAD);
                    } else {
                        setForeground(AppTheme.TEXT_PRIMARY);
                        setFont(AppTheme.FONT_TABLE_BODY);
                    }
                }
                return this;
            }
        });
        int[] widths = {80, 90, 100, 80, 75, 75, 80, 75, 100, 100};
        for (int i = 0; i < widths.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    public void refresh() {
        String filter = filterBox != null ? (String) filterBox.getSelectedItem() : "All";
        tableModel.setRowCount(0);
        for (Room r : DataManager.getInstance().getRooms()) {
            boolean show = "All".equals(filter)
                || r.getGender().equals(filter)
                || r.getStatus().equals(filter);
            if (show) {
                tableModel.addRow(new Object[]{
                    r.getRoomNumber(), r.getBlock(), r.getFloor(), r.getType(),
                    r.getCapacity(), r.getOccupied(), r.getAvailableBeds(),
                    r.getGender(), "₹" + (int)r.getRentPerMonth(), r.getStatus()
                });
            }
        }
    }

    private Room getSelectedRoom() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a room first.", "No Selection", JOptionPane.WARNING_MESSAGE); return null; }
        String rNo = (String) tableModel.getValueAt(row, 0);
        return DataManager.getInstance().getRoomByNumber(rNo);
    }

    private void toggleMaintenance() {
        Room r = getSelectedRoom();
        if (r == null) return;
        if (r.getOccupied() > 0) {
            JOptionPane.showMessageDialog(this, "Cannot set room to maintenance while occupied.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ("Maintenance".equals(r.getStatus())) r.setStatus("Available");
        else r.setStatus("Maintenance");
        refresh();
    }

    private void deleteRoom() {
        Room r = getSelectedRoom();
        if (r == null) return;
        if (r.getOccupied() > 0) {
            JOptionPane.showMessageDialog(this, "Cannot delete room with occupants.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int res = JOptionPane.showConfirmDialog(this, "Delete room " + r.getRoomNumber() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            DataManager.getInstance().deleteRoom(r.getRoomNumber());
            refresh();
        }
    }

    private void showRoomDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Room", true);
        dialog.setSize(420, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 32, 10, 32));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel dlgTitle = new JLabel("Add New Room");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dlgTitle.setForeground(AppTheme.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.insets = new Insets(0,0,16,0);
        panel.add(dlgTitle, gbc);

        String[] fldLbls = {"Room Number", "Floor", "Block"};
        JTextField[] flds = new JTextField[fldLbls.length];
        for (int i = 0; i < fldLbls.length; i++) {
            addLabel(panel, fldLbls[i], 0, 1 + i*2, gbc);
            flds[i] = new JTextField();
            flds[i].setFont(AppTheme.FONT_BODY);
            flds[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER), BorderFactory.createEmptyBorder(6,10,6,10)));
            gbc.gridy = 2 + i*2; gbc.insets = new Insets(0,0,6,0);
            panel.add(flds[i], gbc);
        }

        addLabel(panel, "Room Type", 0, 7, gbc);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Single","Double","Triple"});
        typeBox.setFont(AppTheme.FONT_BODY);
        gbc.gridy = 8; panel.add(typeBox, gbc);

        addLabel(panel, "Gender", 0, 9, gbc);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male","Female"});
        genderBox.setFont(AppTheme.FONT_BODY);
        gbc.gridy = 10; panel.add(genderBox, gbc);

        addLabel(panel, "Rent per Month (₹)", 0, 11, gbc);
        JTextField rentField = new JTextField();
        rentField.setFont(AppTheme.FONT_BODY);
        rentField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER), BorderFactory.createEmptyBorder(6,10,6,10)));
        gbc.gridy = 12; panel.add(rentField, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setBorder(new EmptyBorder(10, 24, 16, 24));
        JButton cancel = makeBtn("Cancel", AppTheme.GRAY);
        cancel.setPreferredSize(new Dimension(100, 36));
        JButton save = makeBtn("Add Room", AppTheme.SUCCESS);
        save.setPreferredSize(new Dimension(120, 36));
        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            try {
                String roomNo = flds[0].getText().trim();
                String floor  = flds[1].getText().trim();
                String block  = flds[2].getText().trim();
                String type   = (String) typeBox.getSelectedItem();
                String gender = (String) genderBox.getSelectedItem();
                double rent   = Double.parseDouble(rentField.getText().trim());
                int cap = type.equals("Single") ? 1 : type.equals("Double") ? 2 : 3;

                if (roomNo.isEmpty() || floor.isEmpty() || block.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Room r = new Room(roomNo, type, cap, floor, block, gender, rent);
                if (!DataManager.getInstance().addRoom(r)) {
                    JOptionPane.showMessageDialog(dialog, "Room number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                refresh(); dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter valid rent amount.", "Validation", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRow.add(cancel); btnRow.add(save);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addLabel(JPanel panel, String text, int x, int y, GridBagConstraints gbc) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_HEADER);
        l.setForeground(AppTheme.TEXT_SECONDARY);
        gbc.gridx = x; gbc.gridy = y; gbc.insets = new Insets(8, 0, 2, 0);
        panel.add(l, gbc);
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
        btn.setPreferredSize(new Dimension(160, 38));
        return btn;
    }
}
