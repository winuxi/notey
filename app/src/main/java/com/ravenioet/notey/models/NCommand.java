package com.ravenioet.notey.models;

import com.ravenioet.notey.interfaces.NoteListener;

public class NCommand {
    Note note;
    NoteListener listener;
    String message;
    boolean takeAction = true;
    public NCommand(NoteListener listener) {
        this.listener = listener;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public NoteListener getListener() {
        return listener;
    }

    public void setListener(NoteListener listener) {
        this.listener = listener;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTakeAction() {
        return takeAction;
    }

    public void setTakeAction(boolean takeAction) {
        this.takeAction = takeAction;
    }
}
