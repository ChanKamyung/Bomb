package com.bomb.bomb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jinrong on 2018/1/3.
 */

public class SendNmCmdActivity extends AppCompatActivity {

    private Button mCancelButton;
    private Button mSureButton;
    private EditText mCode;
    private EditText mPwd;
    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;
    private UserDataManager mUserDataManager;         //用户数据管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnmcmd);

        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mSureButton = (Button) findViewById(R.id.btn_sure);
        mCode = (EditText) findViewById(R.id.bomb_code);
        mPwd = (EditText) findViewById(R.id.bomb_pwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("输入参数");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InitView();
    }

    private void InitView() {

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);//建立本地数据库
        }
        mUserDataManager.openDataBase();

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收cmd值
        final String cmd = bundle.getString("cmd");

        //为发送短信设置要监听的广播
        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver, sendFilter);

        mSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCodeAndPwdValid()) {
                    //接收edittext中的内容,并且进行加密
                    //倘若char+8超出了表示范围，则把原字符发过去
                    String mPhone = "";

                    Cursor cursor = mUserDataManager.fetchAllUserDatas();
                    while (cursor.moveToNext()) {
                        String bomb_code = cursor.getString(1);
                        String bomb_num = cursor.getString(2);
                        if (bomb_code.equals(mCode.getText().toString().trim())) {
                            mPhone += bomb_num;
                            break;
                        }
                    }


                    SmsManager smsManager = SmsManager.getDefault();
                    Intent sentIntent = new Intent("SENT_SMS_ACTION");
                    PendingIntent pi = PendingIntent.getBroadcast(SendNmCmdActivity.this, 0, sentIntent, 0);
                    smsManager.sendTextMessage(mPhone, null, cmd + mPwd.getText().toString().trim(), pi, null);
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean isCodeAndPwdValid() {
        if(mCode.getText().toString().trim().length() != 4){
            Toast.makeText(SendNmCmdActivity.this, "代号应为4位，请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwd.getText().toString().trim().length() != 4){
            Toast.makeText(SendNmCmdActivity.this, "口令应为4位，请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mUserDataManager.findUserByName(mCode.getText().toString().trim()) <= 0 ){
            Toast.makeText(SendNmCmdActivity.this, "该代号不存在，请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    class SendStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK) {
                //发送成功
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //发送失败
                Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity摧毁的时候停止监听
        unregisterReceiver(sendStatusReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}