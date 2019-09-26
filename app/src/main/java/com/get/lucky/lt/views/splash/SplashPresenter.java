package com.get.lucky.lt.views.splash;

import com.get.lucky.lt.models.FileManager;
import java.util.Random;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.get.lucky.lt.Constants.DATA_PATH;
import static com.get.lucky.lt.Constants.TESSDATA;

public class SplashPresenter {
    private FileManager fileManager;
    private SplashViewInterface splashView;

    public SplashPresenter(FileManager fileManager){
        this.fileManager = fileManager;
    }

     void attach(SplashViewInterface splashView){

        this.splashView = splashView;
        Random random = new Random();
        if (random.nextInt() % 2 == 0) splashView.setBlackTheme();
        else splashView.setWhiteTheme();
    }

    void copyTessDataFiles(){

        Disposable a= fileManager.copyTessDataFiles(DATA_PATH+TESSDATA)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe( i-> splashView.openMainActivity(),
                                    throwable-> splashView.createFileError());
    }

     void detach() {
        splashView = null;
    }
}
