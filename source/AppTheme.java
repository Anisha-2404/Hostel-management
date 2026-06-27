import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AllocationPanel extends JPanel {
    private MainFrame mainFrame;
    private JComboBox<Student> studentBox;
    private JComboBox<Room> roomBox;
    private JLabel studentInfoLabel;
    private JLabel roomInfoLabel;
    private JLabel resultLabel;
    private JPanel availableRoomsPanel;

    public AllocationPanel(MainFrame mainFrame) {
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
        JLabel title = new JLabel("Room Allocation");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("Assign rooms to students based on availability and gender");
        sub.setFont(AppTheme.FONT_SUBTITLE);
        sub.setForeground(AppTheme.TEXT_SECONDARY);
        JPanel tp = new JPanel(); tp.setOpaque(false);
        tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        tp.add(title); tp.add(sub);
        header.add(tp, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Main scroll area
        JPanel main = new JPanel();
        main.setBackground(AppTheme.BG_MAIN);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(0, 32, 24, 32));

        // Allocation Form Card
        JPanel card = createCard("🔑  Allocate a Room");
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            new EmptyBorder(24, 28, 28, 28)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Card title
        JLabel cardTitle = new JLabel("🔑  Allocate a Room");
        cardTitle.setFont(AppTheme.FONT_HEADER);
        cardTitle.setForeground(AppTheme.PRIMARY);
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=4; gbc.weightx=1.0;
        card.add(cardTitle, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(AppTheme.BORDER);
        gbc.gridy=1; card.add(sep, gbc);

        // Student selector
        JLabel slbl = new JLabel("Select Student (Pending Allocation):");
        slbl.setFont(AppTheme.FONT_HEADER);
        slbl.setForeground(AppTheme.TEXT_SECONDARY);
        gbc.gridy=2; gbc.gridwidth=2; gbc.weightx=0.5;
        card.add(slbl, gbc);

        JLabel rlbl = new JLabel("Select Available Room:");
        rlbl.setFont(AppTheme.FONT_HEADER);
        rlbl.setForeground(AppTheme.TEXT_SECONDARY);
        gbc.gridx=2;
        card.add(rlbl, gbc);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2;
        studentBox = new JComboBox<>();
        studentBox.setFont(AppTheme.FONT_BODY);
        studentBox.setPreferredSize(new Dimension(0, 38));
        studentBox.addActionListener(e -> onStudentSelected());
        card.add(studentBox, gbc);

        gbc.gridx=2;
        roomBox = new JComboBox<>();
        roomBox.setFont(AppTheme.FONT_BODY);
        roomBox.setPreferredSize(new Dimension(0, 38));
        roomBox.addActionListener(e -> onRoomSelected());
        card.add(roomBox, gbc);

        // Info labels
        studentInfoLabel = new JLabel(" ");
        studentInfoLabel.setFont(AppTheme.FONT_SMALL);
        studentInfoLabel.setForeground(AppTheme.TEXT_SECONDARY);
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2;
        card.add(studentInfoLabel, gbc);

        roomInfoLabel = new JLabel(" ");
        roomInfoLabel.setFont(AppTheme.FONT_SMALL);
        roomInfoLabel.setForeground(AppTheme.TEXT_SECONDARY);
        gbc.gridx=2;
        card.add(roomInfoLabel, gbc);

        // Allocate button
        JButton allocBtn = makeBtn("🔑  Allocate Room Now", AppTheme.SUCCESS);
        allocBtn.setPreferredSize(new Dimension(220, 44));
        allocBtn.addActionListener(e -> doAllocate());
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2; gbc.insets=new Insets(16,0,0,12);
        card.add(allocBtn, gbc);

        resultLabel = new JLabel(" ");
        resultLabel.setFont(AppTheme.FONT_BODY);
        gbc.gridx=2; gbc.insets=new Insets(16,12,0,0);
        card.add(resultLabel, gbc);

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(card);
        main.add(Box.createVerticalStrut(24));

        // Available Rooms Overview
        JLabel overviewLabel = new JLabel("Available Rooms Overview");
        overviewLabel.setFont(AppTheme.FONT_HEADER);
        overviewLabel.setForeground(AppTheme.TEXT_PRIMARY);
        overviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(overviewLabel);
        main.add(Box.createVerticalStrut(10));

        availableRoomsPanel = new JPanel(new GridLayout(0, 3, 12, 12));
        availableRoomsPanel.setOpaque(false);
        availableRoomsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(availableRoomsPanel);

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        // Populate student combo (pending only)
        studentBox.removeAllItems();
        for (Student s : DataManager.getInstance().getStudents()) {
            if (s.getStatus().equals("Pending")) {
                studentBox.addItem(s);
            }
        }
        studentInfoLabel.setText(studentBox.getItemCount() == 0 ? "No pending students." : " ");

        // Update available rooms card grid
        if (availableRoomsPanel != null) {
            availableRoomsPanel.removeAll();
            for (Room r : DataManager.getInstance().getRooms()) {
                if (r.isAvailable()) {
                    availableRoomsPanel.add(createRoomMiniCard(r));
                }
            }
            if (availableRoomsPanel.getComponentCount() == 0) {
                JLabel none = new JLabel("No rooms currently available.");
                none.setFont(AppTheme.FONT_BODY);
                none.setForeground(AppTheme.TEXT_MUTED);
                availableRoomsPanel.add(none);
            }
            availableRoomsPanel.revalidate();
            availableRoomsPanel.repaint();
        }

        onStudentSelected();
    }

    private void onStudentSelected() {
        Student s = (Student) studentBox.getSelectedItem();
        if (s == null) {
            studentInfoLabel.setText(" ");
            roomBox.removeAllItems();
            return;
        }
        studentInfoLabel.setText("  " + s.getGender() + " | " + s.getCourse() + " | Year " + s.getYear());

        // Fill matching rooms
        roomBox.removeAllItems();
        for (Room r : DataManager.getInstance().getAvailableRooms(s.getGender())) {
            roomBox.addItem(r);
        }
        roomInfoLabel.setText(roomBox.getItemCount() == 0 ? "No available rooms for this gender." : " ");
        onRoomSelected();
    }

    private void onRoomSelected() {
        Room r = (Room) roomBox.getSelectedItem();
        if (r == null) { roomInfoLabel.setText(" "); return; }
        roomInfoLabel.setText("  " + r.getBlock() + " | " + r.getFloor() + " | " + r.getAvailableBeds() + " beds free | ₹" + (int)r.getRentPerMonth() + "/mo");
    }

    private void doAllocate() {
        Student s = (Student) studentBox.getSelectedItem();
        Room r = (Room) roomBox.getSelectedItem();
        if (s == null || r == null) {
            showResult("Please select both student and room.", AppTheme.DANGER);
            return;
        }
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String result = DataManager.getInstance().allocateRoom(s.getStudentId(), r.getRoomNumber(), date);
        if ("SUCCESS".equals(result)) {
            showResult("✅  Room " + r.getRoomNumber() + " allocated to " + s.getName() + "!", AppTheme.SUCCESS);
            refresh();
        } else {
            showResult("❌  " + result, AppTheme.DANGER);
        }
    }

    private void showResult(String msg, Color color) {
        resultLabel.setText(msg);
        resultLabel.setForeground(color);
    }

    private JPanel createRoomMiniCard(Room r) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                Color accent = r.getGender().equals("Male") ? new Color(41,82,150) : new Color(180,60,120);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 6, 6, 6);
                g2.fillRect(0, 0, getWidth(), 3);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            new EmptyBorder(14, 16, 14, 16)));
        card.setOpaque(false);

        JLabel rn = new JLabel(r.getRoomNumber());
        rn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rn.setForeground(AppTheme.PRIMARY);

        JLabel type = new JLabel(r.getType() + "  •  " + r.getBlock());
        type.setFont(AppTheme.FONT_SMALL);
        type.setForeground(AppTheme.TEXT_SECONDARY);

        JLabel beds = new JLabel(r.getAvailableBeds() + "/" + r.getCapacity() + " beds free");
        beds.setFont(AppTheme.FONT_SMALL);
        beds.setForeground(AppTheme.SUCCESS);

        JLabel gender = new JLabel(r.getGender().equals("Male") ? "♂ Male" : "♀ Female");
        gender.setFont(AppTheme.FONT_SMALL);
        gender.setForeground(r.getGender().equals("Male") ? new Color(41,82,150) : new Color(180,60,120));

        JLabel rent = new JLabel("₹" + (int)r.getRentPerMonth() + "/month");
        rent.setFont(AppTheme.FONT_SMALL);
        rent.setForeground(AppTheme.TEXT_SECONDARY);

        card.add(rn);
        card.add(Box.createVerticalStrut(4));
        card.add(type);
        card.add(beds);
        card.add(gender);
        card.add(rent);

        return card;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        card.setOpaque(false);
        return card;
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
