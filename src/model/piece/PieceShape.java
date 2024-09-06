package model.piece;

import lombok.Getter;
import model.panels.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static model.MatrixOperations.rotateClockwise;
import static model.MatrixOperations.rotateCounterClockwise;

public enum PieceShape {

    T(new Color(99, 106, 239), 3, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(0,1,0),
                    rowConstructor(1,1,1),
                    rowConstructor(0,0,0)))),
    J(new Color(49,125,187), 3, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(1,0,0),
                    rowConstructor(1,1,1),
                    rowConstructor(0,0,0)))),
    L(new Color(250,149,41), 3, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(0,0,1),
                    rowConstructor(1,1,1),
                    rowConstructor(0,0,0)))),
    S(new Color(100,171,31), 3, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(0,1,1),
                    rowConstructor(1,1,0),
                    rowConstructor(0,0,0)))),
    Z(new Color(255,86,57), 3, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(1,1,0),
                    rowConstructor(0,1,1),
                    rowConstructor(0,0,0)))),
    I(new Color(96,181,188), 4, 1,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(0,0,0,0),
                    rowConstructor(1,1,1,1),
                    rowConstructor(0,0,0,0),
                    rowConstructor(0,0,0,0)))),
    O(new Color(255,203,31), 2, 2,
            new ArrayList<>(Arrays.asList(
                    rowConstructor(1,1),
                    rowConstructor(1,1))));


    @Getter
    private final ArrayList<ArrayList<ArrayList<Integer>>> rotations;
    @Getter
    private final Color color;
    @Getter
    private final int width;
    @Getter
    private final int height;

    PieceShape(Color color, int width, int height, ArrayList<ArrayList<Integer>> grid) {
        this.color = color;
        this.width = width * GamePanel.getTileSize();
        this.height = height * GamePanel.getTileSize();
        this.rotations = new ArrayList<>();
        rotations.add(grid);
        rotations.add(rotateClockwise(grid));
        rotations.add(rotateClockwise(rotations.get(1)));
        rotations.add(rotateCounterClockwise(grid));
    }

    private static ArrayList<Integer> rowConstructor(Integer ... args) {
        return new ArrayList<>(Arrays.asList(args));
    }
}

