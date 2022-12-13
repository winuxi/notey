package com.ravenioet.notey.utils;

import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Note;

public class NoteUtils {
    NoteListener noteListener;
    static NoteUtils noteUtils;
    NoteUtils(){};
    public static NoteUtils getInstance(){
        if(noteUtils == null){
            noteUtils = new NoteUtils();
        }
        return noteUtils;
    }
    public void setNoteListener(NoteListener noteListener){
        this.noteListener = noteListener;
    }
    public void noteCreated(Note note){
        if(noteListener != null){
            noteListener.noteCreated(note);
        }
    }
    public void noteUpdated(Note note){
        if(noteListener != null){
            noteListener.noteUpdated(note);
        }
    }
    public void noteDeleted(Note note){
        if(noteListener != null){
            noteListener.noteDeleted(note);
        }
    }

}
