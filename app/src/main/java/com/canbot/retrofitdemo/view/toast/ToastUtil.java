package com.canbot.retrofitdemo.view.toast;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.canbot.retrofitdemo.R;


/**
 * Created by wangwentao on 2017/1/25.
 * Toast统一管理类
 */

public class ToastUtil {
    private static boolean isShow = true;//默认显示
    private static  Toast mToast = null;//全局唯一的Toast
    private static Toast pToast=null;//图片显示的Toast

    /*private控制不应该被实例化*/
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast){
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public static  void cancelToast() {
        if(isShow && mToast != null){
            mToast.cancel();
        }
        if(isShow&& pToast!=null){
            pToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(Context context, int resId) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }





    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }



    public static void  showImage(Activity context, int duration, Bitmap bitmap) {
        if (isShow){
            if (pToast == null) {
                pToast = new Toast(context);
            }
            LayoutInflater inflater = context.getLayoutInflater();
            View toastView = inflater.inflate(R.layout.toast, null);
            LinearLayout toastLLayout =  toastView.findViewById(R.id.toastll);

            if (toastLLayout == null) {
                return;
            }
            toastLLayout.getBackground().setAlpha(100);
            ImageView imageView =  toastView.findViewById(R.id.toastImageView);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            pToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            pToast.setDuration(duration);
            pToast.setView(toastView);

            pToast.show();
        }
    }
}
