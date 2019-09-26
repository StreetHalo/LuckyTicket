package com.get.lucky.lt.dagger;

import com.get.lucky.lt.views.cam_fragment.CamFragment;
import com.get.lucky.lt.views.keyboard_fragment.KeyboardFragment;
import com.get.lucky.lt.views.splash.SplashActivity;

import dagger.Component;

@Component(modules = {CamModule.class, SplashModule.class, CalculatorModule.class})
public interface CamComponent {

    void inject(CamFragment camFragment);

    void inject(KeyboardFragment keyboardFragmentFragment);

    void inject(SplashActivity splashActivity);

}
