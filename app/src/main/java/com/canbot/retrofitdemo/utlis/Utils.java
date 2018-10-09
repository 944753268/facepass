package com.canbot.retrofitdemo.utlis;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ${ping} on 2018/9/3.
 */
public class Utils {

    /**
     * 创建文件
     */
    public static void createFile(String face_token) {
        String Ppath = Config.KNOW_PICTURE_DIR;//所创建文件的路径
        String Vpath = Config.KNOW_VOICE_DIR;
        String Apath = Config.KNOW_ACTION_DIR;
        String[] strings = {Ppath, Vpath, Apath};
        for (String str : strings) {

            File f = new File(str);
            if (!f.exists()) {
                f.mkdirs();//创建目录
            }
            //创建文件名
            File file = new File(str, face_token);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }
    }

    /******执行命令************/
    public static void ExecCmd(String cmd) {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //读取文件数据
    public static String readOnFocuseActivity(String path) {
        BufferedReader br = null;
        String line = null;
        StringBuffer strbuf = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                strbuf.append(line);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return strbuf.toString();
    }

    public static void writeRecord(String filePath, String str) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str + "\r\n ");// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getProcessName(String activityName) {
        String RobotMoudle = "";

        Map<String, String> mymap = new HashMap<String, String>();
        mymap.put("com.canbot.u05/.activity.WakeUpActivity", "待机");
        mymap.put("com.canbot.u05/.activity.StandbyActivity", "待机");
        mymap.put("com.canbot.u05/.activity.IndustryModActivity", "待机");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.SaleEnterActivity", "导购模式");
        mymap.put("com.canbot.u05/.activity.ushermode.UsherModeActivity", "门童接待");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.RouteActivity", "地点问询");
        mymap.put("com.canbot.u05/.activity.shoppingguide.merchandise.MerchandiseActivity", "商品导购");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.SalesPublicityActivity", "促销揽客");
        mymap.put("com.canbot.u05/.activity.shoppingguide.advisory.AdvisoryActivity", "咨询问答");
        mymap.put("com.canbot.u05/.activity.DancePerformActivity", "舞蹈表演");
        mymap.put("com.canbot.u05/.activity.AnimationActivity", "正在跳舞");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.ServiceActivity", "售后服务");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.ServiceActgeActivity", "会员管理");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.PaymentActivity", "付款结帐");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideEnterActivity", "导览模式");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideNoTaskActivity", "场馆介绍");
        mymap.put("com.canbot.u05/.activity.mapguide.GuidePointerActivity", "路劲指引");
        mymap.put("com.canbot.u05/.activity.OfficeModeActivity", "办公模式");
        mymap.put("com.canbot.u05/.activity.vipguide.VipGuideActivity", "贵宾引导");
        mymap.put("com.canbot.u05/.activity.training.TaskListActivity", "培训主持");
        mymap.put("com.canbot.u05/.activity.presidemode.CompereNoTaskActivity", "主持");
        mymap.put("com.canbot.u05/.activity.RemoteVideoConnectActivity", "远程会议");
        mymap.put("com.interjoy.sksmarteyesdk_object_5/com.interjoy.sksmarteyesdk.MainActivity", "物体识别");
        mymap.put("com.canbot.u05/.activity.playvideo.VideoPlaybackActivit", "视频播放");
        mymap.put("com.canbot.u05/.activity.simultaneous.SimultaneousSelectAudioActivity", "互动同声");
        mymap.put("com.canbot.u05/.activity.bodytrack.BodyTrackActivity", "人体追踪");
        mymap.put("com.canbot.u05/.activity.meeting.MonitorMeetingConnectActivity", "会议追踪");
        mymap.put("com.canbot.u05/.activity.SettingActivity", "设置");
        mymap.put("com.canbot.u05/.activity.WifiActivity", "wifi设置");
        mymap.put("com.canbot.u05/.activity.BluetoothSetupActivity", "蓝牙设置");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideMapRestoreActivity", "地图加载");
        mymap.put("com.canbot.u05/.activity.AboutActivity", "关于优友");
        mymap.put("com.canbot.u05/.activity.RobotIDActivity", "机器人ID");
        mymap.put("com.canbot.u05/.activity.RecoverCallActivity", "联系我们");
        mymap.put("com.canbot.u05/.activity.PowerInfoActivity", "电量查询");


        Iterator<Map.Entry<String, String>> ite = mymap.entrySet().iterator();
        while (ite.hasNext()) {

            Map.Entry str = (Map.Entry) ite.next();
            if (str.getKey().equals(activityName))
                RobotMoudle = (String) str.getValue();
        }

        return RobotMoudle;
    }


    public static String splitData(String str, String strStart, String strEnd) {
        String tempStr = "";
        String temp = "";
        String[] skt;
        if (str.length() < 2)
            return null;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        skt = tempStr.split(" ");
        if (skt.length > 2)
            temp = skt[2];
        return temp;
    }

    //读取文件数据
    public static void InitData(String path, List list) {
        list.clear();
        BufferedReader br = null;
        String line = null;

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
    }

    /**
     * 获取当前机器的mac地址
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        macSerial = macSerial.replaceAll(" ", "");
        String MAC = macSerial.replaceAll(":", "");
        return MAC;
    }

    /**
     * 将文件读取到byte流中
     *
     * @param path
     * @return
     */

    public static byte[] readFile(String path) {
        File file = new File(path);
        FileInputStream input = null;
        byte[] buf = new byte[0];
        try {
            input = new FileInputStream(file);
            buf = new byte[input.available()];
            input.read(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf;
    }

    /**
     * 删除文件.
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>
     * {@code false}: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null
                && (!file.exists() || file.isFile() && file.delete());
    }

    public static void debugger(String str) {

        Log.e("KNOW", str);
    }

    public static void createFileDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void saveMyBitmap(String path, Bitmap mBitmap) {
        File file=new File(path);
        if (!file.exists())
            file.mkdirs();

        File f = new File(path + "face.jpg");
        try {
        if (!f.exists())
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("在保存图片时出错：" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (Exception e) {
         e.printStackTrace();
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
