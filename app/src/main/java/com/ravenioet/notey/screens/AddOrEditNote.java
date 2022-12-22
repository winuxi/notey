package com.ravenioet.notey.screens;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ravenioet.notey.R;
import com.ravenioet.notey.database.APIProvider;
import com.ravenioet.notey.databinding.AddEditNoteBinding;
import com.ravenioet.notey.init.ChildFragment;
import com.ravenioet.notey.init.MainFragment;
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddOrEditNote extends ChildFragment implements NoteListener {

    private AddEditNoteBinding binding;
    Note note;
    Note newNote;
    View root;
    NoteyViewModel noteViewModel;
    NoteListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noteViewModel =
                new ViewModelProvider(requireActivity()).get(NoteyViewModel.class);
        binding = AddEditNoteBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        listener = this;
        boolean forEdit = false;
        if (noteViewModel.getSharedNoteEdit() != null) {
            forEdit = true;
            if (isAutoSaveMode()) {
                initAutoSave();
            }
            note = noteViewModel.getSharedNoteEdit();
            nativeFlag = note.getFlag();
            binding.noteTitle.setText(note.getTitle());
            binding.noteBody.setText(note.getBody());
            if (note.getFlag() == 2) {
                binding.icLock.setImageResource(R.drawable.ic_baseline_lock_24);
            } else if (note.getFlag() == 1) {
                binding.icLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
            } else if (note.getFlag() == 3) {
                binding.icDelete.setImageResource(R.drawable.ic_baseline_restore_from_trash_24);
                binding.icLock.setVisibility(View.GONE);
                binding.icSave.setVisibility(View.GONE);
            } else {

            }


        } else {
            note = new Note("", "", "date", "", 1);
            nativeFlag = note.getFlag();
        }
        if (!forEdit) {
            //binding.icLock.setVisibility(View.GONE);
            binding.icDelete.setVisibility(View.GONE);
        }
        boolean finalForEdit = forEdit;
        binding.icSave.setOnClickListener(view -> {
            String noteTitle = String.valueOf(binding.noteTitle.getText()).trim();
            String noteDesc = String.valueOf(binding.noteBody.getText()).trim();
            if (isNotEmpty(noteTitle) && isMoreThan4(noteTitle) &&
                    isNotEmpty(noteDesc) && isMoreThan10(noteDesc)
                    && imagedLoaded && isNotEmpty(resultUri.toString())) {
                Log.d("addNote", "passed");
                //User user = UserManager.loadUser(getContext());
                /*Note note = new Note(1, noteTitle,
                        noteDesc, resultUri.toString(),
                        "user.getId()", author,
                        pubDate, price,
                        String.valueOf(getRandomNumberDownloads()),
                        author, user.getPhoto());
                */

                if (finalForEdit) {
                    newNote = new Note(noteTitle, noteDesc, "date", "", note.getFlag());
                    updateNote(newNote);
                } else {
                    note = new Note(noteTitle, noteDesc, "date", "", nativeFlag);
                    Command command = new Command(listener);
                    command.setNote(note);
                    noteViewModel.createNote(command);
                }
            } else {
                if (imagedLoaded) {
                    if (isNotEmpty(noteTitle) && isMoreThan4(noteTitle)) {
                        if (isNotEmpty(noteDesc) && isMoreThan10(noteDesc)) {
                            //author
                        } else {
                            showSnack("Note Body Must be more than 10 characters");
                        }
                    } else {
                        showSnack("Note Title Must be more than 3 characters");
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Please choose cover image", Toast.LENGTH_LONG).show();
                }
            }
        });

        boolean finalForEdit1 = forEdit;
        binding.icDelete.setOnClickListener(view -> {
            if (finalForEdit1) {
                Command command = new Command(listener);
                command.setNote(note);
                if (note.getFlag() == 3) {
                    noteViewModel.restore(command);
                } else {
                    noteViewModel.addToTrash(command);
                }
            }
        });
        binding.icLock.setOnClickListener(view -> {
            nativeFlag = note.getFlag();
            if (nativeFlag == 2) {
                note.setFlag(1);
                binding.icLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                showSnack("Note Unsecured");
                Command command = new Command(listener);
                command.setNote(note);
                noteViewModel.restore(command);
            }
            if (nativeFlag == 1) {
                note.setFlag(2);
                binding.icLock.setImageResource(R.drawable.ic_baseline_lock_24);
                showSnack("Note Secured");
                Command command = new Command(listener);
                command.setNote(note);
                noteViewModel.secure(command);
            }
        });

        return root;
    }

    int nativeFlag = -1;

    public int getRandomNumberDownloads() {
        Random rnd = new Random();
        return rnd.nextInt(999999);
    }

    Timer autoPusher;
    boolean fromAuto = false;
    public void initAutoSave() {
        autoPusher = new Timer();
        autoPusher.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        getRootActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fromAuto = true;
                                binding.icSave.callOnClick();
                                initAutoSave();
                            }
                        });
                    }
                },
                30000
        );
    }
    public void updateNote(Note newNote){
        Command command = new Command(listener);
        if (note.isDiffer(newNote)) {
            newNote.setId(note.getId());
            command.setNote(newNote);
            note = newNote;
            if(fromAuto) command.setTakeAction(false);
            noteViewModel.updateNote(command);
        } else {
            if(!fromAuto){
                showSnack("Nothing to update");
            }else {
                showSnack("Silent");
                fromAuto = false;
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(autoPusher != null){
            autoPusher.cancel();
        }
        noteViewModel.setSharedNote(null);
    }

    private boolean isNotEmpty(String value) {
        return !value.equals("");
    }

    private boolean isMoreThan4(String value) {
        return value.length() > 3;
    }

    private boolean isMoreThan10(String value) {
        return value.length() > 10;
    }

    private boolean imagedLoaded = true;
    private Uri resultUri = Uri.parse("holero");

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                RequestOptions options = new RequestOptions();
                Glide.with(this).load(resultUri).apply(options).into(binding.profilePic);
                imagedLoaded = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        */
    }

    private void showSnack(String message) {
        if (binding != null)
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void noteCreated(Command command) {
        showSnack(command.getMessage());
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void noteUpdated(Command command) {
        if(command.isTakeAction()){
            showSnack(command.getMessage());
            Navigation.findNavController(root).navigateUp();
        }else {
            showSnack("here");
        }
    }

    @Override
    public void noteDeleted(Command command) {
        showSnack(command.getMessage());
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void error(String error) {
        //error = note.getId()+"-"+error;
        showSnack(error);
    }


}