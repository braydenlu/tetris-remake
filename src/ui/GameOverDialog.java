package ui;

import model.panels.TetrisGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameOverDialog extends JDialog {

    private JPanel gameOverDialogPanel;
    private JLabel gameOverLabel;
    private JButton restartButton;
    private JButton exitButton;

    private TetrisGame tetrisGame;

    public GameOverDialog(Window owner, TetrisGame tetrisGame) {
        super(owner, "Game Over", ModalityType.APPLICATION_MODAL);
        this.tetrisGame = tetrisGame;

        gameOverDialogPanel = new JPanel();
        gameOverDialogPanel.setLayout(new BoxLayout(gameOverDialogPanel, BoxLayout.Y_AXIS));
        gameOverDialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        gameOverLabel = new JLabel("Game Over");
        restartButton = new TetrisButton("Restart");
        exitButton = new TetrisButton("Exit");

        restartButton.addActionListener(e -> {
            this.tetrisGame.notify("restart");
            this.dispose();
        });
        exitButton.addActionListener(e -> {
            this.tetrisGame.notify("exit");
            this.dispose();
        });

        gameOverDialogPanel.add(gameOverLabel);
        gameOverDialogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gameOverDialogPanel.add(restartButton);
        gameOverDialogPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameOverDialogPanel.add(exitButton);

        for (Component component : gameOverDialogPanel.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        getContentPane().add(gameOverDialogPanel);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(false);
    }
}
