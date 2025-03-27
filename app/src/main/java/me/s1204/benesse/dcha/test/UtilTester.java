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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

import jp.co.benesse.dcha.dchautilservice.IDchaUtilService;

public class UtilTester extends Activity implements View.OnClickListener {
    IDchaUtilService mUtilService = null;
    private static final String UTIL_PACKAGE = "jp.co.benesse.dcha.dchautilservice";
    private static final String UTIL_SERVICE = UTIL_PACKAGE + ".DchaUtilService";

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
            R.id.btn_clearDefaultPreferredApp,
            R.id.btn_copyFile,
            R.id.btn_copyDirectory,
            R.id.btn_deleteFile,
            R.id.btn_existsFile,
            R.id.btn_getCanonicalExternalPath,
            R.id.btn_getDisplaySize,
            R.id.btn_getLcdSize,
            R.id.btn_getUserCount,
            R.id.btn_hideNavigationBar,
            R.id.btn_listFiles,
            R.id.btn_makeDir,
            R.id.btn_sdUnmount,
            R.id.btn_setDefaultPreferredHomeApp,
            R.id.btn_setForcedDisplaySize
    };

    Intent BIND_UTIL = new Intent(UTIL_SERVICE).setPackage(UTIL_PACKAGE);
    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mUtilService = IDchaUtilService.Stub.asInterface(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mUtilService = null;
            unbindService(this);
            makeText("DchaUtilService から切断されました");
            finishAndRemoveTask();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util);
        for (int resId: FUNC_LIST) setOnClickListener(resId);

        if (!bindService(BIND_UTIL, mConn, Context.BIND_AUTO_CREATE)) {
            Toast.makeText(this, "DchaUtilService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }
    }

    @Override
    public void onClick(final View v) {
        final int resId = v.getId();
        if (resId == R.id.backHome) //noinspection deprecation
            onBackPressed();
        try {
            if (resId == R.id.btn_clearDefaultPreferredApp) {
                changeLayout(R.layout.layout_cleardefaultpreferredapp, R.id.exec_clearDefaultPreferredApp);
            } else if (resId == R.id.exec_clearDefaultPreferredApp) {
                String packageName = getBoxText(R.id.clearDefaultPreferredApp_packageName);
                mUtilService.clearDefaultPreferredApp(packageName);
            } else if (resId == R.id.btn_copyFile) {
                changeLayout(R.layout.layout_copyfile, R.id.exec_copyFile);
            } else if (resId == R.id.exec_copyFile) {
                String srcFilePath = getBoxText(R.id.copyFile_srcFilePath);
                String dstFilePath = getBoxText(R.id.copyFile_dstFilePath);
                makeText("copyFile：" + mUtilService.copyFile(srcFilePath, dstFilePath));
            } else if (resId == R.id.btn_copyDirectory) {
                changeLayout(R.layout.layout_copydirectory, R.id.exec_copyDirectory);
            } else if (resId == R.id.exec_copyDirectory) {
                String srcDirPath = getBoxText(R.id.copyDirectory_srcDirPath);
                String dstDirPath = getBoxText(R.id.copyDirectory_dstDirPath);
                String makeTopDir = ((Spinner) findViewById(R.id.copyDirectory_makeTopDir)).getSelectedItem().toString();
                makeText("copyDirectory：" + mUtilService.copyDirectory(srcDirPath, dstDirPath, makeTopDir.equals("true")));
            } else if (resId == R.id.btn_deleteFile) {
                changeLayout(R.layout.layout_deletefile, R.id.exec_deleteFile);
            } else if (resId == R.id.exec_deleteFile) {
                String path = getBoxText(R.id.deleteFile_path);
                makeText("deleteFile：" + mUtilService.deleteFile(path));
            } else if (resId == R.id.btn_existsFile) {
                changeLayout(R.layout.layout_existsfile, R.id.exec_existsFile);
            } else if (resId == R.id.exec_existsFile) {
                String path = getBoxText(R.id.existsFile_path);
                makeText("existsFile：" + mUtilService.existsFile(path));
            } else if (resId == R.id.btn_getCanonicalExternalPath) {
                changeLayout(R.layout.layout_getcanonicalexternalpath, R.id.exec_getCanonicalExternalPath);
            } else if (resId == R.id.exec_getCanonicalExternalPath) {
                String linkPath = getBoxText(R.id.getCanonicalExternalPath_linkPath);
                makeText("getCanonicalExternalPath：" + mUtilService.getCanonicalExternalPath(linkPath));
            } else if (resId == R.id.btn_getDisplaySize) {
                makeText("getDisplaySize：" + Arrays.toString(mUtilService.getDisplaySize()));
            } else if (resId == R.id.btn_getLcdSize) {
                makeText("getLcdSize：" + Arrays.toString(mUtilService.getLcdSize()));
            } else if (resId == R.id.btn_getUserCount) {
                makeText("getUserCount：" + mUtilService.getUserCount());
            } else if (resId == R.id.btn_hideNavigationBar) {
                changeLayout(R.layout.layout_hidenavigationbar, R.id.hideNavBar_true);
                setOnClickListener(R.id.hideNavBar_false);
            } else if (resId == R.id.hideNavBar_true) {
                mUtilService.hideNavigationBar(true);
            } else if (resId == R.id.hideNavBar_false) {
                mUtilService.hideNavigationBar(false);
            } else if (resId == R.id.btn_listFiles) {
                changeLayout(R.layout.layout_listfiles, R.id.exec_listFiles);
            } else if (resId == R.id.exec_listFiles) {
                String path = getBoxText(R.id.listFiles_path);
                makeText("listFiles：" + Arrays.toString(mUtilService.listFiles(path)));
            } else if (resId == R.id.btn_makeDir) {
                changeLayout(R.layout.layout_makedir, R.id.exec_makeDir);
            } else if (resId == R.id.exec_makeDir) {
                String path = getBoxText(R.id.makeDir_path);
                String dirname = getBoxText(R.id.makeDir_dirname);
                makeText("makeDir：" + mUtilService.makeDir(path, dirname));
            } else if (resId == R.id.btn_sdUnmount) {
                mUtilService.sdUnmount();
            } else if (resId == R.id.btn_setForcedDisplaySize) {
                changeLayout(R.layout.layout_setforceddisplaysize, R.id.exec_setForcedDisplaySize);
            } else if (resId == R.id.exec_setForcedDisplaySize) {
                String width = getBoxText(R.id.setForcedDisplaySize_width);
                String height = getBoxText(R.id.setForcedDisplaySize_height);
                makeText(width.isEmpty() || height.isEmpty() ? "値を入力してください" : "setForcedDisplaySize：" + mUtilService.setForcedDisplaySize(Integer.parseInt(width), Integer.parseInt(height)));
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
            setContentView(R.layout.about_util);
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
