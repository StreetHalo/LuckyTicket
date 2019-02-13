package com.example.mihailov.lt.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.example.mihailov.lt.presenter.WorkWithCrop;

import java.io.File;
import java.io.FileOutputStream;


public class CroppedImg  {

    private String TAG = getClass().getSimpleName();

    private int pictureWidth;
    private int pictureHeight;
    private float layoutWidth;
    private float layoutHeight;
    private int x, y,h,w;
    private double scaleH ;
    private double scaleW;
    private WorkWithCrop workWithCrop;
    private AsyncCrop asyncCrop;
    public CroppedImg(WorkWithCrop workWithCrop) {
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

    public void setCroppedImg(byte[] data){

        asyncCrop = new AsyncCrop();
        asyncCrop.execute(data);

    }

    public void stopCroppedImg(){
        if(asyncCrop!=null)asyncCrop.cancel(true);
    }


 class AsyncCrop extends AsyncTask<byte[],Void,Bitmap>{
     @Override
     protected void onPostExecute(Bitmap bitmap) {
         super.onPostExecute(bitmap);
         workWithCrop.setCropImgForOCR(bitmap);
         cancel(true);
     }

     @Override
     protected Bitmap doInBackground(byte[]... bytes) {
         scaleH = pictureHeight/layoutHeight;
         scaleW = pictureWidth/layoutWidth;

         x = (int) (Edge.TOP.getCoordinate()*scaleH );
         y = (int) (Math.abs(layoutWidth-Edge.RIGHT.getCoordinate())*scaleW);
         w = (int) (Edge.getWidth() * scaleW);
         h = (int) (Edge.getHeight() * scaleH);

         final BitmapFactory.Options options = new BitmapFactory.Options();
         options.inPurgeable = true;
         options.inInputShareable = true;


         Bitmap picture = BitmapFactory.decodeByteArray(bytes[0], 0,bytes[0].length , options);

         Matrix matrix = new Matrix();
         matrix.postRotate(90);


         final Bitmap croppedPicture = Bitmap.createBitmap(picture, x, y, h, w,matrix,true);
         Log.d(TAG, "setCroppedImg: "+pictureWidth+" "+pictureHeight);



         File file = new File(new File(Environment.getExternalStorageDirectory() + "/DirName"), "top2.jpg");
         Log.d(TAG, "doInBackground: "+Environment.getExternalStorageDirectory() + "/DirName");

         if (file.exists()) {
             file.delete();
         }

         try
         {
             FileOutputStream os = new FileOutputStream(file);
             croppedPicture.compress(Bitmap.CompressFormat.JPEG, 80, os); // bmp is your Bitmap instance

             os.close();
         }
         catch (Exception e)
         {
             Log.d(TAG, "onPictureTaken: "+e);
         }
         return croppedPicture;
     }

 }
}
