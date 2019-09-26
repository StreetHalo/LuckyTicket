package com.get.lucky.lt.views.splash;

import android.Manifest;
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

import com.get.lucky.lt.App;
import com.get.lucky.lt.views.main.MainActivity;
import com.get.lucky.lt.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity implements SplashViewInterface {

    private ImageView imageLogo;
    private LinearLayout linearLayout;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final RxPermissions rxPermissions = new RxPermissions(this);
    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        App.daggerCamComponent.inject(this);
        imageLogo = findViewById(R.id.logo);
        linearLayout = findViewById(R.id.background_splash);
        splashPresenter.attach(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Disposable a = rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        splashPresenter.copyTessDataFiles();
                    } else {
                        Toast.makeText(this, R.string.need_permissions, Toast.LENGTH_LONG).show();
                    }
                });
        compositeDisposable.add(a);
    }

    @Override
    public void setBlackTheme() {
        imageLogo.setBackgroundResource(R.drawable.logo_black);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.black_theme));
    }

    @Override
    public void setWhiteTheme() {
        imageLogo.setBackgroundResource(R.drawable.logo_white);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.white_theme));

    }

    @Override
    public void createFileError() {
        Toast.makeText(this, R.string.splash_create_file_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openMainActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        splashPresenter.detach();

    }
}
