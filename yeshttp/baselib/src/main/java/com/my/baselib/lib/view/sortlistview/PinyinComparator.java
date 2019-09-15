package com.my.baselib.lib.view.sortlistview;

import java.util.Comparator;

/**
 * create by Administrator at 2017/3/30
 * description:
 */
public class PinyinComparator implements Comparator<ContactSortModel> {
    public int compare(ContactSortModel o1, ContactSortModel o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
