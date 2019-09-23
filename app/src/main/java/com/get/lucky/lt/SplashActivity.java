package com.get.lucky.lt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String result = "empty";
    private final int PERMISSION_REQUEST_CODE = 42;

    private Random random;
    @BindView(R.id.logo)
    ImageView imageLogo;
    @BindView(R.id.background_splash)
    LinearLayout linearLayout;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Lucky/";
    private static final String TESSDATA = "tessdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);
        random = new Random();
        if (random.nextInt() % 2 == 0) setBlackTheme();
        else setWhiteTheme();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
           // doOCR();
           // requestPermissions();

        } else {
            doOCR();
        }
    }

    private void goToActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();

    }

    private void setBlackTheme() {
        imageLogo.setBackgroundResource(R.drawable.logo_black);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.black_theme));
    }

    private void setWhiteTheme() {
        imageLogo.setBackgroundResource(R.drawable.logo_white);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.white_theme));

    }

    @SuppressLint("CheckResult")
    private void doOCR() {
        Observable.just(copyTessDataFiles())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) goToActivity();
                        else error();
                    }
                });
    }

    private void error() {
        Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
    }

    private void prepareDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + path);
        }
    }


    private boolean copyTessDataFiles() {

        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String fileList[] = getAssets().list(TESSDATA);

            for (String fileName : fileList) {

                String pathToDataFile = DATA_PATH + TESSDATA + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(TESSDATA + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    return true;
                }
            }
        } catch (IOException e) {

            return false;
        }
        return true;

    }


}
