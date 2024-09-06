package model.panels;

import model.piece.Piece;
import ui.panels.TetrisPanel;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends TetrisPanel {

    private final TetrisGame tetrisGame;

    public PreviewPanel(TetrisGame tetrisGame) {
        setPreferredSize(new Dimension(previewWindowWidth + borderWidth, 22 * tileSize));
        this.tetrisGame = tetrisGame;
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, borderWidth));
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, 5 * tileSize, tileSize);

        RenderingHints savedHints = graphics2D.getRenderingHints();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(Color.black);
//        graphics2D.setFont(font);
        graphics2D.drawString("NEXT", tileSize / 4,  tileSize * 4 / 5);
        graphics2D.setRenderingHints(savedHints);

        int i = tileSize * 2;
        for (Piece p: tetrisGame.getQueue()) {
            p.setX((5 - p.getGridSize()) * tileSize / 2);
            if (p.getGridSize() != 4) {
                p.setY(i);
                i += p.getHeight() + tileSize;
            } else {
                p.setY(i - tileSize / 2);
                i += p.getHeight() + tileSize * 2;
            }
            tetrisGame.getRenderer().drawPiece(graphics2D, p);
        }

        graphics2D.dispose();
    }
}
