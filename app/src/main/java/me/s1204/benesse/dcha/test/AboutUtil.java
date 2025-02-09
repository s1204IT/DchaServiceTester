package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.os.Bundle;

public class AboutUtil extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_util);
    }
    @Override
    @Deprecated
    public void onBackPressed() {
        finish();
    }
}
