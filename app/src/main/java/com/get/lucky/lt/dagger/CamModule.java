package com.get.lucky.lt.dagger;

import android.content.Context;
import android.view.SurfaceView;

import com.get.lucky.lt.models.Calculator;
import com.get.lucky.lt.models.Cam;
import com.get.lucky.lt.models.CroppedImg;
import com.get.lucky.lt.models.OcrImg;
import com.get.lucky.lt.presenter.CamPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class CamModule {
    private SurfaceView surfaceView;
    private Context context;

    public CamModule(SurfaceView surfaceView, Context context) {
        this.surfaceView = surfaceView;
        this.context = context;
    }

    @Provides
    CamPresenter providePresenter(Cam cam, CroppedImg croppedImg, OcrImg ocrImg, Calculator calculator){
        return new CamPresenter(ocrImg,cam,croppedImg,calculator,context);
    }

    @Provides
    Cam provideCam(){
        return new Cam(surfaceView);
    }

    @Provides
    CroppedImg provideCroppedImg(){
        return new CroppedImg();
    }

    @Provides
    OcrImg provideOcrImg(){
        return new OcrImg(context);
    }

    @Provides
    Calculator provideCalculator(){
        return new Calculator();
    }
}
