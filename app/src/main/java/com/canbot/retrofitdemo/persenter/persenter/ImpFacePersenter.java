package com.canbot.retrofitdemo.persenter.persenter;

import android.graphics.Bitmap;

import com.canbot.retrofitdemo.JsonUtils;
import com.canbot.retrofitdemo.domain.ImpFaceMoudle;
import com.canbot.retrofitdemo.domain.bean.FaceAdd;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;
import com.canbot.retrofitdemo.persenter.imp.Presneter;
import com.canbot.retrofitdemo.view.Imp.IFaceView;

import megvii.facepass.types.FacePassDetectionResult;

/**
 * Created by ${ping} on 2018/8/20.
 */
public class ImpFacePersenter  implements Presneter {

    private ImpFaceMoudle mimpFaceMoudle;
    private IFaceView iFaceView;

    public ImpFacePersenter( ){
     this.mimpFaceMoudle=new ImpFaceMoudle();
     mimpFaceMoudle.setmPresenter(this);
    }

    public  void setFaceView(IFaceView iFaceView){
        this.iFaceView=iFaceView;
    }

   public void  registerFace(byte[] faceData){
        mimpFaceMoudle.addFace(faceData);

   }

    public void regongizedFace(FacePassDetectionResult detectionResult) {

        mimpFaceMoudle.regonizedFace(detectionResult);

    }

    public void loadPicture(String string){
//        mimpFaceMoudle.loadPicture(JsonUtils.getPictureJson(string));
        mimpFaceMoudle.loadPicture(string);

    }


    @Override
    public void addFaceSuccess(FaceAdd faceAdd) {
        mimpFaceMoudle.bindFace(JsonUtils.getFaceJson("zzpwudi",faceAdd.getData().getFace_token()));
    }

    @Override
    public void binFaceSuccess(FaceAdd faceAdd)
    {
    iFaceView.registerSuccess();
    }


    @Override
    public void regonizedSuccess(FaceRecognition faceRecognition) {
        iFaceView.regonizedSuccess(faceRecognition);
    }

    @Override
    public void regonizedFailure(String e) {
        iFaceView.regonizedFaulire();
    }

    @Override
    public void onregisterFailure(String e) {
        iFaceView.registerFaulire();
    }

    @Override
    public void loadPictureSuccess(Bitmap bitmap) {
        iFaceView.onBitmap(bitmap);
    }

    public void cancelAll(){
      mimpFaceMoudle.cancelAllRequest();

    }


}
