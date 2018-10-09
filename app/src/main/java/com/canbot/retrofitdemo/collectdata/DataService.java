package com.canbot.retrofitdemo.collectdata;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.canbot.retrofitdemo.utlis.Config;
import com.canbot.retrofitdemo.utlis.Utils;

import java.io.File;

public class DataService extends Service {

    public LocalReceiver localReceiver;
    public ActionThread actionThread = new ActionThread();
    public LoadThread loadThread = new LoadThread();
    public String faceToken="";

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.canbot.iknowyou");
        localReceiver = new LocalReceiver();
        //注册本地接收器

        //启动采集线程
        actionThread.start();
        //暂停采集线程
        actionThread.setSuspend(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void uploadData() {



       synchronized (DataService.class) {
            //暂停数据收集线程
            if (actionThread.isAlive()) {
                actionThread.setSuspend(true);
            }
            //上传上一个人的数据
            File file = new File(Config.KNOW_PICTURE_DIR);
            File[] f = file.listFiles();
            if (f.length > 0) {
                //上传文件
                new Thread(loadThread).start();

            } else {
                //启动上传线程
                actionThread.setSuspend(false);
            }
            //创建文件
            Utils.createFile(faceToken);
        }

    }

    public class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
           faceToken= intent.getExtras().getString("faceToken");//获取faceToken
            //上传数据
            DataService.this.uploadData();


        }
    }

    public class LoadThread implements Runnable {

        @Override
        public void run() {
            synchronized (LoadThread.class) {


                //上传成功后，删除文件

            }

        }
    }

}
