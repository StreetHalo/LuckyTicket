package com.example.mihailov.lt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE = 42;
    private final String TESS_DATA = "/tessdata";
    private final String TAG = this.getClass().getSimpleName();
    private Random random;
    @BindView(R.id.logo)
    ImageView imageLogo;
    @BindView(R.id.background_splash)
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);
        random = new Random();
        Log.d(TAG, "onCreate: "+random.nextInt());
        if(random.nextInt()%2==0)setBlackTheme();
        else setWhiteTheme();

        if (Build.VERSION.SDK_INT >= 23) {

                requestPermissions();

        } else {
            prepareTessData();
        }

    }


    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1 ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareTessData();

            } else
                Toast.makeText(this, "Для работы приложения необходим доступ к камере", Toast.LENGTH_LONG).show();


        }
    }

    private void prepareTessData(){
        try{
            File dir = getExternalFilesDir(TESS_DATA);
            Log.d(TAG, "prepareTessData: "+getExternalFilesDir("/").getPath() + "/");

            if(!dir.exists()){
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String fileList[] = getAssets().list("");

            for(String fileName : fileList){
                if(fileName.equals("images"))continue;
                if(fileName.equals("webkit"))continue;

                Log.d(TAG, "prepareTessData: "+fileName);
                String pathToDataFile = dir + "/" + fileName;
                if(!(new File(pathToDataFile)).exists()){
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte [] buff = new byte[1024];
                    int len ;
                    while(( len = in.read(buff)) > 0){
                        out.write(buff,0,len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Словарь не загрузился", Toast.LENGTH_LONG).show();

            Log.e(TAG, e.getMessage());
        }

        goToActivity();

    }

    private void goToActivity() {
        Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    private void setBlackTheme(){
            imageLogo.setBackgroundResource(R.drawable.logo_black);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.black_theme));
    }

    private void setWhiteTheme(){
        imageLogo.setBackgroundResource(R.drawable.logo_white);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.white_theme));

    }
}
