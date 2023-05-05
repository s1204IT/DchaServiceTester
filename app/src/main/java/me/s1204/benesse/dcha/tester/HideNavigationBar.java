package me.s1204.benesse.dcha.tester;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class HideNavigationBar extends Activity {
    IDchaService mDchaService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (Settings.System.getInt(getContentResolver(), "hide_navigation_bar") == 1) {
                mDchaService.hideNavigationBar(false);
            } else {
                mDchaService.hideNavigationBar(true);
            }
        } catch (Settings.SettingNotFoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
