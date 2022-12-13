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
            binding.noteDescVal.setText(noteViewModel.getSharedNoteEdit().getBody());
        }
        boolean finalForEdit = forEdit;
        binding.addNote.setOnClickListener(view -> {
            String noteTitle = String.valueOf(binding.noteTitleVal.getText());
            String noteDesc = String.valueOf(binding.noteDescVal.getText());
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
                    publishNote(noteViewModel,note);
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
                            binding.noteDescLay.setError(null);
                        } else {
                            binding.noteDescLay.setError("Note Body Must be more than 10 characters");
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
    public void publishNote(NoteyViewModel noteViewModel,Note note){
        binding.addNote.setText("Processing");
        binding.addNote.setEnabled(false);
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

                        noteViewModel.createNote(note1);
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
    private void showSnack(String message){
        if (binding != null)
            Snackbar.make(binding.getRoot(),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void noteCreated(Note note) {
        Toast.makeText(getContext(),"Note Created",Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigate(R.id.AddEditNote);
    }

    @Override
    public void noteUpdated(Note note) {
        Toast.makeText(getContext(),"Note Updated",Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigate(R.id.AddEditNote);
    }

    @Override
    public void noteDeleted(Note note) {
        Toast.makeText(getContext(),"Note Deleted",Toast.LENGTH_LONG).show();
    }
}