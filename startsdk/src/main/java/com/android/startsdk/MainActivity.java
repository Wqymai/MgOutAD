package com.android.startsdk;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //注意:android 5.0之前是可以通过隐式意图打开其他app的服务的,5.0之后只能通过显式意图来打开.
        final Intent intent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        intent.setComponent(new ComponentName("com.mgoutad", "com.mgoutad.an.MiiActivity"));
//        intent.setAction("android.action.WQYSERVICE");
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        Button btn= (Button) findViewById(R.id.open_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"启动",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
