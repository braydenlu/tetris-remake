package model;

import model.panels.TetrisGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisKeyHandler implements KeyListener {

//    public boolean upPressed;
    public boolean downPressed;
    public boolean rightPressed;
    public boolean leftPressed;

    public boolean aPressed;
    public boolean dPressed;
    public boolean ePressed;

    public boolean spacePressed;
    public boolean cPressed;

    public boolean escapeToggle;

    private TetrisGame tetrisGame;

    public TetrisKeyHandler(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // EMPTY FOR NOW
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
//            upPressed = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = true;
        } else if (keyCode == KeyEvent.VK_C) {
            cPressed = true;
        } else if (keyCode == KeyEvent.VK_E) {
            ePressed = true;
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = true;
            dPressed = false;
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = true;
            aPressed = false;
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            tetrisGame.pause();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
//            upPressed = false;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = false;
        } else if (keyCode == KeyEvent.VK_C) {
            cPressed = false;
        } else if (keyCode == KeyEvent.VK_E) {
            ePressed = false;
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
        }
    }
}
