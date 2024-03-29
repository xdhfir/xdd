package com.my.baselib.lib.view;

/**
 * 手势解锁所需类
 */
public class Point {
    public static int STATE_NORMAL = 0;
    public static int STATE_CHECK = 1; //
    public static int STATE_CHECK_ERROR = 2; //

    public float x;
    public float y;
    public int state = 0;
    public int index = 0;//

    public Point() {

    }

    public Point(float x, float y, int value) {
        this.x = x;
        this.y = y;
        index = value;
    }

    public int getColNum() {
        return (index - 1) % 3;
    }

    public int getRowNum() {
        return (index - 1) / 3;
    }

}
