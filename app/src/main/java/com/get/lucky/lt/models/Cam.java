package com.get.lucky.lt.models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import com.get.lucky.lt.presenter.WorkWithCam;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Cam implements SurfaceHolder.Callback, Camera.PreviewCallback,Camera.AutoFocusCallback,Camera.PictureCallback {
    private static Camera camera = Camera.open();
    private SurfaceView surfaceView;
    private Disposable disposable;
    private int degree = 90;
    private WorkWithCam workWithCam;
    private int w;
    private int h;
    private boolean hasAutoFocus;

    public Cam(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        if(camera!=null)
        openCam();
    }

    public void setPresenterInterface(WorkWithCam workWithCam){
        this.workWithCam = workWithCam;
    }

    public void closeCam(){
        if(camera!=null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;        }
    }

    public void focusCam(){
        camera.autoFocus(this);

    }

    public void openCam(){
        if(camera==null) camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LayoutParams lp = surfaceView.getLayoutParams();
        camera.setDisplayOrientation(degree);
        surfaceView.setLayoutParams(lp);
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        if (b|!hasAutoFocus)
        {
            workWithCam.autoFocus();
            camera.takePicture(null, null, null, this);
          //  camera.startPreview();
        }
        else {
            workWithCam.nonAutoFocus();
        }

    }

    @SuppressLint("CheckResult")
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        camera.startPreview();
        disposable=  Observable.just(getXYfromBytes(bytes))
              .observeOn(Schedulers.io())
              .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {
                        workWithCam.setPicSize(w, h);
                        workWithCam.setImgForCrop(bytes);
                    }
                });
    }

   private byte[] getXYfromBytes(byte[] bytes){
       BitmapFactory.Options option = new BitmapFactory.Options();
       Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
       Matrix mtx = new Matrix();
       mtx.setRotate(degree);
       picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), mtx, true);
       w = picture.getWidth();
       h = picture.getHeight();
       return bytes;
   }

    public void stopCam(){
        if(disposable!=null) disposable.dispose();

    }
}
