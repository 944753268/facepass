package com.canbot.retrofitdemo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${ping} on 2018/8/15.
 */
public class JsonUtils {

    public String getJsong(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("group_name","zzpwudi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
      String  jspn= jsonObject.toString();
        return  jspn;
    }

    public  static String  getFaceJson(String face_group,String face_token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("group_name", face_group);
            jsonObject.put("face_token", face_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    public static String getPictureJson(String facktoken){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("face_token", facktoken);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }
}
