package com.ravenioet.notey.guard;

public class SecPack {
    boolean forUpdate;
    boolean state;
    String message;

    public SecPack(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
