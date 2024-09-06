package model.piece;

import java.util.ArrayList;
import java.util.Random;

public class PieceBag {

    ArrayList<PieceShape> pieceBag;
    Random random;

    // EFFECTS: Creates the piece bag
    public PieceBag() {
        pieceBag = new ArrayList<>();
        random = new Random();
        regenerateBag();
    }

    // MODIFIES: This
    // EFFECTS: Resets the piece bag
    public void regenerateBag() {
        pieceBag.add(PieceShape.I);
        pieceBag.add(PieceShape.S);
        pieceBag.add(PieceShape.Z);
        pieceBag.add(PieceShape.L);
        pieceBag.add(PieceShape.J);
        pieceBag.add(PieceShape.O);
        pieceBag.add(PieceShape.T);
    }

    // MODIFIES: This
    // EFFECTS: Randomly returns a piece shape and removes it from the list
    public PieceShape pullPieceShape() {
        int index = random.nextInt(pieceBag.size());
        PieceShape pulledPieceShape = pieceBag.get(index);
        pieceBag.remove(index);
        if (pieceBag.isEmpty()) {
            regenerateBag();
        }
        return pulledPieceShape;
    }
}
