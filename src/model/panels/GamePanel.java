package model.panels;

import lombok.Getter;
import lombok.Setter;
import model.MovementHandler;
import model.Position;
import model.Renderer;
import model.TetrisKeyHandler;
import model.piece.Piece;
import model.piece.PieceBag;
import ui.panels.TetrisPanel;

import java.awt.*;
import java.util.HashSet;

public class GamePanel extends TetrisPanel {

    // Piece movement
    @Getter @Setter private double gravity = 1;
    @Getter @Setter private double autoRepeatRate;
    @Getter @Setter private double delayedAutoShift;
    @Getter @Setter private double softDropFactor;
    @Getter @Setter private boolean hardDropHeld;
    @Getter @Setter private boolean holdTriggered;

    // Lists of things
    @Getter private HashSet<Point> hardDropPoints;
    private final TetrisGame tetrisGame;
    private final Renderer renderer;
    @Getter private final TetrisKeyHandler tetrisKeyHandler;

    // Objects
    @Getter private Piece activePiece;
    private final PieceBag pieceBag;
    private final MovementHandler movementHandler;

    private String message;
    private Color messageColor;
    private double spacing;
    private boolean gameOver;

    // Test starting positions
    private HashSet<Position> tSpinTest;
//    private HashSet<Position> lSpinTest;
//    private HashSet<Position> iSpinTest;
//    private HashSet<Position> iSpinTest2;
//    private HashSet<Position> lineClearTest;

    public GamePanel(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        autoRepeatRate = tetrisGame.getMainFrame().getHandling().autoRepeatRate();
        delayedAutoShift = tetrisGame.getMainFrame().getHandling().delayedAutoShift();
        softDropFactor = tetrisGame.getMainFrame().getHandling().softDropFactor();

        initializeTestGarbage();

        tetrisKeyHandler = tetrisGame.getTetrisKeyHandler();
        renderer = tetrisGame.getRenderer();
        pieceBag = new PieceBag();
        hardDropPoints = new HashSet<>();
        activePiece = new Piece(pieceBag.pullPieceShape());
        movementHandler = tetrisGame.getMovementHandler();
        movementHandler.setPiece(activePiece);
        movementHandler.initialize(this);

        setBackground(Color.BLACK);
        setFocusable(true);
        setVisible(true);
        addKeyListener(tetrisKeyHandler);
        setPreferredSize(new Dimension(gameWidth, gameHeight));

        messageColor = Color.WHITE;

        for (int i = 0; i < 5; i++) {
            addPieceToQueue();
        }
//        vineBoom.setFile(0);
    }

    public void update() {
        movementHandler.update();
        hardDropPoints = movementHandler.calculatePositions(0, movementHandler.calculateHardDropDistance(), activePiece);
        if (activePiece.isPlaced()) {
            holdTriggered = false;
            checkLockOut();
            activePiece = tetrisGame.getQueue().removeFirst();
            activePiece.setDefaultValues();
            checkBlockOut();
            movementHandler.setPiece(activePiece);
            addPieceToQueue();
        }
        if (messageColor.getAlpha() > 1) {
            messageColor = new Color(messageColor.getRed(), messageColor.getGreen(), messageColor.getBlue(), messageColor.getAlpha() - 2);
            spacing += 0.1;
        }
    }

    public void checkLockOut() {
        int i = 0;
        for (Point p : movementHandler.calculatePositions(0, 0, activePiece)) {
            if (p.getY() < 0) {
                i++;
            }
        }
        if (i == 4) {
            System.out.println("Lock out");
            gameOver();
        }
    }

    public void checkBlockOut() {
        for (Point p : movementHandler.calculatePositions(0, 0, activePiece)) {
            if (tetrisGame.containsLockedPosition(p)) {
                if (activePiece.getY() > -2) {
                    activePiece.setY(activePiece.getY() - tileSize);
                    checkBlockOut();
                } else {
                    System.out.println("Block out");
                    gameOver();
                }
                return;
            }
        }
    }

    private void gameOver() {
        if (!gameOver) {
            gameOver = true;
            activePiece.setY(activePiece.getY() - tileSize);
            repaint();
            tetrisGame.showGameOver();
        }

    }

    public void drawMessage(String message) {
        this.message = message;
        messageColor = Color.WHITE;
        spacing = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (message != null) {
            g.setColor(messageColor);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));
            FontMetrics metrics = g.getFontMetrics(g.getFont());

            double totalWidth = 0;
            for (char c : message.toCharArray()) {
                totalWidth += metrics.charWidth(c) + spacing;
            }
            totalWidth -= spacing; // Remove the last added spacing

            // Calculate the starting x position to center the text
            double x = (GamePanel.getGameWidth() - totalWidth) / 2;
            int y = 150;

