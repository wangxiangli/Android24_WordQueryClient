package com.bush.android24_wordqueryclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bush.wordaidl.IWordAidlInterface;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="--------" ;
    private TextView textView_result;
    private EditText editText_query;
    private IWordAidlInterface wordAidlInterface;
    private ServiceConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initMyBindService();
    }

    private void initMyBindService() {
        Intent intent=new Intent();
        intent.setPackage("com.bush.android24_wordqureyservice");
        intent.setAction("com.bush.android24_wordqureyservice.service.WordService");
        conn=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                wordAidlInterface=IWordAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                wordAidlInterface=null;
            }
        };
       boolean flag= bindService(intent,conn , BIND_AUTO_CREATE);
        Log.i(TAG, "initMyBindService: "+flag);
    }

    private void initView() {
        textView_result= (TextView) findViewById(R.id.text_result);
        editText_query= (EditText) findViewById(R.id.edit_main_query);
    }

    public void clickView(View view){
        String edit_result=editText_query.getText().toString();
        Log.i(TAG, "clickView: "+edit_result);
        switch (view.getId()){
            case R.id.btn_query:
                try {
                    String result=wordAidlInterface.getValue(edit_result)+"";
                    Log.i(TAG, "clickView: "+result);
                    textView_result.setText(result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
