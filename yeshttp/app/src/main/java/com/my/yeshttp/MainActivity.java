package com.my.yeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.my.yeshttp.bean.DemoBean;
import com.my.yeshttp.httpmanager.ApiUtils;
import com.my.yeshttp.httpmanager.AppSubscriber;
import com.my.yeshttp.utils.HttpUtils;

import java.util.HashMap;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    TextView demoOne;
    TextView demoTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoOne = findViewById(R.id.demoOnetest);
        demoTwo = findViewById(R.id.demotwotest);
        demoOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDemoOne();
            }
        });
        demoTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDemoTwo();
            }
        });

    }
    //demoone 方法请求
    private void getDemoOne(){
        Subscriber subscriber = new AppSubscriber<DemoBean>(this, true, true) {
            @Override
            protected void doNext(DemoBean data) {

            }
        };
        HttpUtils.connectNet(ApiUtils.getService().getDemoOne(getDemoOneMap()), subscriber);
    }
    //demotwo 方法请求
    private void getDemoTwo(){
        Subscriber subscriber = new AppSubscriber<DemoBean>(this, true, true) {
            @Override
            protected void doNext(DemoBean data) {

            }
        };
        HttpUtils.connectNet(ApiUtils.getService().getDemoTwo(),subscriber);
    }
    //返回demoone请求参数
    private HashMap<String, String> getDemoOneMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("para","22");
        return map;
    }

}
