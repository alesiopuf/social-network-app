package ubb.scs.map.util.events;


import ubb.scs.map.domain.Friendship;

public class UserEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Friendship data, oldData;

    public UserEntityChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public UserEntityChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}