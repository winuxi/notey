package com.ravenioet.notey.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.NoteDetailBinding;
import com.ravenioet.notey.databinding.SecuredInputBinding;
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

import java.util.Random;

public class SecuredInput extends Fragment {

    private SecuredInputBinding binding;
    private TextView one,two,three,four,five,six,seven,eight,nine,keyValue,zero;
    private ImageView finger,clear;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = SecuredInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initKeys();
        return root;
    }
    private void initKeys(){
        keyValue = binding.keyValue;
        keyValue.setText("");
        one = binding.one;
        two = binding.two;
        three = binding.three;
        four = binding.four;
        five = binding.five;
        six = binding.six;
        seven = binding.seven;
        eight = binding.eight;
        nine = binding.nine;
        zero = binding.zero;
        finger = binding.finger;
        clear = binding.clear;
        initListener();
    }
    private void initListener(){
        one.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"1";
            keyValue.setText(newValue);
        });
        two.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"2";
            keyValue.setText(newValue);
        });
        three.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"3";
            keyValue.setText(newValue);
        });
        four.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"4";
            keyValue.setText(newValue);
        });
        five.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"5";
            keyValue.setText(newValue);
        });
        six.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"6";
            keyValue.setText(newValue);
        });
        seven.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"7";
            keyValue.setText(newValue);
        });
        eight.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"8";
            keyValue.setText(newValue);
        });
        nine.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"9";
            keyValue.setText(newValue);
        });
        finger.setOnClickListener(view -> {
            //keyValue.setText("1");
        });
        zero.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue+"0";
            keyValue.setText(newValue);
        });
        clear.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = clear(prevValue);
            keyValue.setText(newValue);
        });
        clear.setOnLongClickListener(view -> {
            String newValue = "";
            keyValue.setText(newValue);
            return false;
        });

    }
    public String clear(String str) {
        if (str != null && str.length() > 0){
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}