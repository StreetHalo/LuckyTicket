package com.example.mihailov.lt.dagger;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.example.mihailov.lt.models.Calculator;
import com.example.mihailov.lt.models.Cam;
import com.example.mihailov.lt.models.CroppedImg;
import com.example.mihailov.lt.models.OcrImg;
import com.example.mihailov.lt.presenter.CamPresenter;

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
