package com.get.lucky.lt.views.cam_fragment;

public interface WorkWithCam {

    void camFocus();

    void closeCam();

    void nonAutoFocus();

    void autoFocus();

    void setImgForCrop(byte[] bytes);

    void setPicSize(int pictureWidth, int pictureHeight);

    void openCam();
}
