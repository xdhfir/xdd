package com.my.baselib.lib.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.baselib.R;
import com.my.baselib.lib.utils.UIUtils;


/**
 * 标题栏
 */
public class TitleView extends RelativeLayout {
    private RelativeLayout mBack;
    private TextView mName;
    private Context mContext;
    private ImageView mOther;
    private LinearLayout mMain;
    private LinearLayout mMe;
    private LinearLayout mMore;
    private LinearLayout mHelp;
    private LinearLayout mQuit;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = View.inflate(context, R.layout.title_view, this);
        mBack = (RelativeLayout) view.findViewById(R.id.title_back);
        mName = (TextView) view.findViewById(R.id.title_name);
        mOther = (ImageView) view.findViewById(R.id.iv_title);
        initData(attrs);
        initEvent();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void initData(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.TitleView);

        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            if (typedArray.getIndex(i) == R.styleable.TitleView_msg_tv) {
                CharSequence text = typedArray.getText(i);
                mName.setText(text);
            }
        }
    }

    private void initEvent() {
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Activity mContext = (Activity) TitleView.this.mContext;
                    mContext.finish();
                } catch (Exception e) {
                    Log.e("TitleView", "上下文不是Activity类型");
                }
            }
        });
        mOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPupUpWindow();
            }
        });
    }

    public void setTitle(String s){
        mName.setText(s);
    }

    private void showPupUpWindow() {
        View v = View.inflate(mContext, R.layout.popupwindow, null);
        mMain = (LinearLayout) v.findViewById(R.id.main_pop);
        mMe = (LinearLayout) v.findViewById(R.id.me_pop);
        mMore = (LinearLayout) v.findViewById(R.id.more_pop);
        mHelp = (LinearLayout) v.findViewById(R.id.help_pop);
        mQuit = (LinearLayout) v.findViewById(R.id.quit_pop);

        final PopupWindow popupWindow = new PopupWindow(v,
                UIUtils.dip2px(135), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.popupwindow_background));
        // 设置好参数之后再show
        if (mOther != null) {
           popupWindow.showAsDropDown(mOther,-UIUtils.dip2px(100),UIUtils.dip2px(5));
        }
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.main_pop) {
                } else if (i == R.id.me_pop) {
                } else if (i == R.id.more_pop) {
                } else if (i == R.id.help_pop) {
                } else if (i == R.id.quit_pop) {
                    popupWindow.dismiss();

                }
                ((Activity) mContext).finish();
            }
        };

        mMain.setOnClickListener(listener);
        mMe.setOnClickListener(listener);
        mMore.setOnClickListener(listener);
        mHelp.setOnClickListener(listener);
        mQuit.setOnClickListener(listener);
    }
}
