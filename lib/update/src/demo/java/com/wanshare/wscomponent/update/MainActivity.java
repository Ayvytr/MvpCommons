package com.wanshare.wscomponent.update;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.os.Bundle;

import com.wanshare.wscomponent.update.contract.MainContract;
import com.wanshare.wscomponent.update.model.VersionEntity;
import com.wanshare.wscomponent.update.presenter.MainPresenter;
import com.wanshare.wscomponent.update.dialog.BaseDialog;
import com.wanshare.wscomponent.update.dialog.IDialogListener;
import com.wanshare.wscomponent.update.dialog.IUpdateDialog;
import com.wanshare.wscomponent.update.update.UpdateCheckManager;


public class MainActivity extends Activity implements MainContract.View {

    private MainPresenter mMainPresenter;
    private UpdateCheckManager mUpdateCheckManager;
    private Context mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUpdateCheckManager = new UpdateCheckManager.Builder()
                .setNotifyContent("下载更新中..")
                .setNotifyIcon(R.drawable.ic_home_guanbi)
                .setNotifyTitle(getString(R.string.app_name))
                .setNotifyComplete("下载完成")
                .setNotifyCompleteInstall("点击安装")
                .build(mContext);
        mMainPresenter = new MainPresenter(this);
        showVersion(new VersionEntity("1.0.4", "http://192.168.83.173:8080/job/android.wanshare.release.deploy/ws/app/build/outputs/apk/debug/Wanshare_v3.0.0_2018-10-05_300D_jiagu_sign.apk", "false", "1.全新配色更改，页面优化设计，权限ui设计\n" +
                "2.全新配色更改，页面优化设计，权限ui设计\n" +
                "3.全新配色更改，页面优化设计，权限ui设计", "18946638"));

    }


    @Override
    public void showVersion(final VersionEntity version) {
        mUpdateCheckManager.setIUpdateDialog(new UpdateDialogImp());
        mUpdateCheckManager.autoUpdate(version);
    }

    private class UpdateDialogImp extends IUpdateDialog.SubUpdateDialog {
        private UpdateDialog mUpdateTipsDialog;

        @Override
        public BaseDialog getProgressDialog(int progress, VersionEntity entity) {
            initUpdateDialog(entity);
            if (progress > 0) {
                mUpdateTipsDialog.setPbUpdateProgress(progress);
            }
            return mUpdateTipsDialog;
        }

        @Override
        public BaseDialog getUpdateTipsDialog(VersionEntity entity) {
            initUpdateDialog(entity);
            return mUpdateTipsDialog;
        }

        private void initUpdateDialog(VersionEntity entity){
            if (mUpdateTipsDialog == null) {
                mUpdateTipsDialog = getUpdateDialog(entity);
            }
        }

    }

    private UpdateDialog getUpdateDialog(VersionEntity entity) {
        return new UpdateDialog.Builder()
                .setContent(entity.getDescribe())
                .setForcibly(entity.getIsCompel())
                .setVersion(entity.getVersion())
                .setIDialogListener(new IDialogListener.SubDialogListener() {
                    @Override
                    public void confirm() {
                        mUpdateCheckManager.startUpdate();
                    }
                })
                .create(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateCheckManager != null) {
            mUpdateCheckManager.destroy();
        }
    }
}
