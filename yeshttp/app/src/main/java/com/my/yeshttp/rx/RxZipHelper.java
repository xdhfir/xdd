package com.my.yeshttp.rx;

import android.app.Activity;

import com.my.baselib.lib.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;

/**
 * create by Administrator at 2017/4/21
 * description:
 */
public class RxZipHelper {
    @SuppressWarnings("unchecked")
    public void zip2(Activity context, String dialogMsg,
                    Observable observable1, Observable observable2,
                    final ObservableListener listener) {
        final LoadingDialog dialog=new LoadingDialog(context);
        dialogShow(dialogMsg, dialog);
        Observable.zip(observable1, observable2, new Func2<Object, Object, List<Object>>() {
            @Override
            public List<Object> call(Object a, Object b) {
                List<Object> list = new ArrayList<Object>();
                list.add(a);
                list.add(b);
                return list;
            }
        }).compose(RxSchedulersHelper.io_main())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {
                        dialogDismiss(dialog);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.failure();
                        }
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.success(objects);
                        }
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void zip3(Activity context, String dialogMsg,
                     Observable observable1, Observable observable2,Observable observable3,
                     final ObservableListener listener) {
        final LoadingDialog dialog=new LoadingDialog(context);
        dialogShow(dialogMsg, dialog);
        Observable.zip(observable1, observable2, observable3,new Func3<Object, Object, Object,List<Object>>() {
            @Override
            public List<Object> call(Object a, Object b,Object c) {
                List<Object> list = new ArrayList<Object>();
                list.add(a);
                list.add(b);
                list.add(c);
                return list;
            }
        }).compose(RxSchedulersHelper.io_main())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {
                        dialogDismiss(dialog);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.failure();
                        }
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.success(objects);
                        }
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void zip4(Activity context, String dialogMsg,
                     Observable observable1, Observable observable2,
                     Observable observable3,Observable observable4,
                     final ObservableListener listener) {
        final LoadingDialog dialog=new LoadingDialog(context);
        dialogShow(dialogMsg, dialog);
        Observable.zip(observable1, observable2, observable3,observable4,new Func4<Object, Object, Object,Object,List<Object>>() {
            @Override
            public List<Object> call(Object a, Object b,Object c,Object d) {
                List<Object> list = new ArrayList<Object>();
                list.add(a);
                list.add(b);
                list.add(c);
                list.add(d);
                return list;
            }
        }).compose(RxSchedulersHelper.io_main())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {
                        dialogDismiss(dialog);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.failure();
                        }
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        dialogDismiss(dialog);
                        if(listener!=null){
                            listener.success(objects);
                        }
                    }
                });
    }


    private void dialogShow(String dialogMsg, LoadingDialog dialog) {
        if (dialogMsg == null) {
            dialog.setText("正在访问网络，请稍候...");
        } else {
            dialog.setText(dialogMsg);
        }
        dialog.showDialog();
    }

    private void dialogDismiss(LoadingDialog dialog) {
        if(dialog!=null&&dialog.isDialogShowing()){
            dialog.dismissDialog();
        }
    }

    public interface ObservableListener{
        void failure();
        void success(List<Object> data);
    }
}
