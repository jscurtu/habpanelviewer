package de.vier_bier.habpanelviewer;

import android.content.Intent;
import android.util.Log;

import com.jakewharton.processphoenix.ProcessPhoenix;

/**
 * UncaughtExceptionHandler that restarts the app in case of exceptions.
 */
class AppRestartingExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "HPV-AppRestartingExHa";

    private final MainActivity myContext;
    private final int count;

    AppRestartingExceptionHandler(MainActivity context, int restartCount) {
        myContext = context;
        count = restartCount;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Log.e(TAG, "Uncaught exception", exception);

        restartApp(myContext, count);
    }

    private static void restartApp(MainActivity context, int count) {
        Intent mStartActivity = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (mStartActivity != null) {
            mStartActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (count != -1) {
                mStartActivity.putExtra("crash", true);
                mStartActivity.putExtra("restartCount", count + 1);
            }

            context.destroy();
            ProcessPhoenix.triggerRebirth(context, mStartActivity);
        }
    }
}
