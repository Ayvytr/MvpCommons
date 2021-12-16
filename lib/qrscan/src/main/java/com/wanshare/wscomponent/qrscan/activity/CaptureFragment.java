package com.wanshare.wscomponent.qrscan.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanshare.wscomponent.qrscan.OnScanListener;
import com.wanshare.wscomponent.qrscan.R;
import com.wanshare.wscomponent.qrscan.view.CameraView;
import com.wanshare.wscomponent.qrscan.view.ScanView;

/**
 * 自定义实现的扫描Fragment
 *
 * @author wangdunwei
 * @since 1.0.0
 */
public class CaptureFragment extends Fragment {
    public static final String LAYOUT_ID = "layout_id";

    private ScanView scanView;
    private CameraView cameraView;
    private OnScanListener onScanListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        View view = null;
        if (bundle != null) {
            int layoutId = bundle.getInt(LAYOUT_ID);
            if (layoutId != -1) {
                view = inflater.inflate(layoutId, null);
            }
        }

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_capture, null);
        }

        scanView = (ScanView) view.findViewById(R.id.viewfinder_view);
        cameraView = (CameraView) view.findViewById(R.id.preview_view);
        cameraView.setOnScanListener(new OnScanListener() {
            @Override
            public void onSucceed(Bitmap mBitmap, String result) {
                onScanListener.onSucceed(mBitmap, result);
            }

            @Override
            public void onFailed() {
                onScanListener.onFailed();
            }
        });

        cameraView.setFrameWidth(scanView.getFrameWidth());
        return view;
    }

    public void setOnScanListener(OnScanListener onScanListener) {
        this.onScanListener = onScanListener;
    }
}
