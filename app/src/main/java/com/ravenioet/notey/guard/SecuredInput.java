package com.ravenioet.notey.guard;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.SecuredInputBinding;
import com.ravenioet.notey.init.MainActivity;
import com.ravenioet.notey.init.MainFragment;
import com.ravenioet.notey.utils.NoteUtils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SecuredInput extends MainFragment {

    private SecuredInputBinding binding;
    private TextView one, two, three, four, five, six, seven, eight, nine, keyValue, zero;
    private ImageView finger, clear;
    private RelativeLayout topPanel;
    boolean enable = false, update = false, preFound = false, reset = false;
    String prePin = "1234";
    SecPack secPack;

    public void initPreKey() {
        prePin = getPrefMan().getString("pre-pin");
        if (prePin.equals("Default")) {
            binding.hintValue.setText("Input Pin");
            prePin = "1234";
        } else {
            binding.hintValue.setText("Input Pin");
            preFound = true;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = SecuredInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initPreKey();
        if (getArguments() != null) {
            update = getArguments().getBoolean("update");
            enable = getArguments().getBoolean("enable");
            reset = getArguments().getBoolean("reset");
            if(reset){
                binding.hintValue.setText("Input old Pin");
            }
            secPack = new SecPack(update);
            secPack.setState(enable);
        } else {
            secPack = new SecPack(false);
            secPack.setState(enable);
            secPack.setMessage("Something went wrong");
            NoteUtils.getInstance().bioFailure(secPack);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }

        initKeys();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (secPack.isForUpdate()) {
                        toast("For Update");
                        getRootActivity().onSupportNavigateUp();
                        //Navigation.findNavController(binding.getRoot()).navigateUp();
                    } else {
                        //toast("For Auth");
                        getRootActivity().finish();
                    }
                    return true;
                }
            }
            return false;
        });
        return root;
    }

    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


    }

    private void initKeys() {
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
        topPanel = binding.topPanel;
        initListener();
        if (getRootActivity().isBioAuthEnabled()) {
            initFinger(secPack);
        } else {
            toast("Fingerprint Disabled");
        }
    }

    private String tempStore = "";
    private boolean firstKey = false;
    private void createPinCode(String pin) {
        if (!firstKey) {
            tempStore = pin;
            firstKey = true;
            binding.hintValue.setText("Verify Pin");
            keyValue.setText("");
        } else {
            firstKey = false;
            if (tempStore.equals(pin)) {
                reset = false;
                getPrefMan().putString("pre-pin", pin);
                SecPack secPack = new SecPack(update);
                secPack.setState(enable);
                NoteUtils.getInstance().pinPassed(secPack);
                //getRootActivity().onBackPressed();
                Navigation.findNavController(binding.getRoot()).navigateUp();
            } else {
                binding.hintValue.setText("Pin Dose not match!");
                shakeView();
            }
        }
    }

    private void authPinCode(String keys) {
        if (keys.equals(prePin)) {
            if(reset){
                preFound = false;
                binding.hintValue.setText("Input new Pin");
                firstKey = false;
                binding.keyValue.setText("");
            }else {
                SecPack secPack = new SecPack(update);
                secPack.setState(enable);
                NoteUtils.getInstance().pinPassed(secPack);
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        } else {
            shakeView();
        }
    }

    private void keyWatcher(String keys) {
        if (!preFound) {
            createPinCode(keys);
        } else {
            authPinCode(keys);
        }
    }

    private boolean canVibrate() {
        return getRootActivity().isVibrationEnabled();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getRootActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }

    }

    private void shakeView() {
        if (canVibrate()) {
            vibrate();
        } else {
            //toast("Cat vibrate");
        }
        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        topPanel.startAnimation(animShake);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        getRootActivity().runOnUiThread(() -> keyValue.setText(""));
                    }
                },
                1000
        );
    }

    private void initListener() {
        one.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "1";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        two.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "2";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        three.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "3";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        four.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "4";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        five.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "5";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        six.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "6";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        seven.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "7";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        eight.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "8";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        nine.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "9";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
        });
        finger.setOnClickListener(view -> {
            if (getActivity() != null &&
                    ((MainActivity) getActivity()).isBioAuthEnabled()) {
                toast("Put your finger on sensor");
            } else {
                toast("This feature is disabled!");
            }
        });
        zero.setOnClickListener(view -> {
            String prevValue = keyValue.getText().toString();
            String newValue = prevValue + "0";
            keyValue.setText(newValue);
            if (newValue.length() == 4) keyWatcher(newValue);
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
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRootActivity().getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        getRootActivity().getSupportActionBar().show();
    }

    private KeyStore keyStore;
    // Defining variable for storing
    // key in android keystore container
    private static final String KEY_NAME = "GEEKSFORGEEKS";
    private Cipher cipher;
    private TextView errorText;

    private void initFinger(SecPack secPack) {
        // Initializing KeyguardManager and FingerprintManager
        KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);
        } else {
            return;
        }


        // Initializing our error text

        errorText = binding.hintValue;

        if (!fingerprintManager.isHardwareDetected()) {
            errorText.setText("Device does not support fingerprint sensor");
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                errorText.setText("Fingerprint authentication is not enabled");
            } else {
                // Check for at least one registered finger
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    errorText.setText("Register at least one finger");
                } else {
                    if (!keyguardManager.isKeyguardSecure()) {
                        errorText.setText("Screen lock security not enabled");
                    } else {
                        generateKey();
                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerHandler helper = new FingerHandler(getContext());
                            helper.startAuth(fingerprintManager, cryptoObject, secPack, binding);
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("KeyGenerator instance failed", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Cipher failed", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Cipher initialization failed", e);
        }
    }
}