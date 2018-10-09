package retrofitdemo.canbot.com.rxjavastudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import retrofitdemo.canbot.com.rxjavastudy.rxjavademo.Rxjavastudy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startRxjava(View view){
        Rxjavastudy.sayHello();
    }
    public void ansyRxjava(View view){
        Rxjavastudy.startAnsy();

    }

    public void maptest(View view){

        Rxjavastudy.mapTest();
    }
    public void flatmaptest(View view){


    }

}
