package com.ravenioet.notey.init;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ravenioet.core.manager.PrefManager;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public MainActivity getRootActivity() {
        return (MainActivity) getActivity();
    }

    public boolean isAnimationEnabled() {
        return getRootActivity().isAnimationEnabled();
    }

    public boolean isGridEnabled() {
        return getRootActivity().isGridEnabled();
    }

    public PrefManager getPrefMan() {
        return getRootActivity().getPrefMan();
    }
}
