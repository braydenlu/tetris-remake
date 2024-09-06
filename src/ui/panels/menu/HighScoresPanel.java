package ui.panels.menu;

import ui.TetrisButton;
import ui.panels.TetrisPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HighScoresPanel extends TetrisPanel {

    private HashMap<String, Integer> scores;
    private JPanel innerPanel;
    private JButton backButton;

    public HighScoresPanel(HashMap<String, Integer> scores) {
        setBackground(Color.BLACK);
        this.scores = scores;
        setLayout(new BorderLayout());
        createInnerPanel();
        createBackButton();
        add(innerPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private void createInnerPanel() {
        innerPanel = new JPanel();
        innerPanel.setBackground(Color.BLACK);
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 3;
        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        innerPanel.add(title, gridBagConstraints);

        gridBagConstraints.weighty = 0.25;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.1;
        addLabel("RANK", gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 1;
        addLabel("NAME", gridBagConstraints);

        gridBagConstraints.gridx = 2;
        addLabel("SCORE", gridBagConstraints);

        int row = 2;

        for (Map.Entry<String, Integer> pair : scores.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList()) {

            gridBagConstraints.weightx = 0.1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = row;
            addLabel(String.valueOf(row - 1), gridBagConstraints);

            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 1;
            addLabel(pair.getKey(), gridBagConstraints);

            gridBagConstraints.gridx = 2;
            addLabel(String.valueOf(pair.getValue()), gridBagConstraints);

            row++;
        }
    }

    private void createBackButton() {
        backButton = new TetrisButton("Back");
        backButton.addActionListener(e -> notifyObservers("back"));
    }

    private void addLabel(String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBorder(new LineBorder(Color.WHITE, 1));
        label.setForeground(Color.WHITE);
        innerPanel.add(label, gbc);
    }
}
