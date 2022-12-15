package com.ravenioet.notey.interfaces;

import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;

public interface NoteListener {
    void noteCreated(Command note);
    void noteUpdated(Command note);
    void noteDeleted(Command note);
    void error(String error);
}
