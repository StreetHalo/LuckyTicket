package com.get.lucky.lt.views.cam_fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.get.lucky.lt.R;
import com.get.lucky.lt.views.cam_fragment.CamInterface;
import com.get.lucky.lt.models.Calculator;
import com.get.lucky.lt.models.Cam;
import com.get.lucky.lt.models.CroppedImg;
import com.get.lucky.lt.models.OcrImg;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.get.lucky.lt.Constants.EMPTY_ARRAY;
import static com.get.lucky.lt.Constants.LUCKY_NUMBER;
import static com.get.lucky.lt.Constants.NON_LUCKY_NUMBER;

public class CamPresenter implements WorkWithCam, WorkWithCrop, WorkWithOCR {
    private boolean isWork = false;
    private Context context;
    private OcrImg ocrImg;
    private Cam cam;
    private CroppedImg croppedImg;
    private Calculator calculator;
    private CamInterface camInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CamPresenter(OcrImg ocrImg, Cam cam, CroppedImg croppedImg, Calculator calculator, Context context) {
        this.ocrImg = ocrImg;
        this.cam = cam;
        ocrImg.setPresenterInterface(this);
        cam.setPresenterInterface(this);
        this.croppedImg = croppedImg;
        croppedImg.setPresenterInterface(this);
        this.calculator = calculator;
        this.context = context;
    }

    void attach(CamInterface camInterface) {
        this.camInterface = camInterface;
    }

    void initCam(SurfaceView surfaceView) {
        cam.initCam(surfaceView);
    }

    @Override
    public void stopOCR() {
        cam.stopCam();
        camInterface.setDefaultButton();
        camInterface.setProgressBarInvisible();
        isWork = false;

    }


    @Override
    public void camFocus() {
        if (!isWork) {
            cam.focusCam();
            isWork = true;
        } else stopOCR();
    }

    @Override
    public void closeCam() {
        if (cam != null)
            cam.closeCam();
    }

    @Override
    public void nonAutoFocus() {
        Toast.makeText(context, R.string.nonFocus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResult(String result) {
        Disposable a = calculator.getResult(result.toCharArray())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(integer -> {
                            if (isWork) setResultTheme(integer);
                        },
                        throwable -> camInterface.errorMessage());
        compositeDisposable.add(a);
    }

    private void setResultTheme(Integer result) {
        camInterface.setProgressBarInvisible();
        camInterface.setDefaultButton();
        switch (result) {
            case LUCKY_NUMBER:
                camInterface.setLuckyTheme();
                break;
            case NON_LUCKY_NUMBER:
                camInterface.setNonLuckyTheme();
                break;
            case EMPTY_ARRAY:
                camInterface.emptyIntArray();
        }
    }

    @Override
    public void autoFocus() {
        camInterface.freezeButtonCam();
    }

    @Override
    public void setImgForCrop(byte[] bytes) {
        if (isWork) {
            Disposable a = croppedImg.setCroppedImg(bytes)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                                if (isWork) setCropImgForOCR(bitmap);
                            },
                            throwable -> camInterface.errorMessage());
            compositeDisposable.add(a);
        }

    }

    @Override
    public void setCropImgForOCR(Bitmap croppedPicture) {

        Disposable a = ocrImg.getText(croppedPicture)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                            if (isWork) getResult(result);
                        },
                        throwable -> camInterface.errorMessage());
        compositeDisposable.add(a);

    }

    @Override
    public void setLayoutSize(int layoutWidth, int layoutHeight) {
        croppedImg.setLayoutSize(layoutWidth, layoutHeight);
    }

    @Override
    public void setPicSize(int pictureWidth, int pictureHeight) {
        croppedImg.setPictureSize(pictureWidth, pictureHeight);
    }

    @Override
    public void openCam() {
        cam.openCam();
    }


    void detach() {
        compositeDisposable.dispose();
    }
}
