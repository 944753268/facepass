package com.canbot.retrofitdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.canbot.retrofitdemo.domain.executor.FaceUtils;
import com.canbot.retrofitdemo.persenter.persenter.ImpFacePersenter;
import com.canbot.retrofitdemo.utlis.Utils;
import com.canbot.retrofitdemo.view.activity.FaceReActivity;
import com.canbot.retrofitdemo.view.componet.DaggerFaceUtilsComponet;
import com.canbot.retrofitdemo.view.moudle.FaceUtilsModule;
import com.canbot.retrofitdemo.view.permission.QuestPermission;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Inject
    JsonUtils jsonUtils;
    @Inject
    FaceUtils faceUtils;
    @Inject
    QuestPermission questPermission;
    @Inject
    ImpFacePersenter facePersenter;

    //我修改了代码哦‘’‘’
    private static final String URL = "192.168.2.199";
    private static final String USERNAME = "liguolin";
    private static final String PASSW = "Liguolin123";
    private static final String downDir = "/sdcard/canbot/userprofiledownload/";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    File file = new File(downDir);
                    File[] files = file.listFiles();
                    if (files.length < 1)
                        break;
                    File f =new File(downDir + "voice.txt");

                    String voicedata = Utils.readOnFocuseActivity(downDir + "voice.txt");
                    String actiondata = Utils.readOnFocuseActivity(downDir + "action.txt");
                    Utils.debugger("语音数据：" + voicedata);
                    Utils.debugger("行为数据：" + actiondata);

                    break;
                case 2:

                    Utils.ExecCmd("rm -f /sdcard/canbot/userprofiledownload/*");
                    ;
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TAG", "开始启动");

        ButterKnife.bind(this);
        //申请权限
        DaggerFaceUtilsComponet.builder()
                .faceUtilsModule(new FaceUtilsModule(this))
                .build()
                .inject(this);
        init();
        Utils.createFileDir(downDir);//创建当前图片保存的目录
    }

    public void init() {
        if (!questPermission.hasPermission(this)) {
            questPermission.requestPermission(this);
        }
    }


    public void addGroup(View view) {
        faceUtils.addGroup(jsonUtils.getJsong());
    }


    public void deleteGroup(View view) {
        faceUtils.deleteGroup(jsonUtils.getJsong());
    }


    public void addFace(View view) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.nimei);

//        faceUtils.addFace(bmp);

    }

    public void bindFace(View view) {
        faceUtils.bindFace(jsonUtils.getFaceJson("zzpdashen", "urxuJscDGd29IUyQgAMfzw=="));
    }

    String baseUrl = "http://canbot.vip:8083/api/v1/userInfo/";
    String faceToken;
    int i=0;
    public void regonizdeFace(View view) {


        Intent intent = new Intent(this, FaceReActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        faceUtils.cancelAllRequest();

    }


}
