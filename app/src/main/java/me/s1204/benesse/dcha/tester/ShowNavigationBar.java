package me.s1204.benesse.dcha.tester;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class ShowNavigationBar extends Activity {
    IDchaService mDchaService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent("jp.co.benesse.dcha.dchaservice.DchaService").setPackage("jp.co.benesse.dcha.dchaservice"), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    mDchaService.hideNavigationBar(false);
                } catch (RemoteException ignored) {
                }
                unbindService(this);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                unbindService(this);
            }
        }, 1);
        finishAndRemoveTask();
    }
}
