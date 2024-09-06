package ui;

import model.panels.TetrisGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PauseDialog extends JDialog {

    private JPanel pauseDialogPanel;
    private JLabel pauseLabel;
    private JButton resumeButton;
    private JButton restartButton;
    private JButton exitButton;

    private TetrisGame tetrisGame;

    public PauseDialog(Window owner, TetrisGame tetrisGame) {
        super(owner, "Pause Menu", ModalityType.APPLICATION_MODAL);
        this.tetrisGame = tetrisGame;

        pauseDialogPanel = new JPanel();
        pauseDialogPanel.setLayout(new BoxLayout(pauseDialogPanel, BoxLayout.Y_AXIS));
        pauseDialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        pauseLabel = new JLabel("Paused");
        resumeButton = new TetrisButton("Resume");
        restartButton = new TetrisButton("Restart");
        exitButton = new TetrisButton("Exit");

        resumeButton.addActionListener(e -> this.dispose());
        restartButton.addActionListener(e -> {
            this.tetrisGame.notify("restart");
            this.dispose();
        });
        exitButton.addActionListener(e -> {
            this.tetrisGame.notify("exit");
            this.dispose();
        });

        pauseDialogPanel.add(pauseLabel);
        pauseDialogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        pauseDialogPanel.add(resumeButton);
        pauseDialogPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pauseDialogPanel.add(restartButton);
        pauseDialogPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pauseDialogPanel.add(exitButton);

        for (Component component : pauseDialogPanel.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        getContentPane().add(pauseDialogPanel);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(false);
    }
}
