package com.my.baselib.lib.utils;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * create by Administrator at 2017/3/20
 * description: 买买提钱包，获取用户隐私信息，工具类
 */
public class WalletUtils {
    /**
     * @time 2017/3/20 10:25
     * @desc 获取联系人数据库
     */
    public static HashMap<String, String> getContactDb(Context context) throws Exception {
        String[] PHONES_PROJECTION = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            PHONES_PROJECTION = new String[]{
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Photo.LAST_TIME_CONTACTED,
                    ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
                    ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP};
        } else {
            PHONES_PROJECTION = new String[]{
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Photo.LAST_TIME_CONTACTED,
                    ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED};
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        HashMap<String, String> map = new HashMap<>();
        JSONArray array = new JSONArray();
        int contactCount = 0;
        int count7 = 0;
        int count30 = 0;
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                contactCount++;
                /**********************************获得联系人属性ID***********************************/
                int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int LAST_TIME_CONTACTED_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED);
                int TIMES_CONTACTED_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED);
                int CONTACT_LAST_UPDATED_TIMESTAMP_INDEX = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    CONTACT_LAST_UPDATED_TIMESTAMP_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
                }
                int ID = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
                /**********************************获得联系人属性ID***********************************/

                /**********************************获得联系人属性*************************************/
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);//手机号码
                if (TextUtils.isEmpty(phoneNumber)) {
                    //当手机号码为空的或者为空字段 跳过当前循环
                    continue;
                }
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);//联系人名称
                String count = phoneCursor.getString(TIMES_CONTACTED_INDEX);//联系次数
                String strLastContactTime = DateUtils.formatCouponsDate(phoneCursor.getString(LAST_TIME_CONTACTED_INDEX));//最后联系时间
                String updateTime = "0";
                if (CONTACT_LAST_UPDATED_TIMESTAMP_INDEX != 0) {
                    updateTime = phoneCursor.getString(CONTACT_LAST_UPDATED_TIMESTAMP_INDEX);
                }
                //String strUpdateTime = DateUtils.formatCouponsDate(updateTime);//最后修改时间
                String strUpdateTime = DateUtils.formatDate(updateTime);//最后修改时间
                /**********************************获得联系人属性*************************************/

                /**********************************进行相关数据计算***********************************/
                long l = Long.parseLong(updateTime);
                //最早更新的联系人
                String firstTime = map.get("contactAddFirstTime");
                if (!TextUtils.isEmpty(firstTime)) {
                    long ll = Long.parseLong(firstTime);
                    if (l < ll) {
                        map.put("contactAddFirstTime", l + "");
                    }
                } else {
                    map.put("contactAddFirstTime", l + "");
                }
                //最晚更新的联系人
                String lastTime = map.get("contactAddLastTime");
                if (!TextUtils.isEmpty(lastTime)) {
                    long ll = Long.parseLong(lastTime);
                    if (l > ll) {
                        map.put("contactAddLastTime", l + "");
                    }
                } else {
                    map.put("contactAddLastTime", l + "");
                }
                long currentTimeMillis = System.currentTimeMillis();
                long time7 = 7 * 24 * 3600 * 1000l;
                long time30 = 30 * 24 * 3600 * 1000l;
                long dateCount = currentTimeMillis - l;
                //最近7天更新的联系人
                if (dateCount < time7) {
                    count7++;
                }
                //最近30天更新的联系人
                if (dateCount < time30) {
                    count30++;
                }
                /**********************************进行相关数据计算***********************************/

                /**********************************组装JsonObject************************************/
                JSONObject object = new JSONObject();
                object.put("contactName", contactName);
                object.put("contactMobile", phoneNumber);
                /*try {
                    HashMap<String, String> infoById = ContactUtils.getInfoById(context, phoneCursor.getString(ID));
                    object.put("prefix", infoById.get("prefix") == null ? "" : infoById.get("prefix"));
                    object.put("suffix", infoById.get("suffix") == null ? "" : infoById.get("suffix"));
                    object.put("firstName", infoById.get("firstName") == null ? "" : infoById.get("firstName"));
                    object.put("middleName", infoById.get("middleName") == null ? "" : infoById.get("middleName"));
                    object.put("lastName", infoById.get("lastName") == null ? "" : infoById.get("lastName"));
                    object.put("birthday", infoById.get("birthday") == null ? "" : infoById.get("birthday"));
                    object.put("nickName", infoById.get("nickName") == null ? "" : infoById.get("nickName"));
                    object.put("organization", infoById.get("organization") == null ? "" : infoById.get("organization"));
                    object.put("jobtitle", infoById.get("jobTitle") == null ? "" : infoById.get("jobTitle"));
                    object.put("department", infoById.get("department") == null ? "" : infoById.get("department"));
                    object.put("address", infoById.get("address") == null ? "" : infoById.get("address"));
                    object.put("addressLabel", "");
                    object.put("mobileLabel", "");
                    object.put("mobileValue", phoneNumber);
                    object.put("note", infoById.get("note") == null ? "" : infoById.get("note"));
                    object.put("pictureUrl", infoById.get("photo") == null ? "" : infoById.get("photo"));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                object.put("firstAddTime", "");
                object.put("lastAddTime", strUpdateTime);
                /**********************************组装JsonObject************************************/

                /**********************************组装JsonArray*************************************/
                if (TextUtils.isEmpty(contactName) || TextUtils.isEmpty(phoneNumber)) {

                } else {
                    LogUtils.i("联系人Item", object.toString());
                    array.put(object);
                }
                /**********************************组装JsonArray*************************************/
            }
            if (!phoneCursor.isClosed()) {
                phoneCursor.close();
                phoneCursor = null;
            }
        }
        //循环已结束，进行最后的数据计算和参数组装
        /*********************************相关数据计算************************************************/
        String contactAddFirstTime = map.get("contactAddFirstTime");
        String contactAddLastTime = map.get("contactAddLastTime");

        if (TextUtils.isEmpty(contactAddFirstTime)) {
            contactAddFirstTime = "0";
        }
        if (TextUtils.isEmpty(contactAddLastTime)) {
            contactAddLastTime = "0";
        }
        /*********************************相关数据计算************************************************/

        /*********************************进行MAP的组装***********************************************/
        map.put("contactAddFirstTime", DateUtils.formatDate(contactAddFirstTime));//最早更新的联系人
        map.put("contactAddLastTime", DateUtils.formatDate(contactAddLastTime));//最晚更新的联系人
        map.put("contactList", array.toString());//联系人详单
        map.put("contactNum", contactCount + "");//联系人数量
        map.put("addContactNum30d", count30 + "");//30天更新的联系人
        map.put("addContactNum7d", count7 + "");//7天更新的联系人
        /*********************************进行MAP的组装***********************************************/
        LogUtils.i("联系人", map.toString());
        if (map.size() == 0 || array.length() == 0) {
            return null;
        }
        return map;
    }

    /**
     * @time 2017/4/5 16:36
     * @desc 根据id获取该di下面的所有电话号码
     */
    private static JSONArray getPhoneListById(Context context, String contactId) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + "='" + contactId + "'", null, null);
        JSONArray array = new JSONArray();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                array.put(phoneNumber);
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
        return array;
    }

    /**
     * @time 2017/3/21 10:40
     * @desc 获取短信数据库
     */
    public static String getMsgDb(Context context) throws Exception {
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
       /*final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表*/
        Uri uri = Uri.parse(SMS_URI_ALL);
       /* String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type",};*/
        String[] projection = new String[]{"_id", "address", "person",
                "date",};
        // 获取手机内部短信
        Cursor smsCursor = context.getContentResolver().query(uri, projection, null,
                null, "date desc");
        // 获取短信中最新的未读短信
           /* Cursor cur = context.getContentResolver().query(uri, projection,
            "read = ?", new String[]{"0"}, "date desc");*/
        if (smsCursor == null) {
            return null;
        }
        JSONArray array = new JSONArray();
        while (smsCursor.moveToNext()) {
            //StringBuilder smsBuilder = new StringBuilder();
            int index_Address = smsCursor.getColumnIndex("address");
            int index_Person = smsCursor.getColumnIndex("person");
            int index_Date = smsCursor.getColumnIndex("date");
            //int index_Body = smsCursor.getColumnIndex("body");
            //int index_Type = smsCursor.getColumnIndex("type");

            String strAddress = smsCursor.getString(index_Address);
            String intPerson = smsCursor.getString(index_Person);
            //String strBody = smsCursor.getString(index_Body);
            //int intType = smsCursor.getInt(index_Type);

            long longDate = smsCursor.getLong(index_Date);
            String strDate = "";
            try {
                strDate = DateUtils.formatDate(longDate + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* String strType = "";
            if (intType == 1) {
                strType = "接收";
            } else if (intType == 2) {
                strType = "发送";
            } else if (intType == 3) {
                strType = "草稿";
            } else if (intType == 4) {
                strType = "发件箱";
            } else if (intType == 5) {
                strType = "发送失败";
            } else if (intType == 6) {
                strType = "待发送列表";
            } else if (intType == 0) {
                strType = "所以短信";
            } else {
                strType = "null";
            }*/

            JSONObject object = new JSONObject();
            object.put("sendPhone", strAddress);
            if (TextUtils.isEmpty(intPerson)) {
                object.put("sendName", strAddress);
            } else {
                object.put("sendName", intPerson);
            }
            object.put("sendDate", strDate);
            if (!TextUtils.isEmpty(strAddress) &&
                    !TextUtils.isEmpty(strDate)) {
                array.put(object);
            }
        }
        if (!smsCursor.isClosed()) {
            smsCursor.close();
            smsCursor = null;
        }
        if (array.length() == 0 || array.length() == 0) {
            return null;
        }
        return array.toString();
    }

    /**
     * @time 2017/3/21 11:27
     * @desc 获取通话记录数据库
     */
    public static HashMap<String, String> getRecordDb(Context context) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor recordCursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                        , CallLog.Calls.NUMBER// 通话记录的电话号码
                        , CallLog.Calls.DATE// 通话记录的日期
                        , CallLog.Calls.DURATION// 通话时长
                        , CallLog.Calls.TYPE}// 通话类型
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (recordCursor == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        JSONArray array = new JSONArray();
        int count7 = 0;//7天通话人数
        int count30 = 0;//30天通话人数
        long time7 = 7L * 24 * 3600 * 1000;
        long time30 = 30L * 24 * 3600 * 1000;
        while (recordCursor.moveToNext()) {
            String name = recordCursor.getString(recordCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = recordCursor.getString(recordCursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = recordCursor.getLong(recordCursor.getColumnIndex(CallLog.Calls.DATE));
            //最早通话时间
            String firstTime = map.get("callFirstTime");
            if (!TextUtils.isEmpty(firstTime)) {
                long l = Long.parseLong(firstTime);
                if (dateLong < l) {
                    map.put("callFirstTime", dateLong + "");
                }
            } else {
                map.put("callFirstTime", dateLong + "");
            }
            //最晚通话时间
            String lastTime = map.get("callLastTime");
            if (!TextUtils.isEmpty(lastTime)) {
                long l = Long.parseLong(lastTime);
                if (dateLong > l) {
                    map.put("callLastTime", dateLong + "");
                }
            } else {
                map.put("callLastTime", dateLong + "");
            }
            long currentTimeMillis = System.currentTimeMillis();
            long dateCount = currentTimeMillis - dateLong;
            //最近7天通话人数
            if (dateCount < time7) {
                count7++;
            }
            //最近30天通话人数
            if (dateCount < time30) {
                count30++;
            }
            String date = DateUtils.formatCouponsDate(dateLong + "");
            int duration = recordCursor.getInt(recordCursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = recordCursor.getInt(recordCursor.getColumnIndex(CallLog.Calls.TYPE));
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    typeString = "打入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    typeString = "打出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    typeString = "未接";
                    break;
                default:
                    break;
            }
            /*Map<String, String> map = new HashMap<String, String>();
            map.put("name", (name == null) ? "未备注联系人" : name);
            map.put("number", number);
            map.put("date", date);
            map.put("duration", (duration / 60) + "分钟");
            map.put("type", typeString);*/
            if (number != null && !number.equals("") && number.length() > 30) {
                number = number.substring(0, 29);
            }
            JSONObject object = new JSONObject();
            if (TextUtils.isEmpty(name)) {
                object.put("callName", number);
            } else {
                object.put("callName", name);
            }
            object.put("callPhone", number);
            object.put("callDate", date);
            object.put("callDuration", duration);
            object.put("callType", typeString);
            if (!TextUtils.isEmpty(number) &&
                    !TextUtils.isEmpty(date) &&
                    duration != 0 &&
                    !TextUtils.isEmpty(typeString)) {
                array.put(object);
            }
        }
        if (!recordCursor.isClosed()) {
            recordCursor.close();
            recordCursor = null;
        }
        String callLastTime = map.get("callLastTime");
        String callFirstTime = map.get("callFirstTime");
        if (!TextUtils.isEmpty(callLastTime)) {
            map.put("callLastTime", DateUtils.formatDate(callLastTime));//最早通话时间
        }
        if (!TextUtils.isEmpty(callFirstTime)) {
            map.put("callFirstTime", DateUtils.formatDate(callFirstTime));//最近通话时间
        }
        map.put("callList", array.toString());//通话记录详单
        map.put("callNum30d", count30 + "");//30通话次数
        map.put("callNum7d", count7 + "");//7天通话次数

        if (map.size() == 0 || array.length() == 0) {
            return null;
        }
        return map;
    }

    /**
     * @time 2017/3/21 15:16
     * @desc 获取app列表
     */
    @SuppressWarnings("WrongConstant")
    public static HashMap<String, String> getAppList(Context context) throws Exception {
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        JSONArray array = new JSONArray();
       /* Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
            Map<String, String> map = new HashMap<String, String>();

            map.put("name", appLabel);
            map.put("package", pkgName);
            JSONObject object = new JSONObject();
            object.put("name", appLabel);
            object.put("package", pkgName);
            array.put(object);
        }*/
        HashMap<String, String> map = new HashMap<>();
        int count = 0;
        //PackageManager p = UIUtils.getContext().getPackageManager();
        List<ApplicationInfo> infos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo info : infos) {
            String name = info.loadLabel(pm).toString();
            String dir = info.publicSourceDir;
            JSONObject object = new JSONObject();
            /*test ------------------------------------- start*/
            /*PackageInfo n = p.getPackageInfo(info.packageName, 0);
            long firstInstallTime = n.firstInstallTime;
            long uadateTime = n.lastUpdateTime;
            LogUtils.e("firstInstallTime", info.packageName + DateUtils.formatDate(firstInstallTime));
            LogUtils.e("uadateTime", info.packageName + DateUtils.formatDate(uadateTime));
            object.put("appFirtInstallTime", DateUtils.formatDate(firstInstallTime));
            object.put("appUpdateInstallTime", DateUtils.formatDate(uadateTime));*/
            /*test ---------------------------------------end*/

            object.put("appName", name);
            try {
                String date = DateUtils.formatDate(new File(dir).lastModified());
                object.put("appInstallTime", date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            array.put(object);
            count++;
        }
        map.put("appNum", count + "");
        map.put("appList", array.toString());
        LogUtils.i("App", map.toString());
        if (map.size() == 0 || array.length() == 0) {
            return null;
        }
        return map;
    }
}
