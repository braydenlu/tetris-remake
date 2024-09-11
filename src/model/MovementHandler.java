package model;

import lombok.Setter;
import model.exceptions.PiecePlacedException;
import model.panels.GamePanel;
import model.panels.TetrisGame;
import model.piece.Piece;
import model.piece.PieceShape;

import java.awt.*;
import java.util.List;
import java.util.*;

import static model.MatrixOperations.iterateMatrix;

public class MovementHandler {

    // Movement properties
    private int tileSize;
    private double dropInterval;
    private double regularDropInterval;
    private double softDropInterval;
    double arrTime;
    double dasTime;

    private boolean CWPressed;
    private boolean CCWPressed;
    private boolean One80Pressed;

    private Piece piece;
    @Setter private boolean tPiece;
    @Setter private GamePanel gamePanel;
    private TetrisKeyHandler tetrisKeyHandler;
    private final TetrisGame tetrisGame;

    // Keeping track of movement
    private long moveLastTime;
    private double rightPressedTime;
    private double leftPressedTime;
    private double dropDelta;
    private long dropLastTime;

    // Kick tables
    private final HashMap<String, ArrayList<Point>> pieceKickTable;
    private final HashMap<String, ArrayList<Point>> iPieceKickTable;
    private final HashMap<String, Point> kickTable180;

    // EFFECTS: Creates a movementHandler
    public MovementHandler(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        pieceKickTable = new HashMap<>();
        iPieceKickTable = new HashMap<>();
        kickTable180 = new HashMap<>();
        CWPressed = false;
        CCWPressed = false;

        initializeMainKickTable();
        initializeIPieceKickTable();
        initializeKickTable180();
    }

    // MODIFIES: This
    // EFFECTS: Initializes objects and values
    public void initialize(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tetrisKeyHandler = gamePanel.getTetrisKeyHandler();
        tileSize = GamePanel.getTileSize();
        regularDropInterval = (double) 1000 * gamePanel.getGravity();
        softDropInterval = regularDropInterval / gamePanel.getSoftDropFactor();
        arrTime = gamePanel.getAutoRepeatRate();
        dasTime = gamePanel.getDelayedAutoShift();
        resetValues();
    }

    // MODIFIES: This
    // EFFECTS: Resets values related to piece movement
    private void resetValues() {
        dropInterval = regularDropInterval;
        dropDelta = 0;
        dropLastTime = GameClock.getInstance().getGameTime();
        moveLastTime = GameClock.getInstance().getGameTime();
        rightPressedTime = 0;
        leftPressedTime = 0;

    }

    // MODIFIES: This
    // EFFECTS: Updates the piece
    public void update() {
        if (!piece.isPlaced()) {
            try {
                updateValues();
                hold();
                drop();
                move();
            } catch (PiecePlacedException e) {
                // Skips the rest of the movement checks
            }
        }
    }

    // MODIFIES: This
    // EFFECTS: Updates the drop interval
    public void updateValues() {
        if (!tetrisKeyHandler.softDropPressed) {
            dropInterval = regularDropInterval;
        } else {
            if (moveIsAllowed(piece.getRotation(), piece, 0, 1)) {
                dropInterval = softDropInterval;
                System.out.println("soft");
            } else {
                dropInterval = regularDropInterval;
                System.out.println("regular");
            }
        }
    }

    public void hold() {
        if (tetrisKeyHandler.holdPressed && !gamePanel.isHoldTriggered()) {
            gamePanel.swapPiece();
        }
    }


    // MODIFIES: This, tetrisGame, piece
    // EFFECTS: Handles dropping the current piece
    public void drop() throws PiecePlacedException {
        if (tetrisKeyHandler.hardDropPressed && !gamePanel.isHardDropHeld()) {
            gamePanel.setHardDropHeld(true);
            hardDrop();
        } else if (!tetrisKeyHandler.hardDropPressed) {
            gamePanel.setHardDropHeld(false);
        }

        long currentTime = GameClock.getInstance().getGameTime();
        dropDelta += (currentTime - dropLastTime) / dropInterval;
        dropLastTime = currentTime;
        if (dropDelta >= 1) {
            if (!moveIsAllowed(piece.getRotation(), piece,0, 1)) {
                place();
            } else {
                piece.setY(piece.getY() + GamePanel.getTileSize());
                dropDelta--;
                if (dropInterval == softDropInterval) {
                    tetrisGame.softDrop();
                }
            }
            tetrisGame.pieceMoved();
        }
    }

