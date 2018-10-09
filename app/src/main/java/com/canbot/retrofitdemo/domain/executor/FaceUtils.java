package com.canbot.retrofitdemo.domain.executor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import com.canbot.retrofitdemo.FaceAddRe;
import com.canbot.retrofitdemo.domain.bean.FaceAdd;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;
import com.canbot.retrofitdemo.domain.bean.GroupAdd;
import com.canbot.retrofitdemo.domain.retrofitservice.GroupaddService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassImage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${ping} on 2018/8/14.
 */
public class FaceUtils {
//    private  Subscription regonizedSubscription,addSubscription,bindSubscription;

    private GroupaddService groupaddService;

    public FaceUtils() {
        groupaddService = FaceAddRe.getServiceInstance();
    }

    public void addGroup(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        groupaddService.addgroup(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Subscriber<GroupAdd>() {

                    @Override
                    public void onCompleted() {

                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.toString());
                    }


                    @Override
                    public void onNext(GroupAdd groupAdd) {
                        Log.e("TAG", "Observer thread is :" + Thread.currentThread().getName());
//                        Log.e("TAG",response.toString());
                        Log.e("TAG", groupAdd.getTimestamp() + "********" + groupAdd.getTimecost());
                    }
                });

    }

    public void deleteGroup(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        groupaddService.deletegroup(body)
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(Schedulers.io())//主线程显示数据
                .subscribe(new Subscriber<GroupAdd>() {

                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.toString());
                    }


                    @Override
                    public void onNext(GroupAdd groupAdd) {
                        Log.e("TAG", "Observer thread is :" + Thread.currentThread().getName());
                        Log.e("TAG", groupAdd.getTimestamp() + "********" + groupAdd.getTimecost());

                    }
                });

    }

    public Observable<FaceAdd> addFace(byte[] mdata) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        YuvImage img = new YuvImage(mdata, ImageFormat.NV21, 1920, 1080, null);
        Rect rect = new Rect(0, 0, 1920, 1080);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        img.compressToJpeg(rect, 95, os);
        byte[] tmp = os.toByteArray();
        //byte流转化为bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        //bitmap 旋转90度
        Bitmap bitmap1 = rotateBitmap(bitmap, -90);
        //将bitmap上传
        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 95, os1);
        byte[] tmp1 = os1.toByteArray();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), tmp1);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "test.jpg", body);
        return groupaddService.addface(file);
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<FaceAdd>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("TAG",e.toString());
//                    }
//
//                    @Override
//                    public void onNext(FaceAdd faceAdd) {
//                        Log.e("TAG",faceAdd.getData().getFace_token());
//                    }
//                });
    }

    public Observable<FaceAdd> bindFace(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        return groupaddService.bindFace(body);

    }

    public Observable<FaceRecognition> faceRecognition(FacePassDetectionResult mFacePassDetectionResult) {
        synchronized (FaceUtils.class) {
            List<MultipartBody.Part> parts = new ArrayList<>();
            for (FacePassImage passImage : mFacePassDetectionResult.images) {
                /* 将人脸图转成jpg格式图片用来上传 */
                YuvImage img = new YuvImage(passImage.image, ImageFormat.NV21, passImage.width, passImage.height, null);
                Rect rect = new Rect(0, 0, passImage.width, passImage.height);
                Log.e("TAG", "W:" + passImage.width + "H:" + passImage.height);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                img.compressToJpeg(rect, 95, os);
                byte[] byteArray = os.toByteArray();
                RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), byteArray);
                MultipartBody.Part part = MultipartBody.Part.createFormData("image_" + String.valueOf(passImage.trackId), String.valueOf(passImage.trackId) + ".jpg", body);
                parts.add(part);
            }
            RequestBody firstBody = RequestBody.create(MediaType.parse("multipart/form-data"), "zzpwudi");
            RequestBody secondBody = RequestBody.create(MediaType.parse("multipart/form-data"), new String(mFacePassDetectionResult.message));

            return groupaddService.recongitedFace(firstBody, secondBody, parts);

        }
    }

    public Observable<ResponseBody> loadPicture(String url) {
//     RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),url);
        return groupaddService.downloadFile(url);
    }


    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
