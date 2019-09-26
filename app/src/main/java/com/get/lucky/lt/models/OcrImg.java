package com.get.lucky.lt.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.get.lucky.lt.views.cam_fragment.WorkWithOCR;
import com.googlecode.tesseract.android.TessBaseAPI;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OcrImg {
    private WorkWithOCR workWithOCR;
    private Disposable disposable;
    private String retStr = "No result";
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Lucky/";

    private static final String lang = "eng";

    public void setPresenterInterface(WorkWithOCR workWithOCR){
        this.workWithOCR = workWithOCR;
    }

    @SuppressLint("CheckResult")
    public Observable<String> getText(Bitmap bitmap){
        return  Observable.fromCallable(()->setBitmap(bitmap));
    }

    private String setBitmap(Bitmap bitmap){

        TessBaseAPI  tessBaseApi = new TessBaseAPI();
        tessBaseApi.init(DATA_PATH, lang);
        tessBaseApi.setImage(bitmap);
        try{

            retStr = tessBaseApi.getUTF8Text();
            tessBaseApi.clear();
            tessBaseApi.end();
            retStr = retStr.replaceAll("\\d+[a-z,A-Z]", " ");
            retStr = retStr.replaceAll("[^0-9]+", " ");
            retStr = retStr.replaceAll("\\s+","");

        }catch (Exception e){
            Log.d("ERROR", "setBitmap: "+e.getMessage());
        }
        return retStr;

    }
}