            // Draw each character with the specified spacing
            for (char c : message.toCharArray()) {
                g.drawString(String.valueOf(c), (int) x, y);
                x += metrics.charWidth(c) + spacing;
            }
        }

        Graphics2D graphics2D = (Graphics2D) g;

        for (Point p: hardDropPoints) {
            Color activeColor = activePiece.getPieceShape().getColor();

            Color color = new Color(activeColor.getRed() / 3, activeColor.getGreen() / 3,
                    activeColor.getBlue() / 3);
            Renderer.drawSquare(g, p.x, p.y, tileSize, tileSize, color);
        }

        renderer.drawPiece(graphics2D, activePiece);

        for (Position p : tetrisGame.getLockedPositions()) {
            Renderer.drawSquare(graphics2D, p.point().x, p.point().y, tileSize, tileSize, p.color());
        }
    }

    // MODIFIES: This
    // EFFECTS: Takes a piece and swaps it with the held piece. If there is no held piece, puts the piece in held and
    // makes a new piece
    public void swapPiece() {
        Piece pieceToSwap = activePiece;
        holdTriggered = true;
        if (tetrisGame.getHoldPiece() == null) {
            activePiece = tetrisGame.getQueue().removeFirst();
            addPieceToQueue();
        } else {
            activePiece = tetrisGame.getHoldPiece();
        }
        activePiece.setDefaultValues();
        movementHandler.setPiece(activePiece);
        tetrisGame.setHoldPiece(pieceToSwap);
    }

    private void addPieceToQueue() {
        tetrisGame.getQueue().add(new Piece(pieceBag.pullPieceShape()));
    }

//    public void playVineBoom() {
//        vineBoom.play();
//    }
//
//    public void stopVineBoom() {
//        vineBoom.stop();
//    }

    // Generates locked positions from a HashSet of Points labeled using conventional x,y grid notation (increasing
    // from left to right, bottom to top)
    public HashSet<Position> generateLockedPositions(HashSet<Point> points) {
        HashSet<Position> lockedPositions = new HashSet<>();
        int x;
        int y;
        for (Point p: points) {
            x = p.x * tileSize;
            y = gameHeight - p.y * tileSize;
            lockedPositions.add(new Position(new Point(x, y), Color.gray));
        }
        return lockedPositions;
    }

    public void initializeTestGarbage() {
//        lSpinTest = generateLockedPositions(new HashSet<>() {{
//            add(new Point(0,1));
//            add(new Point(1,1));
//            add(new Point(2,1));
//            add(new Point(3,1));
//            add(new Point(5,1));
//            add(new Point(6,1));
//            add(new Point(7,1));
//            add(new Point(8,1));
//            add(new Point(9,1));
//            add(new Point(0,2));
//            add(new Point(1,2));
//            add(new Point(2,2));
//            add(new Point(3,2));
//            add(new Point(7,2));
//            add(new Point(8,2));
//            add(new Point(9,2));
//            add(new Point(0,3));
//            add(new Point(1,3));
//            add(new Point(2,3));
//            add(new Point(3,3));
//            add(new Point(4, 3));
//            add(new Point(5,3));
//            add(new Point(7,3));
//            add(new Point(8,3));
//            add(new Point(9,3));
//        }});
//
//        iSpinTest = generateLockedPositions(new HashSet<>() {{
//            add(new Point(3, 1));
//            add(new Point(3, 2));
//            add(new Point(3, 3));
//            add(new Point(5, 2));
//            add(new Point(6, 2));
//            add(new Point(7, 2));
//            add(new Point(8, 2));
//            add(new Point(9, 2));
//            add(new Point(6, 3));
//            add(new Point(7, 3));
//        }});
//
//        iSpinTest2 = generateLockedPositions(new HashSet<>() {{
//            add(new Point(1, 1));
//            add(new Point(1, 2));
//            add(new Point(1, 3));
//            add(new Point(1, 4));
//            add(new Point(1, 5));
//            add(new Point(2, 5));
//            add(new Point(3, 1));
//            add(new Point(3, 2));
//            add(new Point(3, 3));
//            add(new Point(4, 1));
//            add(new Point(4, 2));
//            add(new Point(4, 3));
//            add(new Point(5, 1));
//            add(new Point(5, 2));
//            add(new Point(5, 3));
//        }});
//
//        lineClearTest = generateLockedPositions(new HashSet<>() {{
//            add(new Point(1, 1));
//            add(new Point(2, 1));
//            add(new Point(3, 1));
//            add(new Point(4, 1));
//            add(new Point(5, 1));
//            add(new Point(6, 1));
//            add(new Point(7, 1));
//            add(new Point(8, 1));
//            add(new Point(9, 1));
//            add(new Point(1, 2));
//            add(new Point(2, 2));
//            add(new Point(3, 2));
//            add(new Point(4, 2));
//            add(new Point(5, 2));
//            add(new Point(6, 2));
//            add(new Point(7, 2));
//            add(new Point(8, 2));
//            add(new Point(9, 2));
//            add(new Point(1, 3));
//            add(new Point(2, 3));
//            add(new Point(3, 3));
//            add(new Point(4, 3));
//            add(new Point(5, 3));
//            add(new Point(6, 3));
//            add(new Point(7, 3));
//            add(new Point(8, 3));
//            add(new Point(9, 3));
//            add(new Point(1, 4));
//            add(new Point(2, 4));
//            add(new Point(3, 4));
//            add(new Point(4, 4));
//            add(new Point(5, 4));
//            add(new Point(6, 4));
//            add(new Point(7, 4));
//            add(new Point(8, 4));
//            add(new Point(9, 4));
//        }});
//        tSpinTest = generateLockedPositions(new HashSet<>() {{
//            add(new Point(0, 1));
//            add(new Point(0, 2));
//            add(new Point(0, 3));
//            add(new Point(0, 4));
//            add(new Point(0, 5));
//            add(new Point(1, 5));
//            add(new Point(2, 3));
//            add(new Point(2, 1));
//            add(new Point(3, 1));
//            add(new Point(3, 2));
//            add(new Point(3, 3));
//            add(new Point(4, 1));
//        }});
//        tetrisGame.setLockedPositions(tSpinTest);
    }
}
