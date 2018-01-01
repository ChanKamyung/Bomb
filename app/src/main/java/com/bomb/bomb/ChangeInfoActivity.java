package com.bomb.bomb;

/**
 * Created by jinrong on 2018/1/1.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.bomb.bomb.R.id.toolbar;


public class ChangeInfoActivity extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("信息确认");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccount = (EditText) findViewById(R.id.name_old);
        mPwd = (EditText) findViewById(R.id.pwd_old);

        mSureButton = (Button) findViewById(R.id.btn_sure);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);

        mSureButton.setOnClickListener(mListener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(mListener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    View.OnClickListener mListener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:                       //确认按钮的监听事件
                    check();
                    break;
                case R.id.btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    finish();
                    break;
            }
        }
    };
    public void check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            //检查用户是否存在
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result >= 1) {
                //返回1说明用户名和密码均正确
                Intent intent = new Intent(ChangeInfoActivity.this, ResetInfoActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递name参数为userName
                bundle.putString("name", userName);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this,"用户名或密码不正确，请重新输入！" ,Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        int count=mUserDataManager.findUserByName(userName);

        if(count<=0){
            Toast.makeText(this, "用户名不存在，请重新输入！",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userName.equals("")) {
            Toast.makeText(this, "用户名为空，请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd.equals("")) {
            Toast.makeText(this, "密码为空，请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}