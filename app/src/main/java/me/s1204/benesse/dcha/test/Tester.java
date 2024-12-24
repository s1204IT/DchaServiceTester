package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class Tester extends Activity {
    IDchaService mDchaService;
    private static final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
    private static final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";

    private void backHome() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        // DchaService をバインド
        try {
            if (bindService(new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mDchaService = IDchaService.Stub.asInterface(iBinder);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    unbindService(this);
                    Toast.makeText(getApplicationContext(), "DchaService から切断されました", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
            }, Context.BIND_AUTO_CREATE)) {

                // cancelSetup
                findViewById(R.id.btn_cancelSetup).setOnClickListener(view -> {
                    try {
                        mDchaService.cancelSetup();
                    } catch (RemoteException ignored) {
                    }
                });

                // checkPadRooted
                findViewById(R.id.btn_checkPadRooted).setOnClickListener(view -> {
                    try {
                        // false を返す
                        String result = String.valueOf(mDchaService.checkPadRooted());
                        Toast.makeText(getApplicationContext(), "checkPadRooted：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // clearDefaultPreferredApp
                findViewById(R.id.btn_clearDefaultPreferredApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_cleardefaultpreferredapp);

                    // clearDefaultPreferredApp(packageName)
                    findViewById(R.id.exec).setOnClickListener(view1 -> {
                        EditText packageNameBox = findViewById(R.id.clearDefaultPreferredApp_packageName);
                        String packageName = packageNameBox.getText().toString();
                        if (packageName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            mDchaService.clearDefaultPreferredApp(packageName);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view12 -> backHome());
                });

                // copyFile
                findViewById(R.id.btn_copyFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_copyfile);

                    // copyFile(srcFilePath, dstFilePath)
                    findViewById(R.id.exec).setOnClickListener(view13 -> {
                        EditText srcFilePathBox = findViewById(R.id.copyFile_srcFilePath);
                        EditText dstFilePathBox = findViewById(R.id.copyFile_dstFilePath);
                        String srcFilePath = srcFilePathBox.getText().toString();
                        String dstFilePath = dstFilePathBox.getText().toString();
                        if (!srcFilePath.startsWith("/") || !dstFilePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (srcFilePath.endsWith("/") || dstFilePath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mDchaService.copyFile(srcFilePath, dstFilePath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view14 -> backHome());
                });

                // copyUpdateImage
                findViewById(R.id.btn_copyUpdateImage).setOnClickListener(view -> {
                    setContentView(R.layout.layout_copyupdateimage);

                    // copyUpdateImage(srcFilePath, dstFilePath)
                    findViewById(R.id.exec).setOnClickListener(view15 -> {
                        EditText srcFilePathBox = findViewById(R.id.copyUpdateImage_srcFilePath);
                        EditText dstFilePathBox = findViewById(R.id.copyUpdateImage_dstFilePath);
                        String srcFilePath = srcFilePathBox.getText().toString();
                        String dstFilePath = dstFilePathBox.getText().toString();
                        if (!srcFilePath.startsWith("/") || !dstFilePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (srcFilePath.endsWith("/") || dstFilePath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!dstFilePath.startsWith("/cache")) {
                            dstFilePath = "/cache/.." + dstFilePath;
                        }
                        try {
                            String result = String.valueOf(mDchaService.copyUpdateImage(srcFilePath, dstFilePath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view16 -> backHome());
                });

                // deleteFile
                findViewById(R.id.btn_deleteFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_deletefile);

                    // deleteFile(path)
                    findViewById(R.id.exec).setOnClickListener(view17 -> {
                        EditText pathBox = findViewById(R.id.deleteFile_path);
                        String path = pathBox.getText().toString();
                        if (!path.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mDchaService.deleteFile(path));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view18 -> backHome());
                });

                // disableADB
                findViewById(R.id.btn_disableADB).setOnClickListener(view -> {
                    try {
                        mDchaService.disableADB();
                    } catch (RemoteException ignored) {
                    }
                });

                // getCanonicalExternalPath
                findViewById(R.id.btn_getCanonicalExternalPath).setOnClickListener(view -> {
                    setContentView(R.layout.layout_getcanonicalexternalpath);

                    // getCanonicalExternalPath(linkPath)
                    findViewById(R.id.exec).setOnClickListener(view111 -> {
                        EditText linkPathBox = findViewById(R.id.getCanonicalExternalPath_linkPath);
                        String linkPath = linkPathBox.getText().toString();
                        try {
                            String result = String.valueOf(mDchaService.getCanonicalExternalPath(linkPath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view112 -> backHome());
                });

                // getForegroundPackageName
                findViewById(R.id.btn_getForegroundPackageName).setOnClickListener(view -> {
                    try {
                        String result = mDchaService.getForegroundPackageName();
                        Toast.makeText(getApplicationContext(), "getForegroundPackageName：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // getSetupStatus
                findViewById(R.id.btn_getSetupStatus).setOnClickListener(view -> {
                    try {
                        int result = mDchaService.getSetupStatus();
                        Toast.makeText(getApplicationContext(), "getSetupStatus：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // getUserCount
                findViewById(R.id.btn_getUserCount).setOnClickListener(view -> {
                    try {
                        int result = mDchaService.getUserCount();
                        Toast.makeText(getApplicationContext(), "getUserCount：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // hideNavigationBar
                findViewById(R.id.btn_hideNavigationBar).setOnClickListener(view -> {
                    setContentView(R.layout.layout_hidenavigationbar);
                    // hideNavigationBar(true)
                    findViewById(R.id.hideNavBar_true).setOnClickListener(view19 -> {
                        try {
                            mDchaService.hideNavigationBar(true);
                        } catch (RemoteException ignored) {
                        }
                    });
                    // hideNavigationBar(false)
                    findViewById(R.id.hideNavBar_false).setOnClickListener(view110 -> {
                        try {
                            mDchaService.hideNavigationBar(false);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view111 -> backHome());
                });

                // installApp
                findViewById(R.id.btn_installApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_installapp);

                    // installApp(packagePath, flags)
                    findViewById(R.id.exec).setOnClickListener(view112 -> {
                        EditText fileBox = findViewById(R.id.installApp_packagePath);
                        EditText flagsBox = findViewById(R.id.installApp_flags);
                        String filePath = fileBox.getText().toString();
                        String flags = flagsBox.getText().toString();
                        if (!filePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(),"フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!filePath.endsWith(".apk")) {
                            Toast.makeText(getApplicationContext(), "APKファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (flags.isEmpty()) {
                            flags = "2";
                            /*
                             * 0：64 = INSTALL_ALL_USERS
                             *   通常インストール
                             * 1：66 = INSTALL_ALL_USERS | INSTALL_REPLACE_EXISTING
                             *   上書き許可
                             * 2：66 | 128 = INSTALL_ALL_USERS | INSTALL_REPLACE_EXISTING | INSTALL_ALLOW_DOWNGRADE
                             *   ダウングレード許可（debuggable のみ）
                             *   バージョンの比較があるので上書き前提
                             */
                        }
                        try {
                            String result = String.valueOf(mDchaService.installApp(filePath, Integer.parseInt(flags)));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view113 -> backHome());
                });

                // isDeviceEncryptionEnabled
                findViewById(R.id.btn_isDeviceEncryptionEnabled).setOnClickListener(view -> {
                    try {
                        // false を返す
                        String result = String.valueOf(mDchaService.isDeviceEncryptionEnabled());
                        Toast.makeText(getApplicationContext(), "isDeviceEncryptionEnabled：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // rebootPad
                findViewById(R.id.btn_rebootPad).setOnClickListener(view -> {
                    setContentView(R.layout.layout_rebootpad);

                    // rebootPad(flags, path)
                    findViewById(R.id.exec).setOnClickListener(view114 -> {
                        EditText flagsBox = findViewById(R.id.rebootPad_flags);
                        EditText pathBox = findViewById(R.id.rebootPad_packagePath);
                        String flags = flagsBox.getText().toString();
                        String path = pathBox.getText().toString();
                        if (flags.isEmpty()) {
                            flags = "0";
                            path = null;
                        } else if (flags.equals("2")) {
                            if (!path.startsWith("/")) {
                                Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (path.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        try {
                            mDchaService.rebootPad(Integer.parseInt(flags), path);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view115 -> backHome());
                });

                // removeTask
                findViewById(R.id.btn_removeTask).setOnClickListener(view -> {
                    setContentView(R.layout.layout_removetask);

                    // removeTask(packageName)
                    findViewById(R.id.exec).setOnClickListener(view116 -> {
                        EditText packageNameBox = findViewById(R.id.removeTask_packageName);
                        String packageName = packageNameBox.getText().toString();
                        if (packageName.isEmpty()) {
                            packageName = null;
                        }
                        try {
                            mDchaService.removeTask(packageName);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view117 -> backHome());
                });

                // sdUnmount
                findViewById(R.id.btn_sdUnmount).setOnClickListener(view -> {
                    try {
                        mDchaService.sdUnmount();
                        Toast.makeText(getApplicationContext(), "実行しました：sdUnmount", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // setDefaultParam
                findViewById(R.id.btn_setDefaultParam).setOnClickListener(view -> {
                    try {
                        mDchaService.setDefaultParam();
                        Toast.makeText(getApplicationContext(), "実行しました：setDefaultParam", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // setDefaultPreferredHomeApp
                findViewById(R.id.btn_setDefaultPreferredHomeApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setdefaultpreferredhomeapp);

                    // setDefaultPreferredHomeApp(packageName)
                    findViewById(R.id.exec).setOnClickListener(view118 -> {
                        EditText packageNameBox = findViewById(R.id.setDefaultPreferredHomeApp_packageName);
                        String packageName = packageNameBox.getText().toString();
                        if (packageName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            mDchaService.setDefaultPreferredHomeApp(packageName);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view119 -> backHome());
                });

                // setPermissionEnforced
                findViewById(R.id.btn_setPermissionEnforced).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setpermissionenforced);
                    // setPermissionEnforced(true)
                    findViewById(R.id.setPermissionEnforced_true).setOnClickListener(view120 -> {
                        try {
                            mDchaService.setPermissionEnforced(true);
                        } catch (RemoteException ignored) {
                        }
                    });
                    // setPermissionEnforced(false)
                    findViewById(R.id.setPermissionEnforced_false).setOnClickListener(view121 -> {
                        try {
                            mDchaService.setPermissionEnforced(false);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view122 -> backHome());
                });

                // setSetupStatus
                findViewById(R.id.btn_setSetupStatus).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setsetupstatus);

                    // setSetupStatus(0)
                    findViewById(R.id.setSetupStatus_0).setOnClickListener(view123 -> {
                        try {
                            mDchaService.setSetupStatus(0);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // setSetupStatus(1)
                    findViewById(R.id.setSetupStatus_1).setOnClickListener(view124 -> {
                        try {
                            mDchaService.setSetupStatus(1);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // setSetupStatus(2)
                    findViewById(R.id.setSetupStatus_2).setOnClickListener(view125 -> {
                        try {
                            mDchaService.setSetupStatus(2);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // setSetupStatus(3)
                    findViewById(R.id.setSetupStatus_3).setOnClickListener(view126 -> {
                        try {
                            mDchaService.setSetupStatus(3);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view127 -> backHome());
                });

                // setSystemTime
                findViewById(R.id.btn_setSystemTime).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setsystemtime);

                    // setSystemTime(date, format)
                    findViewById(R.id.exec).setOnClickListener(view128 -> {
                        EditText dateBox = findViewById(R.id.setSystemTime_date);
                        String date = dateBox.getText().toString();
                        EditText formatBox = findViewById(R.id.setSystemTime_format);
                        String format = formatBox.getText().toString();
                        if (date.isEmpty() || format.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "値を入力してください", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            mDchaService.setSystemTime(date, format);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view129 -> backHome());
                });

                // uninstallApp
                findViewById(R.id.btn_uninstallApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_uninstallapp);

                    // uninstallApp(packageName, flags)
                    findViewById(R.id.exec).setOnClickListener(view130 -> {
                        EditText packageNameBox = findViewById(R.id.uninstallApp_packageName);
                        EditText flagsBox = findViewById(R.id.uninstallApp_flags);
                        String packageName = packageNameBox.getText().toString();
                        String flags = flagsBox.getText().toString();
                        if (packageName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (flags.isEmpty()) {
                            flags = "0";
                            /*
                             * 1 以外：2 = DELETE_ALL_USERS
                             *   通常アンインストール
                             * 1 の時：3 = DELETE_KEEP_DATA | DELETE_ALL_USERS
                             *   データを保持しパッケージのみ削除
                             */
                        }
                        try {
                            String result = String.valueOf(mDchaService.uninstallApp(packageName, Integer.parseInt(flags)));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view131 -> backHome());
                });

                // verifyUpdateImage
                findViewById(R.id.btn_verifyUpdateImage).setOnClickListener(view -> {
                    setContentView(R.layout.layout_verifyupdateimage);

                    // verifyUpdateImage(path)
                    findViewById(R.id.exec).setOnClickListener(view132 -> {
                        EditText pathBox = findViewById(R.id.verifyUpdateImage_path);
                        String path = pathBox.getText().toString();
                        if (!path.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (path.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            // true を返す
                            String result = String.valueOf(mDchaService.verifyUpdateImage(path));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view133 -> backHome());
                });

            } else {
                Toast.makeText(this, "DchaService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }

        } catch (SecurityException ignored) {
            finish();
            Toast.makeText(this, "｢許可｣を押して権限を昇格して下さい", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW).setClassName(getPackageName(), getPackageName() + ".RequestPermission").addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }

    }

    @Deprecated
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
