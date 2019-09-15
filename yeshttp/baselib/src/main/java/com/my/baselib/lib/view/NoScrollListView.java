package com.my.baselib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**c
 * 最大高度的ListView,用于和SrollView嵌套使用
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
