package ui.gamedata;

public enum Control {
    MOVE_LEFT("Move Left"),
    MOVE_RIGHT("Move Right"),
    SOFT_DROP("Soft Drop"),
    HARD_DROP("Hard Drop"),
    ROTATE_CLOCKWISE("Rotate Clockwise"),
    ROTATE_COUNTERCLOCKWISE("Rotate Counterclockwise"),
    ROTATE_180("Rotate 180 Degrees"),
    HOLD("Hold"),
    PAUSE("Pause");

    private final String controlName;

    Control(String controlName) {
        this.controlName = controlName;
    }

    @Override
    public String toString() {
        return controlName;
    }
}
