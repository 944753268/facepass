package retrofitdemo.canbot.com.rxjavastudy.rxjavademo;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${ping} on 2018/8/14.
 */
public class Rxjavastudy {

    public static final String TAG = "TAG";

    /**
     * 运行简单的编程
     */
    public static void sayHello() {
        //创建被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("废物EZ");
                e.onNext("废物下路二人组");
                e.onComplete();
                print("当前的线程是" + Thread.currentThread().getName());
            }
        });

        //创建观察者

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                print("接收到的值为:" + value);
            }

            @Override
            public void onError(Throwable e) {
                print(e.toString());

            }

            @Override
            public void onComplete() {
                print("当前的线程是" + Thread.currentThread().getName());
                print("onComplete");

            }
        };
        //被观察者被观察者订阅
        observable.subscribe(observer);

    }

    /**
     * 打印log
     *
     * @param str
     */
    public static void print(String str) {

        Log.e(TAG, str);
    }


    /**
     * 运行异步链式rxjava
     */
    public static void startAnsy() {

        //注释的的部分是全部的Observer写法
//        Observable.create(new ObservableOnSubscribe<String>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//
//                e.onNext("现在的中单也是垃圾");
//                e.onNext("想在的打野也是垃圾");
//                e.onNext("现在的上单最牛逼");
//                e.onComplete();
//                print("当前的线程是" + Thread.currentThread().getName());
//
//            }
//
//        }).subscribeOn(Schedulers.io())
//           .observeOn(Schedulers.io())
//           .subscribe(new Observer<String>() {
//               @Override
//               public void onSubscribe(Disposable d) {
//
//               }
//
//               @Override
//               public void onNext(String value) {
//
//                   print("接收到的值是："+value);
//
//               }
//
//               @Override
//               public void onError(Throwable e) {
//
//               }
//
//               @Override
//               public void onComplete() {
//                   print("onComplete");
//                   print("当前的线程是" + Thread.currentThread().getName());
//               }
//           });

        //下面的是观察者只需要订阅onNext 中的方法事项
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                e.onNext("现在的中单也是垃圾");
                e.onNext("想在的打野也是垃圾");
                e.onNext("现在的上单最牛逼");
                e.onComplete();
                print("当前的线程是" + Thread.currentThread().getName());

            }

        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print("接收到的值是："+s);
                        print("当前的线程是" + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * map 就是转换，
     * filter就是过滤
     */
    public static void  mapTest(){

      Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext("5");
                e.onNext("66");
                e.onNext("7");
                e.onComplete();
            }
        }).map(new Function<String ,Integer>() {


            @Override
            public Integer apply(String s) throws Exception {
                print("第一次map的值："+s);
                return Integer.parseInt(s);
            }
        }).map(new Function<Integer,String>() {


            @Override
            public String apply(Integer integer) throws Exception {
                print("第二次的值是："+integer);
                return integer+"-";
            }
        }).filter(new Predicate<String>() {

            @Override
            public boolean test(String s) throws Exception {
                return s.length()>2;
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<String>() {
              /**
               * Consume the given value.
               *
               * @param s the value
               * @throws Exception on error
               */
              @Override
              public void accept(String s) throws Exception {
                  print("最终接收的的数据是"+s);
              }
          });




    }

    /**
     * flatMap 就是分发以一个的被观察者的对象
     */
    public static void flatMapTest(){







    }


}
