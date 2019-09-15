package com.my.baselib.lib.view.numberpwdview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.my.baselib.R;
import com.my.baselib.lib.base.BaseViewHolder;
import com.my.baselib.lib.base.MBaseAdapter;
import com.my.baselib.lib.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * create by Administrator at 2017/3/1
 * description: 六位数字密码输入框
 */
public class NumberPwdView extends LinearLayout implements View.OnClickListener {
    
    NumberPwdAdapter mAdapter;
    private FrameLayout back;
    private TextView numberOne;
    private TextView numberTwo;
    private TextView numberThree;
    private TextView numberFour;
    private TextView numberFive;
    private TextView numberSix;
    private TextView forgetPwd;
    private NoScrollGridView gv;
    private Context mContext;
    private List data;
    private List<TextView> tvData;
    private boolean isReverse = false;

    public NumberPwdView(Context context) {
        this(context, null);
    }

    public NumberPwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View v = View.inflate(context, R.layout.view_number_pwd, this);
        back = (FrameLayout) v.findViewById(R.id.back_number_pwd_view);
        numberOne = (TextView) v.findViewById(R.id.number_one_number_pwd_view);
        numberTwo = (TextView) v.findViewById(R.id.number_two_number_pwd_view);
        numberThree = (TextView) v.findViewById(R.id.number_three_number_pwd_view);
        numberFour = (TextView) v.findViewById(R.id.number_four_number_pwd_view);
        numberFive = (TextView) v.findViewById(R.id.number_five_number_pwd_view);
        numberSix = (TextView) v.findViewById(R.id.number_six_number_pwd_view);
        forgetPwd = (TextView) v.findViewById(R.id.forget_pwd_number_pwd_view);
        gv = (NoScrollGridView) v.findViewById(R.id.gv_number_pwd_view);
        tvData = new ArrayList<>();
        tvData.add(numberSix);
        tvData.add(numberFive);
        tvData.add(numberFour);
        tvData.add(numberThree);
        tvData.add(numberTwo);
        tvData.add(numberOne);
        initData(attrs);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NumberPwdKeyBoardBean o = (NumberPwdKeyBoardBean) data.get(position);
                inputToTextView(o);
            }
        });
    }

    /**
     * @time 2017/3/1 17:46
     * @desc 监听键盘输入，然后改变密码显示框显示的密码
     */
    private void inputToTextView(NumberPwdKeyBoardBean o) {
        if (o.type == 2) {
            //代表是删除按钮
            deleteNumber();
        } else if (o.type == 1) {
            //小数点
            addDot();
        } else if (o.type == 0) {
            //代表的是正常的输入
            inputNumber(o);
        }
    }

    private void addDot() {

    }

    /**
     * @time 2017/3/1 17:49
     * @desc 输入数字
     */
    private void inputNumber(NumberPwdKeyBoardBean bean) {
        if (!isReverse) {
            Collections.reverse(tvData);
            isReverse = true;
        }
        for (TextView tv : tvData) {
            String s = tv.getText().toString().trim();
            if (TextUtils.isEmpty(s)) {
                tv.setText(bean.name);
                break;
            }
        }

        String six = numberSix.getText().toString().trim();
        if (!TextUtils.isEmpty(six)) {
            if (numberPwdViewClickListener != null) {
                numberPwdViewClickListener.input(getText());
            }
        }
    }

    /**
     * @time 2017/3/1 17:49
     * @desc 删除数字
     */
    private void deleteNumber() {
        if (isReverse) {
            Collections.reverse(tvData);
            isReverse = false;
        }
        for (TextView tv : tvData) {
            String s = tv.getText().toString().trim();
            if (!TextUtils.isEmpty(s)) {
                tv.setText("");
                break;
            }
        }
    }

    private void initData(AttributeSet attrs) {
        resetPwdTextHeight();
        setDataToKeyBoard();
    }

    /**
     * @time 2017/3/1 17:35
     * @desc 给键盘设置数据
     */
    @SuppressWarnings("unchecked")
    private void setDataToKeyBoard() {
        data = new ArrayList();
        data.add(new NumberPwdKeyBoardBean("1", 0, 0));
        data.add(new NumberPwdKeyBoardBean("2", 0, 0));
        data.add(new NumberPwdKeyBoardBean("3", 0, 0));
        data.add(new NumberPwdKeyBoardBean("4", 0, 0));
        data.add(new NumberPwdKeyBoardBean("5", 0, 0));
        data.add(new NumberPwdKeyBoardBean("6", 0, 0));
        data.add(new NumberPwdKeyBoardBean("7", 0, 0));
        data.add(new NumberPwdKeyBoardBean("8", 0, 0));
        data.add(new NumberPwdKeyBoardBean("9", 0, 0));
        data.add(new NumberPwdKeyBoardBean(".", 1, 0));
        data.add(new NumberPwdKeyBoardBean("0", 0, 0));
        data.add(new NumberPwdKeyBoardBean("X", 2, 0));
        if (mAdapter == null) {
            mAdapter = new NumberPwdAdapter();
        }
        mAdapter.setData(data);
        gv.setAdapter(mAdapter);
    }

    //根据手机屏幕的不同，重新设置密码输入框的高度
    private void resetPwdTextHeight() {
        final ViewTreeObserver observer = numberOne.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                ViewGroup.LayoutParams layoutParams = numberOne.getLayoutParams();
                layoutParams.height = numberOne.getWidth();
                numberOne.setLayoutParams(layoutParams);
                numberTwo.setLayoutParams(layoutParams);
                numberThree.setLayoutParams(layoutParams);
                numberFour.setLayoutParams(layoutParams);
                numberFive.setLayoutParams(layoutParams);
                numberSix.setLayoutParams(layoutParams);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (numberPwdViewClickListener != null) {
            int i = v.getId();
            if (i == R.id.back_number_pwd_view) {
                numberPwdViewClickListener.click(0);

            } else if (i == R.id.forget_pwd_number_pwd_view) {
                numberPwdViewClickListener.click(1);
            }
        }
    }

    /**
     * @time 2017/3/1 16:57
     * @desc 将已输入的密码返回
     */
    public String getText() {
        String one = numberOne.getText().toString().trim();
        String two = numberTwo.getText().toString().trim();
        String three = numberThree.getText().toString().trim();
        String four = numberFour.getText().toString().trim();
        String five = numberFive.getText().toString().trim();
        String six = numberSix.getText().toString().trim();

        if (TextUtils.isEmpty(one) ||
                TextUtils.isEmpty(two) ||
                TextUtils.isEmpty(three) ||
                TextUtils.isEmpty(four) ||
                TextUtils.isEmpty(five) ||
                TextUtils.isEmpty(six)) {
            return "";
        }

        if (one.length() > 1 ||
                two.length() > 1 ||
                three.length() > 1 ||
                four.length() > 1 ||
                five.length() > 1 ||
                six.length() > 1) {
            return "";
        }

        return one + two + three + four + five + six;
    }

    /**
     * @time 2017/3/1 17:02
     * @desc
     */
    public interface NumberPwdViewClickListener {
        //0 代表按的是返回键
        //1 代表按的是忘记密码
        void click(int flag);

        //密码输入完成之后，将会调用该方法
        void input(String pwd);
    }

    private NumberPwdViewClickListener numberPwdViewClickListener;

    public void setNumberPwdViewClickListener(NumberPwdViewClickListener numberPwdViewClickListener) {
        this.numberPwdViewClickListener = numberPwdViewClickListener;
    }

    /**
     * @time 2017/3/1 17:15
     * @desc 键盘适配器
     */
    public class NumberPwdAdapter extends MBaseAdapter {
        @Override
        protected void contactData2View(Object item, BaseViewHolder baseViewHolder) {
            NumberPwdKeyBoardBean bean = (NumberPwdKeyBoardBean) item;
            TextView tv = baseViewHolder.getView(R.id.tv_item_number_pwd_view);
            tv.setText(bean.name);
            if (bean.image != 0) {
                tv.setBackgroundResource(bean.image);
            } else {
                tv.setBackgroundColor(mContext.getResources().getColor(R.color.base_white));
            }

            if(bean.type==1){
                tv.setBackgroundResource(R.drawable.number_pwd_other_selector);
                //tv.setBackgroundColor(mContext.getResources().getColor(R.color.base_gray_2));
            }else if(bean.type==2){
                tv.setBackgroundResource(R.drawable.number_pwd_other_selector);
                //tv.setBackgroundColor(mContext.getResources().getColor(R.color.base_gray_2));
            }else {
                tv.setBackgroundResource(R.drawable.number_pwd_number_selector);
                //tv.setBackgroundColor(mContext.getResources().getColor(R.color.base_white));
            }
        }

        @Override
        protected int idLayout() {
            return R.layout.item_number_pwd_view;
        }
    }

    public class NumberPwdKeyBoardBean {
        NumberPwdKeyBoardBean(String name, int type, int image) {
            this.name = name;
            this.type = type;
            this.image = image;
        }

        public String name;
        public int type;
        public int image;
    }
}
