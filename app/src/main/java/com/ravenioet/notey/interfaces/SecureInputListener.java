package com.ravenioet.notey.interfaces;

import com.ravenioet.notey.guard.SecPack;

public interface SecureInputListener {
    void pinPassed(SecPack secPack);
    void bioSuccess(SecPack secPack);
    void bioFailure(SecPack secPack);
}
