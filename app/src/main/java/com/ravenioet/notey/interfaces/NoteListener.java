package com.ravenioet.notey.interfaces;

import com.ravenioet.notey.models.NCommand;

public interface NoteListener {
    void noteCreated(NCommand note);
    void noteUpdated(NCommand note);
    void noteDeleted(NCommand note);
    void error(String error);
}
