package com.ravenioet.notey.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

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

    public LiveData<List<Note>> load_secured() {
        return noteDao.load_secured();
    }
    public LiveData<List<Note>> load_deleted() {
        return noteDao.load_deleted();
    }

    public LiveData<Note> load_one_note(int noteId) {
        return noteDao.load_one_note(noteId);
    }

    public void update_note(Command command) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            int x = 0;
            try {
                x = noteDao.update_note(command.getNote());
            }catch (Exception e){
                command.getListener().error(e.toString());
            }
            int finalX = x;
            handler.post(() -> {
                if(finalX > 0){
                    command.setMessage(finalX +" notes updated");
                    command.getListener().noteUpdated(command);
                }else {
                    command.getListener().error("Unable to update");
                }
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
                command.getListener().noteCreated(command);
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

    public void publishNote( Note note){
        /*Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(UserManager.loadIp(getContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIProvider service = retrofit.create(APIProvider.class);
        File file = new File(Uri.parse(note.getCover_image()).getPath());
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part photo = MultipartBody.Part.createFormData("cover", file.getName(), requestFile);
        RequestBody title = MultipartBody.create(note.getTitle(), MediaType.parse("text/plain"));
        RequestBody description = MultipartBody.create(note.getDescription(), MediaType.parse("text/plain"));
        RequestBody author = MultipartBody.create(note.getAuthor(), MediaType.parse("text/plain"));
        RequestBody price = MultipartBody.create(note.getPrice(), MediaType.parse("text/plain"));
        RequestBody publisher = MultipartBody.create(note.getPublisher_name(), MediaType.parse("text/plain"));
        RequestBody date = MultipartBody.create(note.getPublished_date(), MediaType.parse("text/plain"));
        RequestBody publisher_pic = MultipartBody.create(note.getPublisher_photo(), MediaType.parse("text/plain"));
        RequestBody target = MultipartBody.create("add_note", MediaType.parse("text/plain"));
        RequestBody userId = MultipartBody.create(note.getUser_id(), MediaType.parse("text/plain"));

        Call<Status> call = service.addNotePro(
                target,
                userId,
                title,
                description,
                author,
                price,
                publisher,
                date,
                publisher_pic,
                photo
        );


        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(@NotNull Call<Status> call, @NotNull Response<Status> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.addNote.setText("Publish Note");
                    binding.addNote.setEnabled(true);
                    Status status = response.body();
                    if (status.isFlag()) {
                        Note note1 = status.getNote();
                        note1.setFlag(1);

                        createNote(note1);
                    } else {
                        showSnack("Error "+status.getError_message());
                        Log.d("notes", status.getError_message());
                    }
                } else {
                    Log.d("notes", response.code() + " code");
                    binding.addNote.setText("Publish Note");
                    binding.addNote.setEnabled(true);
                    showSnack("Unable to process, code: "+response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Status> call, @NotNull Throwable t) {
                Log.d("notes", "error: " + t);
                binding.addNote.setText("Publish Note");
                binding.addNote.setEnabled(true);
                showSnack("Network timeout");
            }
        });*/
    }

}
