package com.ravenioet.notey.screens;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.components.layouts.NoteyListCard;
import com.ravenioet.notey.components.menus.MenuItem;
import com.ravenioet.notey.components.layouts.NoteyRelativeLayout;
import com.ravenioet.notey.components.provider.ThemeProvider;
import com.ravenioet.notey.components.theme.NoteyTheme;
import com.ravenioet.notey.components.theme.Themes;
import com.ravenioet.notey.components.widget.NoteyTextView;
import com.ravenioet.notey.databinding.SettingsBinding;
import com.ravenioet.notey.init.MainFragment;

public class Settings extends MainFragment {

    private SettingsBinding binding;
    boolean bioPass, pinCode, listAnim, animFirst = true, viewFirst = true, listFirst = true, vibrate;
    String viewType, listType;
    @SuppressLint("ResourceType")
    public void initThemeTest(){

        RelativeLayout.LayoutParams darkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        NoteyRelativeLayout dark = new NoteyRelativeLayout(getRootActivity());
        dark.setId(1);
        NoteyTextView darkWriter = new NoteyTextView(getRootActivity());
        darkWriter.setCharacterDelay(10);
        //darkParams.addRule(RelativeLayout.BELOW,dark.getId());
        darkWriter.animateText("This is awesome android magic with java... " +
                "This should be relatively simple from what I have read but it just doesn't seem to work. For reference sakes I am developing on 2.1-update1");
        dark.addView(darkWriter);
        binding.topPanel.addView(dark);

        RelativeLayout.LayoutParams blueParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        NoteyRelativeLayout blue = new NoteyRelativeLayout(getRootActivity());
        blue.setId(2);
        NoteyTextView blueWriter = new NoteyTextView(getRootActivity());
        blueWriter.setCharacterDelay(10);
        blueParams.addRule(RelativeLayout.BELOW,dark.getId());
        blueWriter.animateText("This is awesome android magic with this color is created by great work of team, t still does not work with a TextView");
        blue.addView(blueWriter);
        //binding.topPanel.addView(blue);

        RelativeLayout.LayoutParams orangeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        NoteyRelativeLayout orange = new NoteyRelativeLayout(getRootActivity());
        NoteyTextView orangeWriter = new NoteyTextView(getRootActivity());
        orangeParams.addRule(RelativeLayout.BELOW,blue.getId());
        orangeWriter.setCharacterDelay(10);
        orange.setId(3);
        orangeWriter.animateText("This is awesome android magic with java...t still does not work with a TextView inside the layout. Again, if I apply a static color rather than a drawable it works just fine." +
                " One thing I have noticed is I can (sometimes) get it to work using a selector but that shouldn't be necessary from my understanding");
        orange.addView(orangeWriter);
        //binding.topPanel.addView(orange);
        binding.dark.setOnClickListener(view -> {
            /*NoteyTheme darkTheme13 = new NoteyTheme();
            darkTheme13.setActiveTheme(Themes.dark);
            binding.getRoot().setBackgroundColor(darkTheme13.getActiveTheme().getPrimaryDark());
            getRootActivity().recreate();*/
        });
        binding.blue.setOnClickListener(view -> {
           /* NoteyTheme blueTheme12 = new NoteyTheme();
            blueTheme12.setActiveTheme(Themes.blue);
            binding.getRoot().setBackgroundColor(blueTheme12.getActiveTheme().getPrimaryDark());
            getRootActivity().recreate();*/
        });
        binding.orange.setOnClickListener(view -> {
            /*NoteyTheme orangeTheme1 = new NoteyTheme();
            orangeTheme1.setActiveTheme(Themes.orange);
            binding.getRoot().setBackgroundColor(orangeTheme1.getActiveTheme().getPrimaryDark());
            getRootActivity().recreate();*/
        });
    }
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //User user = UserManager.loadUser(getContext());
        binding = SettingsBinding.inflate(inflater, container, false);
        binding.getRoot().setBackgroundColor(ThemeProvider.getMainTheme().getPrimaryDark());
        RelativeLayout root = binding.getRoot();
        initThemeTest();
        MenuItem menuItem = new MenuItem(getRootActivity());
        menuItem.setId(102);
        menuItem.setTitle("App info");
        menuItem.setHint("click me please");
        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.onClick();
            }
        });
        menuItem.below(binding.theme.getId());
        binding.securitySettings.addView(menuItem);

        NoteyListCard noteyListCard = new NoteyListCard(getRootActivity());
        noteyListCard.setImage(R.drawable.ic_baseline_sticky_note_2_24);
        noteyListCard.setTitle("Wait what");
        noteyListCard.setBody("This is view created by dynamically");
        noteyListCard.below(menuItem.getId());
        binding.securitySettings.addView(noteyListCard);
        initPrefMan();
        binding.bioPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (getPrefMan().putBoolean("bio-pass", b)) {
                    if (b) {
                        toast("Biometric Auth enabled");
                    } else {
                        toast("Biometric Auth disabled");
                    }
                }

            }
        });
        binding.listAnim.setOnCheckedChangeListener((compoundButton, b) -> {
            if (getPrefMan().putBoolean("list-anim", b)) {
                if (b) {
                    toast("Animation enabled");
                } else {
                    toast("Animation disabled");
                }
            }
        });
        binding.vibrate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (getPrefMan().putBoolean("vibrate", b)) {
                if (b) {
                    toast("Animation enabled");
                } else {
                    toast("Animation disabled");
                }
            }
        });

        binding.pinLock.setOnCheckedChangeListener((compoundButton, b) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("enable", b);
            bundle.putBoolean("update", true);
            bundle.putBoolean("reset", false);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.NavSecuredInput, bundle);
        });
        binding.pinReset.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("enable", false);
            bundle.putBoolean("update", true);
            bundle.putBoolean("reset", true);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.NavSecuredInput, bundle);
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
                    getPrefMan().putString("list-type", items[i]);
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
                        getPrefMan().putString("view-type", views[i]);
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
        bioPass = getPrefMan().getBoolean("bio-pass");
        pinCode = getPrefMan().getBoolean("pin-code");
        listAnim = getPrefMan().getBoolean("list-anim");
        viewType = getPrefMan().getString("view-type");
        listType = getPrefMan().getString("list-type");
        vibrate = getPrefMan().getBoolean("vibrate");
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
        binding.bioPass.setChecked(bioPass);
        binding.pinLock.setChecked(pinCode);
        binding.listAnim.setChecked(listAnim);
        binding.vibrate.setChecked(vibrate);
    }

    public String getSelectedListType() {
        log("setting-u", getPrefMan().getString("list-type"));
        if (getPrefMan().getString("list-type").equals("default")) {
            return "Public";
        }
        return getPrefMan().getString("list-type");
    }

    public int getSelectedViewType() {
        switch (getPrefMan().getString("view-type")) {
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