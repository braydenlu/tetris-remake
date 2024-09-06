package model;

import lombok.NoArgsConstructor;
import model.panels.GamePanel;
import model.piece.Piece;

import java.awt.*;

import static model.MatrixOperations.iterateMatrix;

@NoArgsConstructor
public class Renderer {

    private final int tileSize = GamePanel.getTileSize();

    public void drawPiece(Graphics2D g2d, Piece piece) {
        if (piece.isPlaced()) {
            return;
        }
        iterateMatrix(piece, (rowNum, colNum) -> {
            Renderer.drawSquare(g2d, piece.getX() + colNum * tileSize, piece.getY() + rowNum * tileSize, tileSize, tileSize, piece.getPieceShape().getColor());
        });
    }

    public static void drawSquare(Graphics g, int x, int y, int width, int height, Color color) {
        int colourStep = 15;
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        Color lighter1 = new Color(Math.min(red + colourStep, 255),
                Math.min(green + colourStep, 255),
                Math.min(blue + colourStep, 255));
        Color lighter2 = new Color(Math.min(red + 2 * colourStep, 255),
                Math.min(green + 2 * colourStep, 255),
                Math.min(blue + 2 * colourStep, 255));
        Color lighter3 = new Color(Math.min(red + 3 * colourStep, 255),
                Math.min(green + 3 * colourStep, 255),
                Math.min(blue + 3 * colourStep, 255));

        int centerX = x + width / 2;
        int centerY = y + height / 2;

        // Design 1 - four triangles pointing at middle
//        Polygon triangle1 = new Polygon(new int[]{x, centerX, x + width}, new int[]{y, centerY, y}, 3);
//        Polygon triangle2 = new Polygon(new int[]{x + width, centerX, x + width}, new int[]{y, centerY, y + height}, 3);
//        Polygon triangle3 = new Polygon(new int[]{x, centerX, x + width}, new int[]{y + height, centerY, y + height}, 3);
//        Polygon triangle4 = new Polygon(new int[]{x, centerX, x}, new int[]{y, centerY, y + height}, 3);
//
//        g.setColor(lighter3);
//        g.fillPolygon(triangle1);
//        g.setColor(lighter1);
//        g.fillPolygon(triangle2);
//        g.setColor(color);
//        g.fillPolygon(triangle3);
//        g.setColor(lighter2);
//        g.fillPolygon(triangle4);

        // Design 2 - two triangles plus square in middle
        Polygon triangle1 = new Polygon(new int[]{x, x + width, x}, new int[]{y, y, y + height}, 3);
        Polygon triangle2 = new Polygon(new int[]{x + width, x + width, x}, new int[]{y, y + height, y  + height}, 3);

        g.setColor(lighter2);
        g.fillPolygon(triangle1);
        g.setColor(color);
        g.fillPolygon(triangle2);
        g.setColor(lighter1);
        g.fillRect(x + width / 4, y + height / 4, GamePanel.getTileSize() / 2, GamePanel.getTileSize() / 2);

        // Design 3 - two triangles
//        Polygon triangle1 = new Polygon(new int[]{x, x + width, x}, new int[]{y, y, y + height}, 3);
//        Polygon triangle2 = new Polygon(new int[]{x + width, x + width, x}, new int[]{y, y + height, y  + height}, 3);
//
//        g.setColor(lighter2);
//        g.fillPolygon(triangle1);
//        g.setColor(color);
//        g.fillPolygon(triangle2);

        // Design 4 - two triangles plus a square in the bottom right quadrant (made by accident)
//        Polygon triangle1 = new Polygon(new int[]{x, x + width, x}, new int[]{y, y, y + height}, 3);
//        Polygon triangle2 = new Polygon(new int[]{x + width, x + width, x}, new int[]{y, y + height, y  + height}, 3);
//
//        g.setColor(lighter2);
//        g.fillPolygon(triangle1);
//        g.setColor(color);
//        g.fillPolygon(triangle2);
//        g.setColor(lighter1);
//        g.fillRect(x + width / 2, y + height / 2, tileSize / 2, tileSize / 2);

        // Design 5 - shaded checkerboard
//        Polygon triangle1 = new Polygon(new int[]{x, x + width, x}, new int[]{y, y, y + height}, 3);
//        Polygon triangle2 = new Polygon(new int[]{x + width, x + width, x}, new int[]{y, y + height, y  + height}, 3);
//
//        g.setColor(lighter2);
//        g.fillPolygon(triangle1);
//        g.setColor(lighter3);
//        g.fillRect(x, y, tileSize / 2, tileSize / 2);
//
//        g.setColor(color);
//        g.fillPolygon(triangle2);
//        g.setColor(lighter1);
//        g.fillRect(x + width / 2, y + height / 2, tileSize / 2, tileSize / 2);

        // Design 6 - Overlaid squares but with the first inner square missing the top half
//        g.setColor(color);
//        g.fillRect(x, y, tileSize, tileSize);
//        g.setColor(lighter1);
//        g.fillRect(x + tileSize / 8, y + tileSize / 8, tileSize * 3/4, tileSize * 3/8);
//        g.setColor(lighter2);
//        g.fillRect(x + tileSize / 4, y + tileSize / 4, tileSize / 2, tileSize / 2);
//        g.setColor(lighter3);
//        g.fillRect(x + tileSize * 3/8, y + tileSize * 3/8, tileSize / 4, tileSize / 4);

        // Border
//        g.setColor(Color.black);
//        g.drawRect(x, y, width, height);
    }
}
