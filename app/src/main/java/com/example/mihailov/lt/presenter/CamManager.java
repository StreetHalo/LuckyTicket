package com.example.mihailov.lt.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.mihailov.lt.fragments.CamInterface;
import com.example.mihailov.lt.models.Calculator;
import com.example.mihailov.lt.models.Cam;
import com.example.mihailov.lt.models.CroppedImg;
import com.example.mihailov.lt.models.OcrImg;
import com.example.mihailov.lt.R;
import com.example.mihailov.lt.models.StringToNumbers;

public class CamManager implements WorkWithCam,WorkWithCrop,WorkWithOCR {

    private Context context;
    private boolean stopAll;
    private OcrImg ocrImg;
    private Cam cam;
    private CroppedImg croppedImg;
    private CamInterface camInterface;
    private Calculator calculator;
    private String TAG = getClass().getSimpleName();
    public CamManager(Context context, CamInterface camInterface) {
        this.context = context;
        this.camInterface = camInterface;
        croppedImg = new CroppedImg(this);
        ocrImg = new OcrImg(context,this);
        calculator = new Calculator();
    }

    public void createCam(SurfaceView surfaceView){
        cam = new Cam(surfaceView,this);
    }

    @Override
    public void stopOCR() {
        stopAll = true;
        cam.stopCam();
        croppedImg.stopCroppedImg();
        ocrImg.stopOCR();

    }


    @Override
    public void camFocus() {
        if(stopAll) stopAll = false;
        cam.camFocus();
    }

    @Override
    public void closeCam() {
        if(cam!=null)
        cam.closeCam();
    }

    @Override
    public void nonAutoFocus() {
        Toast.makeText(context, R.string.nonFocus,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResult(String result) {
      camInterface.setResult( calculator.getResult(result.toCharArray()));

    }

    @Override
    public void autoFocus() {
        camInterface.freezeButtonCam();
    }

    @Override
    public void setImgForCrop(byte[] bytes) {
        if(!stopAll)
        croppedImg.setCroppedImg(bytes);

    }

    @Override
    public void setCropImgForOCR(Bitmap croppedPicture) {
        if(!stopAll)
            ocrImg.getText(croppedPicture);
    }

    @Override
    public void setLayoutSize(int layoutWidth, int layoutHeight) {
        croppedImg.setLayoutSize(layoutWidth,layoutHeight);
    }

    @Override
    public void setPicSize(int pictureWidth, int pictureHeight) {
        croppedImg.setPictureSize(pictureWidth,pictureHeight);
    }
}
