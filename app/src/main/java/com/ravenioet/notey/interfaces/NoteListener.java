package com.ravenioet.notey.interfaces;

import com.ravenioet.notey.models.Note;

public interface NoteListener {
    void noteCreated(Note note);
    void noteUpdated(Note note);
    void noteDeleted(Note note);
}
