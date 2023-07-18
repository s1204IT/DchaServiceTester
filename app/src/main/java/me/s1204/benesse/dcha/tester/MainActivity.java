package me.s1204.benesse.dcha.tester;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.pm.PackageManager.*;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 文字表示のセットアップ
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(17);
        TextView textView = new TextView(this);
        textView.setText("アクティビティを有効にしました");
        textView.setTextSize(2, 50);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        // アクティビティを有効化
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, HideNavigationBar.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, ShowNavigationBar.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Reboot.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Reset.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        // 文字表示
        setContentView(linearLayout);
    }
}
