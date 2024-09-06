package ui.panels.menu;

import ui.TetrisButton;
import ui.panels.TetrisPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuPanel extends TetrisPanel {

    JLabel title;

    JButton startButton;
    JButton scoresButton;
    JButton controlsButton;
    JButton handlingButton;
    JButton exitButton;

    List<JButton> buttons;

    public MenuPanel() {
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeTitle();
        initializeButtons();
        setPreferredSize(new Dimension(1280, 720));
    }

    private void initializeTitle() {
        title = new JLabel("Java Tetris");
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(title);
    }

    private void initializeButtons() {
        createButtons();
        alignButtons();
        addButtonListeners();
        addButtons();
    }

    private void createButtons() {
        startButton = new TetrisButton("Start");
        scoresButton = new TetrisButton("Scores");
        controlsButton = new TetrisButton("Controls");
        handlingButton = new TetrisButton("Handling");
        exitButton = new TetrisButton("Exit");
        buttons = List.of(startButton, scoresButton, controlsButton, handlingButton, exitButton);
    }

    private void alignButtons() {
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setAlignmentY(Component.CENTER_ALIGNMENT);
        }
    }

    private void addButtonListeners() {
        startButton.addActionListener(e -> {
            notifyObservers("start");
            System.out.println("starting");
        });

        scoresButton.addActionListener(e -> notifyObservers("scores"));

        controlsButton.addActionListener(e -> notifyObservers("controls"));

        handlingButton.addActionListener(e -> notifyObservers("handling"));

        exitButton.addActionListener(e -> notifyObservers("exit"));
    }

    private void addButtons() {
        for (JButton button : buttons) {
            add(Box.createVerticalGlue());
            add(button);
        }
        add(Box.createVerticalGlue());
    }
}
