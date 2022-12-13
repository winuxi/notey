package com.ravenioet.notey.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.ravenioet.notey.database.NoteDao;
import com.ravenioet.notey.database.NoteyDB;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteyRepo {
    private final NoteDao noteDao;
    public boolean note_loader = false;
    static public Application application;

    public NoteyRepo(Application application, String user_spec) {
        NoteyDB noteDB = NoteyDB.getInstance(application, user_spec);
        noteDao = noteDB.noteDao();
        NoteyRepo.application = application;
    }

    //notes
    public void insert_notes(List<Note> notes) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            noteDao.save_bach_note(notes);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public LiveData<List<Note>> load_all_notes() {
        //sync_notes();
        return noteDao.load_all_notes();
    }

    public LiveData<List<Note>> load_cart() {
        return noteDao.load_cart();
    }
    public LiveData<List<Note>> load_shelf() {
        return noteDao.load_shelf();
    }

    public LiveData<Note> load_one_note(int noteId) {
        return noteDao.load_one_note(noteId);
    }

    public void update_note(Command command) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            noteDao.save_note(command.getNote());
            handler.post(() -> {
                command.getListener().noteUpdated(command.getNote());
            });
        });
    }

    public void save_note(Command command) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            noteDao.save_note(command.getNote());
            handler.post(() -> {
                command.getListener().noteCreated(command.getNote());
            });
        });

    }


    /*public void sync_notes() {
        if (!note_loader) {
            note_loader = true;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UserManager.loadIp(application))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            APIProvider service = retrofit.create(APIProvider.class);
            Call<List<Note>> call = null;

            call = service.loadBooks();
            call.enqueue(new Callback<List<Note>>() {
                @Override
                public void onResponse(@NotNull Call<List<Note>> call, @NotNull Response<List<Note>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().size() > 0) {
                            Log.d("notes", "response size: "+response.body().size());
                            List<Note> notes = response.body();
                            for (Note note : notes) {
                                note.setFlag(1);
                                //update_note(note);
                            }
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());
                            executor.execute(() -> {
                                //Background work here
                                noteDao.save_bach_note(notes);
                                handler.post(() -> {
                                    //UI Thread work here
                                });
                            });
                            note_loader = false;
                        } else {
                            Log.d("notes", "no notes");
                            note_loader = false;
                        }
                    } else {
                        Log.d("notes", response.code() + " code");
                        note_loader = false;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<Note>> call, @NotNull Throwable t) {
                    Log.d("notes", "error: " + t);
                    note_loader = false;
                }
            });
        }
    }
    */


}
