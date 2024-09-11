package ui.panels.menu;

import ui.MainFrame;
import ui.TetrisButton;
import ui.gamedata.Control;
import ui.panels.TetrisPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class ControlsPanel extends TetrisPanel {

    MainFrame mainFrame;

    JLabel title;
    JTable controlsTable;
    JButton backButton;

    Map<Control, int[]> controls;

    public ControlsPanel(MainFrame mainFrame) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        this.mainFrame = mainFrame;
        controls = mainFrame.getControls();
        initializeComponents();
    }

    private void initializeComponents() {
        initializeTitle();
        initializeControlsTable();

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
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(20,20,20,20));
    }

    private void initializeControlsTable() {
        String[] columnNames = {"Action", "Primary Key", "Secondary Key"};
        Object[][] data = new Object[controls.size()][3];

        int row = 0;
        for (Control control : controls.keySet()) {
            int[] keyBinds = controls.get(control);
            data[row][0] = control;
            data[row][1] = KeyEvent.getKeyText(keyBinds[0]);
            data[row][2] = keyBinds[1] != -1 ? KeyEvent.getKeyText(keyBinds[1]) : "";
            row++;
        }

        controlsTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;  // Allow editing only in primary/secondary key columns
            }
        };

        controlsTable.setBackground(Color.BLACK);
        controlsTable.setForeground(Color.WHITE);

        controlsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int selectedRow = controlsTable.getSelectedRow();
                int selectedColumn = controlsTable.getSelectedColumn();

                if (selectedRow != -1 && (selectedColumn == 1 || selectedColumn == 2)) {
                    Object object = controlsTable.getValueAt(selectedRow, 0);
                    Control control;
                    if (object instanceof Control) {
                        control = (Control) object;
                    } else {
                        throw new RuntimeException("Expected value at row" + selectedRow + " to be of type Control, " +
                                "but found: " + object.getClass().getSimpleName());
                    }
                    setKeyBind(control, e.getKeyCode(), selectedColumn == 1);

                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        controlsTable.setValueAt("", selectedRow, selectedColumn);
                    } else {
                        controlsTable.setValueAt(KeyEvent.getKeyText(e.getKeyCode()), selectedRow, selectedColumn);
                    }

                    System.out.println(controls);
                    e.consume();
                }
            }
        });
    }

    private void setKeyBind(Control control, int keyEvent, boolean primary) {
        int[] binds = controls.get(control);
        if (keyEvent == KeyEvent.VK_BACK_SPACE) {
            binds[primary ? 0 : 1] = -1;
        } else {
            binds[primary ? 0 : 1] = keyEvent;
        }
    }
}
