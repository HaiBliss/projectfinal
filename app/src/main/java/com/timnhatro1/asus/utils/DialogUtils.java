package com.timnhatro1.asus.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.timnhatro1.asus.view.dialog.NProgressDialog;

public class DialogUtils {
    private static final String TAG = DialogUtils.class.getSimpleName();
    private static volatile NProgressDialog progress;

    private DialogUtils() {

    }
    /**
     * Show dialog progress.
     *
     * @param activity the context is running.
     */
    public static void showProgressDialog(final Activity activity) {
        if (activity == null)
            return;

        if (progress != null) {
            try {
                progress.cancel();
            } catch (Exception ex) {
                Log.e(TAG, "showProgressDialog Exception", ex);
            }
        }


        Tools.hideKeyboard(null, activity);
        progress = new NProgressDialog(activity);
        progress.setTitle("");
        progress.setCancelable(true);
        progress.setOnCancelListener(null);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public static boolean isShowing() {
        if (progress != null) {
            return progress.isShowing();
        }
        return false;
    }

    /**
     * Dismiss progress dialog
     */
    public static void dismissProgressDialog() {
        try {
            if (progress != null)
                progress.dismiss();
        } catch (Exception e) {
            Log.e(TAG, "dismissProgressDialog Exception", e);
        }
    }


    /**
     * Check if context is valid
     */
    public static boolean isValidContext(Context context) {
        if (context == null) {
            return false;
        }
        return !(context instanceof Activity && ((Activity) context).isFinishing());
    }







}
