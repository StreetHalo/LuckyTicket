package com.get.lucky.lt;

import android.app.Application;

import com.get.lucky.lt.dagger.CamModule;
import com.get.lucky.lt.dagger.DaggerCamComponent;
import com.get.lucky.lt.dagger.SplashModule;

public class App extends Application {

    public static DaggerCamComponent  daggerCamComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        daggerCamComponent=(DaggerCamComponent) DaggerCamComponent.builder()
                .camModule(new CamModule(getApplicationContext()))
                .splashModule(new SplashModule(getApplicationContext()))
                .build();
    }
}
