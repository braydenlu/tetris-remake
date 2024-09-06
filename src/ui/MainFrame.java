package ui;

import lombok.Getter;
import lombok.Setter;
import model.panels.TetrisGame;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;
import ui.gamedata.Controls;
import ui.gamedata.Handling;
import ui.panels.TetrisPanel;
import ui.panels.menu.ControlsPanel;
import ui.panels.menu.HighScoresPanel;
import ui.panels.menu.MenuPanel;
import ui.panels.menu.HandlingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class MainFrame extends JFrame implements Observer {

    @Getter Controls primaryControls;
    @Getter Controls secondaryControls;
    @Getter @Setter Handling handling;
    private HashMap<String, Integer> scores;

    private static final String JSON_FILE_LOCATION = "./data/save";

    public MainFrame() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        MenuPanel menuPanel = new MenuPanel();
        menuPanel.addObserver(this);
        getContentPane().add(menuPanel, BorderLayout.CENTER);

        setTitle("Java Tetris Remake");
        pack();
        setSize(TetrisPanel.getWindowWidth() + 14 + 2 * TetrisPanel.getBorderWidth(), TetrisPanel.getGameHeight() + 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        setLocationRelativeTo(null);

        initialize();
    }

    private void initialize() {
        primaryControls = new Controls(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE, KeyEvent.VK_UP, KeyEvent.VK_CONTROL, KeyEvent.VK_E, KeyEvent.VK_C, KeyEvent.VK_ESCAPE);
        secondaryControls = new Controls(null, null, null, null, KeyEvent.VK_X, KeyEvent.VK_Z, null, KeyEvent.VK_SHIFT,KeyEvent.VK_F1);
        loadData();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.this.notify("exit");
                super.windowClosing(e);
            }
        });
    }

    private void loadData() {
        JsonReader jsonReader = new JsonReader(JSON_FILE_LOCATION);
        try {
            HashMap<String, Object> saveData = jsonReader.read();
            scores = (HashMap<String, Integer>) saveData.get("scores");
            handling = (Handling) saveData.get("handling");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void notify(String action) {
        switch(action) {
            case "start":
                SwingUtilities.invokeLater(() -> {
                    TetrisGame tetrisGame = new TetrisGame(this);
                    showPanel(tetrisGame);
                    tetrisGame.requestFocusInWindow();
                });
                break;
            case "scores":
                HighScoresPanel highScoresPanel = new HighScoresPanel(scores);
                showPanel(highScoresPanel);
                break;
            case "controls":
                ControlsPanel controlsPanel = new ControlsPanel(this);
                showPanel(controlsPanel);
                break;
            case "handling":
                HandlingPanel handlingPanel = new HandlingPanel(this);
                showPanel(handlingPanel);
                break;
            case "back":
                MenuPanel menuPanel = new MenuPanel();
                showPanel(menuPanel);
                break;
            default:
                saveData();
                System.exit(0);
        }
    }

    private void saveData() {
        System.out.println("Saving");
        try {
            JsonWriter jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
            jsonWriter.open();
            jsonWriter.write(scores, handling);
            jsonWriter.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to write to file path: " + JSON_FILE_LOCATION);
        }
    }

    private void showPanel(TetrisPanel tetrisPanel) {
        getContentPane().removeAll();
        tetrisPanel.addObserver(this);
        getContentPane().add(tetrisPanel, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
