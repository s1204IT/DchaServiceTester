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

                    // clearDefaultPreferredApp(pkgId)
                    findViewById(R.id.exec).setOnClickListener(view1 -> {
                        EditText pkgBox = findViewById(R.id.clearDefaultPreferredApp_package);
                        String pkgId = pkgBox.getText().toString();
                        if (pkgId.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            mDchaService.clearDefaultPreferredApp(pkgId);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view12 -> backHome());
                });

                // copyFile
                findViewById(R.id.btn_copyFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_copyfile);

                    // copyFile(from, to)
                    findViewById(R.id.exec).setOnClickListener(view13 -> {
                        EditText fromBox = findViewById(R.id.copyFile_from);
                        EditText toBox = findViewById(R.id.copyFile_to);
                        String fromPath = fromBox.getText().toString();
                        String toPath = toBox.getText().toString();
                        if (!fromPath.startsWith("/") || !toPath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (fromPath.endsWith("/") || toPath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mDchaService.copyFile(fromPath, toPath));
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

                    // copyUpdateImage(from, to)
                    findViewById(R.id.exec).setOnClickListener(view15 -> {
                        EditText fromBox = findViewById(R.id.copyUpdateImage_from);
                        EditText toBox = findViewById(R.id.copyUpdateImage_to);
                        String fromPath = fromBox.getText().toString();
                        String toPath = toBox.getText().toString();
                        if (!fromPath.startsWith("/") || !toPath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (fromPath.endsWith("/") || toPath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!toPath.startsWith("/cache")) {
                            toPath = "/cache/.." + toPath;
                        }
                        try {
                            String result = String.valueOf(mDchaService.copyUpdateImage(fromPath, toPath));
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

                    // deleteFile(file)
                    findViewById(R.id.exec).setOnClickListener(view17 -> {
                        EditText fileBox = findViewById(R.id.deleteFile_file);
                        String filePath = fileBox.getText().toString();
                        if (!filePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mDchaService.deleteFile(filePath));
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

                    // installApp(file)
                    findViewById(R.id.exec).setOnClickListener(view112 -> {
                        EditText fileBox = findViewById(R.id.installApp_packagePath);
                        EditText flagBox = findViewById(R.id.installApp_flag);
                        String filePath = fileBox.getText().toString();
                        String flag = flagBox.getText().toString();
                        if (!filePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(),"フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!filePath.endsWith(".apk")) {
                            Toast.makeText(getApplicationContext(), "APKファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (flag.isEmpty()) {
                            flag = "2";
                        }
                        try {
                            String result = String.valueOf(mDchaService.installApp(filePath, Integer.parseInt(flag)));
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

                    // rebootPad(file)
                    findViewById(R.id.exec).setOnClickListener(view114 -> {
                        EditText flagBox = findViewById(R.id.rebootPad_flag);
                        EditText fileBox = findViewById(R.id.rebootPad_packagePath);
                        String flag = flagBox.getText().toString();
                        String filePath = fileBox.getText().toString();
                        if (flag.isEmpty()) {
                            flag = "0";
                            filePath = null;
                        } else if (flag.equals("2")) {
                            if (!filePath.startsWith("/")) {
                                Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (filePath.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        try {
                            mDchaService.rebootPad(Integer.parseInt(flag), filePath);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view115 -> backHome());
                });

                // removeTask
                findViewById(R.id.btn_removeTask).setOnClickListener(view -> {
                    setContentView(R.layout.layout_removetask);

                    // removeTask(pkgId)
                    findViewById(R.id.exec).setOnClickListener(view116 -> {
                        EditText pkgBox = findViewById(R.id.removeTask_package);
                        String pkgId = pkgBox.getText().toString();
                        if (pkgId.isEmpty()) {
                            pkgId = null;
                        }
                        try {
                            mDchaService.removeTask(pkgId);
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

                    // setDefaultPreferredHomeApp(pkgId)
                    findViewById(R.id.exec).setOnClickListener(view118 -> {
                        EditText pkgBox = findViewById(R.id.setDefaultPreferredHomeApp_package);
                        String pkgId = pkgBox.getText().toString();
                        if (pkgId.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            mDchaService.setDefaultPreferredHomeApp(pkgId);
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
                        EditText dateBox = findViewById(R.id.setSystemTime_value);
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

                    // uninstallApp(pkg)
                    findViewById(R.id.exec).setOnClickListener(view130 -> {
                        EditText pkgBox = findViewById(R.id.uninstallApp_pkgId);
                        EditText flagBox = findViewById(R.id.uninstallApp_flag);
                        String pkgId = pkgBox.getText().toString();
                        String flag = flagBox.getText().toString();
                        if (pkgId.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (flag.isEmpty()) {
                            flag = "1"; // 内部フラグが 2(既定) から 3 に変更
                        }
                        try {
                            String result = String.valueOf(mDchaService.uninstallApp(pkgId, Integer.parseInt(flag)));
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

                    // verifyUpdateImage(file)
                    findViewById(R.id.exec).setOnClickListener(view132 -> {
                        EditText fileBox = findViewById(R.id.verifyUpdateImage_file);
                        String filePath = fileBox.getText().toString();
                        if (!filePath.startsWith("/")) {
                            Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (filePath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            // true を返す
                            String result = String.valueOf(mDchaService.verifyUpdateImage(filePath));
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
