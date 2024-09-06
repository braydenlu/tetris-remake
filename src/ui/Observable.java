package ui;

public interface Observable {

    void addObserver(Observer observer);
    void notifyObservers(String message);
}
