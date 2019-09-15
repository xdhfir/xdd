package com.my.baselib.lib.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 */
public class BatteryReceiverManager {

    private BatteryReceiverManager batteryReceiverUtils;
    private BatteryStatusListener batteryStatusListener;
    private BroadcastReceiver broadcastReceiver;
    private final Context mContext;

    public BatteryReceiverManager(Context context, BatteryStatusListener listener) {
        mContext = context;
        batteryStatusListener = listener;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                final String action = intent.getAction();
                String batteryStatus = "耗电中";
                int batteryLevel = 100;
                if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
                    // 当前电池的电压
                    int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                    // 电池的健康状态
                    int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                    switch (health) {
                        case BatteryManager.BATTERY_HEALTH_GOOD:
                        case BatteryManager.BATTERY_HEALTH_COLD:
                            break;
                        case BatteryManager.BATTERY_HEALTH_DEAD:
                            break;
                        case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                            break;
                        case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                            break;
                        case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                            break;
                        case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                            break;
                        default:
                            break;
                    }
                    // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    batteryLevel = level;
                    // 电池电量的最大值
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    // 当前手机使用的是哪里的电源
                    int pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    switch (pluged) {
                        case BatteryManager.BATTERY_PLUGGED_AC:
                            // 电源是AC charger.[应该是指充电器]
                            break;
                        case BatteryManager.BATTERY_PLUGGED_USB:
                            // 电源是USB port
                            break;
                        default:
                            break;
                    }
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    switch (status) {
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            // 正在充电
                            batteryStatus = "充电中";
                            break;
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            break;
                        case BatteryManager.BATTERY_STATUS_FULL:
                            break;
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                            break;
                        case BatteryManager.BATTERY_STATUS_UNKNOWN:
                            break;
                        default:
                            break;
                    }
                    // 电池使用的技术。比如，对于锂电池是Li-ion
                    String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                    // 当前电池的温度
                    int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                    String str = "voltage = " + voltage + " technology = " + technology + " temperature = " + temperature;

                    if (batteryStatusListener != null) {
                        batteryStatusListener.levelPercent(batteryLevel);
                        batteryStatusListener.batteryStatus(batteryStatus);
                    }
                } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_LOW)) {
                    // 表示当前电池电量低
                } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
                    // 表示当前电池已经从电量低恢复为正常
                }
            }
        };
        mContext.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void unRegister() {
        if (broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public interface BatteryStatusListener {
        void levelPercent(int percent);

        void batteryStatus(String status);
    }
}
