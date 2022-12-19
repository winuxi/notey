package com.ravenioet.notey.guard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.SecuredInputBinding;
import com.ravenioet.notey.utils.NoteUtils;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    // Constructor
    public FingerHandler(Context mContext) {
        context = mContext;
    }
    SecPack secPack;
    private SecuredInputBinding binding;
    // Fingerprint authentication starts here..
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject,SecPack secPack, SecuredInputBinding binding) {
        this.secPack = secPack;
        this.binding = binding;
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    // On authentication failed
    @Override
    public void onAuthenticationFailed() {
        //NoteUtils.getInstance().bioFailure("Not Recognized");
        binding.hintValue.setText("Not Recognized");
        binding.hintValue.setTextColor(Color.RED);
    }

    // On successful authentication
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        binding.hintValue.setText("Authorized");
        binding.hintPanel.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
        NoteUtils.getInstance().bioSuccess(secPack);
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }
}
