package com.bomb.bomb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

        findViewById(R.id.btn_resetinfo).setOnClickListener(onClickListener);
        findViewById(R.id.btn_addbomb).setOnClickListener(onClickListener);
        findViewById(R.id.btn_deletebomb).setOnClickListener(onClickListener);
        findViewById(R.id.btn_other).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_resetinfo:
                    startActivity(new Intent(MenuActivity.this, ChangeInfoActivity.class));
                    break;
                case R.id.btn_addbomb:
                    startActivity(new Intent(MenuActivity.this, AddBombActivity.class));
                    break;
                case R.id.btn_deletebomb:
                    startActivity(new Intent(MenuActivity.this, DeleteBombActivity.class));
                    break;
                case R.id.btn_other:
                    startActivity(new Intent(MenuActivity.this, DetailActivity.class));
                    break;
                default:
                    Toast.makeText(MenuActivity.this, "待开发", Toast.LENGTH_SHORT).show();
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