package com.my.baselib.lib.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 *
 */
public class ActivityBackGroundUtils {

    public static void setActivityBackground(Activity activity,float bgAlpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }
}
