package com.bomb.bomb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jinrong on 2017/11/5.
 */

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
        findViewById(R.id.btn_resetpw).setOnClickListener(onClickListener);
        findViewById(R.id.btn_more).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    startActivity(new Intent(MenuActivity.this, RegisterActivity.class));
                    break;
                case R.id.btn_resetpw:
                    startActivity(new Intent(MenuActivity.this, ResetPwdActivity.class));
                    break;
                case R.id.btn_cancel:
                    startActivity(new Intent(MenuActivity.this, CancelActivity.class));
                    break;
                default:
                    Toast.makeText(MenuActivity.this, "待定", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}