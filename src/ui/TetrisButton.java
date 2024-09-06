package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TetrisButton extends JButton {

    Color normalColour = Color.BLACK;
    Color hoverColour = Color.GRAY;
    Color clickColour = Color.WHITE;

    public TetrisButton(String text) {
        super(text);
        setBackground(normalColour);
        setForeground(Color.WHITE);
        LineBorder lineBorder = new LineBorder(Color.WHITE, 1);
        EmptyBorder emptyBorder = new EmptyBorder(5,10,5,10);
        Border border = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
        setBorder(border);

        setBorderPainted(true);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColour);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColour);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickColour);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColour);
            }
        });
    }
}
