package com.canbot.retrofitdemo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canbot.retrofitdemo.BaseActivity;
import com.canbot.retrofitdemo.FTPUtils;
import com.canbot.retrofitdemo.HttpUtils;
import com.canbot.retrofitdemo.R;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;
import com.canbot.retrofitdemo.domain.executor.FaceUtils;
import com.canbot.retrofitdemo.persenter.persenter.ImpFacePersenter;
import com.canbot.retrofitdemo.utlis.Utils;
import com.canbot.retrofitdemo.view.Imp.IFaceView;
import com.canbot.retrofitdemo.view.Imp.InitFaceSdk;
import com.canbot.retrofitdemo.view.Impfasspass.InitHandlerFace;
import com.canbot.retrofitdemo.view.Impfasspass.ShowFace;
import com.canbot.retrofitdemo.view.componet.DaggerFaceUtilsComponet;
import com.canbot.retrofitdemo.view.custom.CameraManager;
import com.canbot.retrofitdemo.view.custom.CameraPreview;
import com.canbot.retrofitdemo.view.custom.CameraPreviewData;
import com.canbot.retrofitdemo.view.custom.FaceView;
import com.canbot.retrofitdemo.view.moudle.FaceUtilsModule;
import com.canbot.retrofitdemo.view.permission.QuestPermission;
import com.canbot.retrofitdemo.view.toast.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;
import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassRecognitionResult;

public class FaceReActivity extends BaseActivity implements CameraManager.CameraListener, InitFaceSdk, IFaceView {
    @BindView(R.id.fcview)
    FaceView faceView;
    @BindView(R.id.preview)
    CameraPreview mcameraPreview;
    @BindView(R.id.countDown)
    TextView countDown;
    @BindView(R.id.iv)
    ImageView imageView;

    @BindView(R.id.show_voicedata)
    TextView show_voice;
    @BindView(R.id.show_actiondata)
    TextView show_action;
    @BindView(R.id.back_up)
    TextView back_up;


    @Inject
    FaceUtils faceUtils;
    @Inject
    QuestPermission questPermission;//获取权限
    @Inject
    CameraManager manager;
    @Inject
    InitHandlerFace initHandlerFace;//faceHandlerSDK 初始化类
    @Inject
    ShowFace showFace;//人脸显示框框
    @Inject
    ImpFacePersenter mimpFacePersenter;
    String faceToken;

    private boolean isPictureCallback = true;//相机回调
    private int recLen = 10;
    private boolean isFaceRecognit = true;//是否人脸识别
    boolean isSuccsee = false;//人脸识别成功
    boolean isTimeEnd = false;//倒计时是否完成
    boolean iscountDownStart = true;//倒计时是否可以开启
    boolean isNetworkComplete = true;//网络是否可以访问
    int track_id;
    private static final String URL = "canbot.vip";
    //    private static final String URL = "canbot.net.cn";
    private static final String USERNAME = "liguolin";
    //    private static final String PASSW = "Liguolin123";
    private static final String PASSW = "EVKKlGy3";
    private static final String downDir = "/sdcard/canbot/userprofiledownload/";

    String baseUrl = "http://canbot.vip:8083/api/v1/userInfo/";

    private static final int SHOW_DATA = 1;
    private static final int SHOW_DATA_END = 2;
    public static byte[] faceData;
    public List<Integer> tackidList = new ArrayList<>();
    private int cameraRotation;
    private static boolean cameraFacingFront = true;
    private Context mcontext;
    private FacePassHandler mFacePassHandler;
    private static final int cameraWidth = 1920;
    private static final int cameraHeight = 1080;
    public FacePassDetectionResult detectionResult = null;
    private static final String authIP = "https://api-cn.faceplusplus.com";
    private static final String apiKey = "MOKKOfAkBYxfrvoa7C9ijVo3yR4WZQ1v";
    private static final String apiSecret = "q5BmLgb1iO8AEu-vXtUkHIKIxnLJydTS";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    countDown.setText("最近数据.....");
                    File file = new File(downDir);
                    File[] files = file.listFiles();
                    if (files.length < 1)
                        break;
                    String voicedata = Utils.readOnFocuseActivity(downDir + "voice.txt");
                    String actiondata = Utils.readOnFocuseActivity(downDir + "action.txt");

                    Utils.debugger("语音数据：" + voicedata);
                    Utils.debugger("行为数据：" + actiondata);
                    if (voicedata.length() < 1)
                        show_voice.setText("没有语音数据");
                    else
                        show_voice.setText(voicedata);
                    if (actiondata.length() < 1)
                        show_action.setText("没有行为数据");
                    else
                        show_action.setText(actiondata);
                    break;
                case 2:
                    show_voice.setText("");
                    show_action.setText("");

