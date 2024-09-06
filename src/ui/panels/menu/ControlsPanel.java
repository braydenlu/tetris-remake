package ui.panels.menu;

import ui.MainFrame;
import ui.TetrisButton;
import ui.gamedata.Controls;
import ui.panels.TetrisPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ControlsPanel extends TetrisPanel {

    MainFrame mainFrame;

    JLabel title;
    JTable controlsTable;
    JScrollPane scrollPane;
    JButton backButton;

    Controls primaryControls;
    Controls secondaryControls;

    public ControlsPanel(MainFrame mainFrame) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        this.mainFrame = mainFrame;
        primaryControls = mainFrame.getPrimaryControls();
        secondaryControls = mainFrame.getSecondaryControls();

        initializeComponents();
    }

    private void initializeComponents() {
        ArrayList<JComponent> components = new ArrayList<>();
        initializeTitle();
        initializeTable();

        components.add(title);
        components.add(scrollPane);

        for (JComponent c : components) {
            c.setAlignmentX(CENTER_ALIGNMENT);
            c.setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        backButton = new TetrisButton("Back");
        backButton.addActionListener(e -> notifyObservers("back"));

        add(title, BorderLayout.NORTH);
        add(controlsTable, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private void initializeTitle() {
        title = new JLabel("Controls");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.setForeground(Color.WHITE);
    }

    private void initializeTable() {
        String[] columnTitles = {"Action", "Primary", "Secondary"};
        String[] controlNames = {"Move Piece Left", "Move Piece Right", "Soft Drop", "Hard Drop", "Rotate Clockwise",
                "Rotate Counterclockwise", "Rotate 180", "Swap Hold Piece", "Pause"};

        Object[] primaryControlArray = getFieldsAsArray(primaryControls);
        Object[] secondaryControlArray = getFieldsAsArray(secondaryControls);

        Object[][] controlTableData = new Object[primaryControlArray.length][3];

        for (int i = 0; i < primaryControlArray.length; i++) {
            controlTableData[i] = new Object[] {controlNames[i], primaryControlArray[i], secondaryControlArray[i]};
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                return this;
            }
        };

        controlsTable = new JTable(controlTableData, columnTitles);
        controlsTable.setRowHeight(60);
        controlsTable.setDefaultRenderer(controlsTable.getColumnClass(1), renderer);

        scrollPane = new JScrollPane(controlsTable);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private Object[] getFieldsAsArray(Object record) {
        if (!record.getClass().isRecord()) {
            throw new IllegalArgumentException("Not record");
        }

        Field[] fields = record.getClass().getDeclaredFields();
        Object[] fieldValues = new Object[fields.length];

        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fieldValues[i] = fields[i].get(record);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fieldValues;
    }
}
