package ubb.scs.map.util.observer;


import ubb.scs.map.util.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}
