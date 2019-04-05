package com.example.mihailov.lt.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.mihailov.lt.SplashActivity;
import com.example.mihailov.lt.presenter.WorkWithCrop;
import com.example.mihailov.lt.presenter.WorkWithOCR;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OcrImg {
    private Context context;
    private WorkWithOCR workWithOCR;
    private Disposable disposable;
    private boolean isWork = false;
    private String retStr = "No result";
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Lucky/";

    private static final String lang = "eng";

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

    public void stopOCR(){
        isWork = false;
        if(disposable!=null) disposable.dispose();
    }

}
