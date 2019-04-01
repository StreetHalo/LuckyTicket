package com.example.mihailov.lt.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.mihailov.lt.presenter.WorkWithCrop;
import com.example.mihailov.lt.presenter.WorkWithOCR;
import com.googlecode.tesseract.android.TessBaseAPI;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OcrImg {
    private String TAG = OcrImg.class.getSimpleName();
    private Context context;
    private WorkWithOCR workWithOCR;
    private Disposable disposable;
    private boolean isWork = false;
    private String retStr = "No result";


    public OcrImg(Context context){
        this.context = context;
    }

    public void setPresenterInterface(WorkWithOCR workWithOCR){
        this.workWithOCR = workWithOCR;
    }

    @SuppressLint("CheckResult")
    public void getText(Bitmap bitmap){ isWork = true;
     disposable =   Observable.just(setBitmap(bitmap))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(isWork)
                        workWithOCR.getResult(s);

                    }
                });

    }

    private String setBitmap(Bitmap bitmap){
        TessBaseAPI  tessBaseAPI = new TessBaseAPI();
        Log.d(TAG, "setBitmap: 1");
        tessBaseAPI.init(context.getExternalFilesDir("/").getPath() + "/","eng");
        tessBaseAPI.setImage(bitmap);
        try{
            retStr = tessBaseAPI.getUTF8Text();
            tessBaseAPI.clear();
            tessBaseAPI.end();
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

    public void stopOCR(){
        isWork = false;
        if(disposable!=null) disposable.dispose();
    }

}