    // EFFECTS: Handles moving a piece
    public void move() throws PiecePlacedException {
        if (piece.isPlaced()) {
            return;
        }
        rotate();

        long currentTime = GameClock.getInstance().getGameTime();

        if (tetrisKeyHandler.moveRightPressed) {
            handleMove(1, currentTime);
        } else if (tetrisKeyHandler.moveLeftPressed) {
            handleMove(-1, currentTime);
        } else {
            rightPressedTime = leftPressedTime = 0;
        }

        moveLastTime = currentTime;
    }

    // EFFECTS: Helper method that moves the current piece upon first key press or when dasTime + arrTime has passed
    private void handleMove(int direction, long currentTime) {
        double pressedTime = (direction == 1) ? rightPressedTime : leftPressedTime;

        if (pressedTime == 0) {
            tryMove(direction);
        } else if (pressedTime >= dasTime + arrTime) {
            tryMove(direction);
            pressedTime -= arrTime;
        }
        pressedTime += (currentTime - moveLastTime);

        if (direction == 1) {
            rightPressedTime = pressedTime;
            leftPressedTime = 0;
        } else {
            leftPressedTime = pressedTime;
            rightPressedTime = 0;
        }
    }

    // MODIFIES: Piece, tetrisGame
    // EFFECTS: Moves the current piece by horizontalMovement tiles horizontally if the movement is allowed
    private void tryMove(int horizontalMovement) {
        if (moveIsAllowed(piece.getRotation(), piece, horizontalMovement, 0)) {
            piece.setX(piece.getX() + horizontalMovement * GamePanel.getTileSize());
            tetrisGame.pieceMoved();
        }
    }

    // MODIFIES: Piece
    // EFFECTS: Hard drops the current piece
    public void hardDrop() throws PiecePlacedException {
        int hardDropDistance = calculateHardDropDistance();
        piece.setY(piece.getY() + hardDropDistance * tileSize);
        tetrisGame.hardDrop(hardDropDistance);
        place();
    }

    // MODIFIES: This, tetrisGame
    // EFFECT: Places a piece in the pile and throws a new PiecePlacedException
    public void place() throws PiecePlacedException {
        tetrisGame.place(piece);
        resetValues();
        throw new PiecePlacedException();
    }

