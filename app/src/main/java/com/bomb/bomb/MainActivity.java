package com.bomb.bomb;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;


import com.ddz.floatingactionbutton.FloatingActionMenu;

public class MainActivity extends AppCompatActivity {
    private UserDataManager mUserDataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Bomb!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionMenu fab = (FloatingActionMenu) findViewById(R.id.fab);
        final TextView tv = (TextView) findViewById(R.id.cloud) ;
        fab.setOnFloatingActionsMenuUpdateListener(new FloatingActionMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                tv.setVisibility(View.VISIBLE);
                AlphaAnimation alphaAnimation=new AlphaAnimation(0,0.7f);
                alphaAnimation.setDuration(500);
                alphaAnimation.setFillAfter(true);
                tv.startAnimation(alphaAnimation);
            }

            @Override
            public void onMenuCollapsed() {
                AlphaAnimation alphaAnimation=new AlphaAnimation(0.7f,0);
                alphaAnimation.setDuration(500);
                tv.startAnimation(alphaAnimation);
                tv.setVisibility(View.GONE);
            }
        });

        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fab.performClick();
            }

        });

        findViewById(R.id.fab_bomb).setOnClickListener(onClickListener);
        findViewById(R.id.fab_gps).setOnClickListener(onClickListener);
        findViewById(R.id.fab_change).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //用Bundle携带数据
            Bundle bundle = new Bundle();
            Intent intent;
             switch (v.getId()) {
                case R.id.fab_bomb:
                    //传递mcmd参数
                    bundle.putString("cmd", "BOMB#");
                    intent = new Intent(MainActivity.this, SendNmCmdActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.fab_gps:
                    //传递cmd参数
                    bundle.putString("cmd", "GPS#");
                    intent = new Intent(MainActivity.this, SendNmCmdActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                 case R.id.fab_change:
                     //传递cmd参数
                     bundle.putString("cmd", "CHANGE#");
                     intent = new Intent(MainActivity.this, SendChangeCmdActivity.class);
                     intent.putExtras(bundle);
                     startActivity(intent);
                     break;
                default:
                    Toast.makeText(MainActivity.this, "待开发", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_fingerprint:
                Intent intent=new Intent(MainActivity.this,FingerPrintActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
