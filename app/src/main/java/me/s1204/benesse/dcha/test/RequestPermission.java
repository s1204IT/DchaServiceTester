package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class RequestPermission extends Activity {
    public static final String ACCESS_SYSTEM = "jp.co.benesse.dcha.permission.ACCESS_SYSTEM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(ACCESS_SYSTEM) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ACCESS_SYSTEM}, 0);
            }
        }
        finish();
    }
}
