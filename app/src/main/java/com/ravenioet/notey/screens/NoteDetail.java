package com.ravenioet.notey.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.NoteDetailBinding;
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

import java.util.Random;

public class NoteDetail extends Fragment implements NoteListener {

    private NoteDetailBinding binding;
    private NoteyViewModel bookViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookViewModel =
                new ViewModelProvider(requireActivity()).get(NoteyViewModel.class);
        //User user = UserManager.loadUser(getContext());
        binding = NoteDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Note note = bookViewModel.getSharedNote();
        if(note != null) {
            /*if(book.getUser_id().equals(user.getId())){
                binding.toEdit.setVisibility(View.VISIBLE);
            }else {
                binding.toEdit.setVisibility(View.GONE);
            }*/
            binding.toEdit.setVisibility(View.VISIBLE);
            bookViewModel.loadOneNote(note.getId()).observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note book) {
                    binding.bookTitle.setText(book.getTitle());
                    binding.bookDesc.setText(book.getBody());
                    //binding.auther.setText(book.getPublisher_name());
                    //binding.publishedDate.setText(book.getPublished_date());
                    //binding.downloads.setText(getRandomNumberDownloads());
                    //binding.bookPrice.setText(book.getPrice() +".ETB");
                    /*RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_baseline_menu_book_24)
                            .error(R.drawable.ic_baseline_menu_book_24)*//*.circleCrop()*//*;
                            */
                    /*Glide.with(getContext()).load(UserManager.loadIp(getContext())+book.getCover_image())
                            .apply(options).into(binding.bookPic);
                    Glide.with(getContext()).load(UserManager.loadIp(getContext())+book.getPublisher_photo())
                            .apply(options).into(binding.puber);
                    */
                }
            });
        }
        binding.delete.setOnClickListener(view -> {
            if(note != null){
                Command command = new Command(this);
                command.setNote(note);
                bookViewModel.addToTrash(command);
            }

        });
        binding.toEdit.setOnClickListener(view -> {
            bookViewModel.setSharedNoteEdit(note);
            Navigation.findNavController(root).navigate(R.id.AddEditNote);
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        bookViewModel.setSharedNote(null);
    }
    public String getRandomNumberDownloads() {
        Random rnd = new Random();
        return String.valueOf(rnd.nextInt(999999));
    }

    @Override
    public void noteCreated(Note note) {

    }

    @Override
    public void noteUpdated(Note note) {

    }

    @Override
    public void noteDeleted(Note note) {

    }
}