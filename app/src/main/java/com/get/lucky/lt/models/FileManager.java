package com.get.lucky.lt.models;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;

import static com.get.lucky.lt.Constants.DATA_PATH;
import static com.get.lucky.lt.Constants.TESSDATA;

public class FileManager {

    private Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    private boolean prepareDirectory(String path) throws Exception {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileList[] = new String[0];

        fileList = context.getAssets().list(TESSDATA);

        for (String fileName : fileList) {

            String pathToDataFile = DATA_PATH + TESSDATA + "/" + fileName;
            if (!(new File(pathToDataFile)).exists()) {

                InputStream in = context.getAssets().open(TESSDATA + "/" + fileName);

                OutputStream out = new FileOutputStream(pathToDataFile);

                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }

        return true;
    }


    public Observable<Boolean> copyTessDataFiles(String path) {
        return Observable.fromCallable(() -> prepareDirectory(path));
    }
}
