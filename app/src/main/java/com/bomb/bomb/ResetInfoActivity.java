package com.bomb.bomb;

/*
 * Created by jinrong on 2018/1/1.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetInfoActivity extends AppCompatActivity {
    private EditText mAccount_new;                        //用户名编辑
    private EditText mPwd_new;                        //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetinfo);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        name = bundle.getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("信息重置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccount_new = (EditText) findViewById(R.id.name_new);
        mPwd_new = (EditText) findViewById(R.id.pwd_new);
        mPwdCheck = (EditText) findViewById(R.id.pwd_check);

        mSureButton = (Button) findViewById(R.id.btn_sure);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);//建立本地数据库
        }
        mUserDataManager.openDataBase();

        mSureButton.setOnClickListener(mListener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(mListener);
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
            String userName = mAccount_new.getText().toString().trim();
            String userPwd_new = mPwd_new.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();

            if(userPwd_new.equals(userPwdCheck)==false) {
                Toast.makeText(this, "密码确认不正确，请重新输入密码！", Toast.LENGTH_SHORT).show();
                return;
            }else{
                UserData mUser = new UserData(userName, userPwd_new);
                mUserDataManager.insertUserData(mUser);
                mUserDataManager.deleteUserDatabyname(name);
                if(mUserDataManager.findUserByName(name) > 0){
                    Toast.makeText(this, "原信息删除失败！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "信息修改成功！", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        String userName = mAccount_new.getText().toString().trim();
        String userPwd_new = mPwd_new.getText().toString().trim();
        String userPwdCheck = mPwdCheck.getText().toString().trim();

        if (userName.equals("")) {
            Toast.makeText(this, "用户名为空，请重新输入！",Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd_new.equals("")) {
            Toast.makeText(this, "新密码为空，请重新输入！",Toast.LENGTH_SHORT).show();
            return false;
        }else if(userPwdCheck.equals("")) {
            Toast.makeText(this, "密码确认为空，请重新输入！",Toast.LENGTH_SHORT).show();
            return false;
        }else if(userName.length() == 4){
            Toast.makeText(this, "用户名不能为4位，请重新输入！",Toast.LENGTH_SHORT).show();
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
