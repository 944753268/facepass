package com.canbot.retrofitdemo.utlis;

import android.os.Environment;

/**
 * Created by ${ping} on 2018/6/1.
 */
public class Config {

    public static final String  KNOW_PICTURE_DIR= Environment.getExternalStorageDirectory().getPath()+"/"+"canbot/know/picture";
    public static final String  KNOW_VOICE_DIR= Environment.getExternalStorageDirectory().getPath()+"/"+"canbot/know/voice";
    public static final String  KNOW_ACTION_DIR= Environment.getExternalStorageDirectory().getPath()+"/"+"canbot/know/action";

}
