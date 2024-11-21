package ubb.scs.map.util.observer;


import ubb.scs.map.util.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}