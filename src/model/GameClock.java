package model;

import lombok.Getter;

public class GameClock {

    private static GameClock gameClock;

    @Getter private long gameTime;
    private long lastTime;
    private boolean paused = false;

    private long pauseDelta;

    private GameClock() {
        gameTime = 0L;
        lastTime = System.currentTimeMillis();
    }

    public void update() {
        if (!paused) {
            long currentTime = System.currentTimeMillis();
            gameTime += currentTime - lastTime;
            lastTime = currentTime;
        }
    }

    public void pause() {
        paused = true;
        pauseDelta = System.currentTimeMillis() - lastTime;
    }

    public void unpause() {
        paused = false;
        lastTime = System.currentTimeMillis() - pauseDelta;
    }

    public static GameClock getInstance() {
        if (gameClock == null) {
            gameClock = new GameClock();
        }
        return gameClock;
    }
}
