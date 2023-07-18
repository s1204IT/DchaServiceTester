package dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.File;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class Update extends Activity {
    IDchaService mDchaService;
    // 既定のパス (SDカードのルート)
    protected String SDPath = "/storage/sdcard1/update.zip";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(new File(SDPath)).exists()) {
            // ユーザーストレージのルート
            SDPath = "/storage/emulated/0/update.zip";
        }
        if (!(new File(SDPath)).exists()) {
            // Download ディレクトリ
            SDPath = "/storage/emulated/0/Download/update.zip";
        }
        // 以上のパターンに存在するときのみ実行
        if ((new File(SDPath)).exists()) {
            bindService(new Intent("jp.co.benesse.dcha.dchaservice.DchaService").setPackage("jp.co.benesse.dcha.dchaservice"), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mDchaService = IDchaService.Stub.asInterface(iBinder);
                    try {
                        mDchaService.rebootPad(2, SDPath);
                    } catch (RemoteException ignored) {
                    }
                    unbindService(this);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    unbindService(this);
                }
            }, Context.BIND_AUTO_CREATE);
        }
        finishAndRemoveTask();
    }
}
