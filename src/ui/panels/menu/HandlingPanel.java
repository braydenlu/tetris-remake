package ui.panels.menu;

import ui.MainFrame;
import ui.TetrisButton;
import ui.gamedata.Handling;
import ui.panels.TetrisPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HandlingPanel extends TetrisPanel {

    private Handling handling;
    private MainFrame mainFrame;

    private JLabel arrLabel;
    private JLabel dasLabel;
    private JLabel sdfLabel;

    private JPanel arrPanel;
    private JPanel dasPanel;
    private JPanel sdfPanel;

    private JSlider arrSlider;
    private JSlider dasSlider;
    private JSlider sdfSlider;

    private JSpinner arrSpinner;
    private SpinnerNumberModel arrModel;
    private JSpinner dasSpinner;
    private SpinnerNumberModel dasModel;
    private JSpinner sdfSpinner;
    private SpinnerNumberModel sdfModel;

    private JButton saveButton;
    private JButton backButton;

    private final int maximum = 1000;

    public HandlingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        this.handling = mainFrame.getHandling();
        initializeComponents();
        addComponents();
    }

    private void initializeComponents() {
        initializeSlidersAndSpinners();
        initializeLabels();
        initializePanels();
    }

    private void initializeSlidersAndSpinners() {
        arrSlider = new JSlider(JSlider.HORIZONTAL, 0, maximum, handling.autoRepeatRate());
        dasSlider = new JSlider(JSlider.HORIZONTAL, 0,  maximum, handling.delayedAutoShift());
        sdfSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, handling.softDropFactor());

        arrModel = new SpinnerNumberModel(handling.autoRepeatRate(), 0, maximum, 1);
        dasModel = new SpinnerNumberModel(handling.delayedAutoShift(), 0, maximum, 1);
        sdfModel = new SpinnerNumberModel(handling.softDropFactor(), 0, 100, 1);

        arrSpinner = new JSpinner(arrModel);
        dasSpinner = new JSpinner(dasModel);
        sdfSpinner = new JSpinner(sdfModel);

        JSpinner[] jSpinners = {arrSpinner, dasSpinner, sdfSpinner};
        JSlider[] jSliders = {arrSlider, dasSlider, sdfSlider};

        for (int i = 0; i < 3; i++) {
            JSpinner jSpinner = jSpinners[i];
            JSlider jSlider = jSliders[i];

            jSlider.setBorder(new EmptyBorder(10, 10, 10, 10));

            jSpinner.addChangeListener(e -> jSlider.setValue((int) jSpinner.getValue()));
            jSlider.addChangeListener(e -> jSpinner.setValue(jSlider.getValue()));

            jSlider.setPaintTicks(true);
            jSlider.setPaintLabels(true);
            if (jSlider.getMaximum() > 100) {
                jSlider.setLabelTable(jSlider.createStandardLabels(100));
                jSlider.setMinorTickSpacing(20);
                jSlider.setMajorTickSpacing(100);
            } else {
                jSlider.setLabelTable(jSlider.createStandardLabels(10));
                jSlider.setMinorTickSpacing(2);
                jSlider.setMajorTickSpacing(10);
            }
        }
    }

    private void initializeLabels() {
        arrLabel = new JLabel("Auto Repeat Rate (ms)");
        dasLabel = new JLabel("Delayed Auto Shift (ms)");
        sdfLabel = new JLabel("Soft Drop Factor");

        for (JLabel jLabel : new JLabel[]{arrLabel, dasLabel, sdfLabel}) {
            jLabel.setBorder(new EmptyBorder(30, 10, 10, 10));
            jLabel.setForeground(Color.WHITE);
        }
    }

    private void initializePanels() {
        arrPanel = new JPanel();
        dasPanel = new JPanel();
        sdfPanel = new JPanel();

        for (JPanel jPanel : new JPanel[]{arrPanel, dasPanel, sdfPanel}) {
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        }
    }

    private void addComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JComponent[] components = {arrLabel, arrSlider, arrSpinner, dasLabel, dasSlider, dasSpinner, sdfLabel, sdfSlider, sdfSpinner};
        int i = 0;

        for (JPanel jPanel : new JPanel[]{arrPanel, dasPanel, sdfPanel}) {
            add(components[i]);
            i++;
            jPanel.add(components[i]);
            i++;
            jPanel.add(components[i]);
            i++;
            add(jPanel);
        }
        setupButtons();
    }

    private void setupButtons() {
        saveButton = new TetrisButton("Save");
        saveButton.addActionListener(e -> {
            handling = new Handling((int) arrModel.getValue(), (int) dasModel.getValue(), (int) sdfModel.getValue());
            mainFrame.setHandling(this.handling);
        });
        backButton = new TetrisButton("Back");
        backButton.addActionListener(e -> notifyObservers("back"));

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 2));
        jPanel.add(backButton);
        jPanel.add(saveButton);
        add(jPanel);
    }
}
