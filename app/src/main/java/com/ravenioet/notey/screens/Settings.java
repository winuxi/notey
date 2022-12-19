package com.ravenioet.notey.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.components.MenuItem;
import com.ravenioet.notey.databinding.NoteDetailBinding;
import com.ravenioet.notey.databinding.SettingsBinding;
import com.ravenioet.notey.guard.SecPack;
import com.ravenioet.notey.init.MainActivity;
import com.ravenioet.notey.init.MainFragment;
import com.ravenioet.notey.interfaces.NoteListener;
import com.ravenioet.notey.interfaces.SecureInputListener;
import com.ravenioet.notey.models.Command;
import com.ravenioet.notey.models.Note;
import com.ravenioet.notey.utils.NoteUtils;
import com.ravenioet.notey.utils.PrefManager;
import com.ravenioet.notey.viewmodel.NoteyViewModel;

import java.util.Random;

public class Settings extends MainFragment {

    private SettingsBinding binding;
    PrefManager prefManager;
    boolean bioPass, pinCode, listAnim, animFirst = true, viewFirst = true, listFirst = true;
    String viewType, listType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //User user = UserManager.loadUser(getContext());
        binding = SettingsBinding.inflate(inflater, container, false);
        RelativeLayout root = binding.getRoot();
        if (getActivity() != null) {
            prefManager = ((MainActivity) getActivity()).getPrefMan();
        } else {
            prefManager = PrefManager.getPrefMan(getActivity(), "notey-pref");
        }
        MenuItem menuItem = new MenuItem(getContext(), "Info", "list of app infos");
        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.onClick();
            }
        });
        //root.addView(menuItem);
        initPrefMan();
        binding.bioPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (prefManager.putBoolean("bio-pass", b)) {
                    if (b) {
                        toast("Biometric Auth enabled");
                    } else {
                        toast("Biometric Auth disabled");
                    }
                }

            }
        });
        binding.listAnim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (prefManager.putBoolean("list-anim", b)) {
                    if (b) {
                        toast("Animation enabled");
                    } else {
                        toast("Animation disabled");
                    }
                }
            }
        });

        binding.pinLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("enable", b);
                bundle.putBoolean("update", true);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.NavSecuredInput, bundle);
            }
        });
        Spinner listType = binding.listType;
        String[] items = new String[]{"Public", "Secured", "Trash Bin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        listType.setAdapter(adapter);
        listType.setSelection(adapter.getPosition(getSelectedListType()));
        listType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!listFirst) {
                    prefManager.putString("list-type", items[i]);
                } else {
                    listFirst = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner viewType = binding.viewType;
        String[] views = new String[]{"List", "Grid"};
        ArrayAdapter<String> viewAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, views);
        viewType.setAdapter(viewAdapter);
        viewType.setSelection(getSelectedViewType());
        viewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                log("saving", "position " + i);
                if (!viewFirst) {
                    if (getSelectedViewType() != i) {
                        prefManager.putString("view-type", views[i]);
                    } else {
                        log("saving", "passing " + views[i]);
                    }
                } else {
                    viewFirst = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                log("saving", "passing ");
            }
        });

        return root;
    }

    private void toast(String message) {
        //Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    private void log(String tag, String message) {
        Log.d(tag, message);
    }

    private void initPrefMan() {
        bioPass = prefManager.getBoolean("bio-pass");
        pinCode = prefManager.getBoolean("pin-code");
        listAnim = prefManager.getBoolean("list-anim");
        viewType = prefManager.getString("view-type");
        listType = prefManager.getString("list-type");
        if (bioPass) {
            log("saved", " bio-enabled");
        } else {
            log("saved", " bio-disabled");
        }
        if (pinCode) {
            log("saved", " pin-enabled");
        } else {
            log("saved", " pin-disabled");
        }
        log("saved", "list-type " + prefManager.getString("list-type"));
        log("saved", "view-type " + prefManager.getString("view-type"));
        binding.bioPass.setChecked(bioPass);
        binding.pinLock.setChecked(pinCode);
        binding.listAnim.setChecked(listAnim);
    }

    public String getSelectedListType() {
        log("setting-u", prefManager.getString("list-type"));
        if (prefManager.getString("list-type").equals("default")) {
            return "Public";
        }
        return prefManager.getString("list-type");
        /*switch (prefManager.getString("list-type")) {
            case "Public":
                return 0;
            case "Secured":
                return 1;
            case "Trash Bin":
                return 2;

        }
        return 0;*/
    }

    public int getSelectedViewType() {
        switch (prefManager.getString("view-type")) {
            case "List":
                return 0;
            case "Grid":
                return 1;
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}