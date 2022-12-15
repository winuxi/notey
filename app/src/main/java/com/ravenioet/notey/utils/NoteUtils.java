package com.ravenioet.notey.utils;

import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Command;
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
    public void noteCreated(Command command){
        if(noteListener != null){
            noteListener.noteCreated(command);
        }
    }
    public void noteUpdated(Command command){
        if(noteListener != null){
            noteListener.noteUpdated(command);
        }
    }
    public void noteDeleted(Command command){
        if(noteListener != null){
            noteListener.noteDeleted(command);
        }
    }

}
