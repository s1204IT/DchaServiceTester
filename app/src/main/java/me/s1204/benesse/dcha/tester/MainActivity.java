package me.s1204.benesse.dcha.tester;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 文字表示のセットアップ
        String msg;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(17);
        TextView textView = new TextView(this);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        // アクティビティを有効化
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, HideNavigationBar.class), 1, 1);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, ShowNavigationBar.class), 1, 1);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Reboot.class), 1, 1);
        msg = "アクティビティを有効にしました";
        // 文字表示
        textView.setText(msg);
        setContentView(linearLayout);
    }
}
