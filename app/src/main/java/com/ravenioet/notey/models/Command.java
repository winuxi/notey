package com.ravenioet.notey.models;

import com.ravenioet.notey.interfaces.NoteListener;

public class Command {
    Note note;
    NoteListener listener;
    String message;
    public Command(NoteListener listener) {
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
}
