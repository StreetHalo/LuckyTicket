package com.example.mihailov.lt.presenter;

public interface WorkWithCam {

    void camFocus();

    void closeCam();

    void nonAutoFocus();

    void autoFocus();

    void setImgForCrop(byte[] bytes);

    void setPicSize(int pictureWidth, int pictureHeight);


}
