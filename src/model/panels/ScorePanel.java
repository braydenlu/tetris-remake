package model.panels;

import ui.panels.TetrisPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ScorePanel extends TetrisPanel {

    private int score;
    private final TetrisGame tetrisGame;
    private boolean backToBack;

    private JLabel guaranteedTSpinLabel;
    private JLabel twoCornerRuleLabel;
    private JLabel threeCornerRuleLabel;
    private JLabel tSpin;
    private JLabel tSpinMini;
    private JLabel backToBackLabel;

    private JLabel scoreLabel;

    public ScorePanel(TetrisGame tetrisGame) {
        score = 0;
        setPreferredSize(new Dimension(5 * tileSize, 5 * tileSize));
        this.tetrisGame = tetrisGame;
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, borderWidth));

        initializeJLabels();
    }

    private void initializeJLabels() {
        guaranteedTSpinLabel = new JLabel();
        twoCornerRuleLabel = new JLabel();
        threeCornerRuleLabel = new JLabel();
        tSpinMini = new JLabel();
        tSpin = new JLabel();
        scoreLabel = new JLabel();
        backToBackLabel = new JLabel();

        for (JLabel jLabel : Arrays.asList(guaranteedTSpinLabel, twoCornerRuleLabel, threeCornerRuleLabel, tSpin, tSpinMini, scoreLabel, backToBackLabel)) {
            add(jLabel);
            jLabel.setForeground(Color.WHITE);
        }
    }

    public void increaseScore(int linesCleared, boolean threeCornerRule, boolean twoCornerRule) {
        int increase = tetrisGame.getLevel();
        boolean difficult = false;

        switch (linesCleared) {
            case 0:
                if (twoCornerRule) {
                    increase = increase * 400;
                    difficult = true;
                } else if (threeCornerRule) {
                    increase = increase * 100;
                    difficult = true;
                } else {
                    increase = 0;
                    difficult = backToBack;
                }
                break;
            case 1:
                if (twoCornerRule) {
                    increase = increase * 800;
                    difficult = true;
                } else if (threeCornerRule) {
                    increase = increase * 200;
                    difficult = true;
                } else {
                    increase = increase * 100;
                }
                break;
            case 2:
                if (twoCornerRule) {
                    increase = increase * 1200;
                    difficult = true;
                } else if (threeCornerRule) {
                    increase = increase * 400;
                    difficult = true;
                } else {
                    increase = increase * 300;
                }
                break;
            case 3:
                if (twoCornerRule) {
                    increase = increase * 1600;
                    difficult = true;
                } else {
                    increase = increase * 500;
                }
                break;
            default:
                increase = increase * 800;
                difficult = true;
        }

        displayMessage(linesCleared, threeCornerRule, twoCornerRule, difficult);

        if (difficult && backToBack) {
            increase += increase / 2;
        } else {
            backToBack = difficult;
        }

        score += increase;
    }

    private void displayMessage(int linesCleared, boolean threeCornerRule, boolean twoCornerRule, boolean difficult) {
        StringBuilder scoreMessage = new StringBuilder();

        if (difficult && backToBack && ((linesCleared == 4) || threeCornerRule)) {
            scoreMessage.append("B2B ");
        }

        if (threeCornerRule) {
            if (twoCornerRule) {
                scoreMessage.append("T-SPIN");
            } else {
                scoreMessage.append("MINI T-SPIN");
            }
            if (linesCleared > 0) {
                scoreMessage.append(" ");
            }
        }

        switch (linesCleared) {
            case 0:
                break;
            case 1:
                scoreMessage.append("SINGLE");
                break;
            case 2:
                scoreMessage.append("DOUBLE");
                break;
            case 3:
                scoreMessage.append("TRIPLE");
                break;
            default:
                scoreMessage.append("TETRIS");
        }
        if (!scoreMessage.isEmpty()) {
            tetrisGame.drawMessage(scoreMessage.toString());
        }
    }

    public void update() {
//        guaranteedTSpinLabel.setText(("Fin or TST kick: " + tetrisGame.isGuaranteedTSpin()));
//        twoCornerRuleLabel.setText(("Two corner rule: " + tetrisGame.isTwoCornerRule()));
//        threeCornerRuleLabel.setText(("Three corner rule: " + tetrisGame.isThreeCornerRule()));
//        tSpin.setText("T Spin: " + ((tetrisGame.isThreeCornerRule() && tetrisGame.isTwoCornerRule()) ||
//                tetrisGame.isGuaranteedTSpin()));
//        tSpinMini.setText("T Spin Mini: " + (tetrisGame.isThreeCornerRule() && !tetrisGame.isTwoCornerRule()));
//        backToBackLabel.setText("Back to back: " + backToBack);
//        scoreLabel.setText("Your Score: " + score);
    }

    public void hardDrop(int i) {
        score += 2 * i;
    }

    public void softDrop() {
        score+= 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();

        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, holdWindowWidth, tileSize);
//        RenderingHints savedHints = graphics2D.getRenderingHints();
        Font savedFont = graphics2D.getFont();
        graphics2D.setFont(new Font(graphics2D.getFont().getFontName(), Font.BOLD, 25));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString(String.valueOf(score),  tileSize / 4, 2 * tileSize);

        graphics2D.setColor(Color.black);
        graphics2D.setFont(savedFont);
        graphics2D.drawString("SCORE", tileSize / 4, tileSize * 4 / 5);
//        graphics2D.setRenderingHints(savedHints);
    }
}
