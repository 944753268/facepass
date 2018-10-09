package com.canbot.retrofitdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.canbot.retrofitdemo.collectdata.DataService;
import com.canbot.retrofitdemo.utlis.Utils;

public class IndexActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_index);
        Utils.createFile("know");
      Intent intent =new Intent(this, DataService.class);
      startService(intent);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                startActivity(intent);
                IndexActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
