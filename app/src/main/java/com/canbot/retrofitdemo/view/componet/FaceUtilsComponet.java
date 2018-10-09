package com.canbot.retrofitdemo.view.componet;

import com.canbot.retrofitdemo.MainActivity;
import com.canbot.retrofitdemo.persenter.persenter.ImpFacePersenter;
import com.canbot.retrofitdemo.view.activity.FaceReActivity;
import com.canbot.retrofitdemo.view.moudle.FaceUtilsModule;

import dagger.Component;

/**
 * Created by ${ping} on 2018/8/15.
 */

@Component(modules = {FaceUtilsModule.class})
public interface FaceUtilsComponet {
    void inject(MainActivity baseActivity);
    void injectface(FaceReActivity baseActivity);
    void injectfacePersenter( ImpFacePersenter facePersenter);
}
