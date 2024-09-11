package model;

import model.panels.TetrisGame;
import ui.gamedata.Control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class TetrisKeyHandler implements KeyListener {

    public boolean softDropPressed;
    public boolean moveRightPressed;
    public boolean moveLeftPressed;

    public boolean rotateCounterClockwisePressed;
    public boolean rotateClockwisePressed;
    public boolean rotate180Pressed;

    public boolean hardDropPressed;
    public boolean holdPressed;

    private TetrisGame tetrisGame;
    private Map<Control, int[]> controls;

    public TetrisKeyHandler(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        this.controls = tetrisGame.getMainFrame().getControls();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        for (Map.Entry<Control, int[]> entry : controls.entrySet()) {
            Control control = entry.getKey();
            int[] keyBindings = entry.getValue();

            if (keyCode == keyBindings[0] || keyCode == keyBindings[1]) {
                switch (control) {
                    case SOFT_DROP:
                        softDropPressed = true;
                        break;
                    case MOVE_RIGHT:
                        moveRightPressed = true;
                        break;
                    case MOVE_LEFT:
                        moveLeftPressed = true;
                        break;
                    case HOLD:
                        holdPressed = true;
                        break;
                    case ROTATE_180:
                        rotate180Pressed = true;
                        break;
                    case ROTATE_COUNTERCLOCKWISE:
                        rotateCounterClockwisePressed = true;
                        rotateClockwisePressed = false;
                        break;
                    case ROTATE_CLOCKWISE:
                        rotateClockwisePressed = true;
                        rotateCounterClockwisePressed = false;
                        break;
                    case PAUSE:
                        tetrisGame.pause();
                        break;
                    case HARD_DROP:
                        hardDropPressed = true;
                        break;
                    default:
                        break;
                }
            }
        }
//        if (keyCode == controls.get(Control.SOFT_DROP)[0] || keyCode == controls.get(Control.SOFT_DROP)[1]) {
//            softDropPressed = true;
//        } else if (keyCode == KeyEvent.VK_RIGHT) {
//            moveRightPressed = true;
//        } else if (keyCode == KeyEvent.VK_LEFT) {
//            moveLeftPressed = true;
//        } else if (keyCode == KeyEvent.VK_SPACE) {
//            spacePressed = true;
//        } else if (keyCode == KeyEvent.VK_C) {
//            holdPressed = true;
//        } else if (keyCode == KeyEvent.VK_E) {
//            rotate180Pressed = true;
//        } else if (keyCode == KeyEvent.VK_A) {
//            rotateCounterClockwisePressed = true;
//            rotateClockwisePressed = false;
//        } else if (keyCode == KeyEvent.VK_D) {
//            rotateClockwisePressed = true;
//            rotateCounterClockwisePressed = false;
//        } else if (keyCode == KeyEvent.VK_ESCAPE) {
//            tetrisGame.pause();
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        for (Map.Entry<Control, int[]> entry : controls.entrySet()) {
            Control control = entry.getKey();
            int[] keyBindings = entry.getValue();

            if (keyCode == keyBindings[0] || (keyBindings.length > 1 && keyCode == keyBindings[1])) {
                switch (control) {
                    case SOFT_DROP:
                        softDropPressed = false;
                        break;
                    case MOVE_RIGHT:
                        moveRightPressed = false;
                        break;
                    case MOVE_LEFT:
                        moveLeftPressed = false;
                        break;
                    case HOLD:
                        holdPressed = false;
                        break;
                    case ROTATE_180:
                        rotate180Pressed = false;
                        break;
                    case ROTATE_COUNTERCLOCKWISE:
                        rotateCounterClockwisePressed = false;
                        break;
                    case ROTATE_CLOCKWISE:
                        rotateClockwisePressed = false;
                        break;
                    case HARD_DROP:
                        hardDropPressed = false;
                        break;
                    default:
                        break;
                }
            }
        }

//        if (keyCode == KeyEvent.VK_UP) {
////            upPressed = false;
//        } else if (keyCode == KeyEvent.VK_DOWN) {
//            softDropPressed = false;
//        } else if (keyCode == KeyEvent.VK_RIGHT) {
//            moveRightPressed = false;
//        } else if (keyCode == KeyEvent.VK_LEFT) {
//            moveLeftPressed = false;
//        } else if (keyCode == KeyEvent.VK_SPACE) {
//            spacePressed = false;
//        } else if (keyCode == KeyEvent.VK_C) {
//            holdPressed = false;
//        } else if (keyCode == KeyEvent.VK_E) {
//            rotate180Pressed = false;
//        } else if (keyCode == KeyEvent.VK_A) {
//            rotateCounterClockwisePressed = false;
//        } else if (keyCode == KeyEvent.VK_D) {
//            rotateClockwisePressed = false;
//        }
    }
}
