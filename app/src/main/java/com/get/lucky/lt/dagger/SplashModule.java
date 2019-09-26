package com.get.lucky.lt.dagger;

import android.content.Context;
import com.get.lucky.lt.models.FileManager;
import com.get.lucky.lt.views.splash.SplashPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    private Context context;

    public SplashModule(Context context) {
        this.context = context;
    }

    @Provides
    SplashPresenter provideSplashPresenter(FileManager fileManager) {
        return new SplashPresenter(fileManager);
    }

    @Provides
    FileManager provideFileManager() {
        return new FileManager(context);
    }
}
