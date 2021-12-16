package com.base.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Administrator
 */
public class ActivityStack {
    private static List<Activity> list = new LinkedList<>();
    private static int foregroundActivityCount = 0;
    private static Application.ActivityLifecycleCallbacks callbacks;

    public static void registerCallback(@NonNull Application application) {
        if(application == null) {
            return;
        }

        if(callbacks != null) {
            return;
        }

        callbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity,
                                          @Nullable Bundle savedInstanceState) {
                list.add(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                foregroundActivityCount++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                foregroundActivityCount--;
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity,
                                                    @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                list.remove(activity);
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public static int getRunningActivityCount() {
        return list.size();
    }

    public static boolean isForeground() {
        return foregroundActivityCount > 0;
    }

    public static void finish(Class<? extends Activity> clazz) {
        for(int i = list.size() - 1; i >= 0; i--) {
            Activity activity = list.get(i);
            if(activity.getClass().getName().equals(clazz.getName())) {
                activity.finish();
            }
        }
    }

    public static void finishAllExceptTop() {
        for(int i = list.size() - 2; i >= 0; i--) {
            list.get(i).finish();
        }
    }

    public static void finishAllExcept(Class<? extends Activity> clazz) {
        for(int i = list.size() - 1; i >= 0; i--) {
            Activity activity = list.get(i);
            if(!activity.getClass().getName().equals(clazz.getName())) {
                activity.finish();
            }
        }
    }

    public static void finishAll() {
        for(int i = list.size() - 1; i >= 0; i--) {
            list.get(i).finish();
        }
    }

    public static void killSelf() {
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
