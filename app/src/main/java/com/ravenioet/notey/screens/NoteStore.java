package com.ravenioet.notey.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravenioet.notey.R;
import com.ravenioet.notey.adapters.NoteAdapter;
import com.ravenioet.notey.databinding.FragmentHomeBinding;
import com.ravenioet.notey.init.MainFragment;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

public class NoteStore extends MainFragment {

    private FragmentHomeBinding binding;
    private ProgressBar progressBar;
    NoteAdapter bookAdapter;
    NoteyViewModel noteyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noteyViewModel =
                new ViewModelProvider(requireActivity()).get(NoteyViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //prefManager = PrefManager.getPrefMan(getActivity(), "notey-pref");
        View root = binding.getRoot();
        RecyclerView news_list = root.findViewById(R.id.new_recycler);
        progressBar = root.findViewById(R.id.progress);
        TextView no_books = root.findViewById(R.id.no_recent_books);
        if (isGridEnabled()) {
            binding.newRecycler.setLayoutManager(new GridLayoutManager(
                    getContext(), 2
            ));
        } else {
            binding.newRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        news_list.setHasFixedSize(true);
        bookAdapter = new NoteAdapter(getContext(), 2, 2, isAnimationEnabled());
        news_list.setAdapter(bookAdapter);

        noteyViewModel.getAllNotes().observe(getViewLifecycleOwner(), books -> {
            bookAdapter.setNote(books);
            progressBar.setVisibility(View.GONE);
            if (books.size() == 0) {
                no_books.setVisibility(View.GONE);
            } else {
                no_books.setVisibility(View.GONE);
            }
        });

        bookAdapter.onItemClickListener(note -> {
            noteyViewModel.setSharedNote(note);
            //Navigation.findNavController(root).navigate(R.id.NoteDetail);
            Navigation.findNavController(root).navigate(R.id.AddEditNote);
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.AddEditNote);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}