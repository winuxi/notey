package com.ravenioet.notey.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ravenioet.notey.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<Note> load_one_note(int id);

    @Query("SELECT * FROM notes where flag == 1 order by id desc")
    LiveData<List<Note>> load_all_notes();

    @Query("SELECT * FROM notes where flag == 2")
    LiveData<List<Note>> load_secured();
    @Query("SELECT * FROM notes where flag == 3")
    LiveData<List<Note>> load_deleted();

    @Update
    int update_note(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save_note(Note note);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void save_bach_note(List<Note> notes);

}