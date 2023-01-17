package com.ravenioet.notey.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravenioet.notey.models.NCommand;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.repository.NoteyRepo;

import java.util.List;

public class NoteyViewModel extends AndroidViewModel {
    private MutableLiveData<Note> note = new MutableLiveData<>();
    //private MutableLiveData<User> user = new MutableLiveData<>();
    private final NoteyRepo noteRepo;
    public NoteyViewModel(@NonNull Application application) {
        super(application);
        noteRepo = new NoteyRepo(application, "init_db");
    }

    public LiveData<List<Note>> getAllNotes(){
        return noteRepo.load_all_notes();
    }
    public LiveData<List<Note>> getSecuredNotes(){
        return noteRepo.load_secured();
    }
    public LiveData<List<Note>> getFromBin(){
        return noteRepo.load_deleted();
    }

    public LiveData<Note> loadOneNote(int noteId){
        return noteRepo.load_one_note(noteId);
    }
    public void createNote(NCommand command){
        noteRepo.save_note(command);
    }
    public void updateNote(NCommand command){
        noteRepo.update_note(command);
    }
    public void addToTrash(NCommand command){
        command.getNote().setFlag(3);
        noteRepo.update_note(command);
    }
    public void secure(NCommand command){
        command.getNote().setFlag(2);
        noteRepo.update_note(command);
    }

    public void restore(NCommand command){
        command.getNote().setFlag(1);
        noteRepo.update_note(command);
    }
    public void setSharedNote(Note note){
        this.note.setValue(note);
    }
    public Note getSharedNote(){
        return note.getValue();
    }

    public void setSharedNoteEdit(Note note){
        this.note.setValue(note);
    }
    public Note getSharedNoteEdit(){
        return note.getValue();
    }
    /*public void setSharedUser(User user){
        this.user.setValue(user);
    }
    public User getSharedUser(){
        return this.user.getValue();
    }
    */

}