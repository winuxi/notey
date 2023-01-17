package com.ravenioet.notey.utils;

import com.ravenioet.notey.guard.SecPack;
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.interfaces.SecureInputListener;
import com.ravenioet.notey.models.NCommand;

public class NoteUtils {
    NoteListener noteListener;
    SecureInputListener secureInputListener;
    static NoteUtils noteUtils;
    NoteUtils(){};
    public static NoteUtils getInstance(){
        if(noteUtils == null){
            noteUtils = new NoteUtils();
        }
        return noteUtils;
    }
    public void setSecKeyListener(SecureInputListener keyListener){
        this.secureInputListener = keyListener;
    }
    public void setNoteListener(NoteListener noteListener){
        this.noteListener = noteListener;
    }

    public void noteCreated(NCommand command){
        if(noteListener != null){
            noteListener.noteCreated(command);
        }
    }
    public void noteUpdated(NCommand command){
        if(noteListener != null){
            noteListener.noteUpdated(command);
        }
    }
    public void noteDeleted(NCommand command){
        if(noteListener != null){
            noteListener.noteDeleted(command);
        }
    }
    public void pinPassed(SecPack secPack){
        if(secureInputListener != null){
            secureInputListener.pinPassed(secPack);
        }
    }
    public void bioSuccess(SecPack secPack){
        if(secureInputListener != null){
            secureInputListener.bioSuccess(secPack);
        }
    }
    public void bioFailure(SecPack secPack){
        if(secureInputListener != null){
            secureInputListener.bioFailure(secPack);
        }
    }



}
