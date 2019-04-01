package com.example.mihailov.lt.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.mihailov.lt.fragments.CamFragment;
import com.example.mihailov.lt.fragments.CamInterface;
import com.example.mihailov.lt.models.Calculator;
import com.example.mihailov.lt.models.Cam;
import com.example.mihailov.lt.models.CroppedImg;
import com.example.mihailov.lt.models.OcrImg;
import com.example.mihailov.lt.R;

public class CamPresenter implements WorkWithCam,WorkWithCrop,WorkWithOCR {

    private Context context;
    private boolean stopAll;
    private OcrImg ocrImg;
    private Cam cam;
    private CroppedImg croppedImg;
    private CamInterface camInterface;
    private Calculator calculator;


    public CamPresenter(OcrImg ocrImg, Cam cam, CroppedImg croppedImg, Calculator calculator, Context context) {
        this.ocrImg = ocrImg;
        ocrImg.setPresenterInterface(this);
        this.cam = cam;
        cam.setPresenterInterface(this);
        this.croppedImg = croppedImg;
        croppedImg.setPresenterInterface(this);
        this.calculator = calculator;
        this.context = context;
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
        cam.focusCam();
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

    @Override
    public void openCam() {
        cam.openCam();
    }

    public void setFragmentCallback(CamInterface camInterface) {
        this.camInterface = camInterface;
    }

    public CamInterface getFragmentCallback() {
        return camInterface;
    }
}
