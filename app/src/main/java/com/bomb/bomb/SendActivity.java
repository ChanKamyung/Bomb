package com.bomb.bomb;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jinrong on 2017/10/25.
 */

public class SendActivity extends Activity {

    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        InitView();
    }

    private void InitView() {
        Button cancel = (Button) findViewById(R.id.cancel_edit);
        Button send = (Button) findViewById(R.id.send_edit);
        final EditText phone = (EditText) findViewById(R.id.phone_edit_text);
        final EditText msgInput = (EditText) findViewById(R.id.content_edit_text);

        //为发送短信设置要监听的广播
        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver, sendFilter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rightAdd = true;
                //接收edittext中的内容,并且进行加密
                //倘若char+8超出了表示范围，则把原字符发过去
                String address = phone.getText().toString();
                for(int i = 0; i < address.length(); i++){
                    if(address.charAt(i) < '0' || address.charAt(i) > '9'){
                        rightAdd = false;
                        break;
                    }
                }
                if(rightAdd && !address.isEmpty()) {
                    Toast.makeText(SendActivity.this, "加密发送中，请稍后aaaaa" +
                            "aa...", Toast.LENGTH_SHORT).show();
                    String content = msgInput.getText().toString();
                    String contents = "";
                    for (int i = 0; i < content.length(); i++) {
                        try {
                            contents += (char) (content.charAt(i) + 8);
                        } catch (Exception e) {
                            contents += (char) (content.charAt(i));
                        }
                    }
                    contents += ' ';
                    //Log.i("hahaha",contents);

                    //发送短信
                    // 并使用sendTextMessage的第四个参数对短信的发送状态进行监控
                    SmsManager smsManager = SmsManager.getDefault();
                    Intent sentIntent = new Intent("SENT_SMS_ACTION");
                    PendingIntent pi = PendingIntent.getBroadcast(SendActivity.this, 0, sentIntent, 0);
                    if (!address.isEmpty() && !contents.toString().isEmpty()) {
                        smsManager.sendTextMessage(address, null, contents.toString(), pi, null);
                    }
                }
                else {
                    Toast.makeText(SendActivity.this, "请输入正确号码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
}
