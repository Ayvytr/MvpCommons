package com.wanshare.wscomponent.update.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wanshare.wscomponent.update.update.UpdateHelper;

import java.io.File;

public class NotificationUtils extends ContextWrapper {

    private int mNotifyId = 1;
    private String mChannelId = "1";
    private int mSmallIcon;
    private String mTitle;
    private String mContent;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private NotificationManagerCompat mNotificationManagerCompat;

    public NotificationUtils(Context context, int mSmallIcon, String mTitle, String mContent) {
        this(context, 1, null, mSmallIcon, mTitle, mContent);
    }

    public NotificationUtils(Context context, int mNotifyId, int mSmallIcon, String mTitle, String mContent) {
        this(context, mNotifyId, null, mSmallIcon, mTitle, mContent);
    }

    public NotificationUtils(Context context, int mNotifyId, String mChannelId, int mSmallIcon, String mTitle, String mContent) {
        super(context);
        this.mNotifyId = mNotifyId;
        this.mChannelId = mChannelId != null ? mChannelId : this.mNotifyId +"";
        this.mSmallIcon = mSmallIcon;
        this.mTitle = mTitle;
        this.mContent = mContent;
        baseNotification();
    }

    public void notifyProgress(int max, int progress, String title, String content) {
        if (mBuilder != null && progress > 0) {
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(content + progress + "%");
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.setProgress(max, progress, false);
            notify(mBuilder);
        }
    }

    public void completeProgress(String title, String content, File file) {
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setProgress(100, 100, false);
        notify(UpdateHelper.getInstallIntent(getApplicationContext(), file));
    }

    public void notifyed() {
        notify(mBuilder);
    }

    public void notify(Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        notify(mBuilder.setContentIntent(pendingIntent));
    }

    private void notify(NotificationCompat.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationManager();
            mNotificationManager.notify(mNotifyId, builder.build());
        } else {
            getNotificationManagerCompat();
            mNotificationManagerCompat.notify(mNotifyId, builder.build());
        }
    }

    public void cancel(int notifyId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mNotificationManager == null) {
                return;
            }
            mNotificationManager.cancel(notifyId);
        } else {
            if (mNotificationManagerCompat == null) {
                return;
            }
            mNotificationManagerCompat.cancel(notifyId);
        }
    }


    private void baseNotification() {
        mBuilder = getBuilder(getApplicationContext(), mChannelId);
        mBuilder.setSmallIcon(mSmallIcon);
        mBuilder.setContentTitle(mTitle);
        mBuilder.setContentText(mContent);
    }

    private NotificationCompat.Builder getBuilder(Context context, String channelId) {
        return (mBuilder = new NotificationCompat.Builder(context, channelId));
    }

    private NotificationCompat.Builder getBuilder(Context context) {
        return (mBuilder = new NotificationCompat.Builder(context));
    }

    private void getNotificationManagerCompat() {
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    private void getNotificationManager() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mChannelId, "channel_name", importance);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager() {
        return this.mNotificationManager;
    }

    public NotificationCompat.Builder getmBuilder() {
        return mBuilder;
    }
}