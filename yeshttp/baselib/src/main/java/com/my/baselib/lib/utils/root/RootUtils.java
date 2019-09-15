package com.my.baselib.lib.utils.root;

import java.io.File;
/**
 * create by Administrator at 2017/3/21
 * description: 工具类，判断当前机器是否已经root
 */
public class RootUtils {
    public static boolean checkIsRoot() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }
    private static boolean checkRootMethod1(){
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2(){
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    private static boolean checkRootMethod3() {
        return new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null;
    }
}
