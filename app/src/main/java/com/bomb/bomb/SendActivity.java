package com.bomb.bomb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
 * Created by jinrong on 2017/10/25.
 */

public class SendActivity extends AppCompatActivity {

    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("加密短信发送");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InitView();
    }

    private void InitView() {
        Button cancel = (Button) findViewById(R.id.cancel_edit);
        Button send = (Button) findViewById(R.id.send_edit);
        final EditText phone = (EditText) findViewById(R.id.phone_edit_text);
        final EditText msgInput = (EditText) findViewById(R.id.content_edit_text);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收msg值
        String msg = bundle.getString("msg");
        msgInput.setText(msg);

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
                    Toast.makeText(SendActivity.this, "加密发送中，请稍后...", Toast.LENGTH_SHORT).show();
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

                    SmsManager smsManager = SmsManager.getDefault();
                    Intent sentIntent = new Intent("SENT_SMS_ACTION");
                    PendingIntent pi = PendingIntent.getBroadcast(SendActivity.this, 0, sentIntent, 0);
                    if (!address.isEmpty() && !contents.toString().isEmpty()) {
                        smsManager.sendTextMessage(address, null, contents.toString(), pi, null);
                    }
                }
                else {
                    Toast.makeText(SendActivity.this, "请输入正确号码AAAAA", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
