package com.get.lucky.lt.views.main;

import com.get.lucky.lt.views.cam_fragment.CamFragment;
import com.get.lucky.lt.views.keyboard_fragment.KeyboardFragment;

public interface MainViewInterface {
    CamFragment getCamFragment();

    KeyboardFragment getKeyboardFragment();

    void freezeButtonCam();

    void startCamWork();

    void setLuckyTheme();

    void setNonLuckyTheme();

    void emptyPic();

    void emptyIntArrayTheme();

    void setDefaultButton();

    void errorMessage();
}
