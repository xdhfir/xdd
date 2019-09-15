package com.my.baselib.lib.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.my.baselib.lib.utils.IntentUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 所有activity的基类
 */
public class MActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTask.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTask.getInstance().popActivity(this);
    }

    @Override
    public void onBackPressed() {
        backPress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return backPress();
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected boolean backPress(){
        super.onBackPressed();
        return false;
    }

    /**
     * @time 2017/4/14 9:40
     * @desc 跳转到activity
     */
    protected void toActivity(Class c,boolean clearOther,HashMap<String,Object> map) {
        IntentUtils.makeIntent(this,c,clearOther,map);
    }


    /*private boolean isActivity = false;
    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppForground(this)) {//进入后台
            isActivity = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivity) {//后台进入前台
            isActivity = false;
            Intent intent = new Intent();
            intent.putExtra("type", "StartGestureFragment");
            intent.setAction("mmt.wallet.gesture");
            startActivity(intent);

        }

    }


    public boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getClassName().equals(this.getClass().getName())) {
                return false;
            }
        }
        return true;
    }*/


}
