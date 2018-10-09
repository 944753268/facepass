package com.canbot.retrofitdemo.collectdata;

import com.canbot.retrofitdemo.utlis.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${ping} on 2018/7/16.
 */
public class ActionThread extends  Thread {
    public static final int TIME_SPAN = 5 * 1000;//时间间隔
    public static final String focusePath = "/sdcard/canbot/know/data.txt";
    public static final  String filePath = "/sdcard/canbot/know/action";
    public static  String recordPath = "";

    public static String cmd = "dumpsys activity | grep mFocusedActivity > /sdcard/canbot/know/data.txt";

    String activityData = "";
    String nowTime = "";
    String lastData = null;
    String writeDate = "";
    private String thriftControl = "thriftcontrol";
    private boolean thriftSuspend = false;

    @Override
    public void run() {
        while (true) {


            try {
                Thread.sleep(TIME_SPAN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /************* 线程控制-暂停 **************/
            if (thriftSuspend == true) {

                synchronized (thriftControl) {
                    try {
                        thriftControl.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            //获取栈顶的Activity名字
            Utils.ExecCmd(cmd);
            //获取命令得到的数据
            activityData = Utils.readOnFocuseActivity(focusePath);
            if (activityData.length() < 2)
                continue;
            //将获取的数据拆分，只取需要的部分
            activityData = Utils.splitData(activityData, "{", "}");
            if (activityData.length() < 2)
                continue;
            writeDate = Utils.getProcessName(activityData);
            if (writeDate.length() < 2)
                continue;
          //获取文件名字
            File fileDir= new File(filePath);
            File[] array = fileDir.listFiles();
            if(array.length>0)
                recordPath=array[0].getName();
            recordPath=filePath+"/"+recordPath;
//            File file = new File(recordPath);
//            if(!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            nowTime = df.format(new Date());// new Date()为获取当前系统时间

            if (writeDate.equals(lastData)) {
                //在同一个活动中不作任何事情
            } else {
                //把数据接写进文件
                if (lastData == null)
                    Utils.writeRecord(recordPath, nowTime + "," + "启动程序");
                else
                    Utils.writeRecord(recordPath, nowTime + "-----" + lastData + "结束");
                Utils.writeRecord(recordPath, nowTime + "-----" + writeDate + "启动");
            }
            lastData = writeDate;
        }

    }
    public void setSuspend(boolean suspend){
        if (!suspend) {
            synchronized (thriftControl) {
                thriftControl.notifyAll();
            }
        }
        this.thriftSuspend = suspend;
    }

}
