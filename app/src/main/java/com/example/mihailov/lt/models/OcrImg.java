package com.example.mihailov.lt.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mihailov.lt.presenter.WorkWithOCR;
import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrImg {
    private String TAG = OcrImg.class.getSimpleName();
    private Context context;
    private WorkWithOCR workWithOCR;
    private String retStr = "No result";
    private AsyncOcr asyncOcr;
    public OcrImg(Context context, WorkWithOCR workWithOCR){
        this.context = context;
        this.workWithOCR = workWithOCR;
    }

    public void getText(Bitmap bitmap){

        asyncOcr = new AsyncOcr();
        asyncOcr.execute(bitmap);

    }

    public void stopOCR(){

        if(asyncOcr!=null)
            asyncOcr.cancel(true);

    }


    private class AsyncOcr extends AsyncTask<Bitmap,Void,String>{

        @Override
        protected String doInBackground(Bitmap... voids) {
            TessBaseAPI  tessBaseAPI = new TessBaseAPI();
            tessBaseAPI.init(context.getExternalFilesDir("/").getPath() + "/","eng");
            Log.d(TAG, "doInBackground: path "+context.getExternalFilesDir("/").getPath() + "/"+"eng");
            //   tessBaseAPI.setVariable("tessedit_char_whitelist", "0123456789");
            tessBaseAPI.setImage(voids[0]);
            try{
                retStr = tessBaseAPI.getUTF8Text();
                tessBaseAPI.clear();
                tessBaseAPI.end();
                if(isCancelled()) return null;
                Log.d(TAG, "getText in: "+retStr);
               retStr = retStr.replaceAll("\\d+[a-z,A-Z]", " ");
                Log.d(TAG, "getText out: "+retStr);
                retStr = retStr.replaceAll("[^0-9]+", " ");
                Log.d(TAG, "getText out1: "+retStr);
                retStr = retStr.replaceAll("\\s+","");

            }catch (Exception e){
                Log.e(TAG, e.getMessage());

            }
            return retStr;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: "+aVoid);
            workWithOCR.getResult(aVoid);
            cancel(true);
        }

    }
}
