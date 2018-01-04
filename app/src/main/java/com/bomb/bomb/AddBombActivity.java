package com.bomb.bomb;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jinrong on 2018/1/3.
 */

public class AddBombActivity extends AppCompatActivity {
    private EditText mCode;                           //代号编辑
    private EditText mNum;                            //号码编辑
    private EditText mNumCheck;                       //号码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbomb);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("添加炸弹");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCode = (EditText) findViewById(R.id.bomb_code);
        mNum = (EditText) findViewById(R.id.bomb_num);
        mNumCheck = (EditText) findViewById(R.id.bomb_check);

        mSureButton = (Button) findViewById(R.id.btn_sure);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);//建立本地数据库
        }
        mUserDataManager.openDataBase();

        mSureButton.setOnClickListener(m_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_Listener);
    }
    View.OnClickListener m_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
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
            String bombCode = mCode.getText().toString().trim();
            String bombNum = mNum.getText().toString().trim();
            String bombNumCheck = mNumCheck.getText().toString().trim();

            Cursor cursor = mUserDataManager.fetchAllUserDatas();
            while (cursor.moveToNext()) {
                String bomb_code = cursor.getString(1);
                String bomb_num = cursor.getString(2);
                if(bomb_num.equals(bombNum)){
                    Toast.makeText(this, "号码已保存，对应代号为"+bomb_code,Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }else if(bomb_code.equals(bombCode)){
                    Toast.makeText(this, "代号已存在，请重新输入！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if(bombNum.equals(bombNumCheck)==false){     //两次号码输入不一样
                Toast.makeText(this, "号码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mBomb = new UserData(bombCode, bombNum);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mBomb); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, "添加失败，请重新尝试！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "添加成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mCode.getText().toString().trim().length() != 4) {
            Toast.makeText(this, "代号应为4位，请重新输入！",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mNum.getText().toString().trim().length() != 11) {
            Toast.makeText(this, "号码应为11位，请重新输入！",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mNumCheck.getText().toString().trim().length() != 11) {
            Toast.makeText(this, "号码确认应为11位，请重新输入！ ",
                    Toast.LENGTH_SHORT).show();
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