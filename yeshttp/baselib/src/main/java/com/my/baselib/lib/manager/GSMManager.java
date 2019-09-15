package com.my.baselib.lib.manager;

import android.app.Activity;
import android.content.Context;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
/**
 * create by Administrator at 2017/6/22
 * description:
 */
public class GSMManager {
    TelephonyManager telephonyManager;
    PhoneStateListener phoneStateListener;
    GsmListener gsmListener;

    public GSMManager(Activity context, GsmListener gsmListener) {
        this.gsmListener = gsmListener;
        InitPhoneStateListener();
    }

    private void InitPhoneStateListener() {
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {// gsm网络

                } else {// 其他CDMA等网络
                    try {
                        Class cdmaClass = Class.forName("android.telephony.cdma.CdmaCellLocation");
                    } catch (ClassNotFoundException classnotfoundexception) {
                        classnotfoundexception.printStackTrace();
                    }
                }// end CDMA网络
                super.onCellLocationChanged(location);
            }// end onCellLocationChanged

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }

            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                int asu = signalStrength.getGsmSignalStrength();
                int lastSignal = -113 + 2 * asu; //信号强度
                if (gsmListener != null) {
                    gsmListener.gsm(lastSignal);
                }
                super.onSignalStrengthsChanged(signalStrength);
            }
        };
    }

    public interface GsmListener {
        void gsm(int gsm);
    }
}