package model.panels;

import lombok.Getter;
import lombok.Setter;
import model.Renderer;
import model.*;
import model.piece.Piece;
import ui.GameOverDialog;
import ui.MainFrame;
import ui.Observer;
import ui.PauseDialog;
import ui.panels.TetrisPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class TetrisGame extends TetrisPanel implements Runnable, Observer {

    JPanel leftPanel;
    Thread gameThread;

    @Getter MainFrame mainFrame;

    @Getter private TetrisKeyHandler tetrisKeyHandler;
    @Getter private MovementHandler movementHandler;
    @Getter private LinkedList<Piece> queue;
    @Getter @Setter private HashSet<Position> lockedPositions;
    @Getter private Renderer renderer;
    @Getter private Piece holdPiece;

    private GamePanel gamePanel;
    private HoldPanel holdPanel;
    private PreviewPanel previewPanel;
    private ScorePanel scorePanel;

    private JDialog pauseDialog;
    private JDialog gameOverDialog;
    private JPanel glassPane;

    @Getter @Setter private boolean guaranteedTSpin;
    @Getter @Setter private boolean threeCornerRule;
    @Getter @Setter private boolean twoCornerRule;
    private int linesCleared = 0;

    @Setter @Getter private boolean running = false;
    private boolean paused = false;

    @Getter private int level = 1;

    public TetrisGame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        createObjects();
        initializeVisuals();
        revalidate();
        repaint();

        guaranteedTSpin = false;
        threeCornerRule = false;
        twoCornerRule = false;

        startGameThread();
        assert gamePanel != null;
        gamePanel.requestFocusInWindow();
    }

    private void createObjects() {
        lockedPositions = new HashSet<>();
        queue = new LinkedList<>();
        renderer = new Renderer();
        tetrisKeyHandler = new TetrisKeyHandler(this);
        movementHandler = new MovementHandler(this);
        addKeyListener(tetrisKeyHandler);
        gamePanel = new GamePanel(this);
        previewPanel = new PreviewPanel(this);
        holdPanel = new HoldPanel(this);
        scorePanel = new ScorePanel(this);
    }

    private void initializeVisuals() {
        setLayout(new BorderLayout());
        setBackground(Color.black);
        setFocusable(true);
        setVisible(true);

        leftPanel = new JPanel();

        add(gamePanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);
        add(leftPanel, BorderLayout.WEST);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(holdPanel);
        leftPanel.add(scorePanel);

        glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        glassPane.setOpaque(false);
        glassPane.setBackground(new Color(0, 0, 0, 175));
        glassPane.setVisible(false);

        pauseDialog = new PauseDialog(SwingUtilities.getWindowAncestor(this), this);
        gameOverDialog = new GameOverDialog(SwingUtilities.getWindowAncestor(this), this);
    }

    public void setHoldPiece(Piece holdPiece) {
        this.holdPiece = holdPiece;
        holdPanel.setHoldPiece(holdPiece);
    }

    // MODIFIES: This
    // EFFECTS: Runs the game
    @Override
    public void run() {
        double drawInterval = (double) 1000 / FPS;
        double delta = 0;
        long lastTime = GameClock.getInstance().getGameTime();
        long currentTime;

        while (running) {
            if (!paused) {
                GameClock.getInstance().update();
                currentTime = GameClock.getInstance().getGameTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;
                if (delta >= 1) {
                    update();
                    repaint();
                    delta--;
                }
            }
        }
    }

    private void update() {
        gamePanel.update();
        scorePanel.update();
    }

    public void pause() {
        paused = true;
        GameClock.getInstance().pause();
        glassPane.setVisible(true);
        pauseDialog.setVisible(true);
        glassPane.setVisible(false);
        GameClock.getInstance().unpause();
        paused = false;
    }

    public void checkLineClear(Piece activePiece) {
        LinkedHashSet<Point> finalPositions = new LinkedHashSet<>(movementHandler.calculatePositions(0, 0, activePiece));
        LinkedHashSet<Integer> yVals = new LinkedHashSet<>();
        for (Point p : finalPositions) {
            yVals.add(p.y);
        }

        linesCleared = 0;
        for (Integer y : yVals) {
            if (lineIsComplete(y)) {
                clearLine(y);
                linesCleared++;
            }
        }
    }

    public boolean lineIsComplete(Integer y) {
        for (int x = 0; x < gameWidth; x += tileSize) {
            if (!containsLockedPosition(new Point(x, y))) {
                return false;
            }
        }
        return true;
    }

    // MODIFIES: This
    // EFFECTS: Clears a horizontal line of pieces
    public void clearLine(int yToClear) {
        lockedPositions.removeIf(lockedPosition -> lockedPosition.point().getY() == yToClear);
        HashSet<Position> newPositions = new HashSet<>();

        Iterator<Position> iterator = lockedPositions.iterator();
        while (iterator.hasNext()) {
            Position lockedPosition = iterator.next();
            if (lockedPosition.getY() < yToClear) {
                Position newPosition = new Position(new Point(lockedPosition.getX(),
                        lockedPosition.getY() + tileSize), lockedPosition.color());
                iterator.remove();
                newPositions.add(newPosition);
            }
        }
        lockedPositions.addAll(newPositions);
//        playVineBoom();
    }

    // MODIFIES: This
    // EFFECTS: Adds positions of every square of piece to lockedPositions
    public void addLockedPositions(Piece piece) {
        for (Point p : movementHandler.calculatePositions(0, 0, piece)) {
            lockedPositions.add(new Position(p, piece.getPieceShape().getColor()));
        }
    }

    // EFFECTS: Returns true if lockedPositions contains a LockedPosition with x and y same as point
    public boolean containsLockedPosition(Point p) {
        for (Position lockedPosition : lockedPositions) {
            if (lockedPosition.point().equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void place(Piece piece) {
        addLockedPositions(piece);
        piece.setPlaced(true);
        checkLineClear(piece);

        if (guaranteedTSpin) {
            scorePanel.increaseScore(linesCleared, true, true);
        } else {
            scorePanel.increaseScore(linesCleared, threeCornerRule, twoCornerRule);
        }

        guaranteedTSpin = false;
        twoCornerRule = false;
        threeCornerRule = false;
    }

    public void pieceMoved() {
        guaranteedTSpin = false;
        threeCornerRule = false;
    }

    public void hardDrop(int i) {
        scorePanel.hardDrop(i);
    }

    public void softDrop() {
        scorePanel.softDrop();
    }

    public void drawMessage(String message) {
        gamePanel.drawMessage(message);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    // MODIFIES: This
    // EFFECTS: Starts game thread
    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public void notify(String message) {
        switch (message) {
            case "exit":
                stop();
                notifyObservers("back");
                break;
            case "restart":
                stop();
                notifyObservers("start");
                break;
            default:
        }
    }

    private void stop() {
        running = false;
        removeAll();
    }

    public void showGameOver() {
        paused = true;
        GameClock.getInstance().pause();
        glassPane.setVisible(true);
        gameOverDialog.setVisible(true);
        glassPane.setVisible(false);
        GameClock.getInstance().unpause();
        paused = false;
    }
}
