package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {

    public boolean leftMousePressed;
    public boolean rightMousePressed;
    public boolean mouseInWindow;
    int x;
    int y;

    public MouseHandler() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = true;
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
            x = 0;
            y = 0;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component component = e.getComponent();
        if (component instanceof JFrame) {
            mouseInWindow = true;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Component component = e.getComponent();
        if (component instanceof JFrame) {
            mouseInWindow = false;
        }
    }
}
