package ui.panels;

import lombok.Getter;
import ui.Observable;
import ui.Observer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TetrisPanel extends JPanel implements Observable {

    List<Observer> observers;

    @Getter protected static final int borderWidth = 2;

    @Getter protected static final int FPS = 60;
    protected static final int originalTileSize = 16;
    protected static final int scale = 2;
    @Getter protected static final int tileSize = scale * originalTileSize;

    @Getter protected static final int gameWidth = 10 * tileSize;
    @Getter protected static final int gameHeight = 20 * tileSize;
    @Getter protected static final int holdWindowWidth = 5 * tileSize;
    @Getter protected static final int previewWindowWidth = 5 * tileSize;
    @Getter protected static final int windowWidth = gameWidth + holdWindowWidth + previewWindowWidth;

    public TetrisPanel() {

    }

    public void addObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    };
    public void notifyObservers(String message) {
        if (observers == null) {
            return;
        }
        for (Observer observer : observers) {
            observer.notify(message);
        }
    };
}
