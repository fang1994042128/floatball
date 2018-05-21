package com.utf.floatball;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView startFloatBar;
    private Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startFloatBar = (TextView) findViewById(R.id.startFloatBar);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        startFloatBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Permissions.checkUsageStatsPermission(MainActivity.this) && Permissions.isFloatWindowOpAllowed(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "已成功开启悬浮窗权限", Toast.LENGTH_SHORT).show();

                } else {
                    openUsageStatsPermissionPage(MainActivity.this);
                    Toast.makeText(MainActivity.this, "请去权限管理中心 开启悬浮窗权限和 获取查看使用情况权限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Permissions.checkUsageStatsPermission(MainActivity.this) && Permissions.isFloatWindowOpAllowed(MainActivity.this)) {
                        updateFloatBallServiceState(true);
                    } else {
                        Toast.makeText(MainActivity.this, "未开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    updateFloatBallServiceState(false);
                    Toast.makeText(MainActivity.this, "已关闭悬浮窗", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void openUsageStatsPermissionPage(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateFloatBallServiceState(boolean isStart) {
        if (isStart) {
            LauncherFloatBallService.startService();
        } else {
            LauncherFloatBallService.stopService();
        }
    }
}