                    Utils.ExecCmd("rm -f /sdcard/canbot/userprofiledownload/*");
                    restartRegonized();
                    ;
                    break;

            }

        }
    };
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                recLen--;
                if (!isFaceRecognit) {//人脸注册
                    countDown.setText("人脸采集,倒计时" + Integer.toString(recLen) + "秒，请将人脸对准摄像头！");
                } else {//人脸识别中
                    countDown.setText("人脸识别中......" + Integer.toString(recLen));
                }
                if (recLen < 1) {
                    isTimeEnd = true;
                    return;
                }

                handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_re);
        mcontext = this;

        //初始化相机旋转值
        initView();
        DaggerFaceUtilsComponet.builder()
                .faceUtilsModule(new FaceUtilsModule(this, mcontext, cameraRotation))
                .build()
                .injectface(this);
        ButterKnife.bind(this);
        //初始化HandlerSdk使用
        initFaceHandlerSDK();
        mimpFacePersenter.setFaceView(this);
        Utils.createFileDir(downDir);//创建保存当前facetoken语音、行为、图片目录
    }

    public void initFaceHandlerSDK() {
        if (!questPermission.hasPermission(this)) {
            questPermission.requestPermission(this);
        } else {
            initFacePassSDK();
        }
        initHandlerFace.setInitFaceSdk(this);//初始化人脸识别的sdk
        initHandlerFace.initFaceHandler();
        manager.setPreviewDisplay(mcameraPreview);
        manager.setListener(this);
    }

    /**
     * 获取facepass使用权限
     */
    public void initFacePassSDK() {
        FacePassHandler.getAuth(authIP, apiKey, apiSecret);
        FacePassHandler.initSDK(getApplicationContext());
    }

    public void initView() {
        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
        if (windowRotation == 0) {
            cameraRotation = FacePassImageRotation.DEG90;
        } else if (windowRotation == 90) {
            cameraRotation = FacePassImageRotation.DEG0;
        } else if (windowRotation == 270) {
            cameraRotation = FacePassImageRotation.DEG180;
        } else {
            cameraRotation = FacePassImageRotation.DEG270;
        }
        cameraFacingFront = true;//摄像头变为前置


    }

    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {
        if (mFacePassHandler == null || !isPictureCallback) {
            return;
        }
        /*****将相机预览的每一帧数据转换成SDK每一帧所需的格式*******/
        FacePassImage image;
        try {
            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
        } catch (FacePassException e) {
            e.printStackTrace();
            return;
        }
        detectionResult = null;
        /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
        try {
            detectionResult = mFacePassHandler.feedFrame(image);
        } catch (FacePassException e) {
            e.printStackTrace();
        }
        /********检测图片中是否有人脸*************/
        if (detectionResult == null || detectionResult.faceList.length == 0) {
            /* 当前帧没有检出人脸 */
            faceView.clear();
            faceView.invalidate();
            if (isFaceRecognit && recLen < 9) {//如果人脸识别状态，并且执行了一次倒计时，没有检测到人脸就直接重新开始识别
                handler.removeCallbacks(runnable);
                restartRegonized();
            }
            countDown.setText("no face");
            return;
        } else {
            showFace.showFacePassFace(detectionResult.faceList, faceView, manager, mcameraPreview, cameraRotation);
            faceData = cameraPreviewData.nv21Data;
            for (FacePassFace face : detectionResult.faceList) {
                track_id = (int) face.trackId;
                if (tackidList.contains((int) face.trackId)) {
                    countDown.setText("您此张人脸图片已经录入过了，请重新进入镜头......");
                    return;
                } else {
                    if (isFaceRecognit) {
                        if (iscountDownStart) {//开启倒计时
                            handler.postDelayed(runnable, 1000);//每一秒执行一次
                            iscountDownStart = false;//开启一次后不能继续开启
                        }
                        if (isNetworkComplete) {//开启访问网络
                            mimpFacePersenter.regongizedFace(detectionResult);
                            isNetworkComplete = false;
                        }

                        if (isTimeEnd) {//倒计时完成
                            if (!isSuccsee) {//人脸识别没有成功
                                //开启人脸注册
                                startFaceRegister();
                            } else {//继续人脸识别
                                //重新开始人脸识别
                                restartRegonized();
                            }
                        }
                    } else {
                        /*********执行人脸注册*************/
                        if (iscountDownStart) {//开启倒计时
                            handler.postDelayed(runnable, 1000);//每一秒执行一次
                            iscountDownStart = false;//开启一次后不能继续开启
                        }
                        if (isTimeEnd) {//倒计时结束
                            mimpFacePersenter.registerFace(faceData);//添加人脸
                            countDown.setText("正在向服务端添加人脸.......");
                            isPictureCallback = false;
                        }
                    }

                }
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //打开相机，获取相机回调
        if (questPermission.hasPermission(this)) {
            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);
        }
    }

    @Override
    public void initFaceSdk(FacePassHandler facePassHandler) {
        mFacePassHandler = facePassHandler;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mimpFacePersenter.cancelAll();
        manager.release();
    }


    @Override
    public void regonizedSuccess(FaceRecognition faceRecognition) {
        isNetworkComplete = true;//当次网络访问成功
        //将服务端返回的json 生成jsonbean

        int code;
        code = faceRecognition.getCode();
        if (code != 0)
            return;
        FacePassRecognitionResult[] result = null;
        try {
            result = mFacePassHandler.decodeResponse(faceRecognition.getData().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Log.e("TAG", "length:" + result.length);
        if (result == null || result.length == 0) {
            return;
        }
        for (FacePassRecognitionResult res : result) {
            faceToken = new String(res.faceToken);//获取识别后的人脸
            Log.e("TAG", faceToken);
            if (faceToken.length() > 2) {//人脸识别成功，下载图片
                tackidList.add((int) res.trackId);
                //识别成功后终端已经启动的线程
                handler.removeCallbacks(runnable);
                mimpFacePersenter.loadPicture(faceToken);

                //发送facetoken的数据
                Intent intent = new Intent();
                intent.setAction("com.canbot.userporfile.receiver");
                intent.putExtra("facetoken", faceToken);
                sendBroadcast(intent);

                isPictureCallback = false;
                countDown.setText("正在从云端下载最新数据.....");
                faceView.clear();
                faceView.invalidate();


//                restartRegonized();//重新开始识别

            } else {

            }
        }

    }

    @Override
    public void registerSuccess() {
        tackidList.add(track_id);//将此id 加入list 库
        Toast.makeText(this, "绑定人脸到底层库成功，重新识别人脸", Toast.LENGTH_LONG).show();
        mimpFacePersenter.cancelAll();
        restartRegonized();

    }

    @Override
    public void regonizedFaulire() {
        isNetworkComplete = true;//当次网络访问成功
    }

    @Override
    public void registerFaulire() {
        Log.e("TAG", "人脸注册失败");
        Toast.makeText(this, "向服务端添加人脸失败，请重试", Toast.LENGTH_LONG).show();
        startFaceRegister();//开始人脸注册
    }

    @Override
    public void onBitmap(Bitmap bitmap) {
//        imageView.setImageBitmap(bitmap);
        Log.e("TAG", "bitmap=:" + bitmap.toString());
        if (bitmap == null)
            return;
        //将图片保存到本地，下载语音和行文历史纪录
        ToastUtil.showImage(this, Toast.LENGTH_LONG, bitmap);
        String path = "/sdcard/canbot/userprofile/" + faceToken + "/";

        Utils.saveMyBitmap(path, bitmap);//将图片保存到本地
        //将bitmap对象保存到本地

        new Thread() {
            @Override
            public void run() {
                super.run();


                String str = HttpUtils.getFacetoken(baseUrl + faceToken);
                Utils.debugger("下载文件");
                str = str.toString().replace("\\", "");
                Utils.debugger("识别成功后从服务器返回的url信息为：" + str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String strface = jsonObject.getString("face");
                    String straction = jsonObject.getString("action");
                    String strvoice = jsonObject.getString("voice");
                    //ftp 服务器下载文件

                    if (strface != null && straction != null && strvoice != null) {
                        FTPUtils ftpUtils = FTPUtils.getInstance();
                        //下载历史语音和纪录
//                        ftpUtils.ftpDown(URL, 21, USERNAME, PASSW, downDir, strface.substring(strface.indexOf("U")), "face.jpg");
                        ftpUtils.ftpDown(URL, 21, USERNAME, PASSW, downDir, straction.substring(straction.indexOf("U")), "action.txt", "action");
                        ftpUtils.ftpDown(URL, 21, USERNAME, PASSW, downDir, strvoice.substring(strvoice.indexOf("U")), "voice.txt", "voice");
                        Utils.debugger("ftp下载完成");
                    } else {
                        Utils.debugger("服务端没有该机器的信息");
                    }

                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //显示3秒后删除文件
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg1 = new Message();
                msg1.what = 2;
                handler.sendMessage(msg1);
            }
        }.start();


    }

    /**
     * 重新识别
     */


    public void restartRegonized() {
        recLen = 10;
        isFaceRecognit = true;//开启人脸识别
        isSuccsee = false;//人脸识别成功
        isTimeEnd = false;//倒计时是否完成
        iscountDownStart = true;//倒计时是否可以开启
        isNetworkComplete = true;//网络是否可以访问
        isPictureCallback = true;
    }

    public void startFaceRegister() {
        isPictureCallback = true;
        recLen = 4;
        isFaceRecognit = false;//开启人脸注册
        isSuccsee = false;//人脸识别成功
        isTimeEnd = false;//倒计时是否完成
        iscountDownStart = true;//倒计时是否可以开启
        isNetworkComplete = true;//网络是否可以访问
    }

    public void back_up(View view) {
        FaceReActivity.this.finish();
    }
}
