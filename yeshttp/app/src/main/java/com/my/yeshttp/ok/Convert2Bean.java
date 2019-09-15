package com.my.yeshttp.ok;

import com.google.gson.internal.LinkedTreeMap;
import com.my.baselib.lib.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 *
 */
public class Convert2Bean<T> {
    public static <T> ArrayList convert(Class bean, LinkedTreeMap list) {
        ArrayList result = new ArrayList<>();
        //获取bean对象内的属性：
        Field[] fields = bean.getDeclaredFields();
        //循环遍历list,获取linkedTreeMap
        for (int i = 0; i < list.size(); i++) {
            LinkedTreeMap map = (LinkedTreeMap) list.get(i);
            LogUtils.i("mapo", map.toString());
            try {
                T instance = (T) bean.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(instance, map.get(field.getName()));
                }
                result.add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
