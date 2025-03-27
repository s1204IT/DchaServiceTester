package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class Tester extends Activity implements View.OnClickListener {
    IDchaService mDchaService = null;
    private static final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
    private static final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";

    private void makeText(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }

    private void setOnClickListener(int resId) {
        findViewById(resId).setOnClickListener(this);
    }

    private void changeLayout(int layout, int btnId) {
        setContentView(layout);
        setOnClickListener(btnId);
        setOnClickListener(R.id.backHome);
    }

    private String getBoxText(int resId) {
        return ((EditText) findViewById(resId)).getText().toString();
    }

    private static final int[] FUNC_LIST = {
            R.id.btn_cancelSetup,
            R.id.btn_checkPadRooted,
            R.id.btn_clearDefaultPreferredApp,
            R.id.btn_copyFile,
            R.id.btn_copyUpdateImage,
            R.id.btn_deleteFile,
            R.id.btn_disableADB,
            R.id.btn_getCanonicalExternalPath,
            R.id.btn_getForegroundPackageName,
            R.id.btn_getSetupStatus,
            R.id.btn_getUserCount,
            R.id.btn_hideNavigationBar,
            R.id.btn_installApp,
            R.id.btn_isDeviceEncryptionEnabled,
            R.id.btn_rebootPad,
            R.id.btn_removeTask,
            R.id.btn_sdUnmount,
            R.id.btn_setDefaultParam,
            R.id.btn_setDefaultPreferredHomeApp,
            R.id.btn_setPermissionEnforced,
            R.id.btn_setSetupStatus,
            R.id.btn_setSystemTime,
            R.id.btn_uninstallApp,
            R.id.btn_verifyUpdateImage
    };

    Intent BIND_DCHA = new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE);
    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mDchaService = IDchaService.Stub.asInterface(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mDchaService = null;
            unbindService(this);
            makeText("DchaService から切断されました");
            finishAndRemoveTask();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        for (int resId: FUNC_LIST) setOnClickListener(resId);

        if (!bindService(BIND_DCHA, mConn, Context.BIND_AUTO_CREATE)) {
            Toast.makeText(this, "DchaService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }
    }

    @Override
    public void onClick(final View v) {
        final int resId = v.getId();
        if (resId == R.id.backHome) //noinspection deprecation
            onBackPressed();
        try {
            if (resId == R.id.btn_cancelSetup) {
                mDchaService.cancelSetup();
            } else if (resId == R.id.btn_checkPadRooted) {
                makeText("checkPadRooted：" + mDchaService.checkPadRooted());
            } else if (resId == R.id.btn_clearDefaultPreferredApp) {
                changeLayout(R.layout.layout_cleardefaultpreferredapp, R.id.exec_clearDefaultPreferredApp);
            } else if (resId == R.id.exec_clearDefaultPreferredApp) {
                String packageName = getBoxText(R.id.clearDefaultPreferredApp_packageName);
                mDchaService.clearDefaultPreferredApp(packageName);
            } else if (resId == R.id.btn_copyFile) {
                changeLayout(R.layout.layout_copyfile, R.id.exec_copyFile);
            } else if (resId == R.id.exec_copyFile) {
                String srcFilePath = getBoxText(R.id.copyFile_srcFilePath);
                String dstFilePath = getBoxText(R.id.copyFile_dstFilePath);
                makeText("copyFile：" + mDchaService.copyFile(srcFilePath, dstFilePath));
            } else if (resId == R.id.btn_copyUpdateImage) {
                changeLayout(R.layout.layout_copyupdateimage, R.id.btn_copyUpdateImage);
            } else if (resId == R.id.exec_copyUpdateImage) {
                String srcFilePath = getBoxText(R.id.copyUpdateImage_srcFilePath);
                String dstFilePath = getBoxText(R.id.copyUpdateImage_dstFilePath);
                makeText("copyUpdateImage：" + mDchaService.copyUpdateImage(srcFilePath, dstFilePath));
            } else if (resId == R.id.btn_deleteFile) {
                changeLayout(R.layout.layout_deletefile, R.id.exec_deleteFile);
            } else if (resId == R.id.exec_deleteFile) {
                String path = getBoxText(R.id.deleteFile_path);
                makeText("deleteFile：" + mDchaService.deleteFile(path));
            } else if (resId == R.id.btn_disableADB) {
                mDchaService.disableADB();
            } else if (resId == R.id.btn_getCanonicalExternalPath) {
                changeLayout(R.layout.layout_getcanonicalexternalpath, R.id.exec_getCanonicalExternalPath);
            } else if (resId == R.id.exec_getCanonicalExternalPath) {
                String linkPath = getBoxText(R.id.getCanonicalExternalPath_linkPath);
                makeText("getCanonicalExternalPath：" + mDchaService.getCanonicalExternalPath(linkPath));
            } else if (resId == R.id.btn_getForegroundPackageName) {
                makeText("getForegroundPackageName：" + mDchaService.getForegroundPackageName());
            } else if (resId == R.id.btn_getSetupStatus) {
                makeText("getSetupStatus：" + mDchaService.getSetupStatus());
            } else if (resId == R.id.btn_getUserCount) {
                makeText("getUserCount：" + mDchaService.getUserCount());
            } else if (resId == R.id.btn_hideNavigationBar) {
                changeLayout(R.layout.layout_hidenavigationbar, R.id.hideNavBar_true);
                setOnClickListener(R.id.hideNavBar_false);
            } else if (resId == R.id.hideNavBar_true) {
                mDchaService.hideNavigationBar(true);
            } else if (resId == R.id.hideNavBar_false) {
                mDchaService.hideNavigationBar(false);
            } else if (resId == R.id.btn_installApp) {
                changeLayout(R.layout.layout_installapp, R.id.exec_installApp);
            } else if (resId == R.id.exec_installApp) {
                String path = getBoxText(R.id.installApp_path);
                String installFlag = getBoxText(R.id.installApp_installFlag);
                if (installFlag.isEmpty()) installFlag = "2";
                makeText("installApp：" + mDchaService.installApp(path, Integer.parseInt(installFlag)));
            } else if (resId == R.id.btn_isDeviceEncryptionEnabled) {
                makeText("isDeviceEncryptionEnabled：" + mDchaService.isDeviceEncryptionEnabled());
            } else if (resId == R.id.btn_rebootPad) {
                changeLayout(R.layout.layout_rebootpad, R.id.exec_rebootPad);
            } else if (resId == R.id.exec_rebootPad) {
                String rebootMode = getBoxText(R.id.rebootPad_rebootMode);
                String srcPath = getBoxText(R.id.rebootPad_srcFile);
                if (rebootMode.isEmpty()) rebootMode = "0";
                if (srcPath.isEmpty()) srcPath = null;
                mDchaService.rebootPad(Integer.parseInt(rebootMode), srcPath);
            } else if (resId == R.id.btn_removeTask) {
                changeLayout(R.layout.layout_removetask, R.id.exec_removeTask);
            } else if (resId == R.id.exec_removeTask) {
                String packageName = getBoxText(R.id.removeTask_packageName);
                if (packageName.isEmpty()) packageName = null;
                mDchaService.removeTask(packageName);
            } else if (resId == R.id.btn_sdUnmount) {
                mDchaService.sdUnmount();
            } else if (resId == R.id.btn_setDefaultParam) {
                mDchaService.setDefaultParam();
            } else if (resId == R.id.btn_setDefaultPreferredHomeApp) {
                changeLayout(R.layout.layout_setdefaultpreferredhomeapp, R.id.exec_setDefaultPreferredHomeApp);
            } else if (resId == R.id.exec_setDefaultPreferredHomeApp) {
                String packageName = getBoxText(R.id.setDefaultPreferredHomeApp_packageName);
                mDchaService.setDefaultPreferredHomeApp(packageName);
            } else if (resId == R.id.btn_setPermissionEnforced) {
                changeLayout(R.layout.layout_setpermissionenforced, R.id.setPermissionEnforced_true);
                setOnClickListener(R.id.setPermissionEnforced_false);
            } else if (resId == R.id.setPermissionEnforced_true) {
                mDchaService.setPermissionEnforced(true);
            } else if (resId == R.id.setPermissionEnforced_false) {
                mDchaService.setPermissionEnforced(false);
            } else if (resId == R.id.btn_setSetupStatus) {
                changeLayout(R.layout.layout_setsetupstatus, R.id.setSetupStatus_0);
                setOnClickListener(R.id.setSetupStatus_1);
                setOnClickListener(R.id.setSetupStatus_2);
                setOnClickListener(R.id.setSetupStatus_3);
            } else if (resId == R.id.setSetupStatus_1) {
                mDchaService.setSetupStatus(1);
            } else if (resId == R.id.setSetupStatus_2) {
                mDchaService.setSetupStatus(2);
            } else if (resId == R.id.setSetupStatus_3) {
                mDchaService.setSetupStatus(3);
            } else if (resId == R.id.btn_setSystemTime) {
                changeLayout(R.layout.layout_setsystemtime, R.id.exec_setSystemTime);
            } else if (resId == R.id.exec_setSystemTime) {
                String time = getBoxText(R.id.setSystemTime_time);
                String timeFormat = getBoxText(R.id.setSystemTime_timeFormat);
                mDchaService.setSystemTime(time, timeFormat);
            } else if (resId == R.id.btn_uninstallApp) {
                changeLayout(R.layout.layout_uninstallapp, R.id.exec_uninstallApp);
            } else if (resId == R.id.exec_uninstallApp) {
                String packageName = getBoxText(R.id.uninstallApp_packageName);
                String uninstallFlag = getBoxText(R.id.uninstallApp_uninstallFlag);
                if (uninstallFlag.isEmpty()) uninstallFlag = "1";
                makeText("uninstallApp：" + mDchaService.uninstallApp(packageName, Integer.parseInt(uninstallFlag)));
            } else if (resId == R.id.btn_verifyUpdateImage) {
                changeLayout(R.layout.layout_verifyupdateimage, R.id.exec_verifyUpdateImage);
            } else if (resId == R.id.exec_verifyUpdateImage) {
                String updateFile = getBoxText(R.id.verifyUpdateImage_updateFile);
                makeText("verifyUpdateImage：" + mDchaService.verifyUpdateImage(updateFile));
            }
        } catch (RemoteException ignored) {
        } catch (SecurityException ignored) {
            finish();
            makeText("｢許可｣を押して権限を昇格して下さい");
            startActivity(new Intent(Intent.ACTION_VIEW).setClassName(getPackageName(), RequestPermission.class.getName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    /** @noinspection DeprecatedIsStillUsed*/
    @Deprecated
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_about) {
            setContentView(R.layout.about_dcha);
            return true;
        } else if (itemId == R.id.menu_settings) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            return true;
        } else if (itemId == R.id.menu_devopts) {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
