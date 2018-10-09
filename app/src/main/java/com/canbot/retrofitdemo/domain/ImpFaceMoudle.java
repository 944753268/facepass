package com.canbot.retrofitdemo.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.canbot.retrofitdemo.domain.bean.FaceAdd;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;
import com.canbot.retrofitdemo.domain.executor.FaceUtils;
import com.canbot.retrofitdemo.domain.imp.FaceMoudle;
import com.canbot.retrofitdemo.persenter.imp.Presneter;

import java.io.InputStream;

import megvii.facepass.types.FacePassDetectionResult;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${ping} on 2018/8/20.
 */
public class ImpFaceMoudle implements FaceMoudle {
    private FaceUtils faceUtils;
    private Presneter mPresenter;
    private Subscription regonizedSubscription,addSubscription,bindSubscription,loadSubscription;

    public void setmPresenter(Presneter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public ImpFaceMoudle() {
        this.faceUtils = new FaceUtils();
    }

    @Override
    public void addFace(byte[] mdata) {
        addSubscription =faceUtils.addFace(mdata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceAdd>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.onregisterFailure(e.toString());
                    }

                    @Override
                    public void onNext(FaceAdd faceAdd) {
                        mPresenter.addFaceSuccess(faceAdd);
                    }
                });
    }

    @Override
    public void bindFace(String json) {
        bindSubscription=faceUtils.bindFace(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceAdd>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.onregisterFailure(e.toString());
                    }

                    @Override
                    public void onNext(FaceAdd faceAdd) {
                        mPresenter.binFaceSuccess(faceAdd);

                    }
                });

    }

    @Override
    public void regonizedFace(FacePassDetectionResult mFacePassDetectionResult) {
        regonizedSubscription= faceUtils.faceRecognition(mFacePassDetectionResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceRecognition>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.regonizedFailure(e.toString());

                        Log.e("TAG", e.toString());
                    }

                    @Override
                    public void onNext(FaceRecognition faceRecognition) {
                        mPresenter.regonizedSuccess(faceRecognition);
                    }
                });
    }

    public void loadPicture(String json){
        loadSubscription= faceUtils.loadPicture(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG",e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        mPresenter.loadPictureSuccess(bitmap);
                    }
                });
    }


    public void cancelAllRequest(){
        if(regonizedSubscription!=null)
            regonizedSubscription.unsubscribe();
        if(addSubscription!=null)
            addSubscription.unsubscribe();
        if(bindSubscription!=null)
            bindSubscription.unsubscribe();
    }
}
