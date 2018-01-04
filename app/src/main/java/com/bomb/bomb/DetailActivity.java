package com.bomb.bomb;

import android.app.Activity;

/**
 * Created by jinrong on 2017/11/6.
 */

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DetailActivity extends Activity {

    List<UserData> userList;
    UserDataManager mUserDataManager;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ListView lv = (ListView) findViewById(R.id.user_lv);
        userList = new ArrayList<UserData>();

        // 创建MyOpenHelper实例
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
        }
        mUserDataManager.openDataBase();

        Cursor cursor = mUserDataManager.fetchAllUserDatas();
        while (cursor.moveToNext()) {
            String user_name = cursor.getString(1);
            String user_pwd = cursor.getString(2);
            UserData mUserData = new UserData(user_name, user_pwd);
            userList.add(mUserData);
        }

        // 创建MyAdapter实例
        myAdapter = new MyAdapter(this);
        // 向listview中添加Adapter
        lv.setAdapter(myAdapter);
    }

    // 创建MyAdapter继承BaseAdapter
    class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //  TODO Auto-generated method stub
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 从personList取出Person
            UserData u = userList.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.detail_item, null);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewHolder.txt_pwd = (TextView) convertView
                        .findViewById(R.id.user_pwd);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_name.setText(u.getUserName());
            viewHolder.txt_pwd.setText(u.getUserPwd());
            return convertView;
        }
    }

    class ViewHolder {
        private TextView txt_name;
        private TextView txt_pwd;
    }
}
