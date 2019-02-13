package com.example.mihailov.lt.presenter;

import android.graphics.Bitmap;

public interface WorkWithCrop {

    void setCropImgForOCR(Bitmap croppedPicture);

    void setLayoutSize(int layoutWidth, int layoutHeight);

}
