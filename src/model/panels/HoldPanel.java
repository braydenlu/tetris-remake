package model.panels;

import lombok.Getter;
import model.piece.Piece;
import model.piece.PieceShape;
import ui.panels.TetrisPanel;

import javax.swing.*;
import java.awt.*;

@Getter
public class HoldPanel extends TetrisPanel {

    private final TetrisGame tetrisGame;
    private Piece holdPiece;

    public HoldPanel(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, borderWidth));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();

        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, holdWindowWidth, tileSize);

        RenderingHints savedHints = graphics2D.getRenderingHints();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(Color.black);
//        graphics2D.setFont(font);
        graphics2D.drawString("HOLD", tileSize / 4, tileSize * 4 / 5);
        graphics2D.setRenderingHints(savedHints);

        if (holdPiece != null) {
            tetrisGame.getRenderer().drawPiece(graphics2D, holdPiece);
        }
    }

    public void setHoldPiece(Piece holdPiece) {
        holdPiece.setDefaultValues();
        this.holdPiece = holdPiece;
        holdPiece.setX((5 - holdPiece.getGridSize()) * tileSize / 2);
        if (holdPiece.getPieceShape() != PieceShape.I) {
            holdPiece.setY(tileSize * 2);
        } else {
            holdPiece.setY(tileSize * 3 / 2);
        }
    }
}
