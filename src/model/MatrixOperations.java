package model;

import model.piece.Piece;
import model.piece.PieceShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiConsumer;

public final class MatrixOperations {

    private MatrixOperations() {

    }

    // Performs an action on each square of a grid
    // Accepts a piece and action that uses rowNum and colNum
    public static void iterateMatrix(Piece piece, BiConsumer<Integer, Integer> action) {
        int rowNum = 0;
        int colNum;
        for (ArrayList<Integer> row : piece.getGrid()) {
            int i = 0;
            for (Integer filled : row) {
                if (filled == 1) {
                    colNum = i % 4;
                    action.accept(rowNum, colNum);
                }
                i++;
            }
            rowNum++;
        }
    }

    public static ArrayList<ArrayList<Integer>> copyGrid(PieceShape pieceShape, int i) {
        ArrayList<ArrayList<Integer>> gridCopy = new ArrayList<>();
        for (ArrayList<Integer> row : pieceShape.getRotations().get(i)) {
            ArrayList<Integer> rowCopy = new ArrayList<>(row);
            gridCopy.add(rowCopy);
        }
        return gridCopy;
    }

    public static ArrayList<ArrayList<ArrayList<Integer>>> copyRotations(PieceShape pieceShape) {
        ArrayList<ArrayList<ArrayList<Integer>>> rotationsCopy = new ArrayList<>();
        for (int i = 0; i < pieceShape.getRotations().size(); i++) {
            ArrayList<ArrayList<Integer>> gridCopy = copyGrid(pieceShape, i);
            rotationsCopy.add(gridCopy);
        }
        return rotationsCopy;
    }

    public static <T> ArrayList<ArrayList<T>> rotateClockwise(ArrayList<ArrayList<T>> matrix) {
        ArrayList<ArrayList<T>> transpose = new ArrayList<>();
        int N = matrix.size();
        for (int i = 0; i < N; i++) {
            ArrayList<T> column = new ArrayList<>();
            for (ArrayList<T> row : matrix) {
                column.add(row.get(i));
            }
            transpose.add(column);
        }

        for (ArrayList<T> arrayList : transpose) {
            Collections.reverse(arrayList);
        }

        return transpose;
    }

    public static <T> ArrayList<ArrayList<T>> rotateCounterClockwise(ArrayList<ArrayList<T>> matrix) {
        ArrayList<ArrayList<T>> transpose = new ArrayList<>();
        int N = matrix.size();
        for (int i = N - 1; i >= 0; i--) {
            ArrayList<T> column = new ArrayList<T>();
            for (ArrayList<T> row : matrix) {
                column.add(row.get(i));
            }
            transpose.add(column);
        }
        return transpose;
    }
}
