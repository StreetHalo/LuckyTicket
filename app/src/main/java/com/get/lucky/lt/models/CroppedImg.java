package com.get.lucky.lt.models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.edmodo.cropper.cropwindow.edge.Edge;
import com.get.lucky.lt.views.cam_fragment.WorkWithCrop;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CroppedImg  {

    private Disposable disposable;
    private int pictureWidth;
    private int pictureHeight;
    private float layoutWidth;
    private float layoutHeight;
    private WorkWithCrop workWithCrop;



    public void setPresenterInterface(WorkWithCrop workWithCrop){
        this.workWithCrop = workWithCrop;
    }
    public void setPictureSize(int pictureWidth, int pictureHeight) {
        this.pictureWidth = pictureWidth;
        this.pictureHeight = pictureHeight;
    }

    public void setLayoutSize(int layoutWidth, int layoutHeight) {
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    public Observable<Bitmap> setCroppedImg(byte[] data) {

       return Observable.fromCallable(()->getBitmap(data));

    }

    private Bitmap getBitmap(byte[] data) {
        double scaleH = pictureHeight / layoutHeight;
        double scaleW = pictureWidth / layoutWidth;
        int x = (int) (Edge.TOP.getCoordinate() * scaleH);
        int y = (int) (Math.abs(layoutWidth - Edge.RIGHT.getCoordinate()) * scaleW);
        int w = (int) (Edge.getWidth() * scaleW);
        int h = (int) (Edge.getHeight() * scaleH);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap picture = BitmapFactory.decodeByteArray(data, 0,data.length , options);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(picture, x, y, h, w,matrix,true);
    }

}
