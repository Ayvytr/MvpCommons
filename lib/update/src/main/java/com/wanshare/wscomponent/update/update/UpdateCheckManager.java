package com.wanshare.wscomponent.update.update;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.wanshare.wscomponent.update.R;
import com.wanshare.wscomponent.update.dialog.BaseDialog;
import com.wanshare.wscomponent.update.dialog.IDialogListener;
import com.wanshare.wscomponent.update.dialog.IUpdateDialog;
import com.wanshare.wscomponent.update.download.DownloadManager;
import com.wanshare.wscomponent.update.model.VersionEntity;
import com.wanshare.wscomponent.update.utils.NotificationUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * @author Xu wenxiang
 * create at 2018/10/8
 * description:更新下载检查
 */
public class UpdateCheckManager {

    private static UpdateCheckManager mUpdateCheckManager;

    private Context mContext;
    private VersionEntity mVersionEntity;
    private IUpdateDialog mIUpdateDialog;
    private int mOldProgress;
    private NotificationUtils mNotificationUtils;

    private String notifyContent;
    private String notifyTitle;
    private String notifyComplete;
    private String notifyCompleteInstall;
    private int notifyIcon;

    private boolean isbBackstageNotify = false;
    private boolean isDownloading = false;

    private UpdateCheckManager(Context context, Builder builder) {
        mContext = context;
        setBuilder(builder);
    }

    private void setBuilder(Builder builder) {
        if (builder == null) {
            return;
        }
        notifyIcon = builder.notifyIcon;
        notifyContent = builder.notifyContent;
        notifyTitle = builder.notifyTitle;
        notifyComplete = builder.notifyComplete;
        notifyCompleteInstall = builder.notifyCompleteInstall;
    }

    public synchronized static UpdateCheckManager getInstance(Context context, Builder builder) {
        if (mUpdateCheckManager == null) {
            mUpdateCheckManager = new UpdateCheckManager(context.getApplicationContext(), builder);
        } else {
            mUpdateCheckManager.setBuilder(builder);
        }
        return mUpdateCheckManager;
    }

    public void setIUpdateDialog(IUpdateDialog IUpdateDialog) {
        if (mIUpdateDialog != null) {
            mIUpdateDialog = null;
        }
        mIUpdateDialog = IUpdateDialog;
    }

    /**
     * 检查版本号
     */
    public boolean isUpdate(VersionEntity entity) {
        if (entity == null) {
            return false;
        }
        return UpdateHelper.checkVersion(entity.getLocalVersionCode(), entity.getRemoteVersionCode());
    }

    /**
     * 自动更新
     *
     * @param entity
     */
    public void autoUpdate(VersionEntity entity) {
        if (!isUpdate(entity)) {
            destroy();
            return;
        }
        manualUpdate(entity);
    }

