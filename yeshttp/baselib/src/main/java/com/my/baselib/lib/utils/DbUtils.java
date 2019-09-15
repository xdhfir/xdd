package com.my.baselib.lib.utils;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Db工具类
 */
public class DbUtils {
    //获取Database所在目录
    public static String getDbPath(Context context) {
        String DB_PATH = null;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        return DB_PATH;
    }

    /**
     * param activity--不解释，dbName--本地化的文件，saveDB--要保存到的数据库
     * TODO 获取根底数据库文件
     */
    public static boolean imporDatabase(Activity activity, int dbName, String saveDB) {
        //存放数据库的目录
        String dirPath = getDbPath(activity);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //数据库文件
        File file = new File(dir, saveDB);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //加载需要导入的数据库
            InputStream is = activity.getApplicationContext().getResources().openRawResource(dbName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffere = new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
