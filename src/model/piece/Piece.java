package model.piece;

import lombok.Getter;
import lombok.Setter;
import model.panels.GamePanel;

import java.util.ArrayList;

import static model.MatrixOperations.copyGrid;
import static model.MatrixOperations.copyRotations;

@Getter
public class Piece {

    // Objects in this class
    private final PieceShape pieceShape;

    // Piece properties
    @Setter private int rotation;
    @Setter private boolean placed;
    @Setter private int x;
    @Setter private int y;
    private ArrayList<ArrayList<Integer>> grid;
    private ArrayList<ArrayList<ArrayList<Integer>>> rotations;
    private int gridSize;
    private final int height;
    private final int width;

    // EFFECTS: Constructs a piece with a GamePanel, KeyHandler, and PieceShape
    public Piece(PieceShape pieceShape) {
        this.pieceShape = pieceShape;
        height = pieceShape.getHeight();
        width = pieceShape.getWidth();
        setDefaultValues();
    }

    // MODIFIES: This
    // EFFECTS: Initializes the default values of a piece
    public void setDefaultValues() {
        grid = copyGrid(pieceShape, 0);
        rotations = copyRotations(pieceShape);
        rotation = 0;

        gridSize = grid.size();
        if (gridSize == 2) {
            x = 4 * GamePanel.getTileSize();
        } else {
            x = 3 * GamePanel.getTileSize();
        }

        y = 0;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        this.grid = rotations.get(rotation);
    }
}
