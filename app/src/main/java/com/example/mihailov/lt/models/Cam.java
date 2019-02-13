package com.example.mihailov.lt.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import com.example.mihailov.lt.presenter.WorkWithCam;
import java.io.IOException;
import java.util.List;

public class Cam implements SurfaceHolder.Callback, Camera.PreviewCallback,Camera.AutoFocusCallback,Camera.PictureCallback {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private String TAG = getClass().getSimpleName();
    private int degree = 90;
    private WorkWithCam workWithCam;
    private int w;
    private int h;
    private boolean hasAutoFocus;
    private AsyncCam asyncCam;
    public Cam(SurfaceView surfaceView, WorkWithCam workWithCam) {
        this.workWithCam = workWithCam;
        this.surfaceView = surfaceView;



       camera = Camera.open();

        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
        Log.d(TAG, "Cam: "+hasAutoFocus);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void closeCam(){
        if(camera!=null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void camFocus(){
        camera.autoFocus(this);

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
        }
        else {
            workWithCam.nonAutoFocus();
        }

    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Log.d(TAG, "onPictureTaken: ");
        asyncCam = new AsyncCam();
        asyncCam.execute(bytes);
        camera.startPreview();
    }

/*    private Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }*/

    public void stopCam(){
        if(asyncCam!=null)asyncCam.cancel(true);
    }

   private class AsyncCam extends AsyncTask<byte[],Void,byte[]>{
        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            workWithCam.setPicSize(w, h);
            workWithCam.setImgForCrop(bytes);

        }

        @Override
        protected byte[] doInBackground(byte[]... bytes) {

            BitmapFactory.Options option = new BitmapFactory.Options();
            Bitmap picture = BitmapFactory.decodeByteArray(bytes[0], 0, bytes[0].length, option);
            Log.d(TAG, "doInBackground1: w "+picture.getWidth()+" h "+picture.getHeight());
            Matrix mtx = new Matrix();
            mtx.setRotate(degree);
            picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), mtx, true);
            w = picture.getWidth();
            h = picture.getHeight();
            Log.d(TAG, "doInBackground2: w "+picture.getWidth()+" h "+picture.getHeight());

            return bytes[0];
        }
    }
}