    /**
     * 手动更新
     *
     * @param entity
     */
    public void manualUpdate(VersionEntity entity) {
        mVersionEntity = entity;
        BaseDialog dialog = mIUpdateDialog.getUpdateTipsDialog(entity);
        if (dialog == null) {
            return;
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isDownloading || mVersionEntity.getIsCompel()) {
                    return;
                }
                initNotify();
            }
        });
        dialog.show();
    }

    /**
     * 启动更新
     */
    public void startUpdate() {
        requestPermission();
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        XXPermissions.with(mContext)
                     .permission(Manifest.permission_group.STORAGE)
                     .request(new OnPermissionCallback() {
                         @Override
                         public void onGranted(List<String> permissions, boolean all) {
                             if(all) {
                                 createFile();
                             }
                         }
                     });
    }

    /**
     * 创建文件夹
     */
    private void createFile() {
        if (mVersionEntity == null || TextUtils.isEmpty(mVersionEntity.getAddressUrl())) {
            Toast.makeText(mContext, R.string.update_download_url_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = filePath + "/" + UpdateHelper.getPackageName(mContext) + "-" + mVersionEntity.getRemoteVersionCode() + ".apk";
                File file = new File(fileName);
                isExists(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, R.string.update_no_sd_card, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否存在
     *
     * @param file
     */
    private void isExists(File file) {
        if (!file.exists()) {
            checkNetTips(file);
        } else {
            if (file.length() == Long.parseLong(mVersionEntity.fileSize)) {
//                progressDialogDismiss();
                UpdateHelper.installApkFile(mContext, file);
                return;
            } else {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkNetTips(file);
            }
        }
    }

    /**
     * 检查网络流量提醒
     */
    private void checkNetTips(final File file) {
        BaseDialog dialog = mIUpdateDialog.getNetTipsDialog();
        if (dialog == null) {
            download(file);
            return;
        }
        dialog.setIDialogListener(new IDialogListener.SubDialogListener() {
            @Override
            public boolean confirm() {
                download(file);
                return true;
            }
        });

        dialog.show();
    }

    private void initNotify() {
        if (mVersionEntity.getIsCompel()) {
            return;
        }
        if (mNotificationUtils != null) {
            return;
        }
        isbBackstageNotify = true;
        mNotificationUtils = new NotificationUtils(mContext, notifyIcon, notifyTitle, notifyContent);
    }

    /**
     * 开始下载
     *
     * @param file
     */
    public void download(File file) {
        if (file == null) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {

            if (!file.createNewFile()) {
                Toast.makeText(mContext, R.string.update_file_create_fail, Toast.LENGTH_SHORT).show();
            } else {
                startDownload(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载任务
     *
     * @param file
     */
    private void startDownload(final File file) {
        isDownloading = true;
        DownloadManager.cancelDown();
        initNotify();
        DownloadManager.downloadApk(mVersionEntity.getAddressUrl(), file.getPath(), new DownloadManager.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                isDownloading = false;
                progressDialogDismiss();
                Toast.makeText(mContext, R.string.update_download_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                isDownloading = false;
                try {
                    final File file = new File(response.toString());
//                    if (file != null && file.length() == Long.parseLong(mVersionEntity.fileSize)) {
                        if (isbBackstageNotify) {
                            mNotificationUtils.completeProgress(notifyCompleteInstall, notifyComplete, file);
                        } else {
//                            progressDialogDismiss();
                            install(file);
                        }
//                    } else {
//                        Toast.makeText(mContext, R.string.update_network_error_download_fail, Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onProgress(long total, long current) {
                int progress = (int) ((current * 100) / total);
                if (!isbBackstageNotify) {
                    mIUpdateDialog.getProgressDialog(progress, mVersionEntity);
                    return;
                }
                if (progress != mOldProgress && progress - mOldProgress > 5) {
                    mOldProgress = progress;
                    Log.d("onProgress", progress + "------------");
                    mNotificationUtils.notifyProgress(100, progress, notifyTitle, notifyContent);
                }


            }

        });
    }

    /**
     * 隐藏进度条dialog
     */
    private void progressDialogDismiss() {
        if (mVersionEntity.getIsCompel()) {
            return;
        }
        BaseDialog dialog = mIUpdateDialog.getProgressDialog(0, mVersionEntity);
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    /**
     * 安装apk
     *
     * @param file
     */
    private void install(final File file) {
        BaseDialog dialog = mIUpdateDialog.getInstallDialog();
        if (dialog == null) {
            UpdateHelper.installApkFile(mContext, file);
            return;
        }
        dialog.setIDialogListener(new IDialogListener.SubDialogListener() {

            @Override
            public boolean confirm() {
                UpdateHelper.installApkFile(mContext, file);
                return true;
            }
        });
        dialog.show();
    }

    /**
     * 销毁更新
     */
    public void destroy() {
        DownloadManager.cancelDown();
        if (mNotificationUtils != null) {
            mNotificationUtils.cancel(1);
        }
        if (mIUpdateDialog != null) {
            cancelDialog(mIUpdateDialog.getInstallDialog());
            cancelDialog(mIUpdateDialog.getNetTipsDialog());
            cancelDialog(mIUpdateDialog.getProgressDialog(0, mVersionEntity));
            cancelDialog(mIUpdateDialog.getUpdateTipsDialog(mVersionEntity));
        }
        mUpdateCheckManager = null;
    }

    /**
     * 销毁dialog
     */
    private void cancelDialog(BaseDialog dialog) {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public static class Builder {
        public String notifyContent;
        public String notifyTitle;
        public int notifyIcon;
        public String notifyComplete;
        public String notifyCompleteInstall;

        public Builder setNotifyContent(String notifyContent) {
            this.notifyContent = notifyContent;
            return this;
        }

        public Builder setNotifyIcon(int notifyIcon) {
            this.notifyIcon = notifyIcon;
            return this;
        }

        public Builder setNotifyTitle(String notifyTitle) {
            this.notifyTitle = notifyTitle;
            return this;
        }

        public Builder setNotifyComplete(String notifyComplete) {
            this.notifyComplete = notifyComplete;
            return this;
        }

        public Builder setNotifyCompleteInstall(String notifyCompleteInstall) {
            this.notifyCompleteInstall = notifyCompleteInstall;
            return this;
        }

        public UpdateCheckManager build(Context context) {
            return UpdateCheckManager.getInstance(context, this);
        }
    }

}
