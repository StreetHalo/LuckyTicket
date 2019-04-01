package com.example.mihailov.lt.fragments;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.edmodo.cropper.cropwindow.CropOverlayView;
import com.example.mihailov.lt.MainActivity;
import com.example.mihailov.lt.R;
import com.example.mihailov.lt.dagger.CamModule;
import com.example.mihailov.lt.dagger.DaggerCamComponent;
import com.example.mihailov.lt.presenter.CamPresenter;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CamFragment extends Fragment implements CamInterface {

    @BindView(R.id.cam)
    SurfaceView surfaceView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout_cam)
    RelativeLayout frameLayout;
    @BindView(R.id.cropOverlayView)
    CropOverlayView cropOverlayView;
    private Rect cropRect;
    @Inject
    CamPresenter camPresenter;
    private MainActivity mainActivity;

    @Override
    public void onResume() {
        super.onResume();
        if(camPresenter !=null) camPresenter.openCam();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ocr_layout, container, false);
        ButterKnife.bind(this, view);
        DaggerCamComponent.builder()
                .camModule(new CamModule(surfaceView,getContext()))
                .build().inject(this);

        camPresenter.setFragmentCallback(this);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.cpb_red),PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.INVISIBLE);
        cropRect = new Rect();
        cropRect.left = 0;
        cropRect.top = 0;
        cropRect.right = 200;
        cropRect.bottom = 200;
        cropOverlayView.setBitmapRect(cropRect);
        ViewTreeObserver vto = frameLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                cropRect.left = 0;
                cropRect.top = 0;
                cropRect.right = frameLayout.getMeasuredWidth();
                cropRect.bottom = frameLayout.getMeasuredHeight();
                cropOverlayView.setBitmapRect(cropRect);
                camPresenter.setLayoutSize(frameLayout.getMeasuredWidth(),frameLayout.getMeasuredHeight());
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
        mainActivity.setCamTheme();
    }

    private void closeCam(){
        camPresenter.closeCam();
    }

    public void camFocus(){

        camPresenter.camFocus();
    }

    public void stopOCR(){
        progressBar.setVisibility(View.INVISIBLE);
        camPresenter.stopOCR();
    }

    @Override
    public void freezeButtonCam() {
        progressBar.setVisibility(View.VISIBLE);
        mainActivity.freezeButtonCam();

    }
    @Override
    public void setResult(int result) {
        progressBar.setVisibility(View.INVISIBLE);
        mainActivity.setResultTheme(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        closeCam();

    }
}