    // EFFECTS: Returns true if moving the pieceToCheck by deltaX and deltaY and rotating it is allowed
    public boolean moveIsAllowed(int newRotationIndex, Piece pieceToCheck, int deltaX, int deltaY) {
        HashSet<Point> newPositions;
        Piece newPositionPiece;

        if (newRotationIndex != piece.getRotation()) {
            newPositionPiece = new Piece(pieceToCheck.getPieceShape());
            newPositionPiece.setRotation(newRotationIndex);
            newPositionPiece.setY(pieceToCheck.getY());
            newPositionPiece.setX(pieceToCheck.getX());
        } else {
            newPositionPiece = pieceToCheck;
        }

        newPositions = calculateNewPositions(deltaX, deltaY, pieceToCheck, newPositionPiece);

        for (Point p : newPositions) {
            if (pointIsOccupied(p)) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: Returns true if a point is occupied
    public boolean pointIsOccupied(Point p) {
        return p.x >= GamePanel.getGameWidth() || p.y >= GamePanel.getGameHeight() || p.x < 0
                || tetrisGame.containsLockedPosition(p);
    }

    // EFFECTS: Returns a hashset of all the positions occupied by the given piece
    public LinkedHashSet<Point> calculatePositions(int deltaX, int deltaY, Piece piece) {
        LinkedHashSet<Point> positions = new LinkedHashSet<>();
        iterateMatrix(piece, (rowNum, colNum) -> {
            int newX = piece.getX() + (deltaX + colNum) * tileSize;
            int newY = piece.getY() + (deltaY + rowNum) * tileSize;
            positions.add(new Point(newX, newY));
        });
        return positions;
    }

    // EFFECTS: Returns a hashset of all the new points occupied by a movement of piece by deltaX and deltaY
    public HashSet<Point> calculateNewPositions(int deltaX, int deltaY, Piece piece, Piece newPiece) {
        HashSet<Point> currentPositions = calculatePositions(0, 0, piece);
        HashSet<Point> newPositions = calculatePositions(deltaX, deltaY, newPiece);
        newPositions.removeIf(currentPositions::contains);
        return newPositions;
    }

    // EFFECTS: Returns the distance the active piece needs to fall to hard drop
    public int calculateHardDropDistance() {
        int deltaY = 0;
        for (int i = 0; moveIsAllowed(piece.getRotation(), piece, 0, i); i++) {
            deltaY = i;
        }
        return deltaY;
    }

    // MODIFIES: Piece
    // EFFECTS: Moves a piece by deltaX and deltaY tiles
    public void kick(int deltaX, int deltaY) {
        piece.setX(piece.getX() + deltaX * tileSize);
        piece.setY(piece.getY() + deltaY * tileSize);
    }

    // MODIFIES: Piece
    // EFFECTS: Handles rotating the piece when a rotation key is pressed. Prevents holding the key down from spinning
    // the piece.
    public void rotate() {
        if (tetrisKeyHandler.rotateClockwisePressed) {
            if (!CWPressed) {
                updateOrientation(1);
                CWPressed = true;
                CCWPressed = One80Pressed = false;
            }
        } else if (tetrisKeyHandler.rotateCounterClockwisePressed) {
            if (!CCWPressed) {
                updateOrientation(-1);
                CCWPressed = true;
                CWPressed = One80Pressed = false;
            }
        } else if (tetrisKeyHandler.rotate180Pressed) {
            if (!One80Pressed) {
                updateOrientation(2);
                One80Pressed = true;
                CWPressed = CCWPressed = false;
            }
        } else {
            CWPressed = CCWPressed = One80Pressed = false;
        }
    }

    // MODIFIES: Piece
    // EFFECTS: Helper function that updates the orientation of the piece given a rotation direction
    public void updateOrientation(int rotationDirection) {
        if (rotationDirection == 0) {
            return;
        }

        boolean successful = false;

        int newRotationIndex = (piece.getRotation() + rotationDirection + 4) % 4;

        if (moveIsAllowed(newRotationIndex, piece, 0, 0)) {
            piece.setRotation(newRotationIndex);
            successful = true;
        } else {
            if (doKicks(newRotationIndex)) {
                successful = true;
            };
        }

        if (tPiece && successful) {
            List<Point> cornersToCheck = new ArrayList<>();
            addPoint(cornersToCheck, piece.getX(), piece.getY());
            addPoint(cornersToCheck, piece.getX() + 2 * tileSize, piece.getY());
            addPoint(cornersToCheck, piece.getX(), piece.getY() + 2 * tileSize);
            addPoint(cornersToCheck, piece.getX() + 2 * tileSize, piece.getY() + 2 * tileSize);

            int i = 0;
            List<Integer> occupiedCorners = new ArrayList<>();
            for (Point p : cornersToCheck) {
                if (tetrisGame.containsLockedPosition(p) || pointIsOccupied(p)) {
                    occupiedCorners.add(i);
                }
                i++;
            }
            if (occupiedCorners.size() >= 3) {
                switch (piece.getRotation()) {
                    case 0:
                        if (occupiedCorners.contains(0) && occupiedCorners.contains(1)) {
                            tetrisGame.setTwoCornerRule(true);
                        }
                        break;
                    case 1:
                        if (occupiedCorners.contains(3) && occupiedCorners.contains(1)) {
                            tetrisGame.setTwoCornerRule(true);
                        }
                        break;
                    case 2:
                        if (occupiedCorners.contains(2) && occupiedCorners.contains(3)) {
                            tetrisGame.setTwoCornerRule(true);
                        }
                        break;
                    default:
                        if (occupiedCorners.contains(0) && occupiedCorners.contains(2)) {
                            tetrisGame.setTwoCornerRule(true);
                        }
                }
                tetrisGame.setThreeCornerRule(true);
            } else {
                tetrisGame.setThreeCornerRule(false);
                tetrisGame.setTwoCornerRule(false);
            }
        }
    }

    // MODIFIES: Piece
    // EFFECTS: Goes through the kicks for the rotation direction and completes the first allowed kick. Also checks for
    // 2 specific kicks with a T piece which guarantee a T-Spin in Tetris guideline scoring
    public boolean doKicks(int newRotationIndex) {
        if (Math.abs(piece.getRotation() - newRotationIndex) == 2) {
            Point kick = kickTable180.get(piece.getRotation() + ">>"
                    + newRotationIndex);
            tryKick(kick, newRotationIndex);
        } else {
            ArrayList<Point> kicks = (piece.getPieceShape() == PieceShape.I)
                    ? iPieceKickTable.get(piece.getRotation() + ">>" + newRotationIndex)
                    : pieceKickTable.get(piece.getRotation() + ">>" + newRotationIndex);

            if (tPiece &&
                    (piece.getRotation() == 2 && (newRotationIndex == 1  || newRotationIndex == 3) ||
                    piece.getRotation() == 0 && (newRotationIndex == 1 || newRotationIndex == 3))) {
                for (int i = 0; i < 3; i++) {
                    Point p = kicks.get(i);
                    if (tryKick(p, newRotationIndex)) {
                        return true;
                    }
                }
                Point p = kicks.get(3);

                if (tryKick(p, newRotationIndex)) {
                    tetrisGame.setGuaranteedTSpin(true);
                    return true;
                }
            } else {
                for (Point p : kicks) {
                    if (tryKick(p, newRotationIndex)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // MODIFIES: Piece
    // EFFECTS: Returns true if the given translation and rotation of the piece is allowed and completes the movements.
    // Otherwise, returns false and does nothing.
    private boolean tryKick(Point p, int newRotationIndex) {
        if (moveIsAllowed(newRotationIndex, piece, p.x, p.y)) {
            piece.setRotation(newRotationIndex);
            kick(p.x, p.y);
            return true;
        }
        return false;
    }

    // MODIFIES: This
    // EFFECTS: Sets the active piece to be handled and sets tPiece to true if it is a tPiece
    public void setPiece(Piece piece) {
        this.piece = piece;
        tPiece = piece.getPieceShape() == PieceShape.T;
    }

    // Kick table initialization methods

    // MODIFIES: This
    // EFFECTS: Initializes the main kick table (the table used for all non I pieces)
    private void initializeMainKickTable() {
        ArrayList<Point> pointList1 = new ArrayList<>();
        ArrayList<Point> pointList2 = new ArrayList<>();
        ArrayList<Point> pointList3 = new ArrayList<>();
        ArrayList<Point> pointList4 = new ArrayList<>();
        ArrayList<Point> pointList5 = new ArrayList<>();
        ArrayList<Point> pointList6 = new ArrayList<>();
        ArrayList<Point> pointList7 = new ArrayList<>();
        ArrayList<Point> pointList8 = new ArrayList<>();

        addPoint(pointList1, -1, 0);
        addPoint(pointList1, -1, -1);
        addPoint(pointList1, 0, 2);
        addPoint(pointList1, -1, 2);

        addPoint(pointList2, 1, 0);
        addPoint(pointList2, 1, 1);
        addPoint(pointList2, 0, -2);
        addPoint(pointList2, 1, -2);

        addPoint(pointList3, 1, 0);
        addPoint(pointList3, 1, 1);
        addPoint(pointList3, 0, -2);
        addPoint(pointList3, 1, -2);

        addPoint(pointList4, -1, 0);
        addPoint(pointList4, -1, -1);
        addPoint(pointList4, 0, 2);
        addPoint(pointList4, -1, 2);

        addPoint(pointList5, 1, 0);
        addPoint(pointList5, 1, -1);
        addPoint(pointList5, 0, 2);
        addPoint(pointList5, 1, 2);

        addPoint(pointList6, -1, 0);
        addPoint(pointList6, -1, 1);
        addPoint(pointList6, 0, -2);
        addPoint(pointList6, -1, -2);

        addPoint(pointList7, -1, 0);
        addPoint(pointList7, -1, 1);
        addPoint(pointList7, 0, -2);
        addPoint(pointList7, -1, -2);

        addPoint(pointList8, 1, 0);
        addPoint(pointList8, 1, -1);
        addPoint(pointList8, 0, 2);
        addPoint(pointList8, 1, 2);

        pieceKickTable.put("0>>1", pointList1); // TST kick
        pieceKickTable.put("1>>0", pointList2);
        pieceKickTable.put("1>>2", pointList3);
        pieceKickTable.put("2>>1", pointList4); // Fin kick
        pieceKickTable.put("2>>3", pointList5); // Fin kick
        pieceKickTable.put("3>>2", pointList6);
        pieceKickTable.put("3>>0", pointList7);
        pieceKickTable.put("0>>3", pointList8); // TST kick
    }

    // MODIFIES: This
    // EFFECTS: Initializes the kick table specifically for the I piece
    private void initializeIPieceKickTable() {
        ArrayList<Point> pointList1 = new ArrayList<>();
        ArrayList<Point> pointList2 = new ArrayList<>();
        ArrayList<Point> pointList3 = new ArrayList<>();
        ArrayList<Point> pointList4 = new ArrayList<>();
        ArrayList<Point> pointList5 = new ArrayList<>();
        ArrayList<Point> pointList6 = new ArrayList<>();
        ArrayList<Point> pointList7 = new ArrayList<>();
        ArrayList<Point> pointList8 = new ArrayList<>();

        addPoint(pointList1, -2, 0);
        addPoint(pointList1, 1, 0);
        addPoint(pointList1, -2, 1);
        addPoint(pointList1, 1, -2);

        addPoint(pointList2, 2, 0);
        addPoint(pointList2, -1, 0);
        addPoint(pointList2, 2, -1);
        addPoint(pointList2, -1, 2);

        addPoint(pointList3, -1, 0);
        addPoint(pointList3, 2, 0);
        addPoint(pointList3, -1, -2);
        addPoint(pointList3, 2, 1);

        addPoint(pointList4, 1, 0);
        addPoint(pointList4, -2, 0);
        addPoint(pointList4, 1, 2);
        addPoint(pointList4, -2, -1);

        addPoint(pointList5, 2, 0);
        addPoint(pointList5, -1, 0);
        addPoint(pointList5, 2, -1);
        addPoint(pointList5, -1, 2);

        addPoint(pointList6, -2, 0);
        addPoint(pointList6, 1, 0);
        addPoint(pointList6, -2, 1);
        addPoint(pointList6, 1, -2);

        addPoint(pointList7, 1, 0);
        addPoint(pointList7, -2, 0);
        addPoint(pointList7, 1, 2);
        addPoint(pointList7, -2, -1);

        addPoint(pointList8, -1, 0);
        addPoint(pointList8, 2, 0);
        addPoint(pointList8, -1, -2);
        addPoint(pointList8, 2, 1);

        iPieceKickTable.put("0>>1", pointList1);
        iPieceKickTable.put("1>>0", pointList2);
        iPieceKickTable.put("1>>2", pointList3);
        iPieceKickTable.put("2>>1", pointList4);
        iPieceKickTable.put("2>>3", pointList5);
        iPieceKickTable.put("3>>2", pointList6);
        iPieceKickTable.put("3>>0", pointList7);
        iPieceKickTable.put("0>>3", pointList8);
    }

    // MODIFIES: This
    // EFFECTS: Initializes the kick table for 180 degree rotations
    private void initializeKickTable180() {
        kickTable180.put("0>>2", new Point(0, -1));
        kickTable180.put("1>>3", new Point(1, 0));
        kickTable180.put("2>>0", new Point(0, 1));
        kickTable180.put("3>>1", new Point(-1, 0));
    }

    // MODIFIES: pointList
    // EFFECTS: Adds a point to an arraylist of points; for kick table initialization
    private static void addPoint(List<Point> pointList, int x, int y) {
        pointList.add(new Point(x, y));
    }
}
