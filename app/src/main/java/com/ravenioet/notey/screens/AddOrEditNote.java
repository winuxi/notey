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
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddOrEditNote extends Fragment implements NoteListener {

    private AddEditNoteBinding binding;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NoteyViewModel noteViewModel =
                new ViewModelProvider(requireActivity()).get(NoteyViewModel.class);
        binding = AddEditNoteBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        boolean forEdit = false;
        if (noteViewModel.getSharedNoteEdit() != null) {
            forEdit = true;
            binding.noteTitleVal.setText(noteViewModel.getSharedNoteEdit().getTitle());
            binding.noteBody.setText(noteViewModel.getSharedNoteEdit().getBody());
        }
        boolean finalForEdit = forEdit;
        binding.addNote.setOnClickListener(view -> {
            String noteTitle = String.valueOf(binding.noteTitleVal.getText());
            String noteDesc = String.valueOf(binding.noteBody.getText());
            if (isNotEmpty(noteTitle) && isMoreThan4(noteTitle) &&
                    isNotEmpty(noteDesc) && isMoreThan10(noteDesc)
                    && imagedLoaded && isNotEmpty(resultUri.toString())) {
                Log.d("addNote","passed");
                //User user = UserManager.loadUser(getContext());
                /*Note note = new Note(1, noteTitle,
                        noteDesc, resultUri.toString(),
                        "user.getId()", author,
                        pubDate, price,
                        String.valueOf(getRandomNumberDownloads()),
                        author, user.getPhoto());
                */
                Note note = new Note(noteTitle,noteDesc,"date","",1);

                if (finalForEdit) {
                    note.setId(noteViewModel.getSharedNoteEdit().getId());
                    Command command = new Command(this);
                    command.setNote(note);
                    noteViewModel.updateNote(command);
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                } else {
                    Command command = new Command(this);
                    command.setNote(note);
                    noteViewModel.createNote(command);
                }
                //Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_home);
            } else {
                if (imagedLoaded) {
                    if (isNotEmpty(noteTitle) && isMoreThan4(noteTitle)) {
                        binding.noteTitleLay.setError(null);
                        if (isNotEmpty(noteDesc) && isMoreThan10(noteDesc)) {
                            //author
                        } else {
                            showSnack("Note Body Must be more than 10 characters");
                        }
                    } else {
                        binding.noteTitleLay.setError("Note Title Must be more than 3 characters");
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Please choose cover image", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*binding.profilePic.setOnClickListener(view -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(6,4)
                .start(getContext(),this));
        */
        return root;
    }
    public int getRandomNumberDownloads() {
        Random rnd = new Random();
        return rnd.nextInt(999999);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
    private void showSnack(String message){
        if (binding != null)
            Snackbar.make(binding.getRoot(),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void noteCreated(Note note) {
        Toast.makeText(getContext(),"Note Created",Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void noteUpdated(Note note) {
        Toast.makeText(getContext(),"Note Updated",Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void noteDeleted(Note note) {
        Toast.makeText(getContext(),"Note Deleted",Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigateUp();
    }
}