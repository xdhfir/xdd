package com.my.baselib.lib.utils;

import android.text.InputType;
import android.widget.EditText;

/**
 *
 */
public class EditTextUtils {
    public static void inputShow(EditText et){
        et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }
    public static void inputDismiss(EditText et){
        et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
    public static void inputDismissNumber(EditText et){
        et.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_NUMBER);
    }
    public static void inputShowNumber(EditText et){
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
