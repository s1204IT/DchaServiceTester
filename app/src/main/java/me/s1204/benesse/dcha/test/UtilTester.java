package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import jp.co.benesse.dcha.dchautilservice.IDchaUtilService;

public class UtilTester extends Activity {
    IDchaUtilService mUtilService;
    private static final String UTIL_PACKAGE = "jp.co.benesse.dcha.dchautilservice";
    private static final String UTIL_SERVICE = UTIL_PACKAGE + ".DchaUtilService";

    private void backHome() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DchaUtilService をバインド
        try {
            if (bindService(new Intent(UTIL_SERVICE).setPackage(UTIL_PACKAGE), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mUtilService = IDchaUtilService.Stub.asInterface(iBinder);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    unbindService(this);
                    Toast.makeText(getApplicationContext(), "DchaUtilService から切断されました", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
            }, Context.BIND_AUTO_CREATE)) {

                setContentView(R.layout.util);

                // clearDefaultPreferredApp
                findViewById(R.id.btn_clearDefaultPreferredApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_cleardefaultpreferredapp);

                    // clearDefaultPreferredApp(packageName)
                    findViewById(R.id.exec).setOnClickListener(view1 -> {
                        EditText packageNameBox = findViewById(R.id.clearDefaultPreferredApp_packageName);
                        String packageName = packageNameBox.getText().toString();
                        if (packageName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            mUtilService.clearDefaultPreferredApp(packageName);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view12 -> backHome());
                });

                // copyDirectory
                findViewById(R.id.btn_copyDirectory).setOnClickListener(view -> {
                    setContentView(R.layout.layout_copydirectory);

                    // copyDirectory(srcDirPath, dstDirPath, makeTopDir)
                    findViewById(R.id.exec).setOnClickListener(view13 -> {
                        EditText srcDirPathBox = findViewById(R.id.copyDirectory_srcDirPath);
                        EditText dstDirPathBox = findViewById(R.id.copyDirectory_dstDirPath);
                        EditText makeTopDirBox = findViewById(R.id.copyDirectory_makeTopDir);
                        String srcDirPath = srcDirPathBox.getText().toString();
                        String dstDirPath = dstDirPathBox.getText().toString();
                        String makeTopDir = makeTopDirBox.getText().toString();
                        if (makeTopDir.isEmpty()) {
                            makeTopDir = "false";
                            /*
                             * true ：フォルダ自体をコピー
                             *   srcDir の最終ディレクトリ名で新しいフォルダを作成し、中身をコピー
                             * false：フォルダの中身のみコピー
                             *   srcDir の中身をコピー
                             */
                        } else if (!makeTopDir.equals("true") && !makeTopDir.equals("false")) {
                            Toast.makeText(getApplicationContext(), "真理値を正しく入力してください", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mUtilService.copyDirectory(srcDirPath, dstDirPath, Boolean.parseBoolean(makeTopDir)));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view14 -> backHome());
                });

                // copyFile
                findViewById(R.id.btn_copyFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_copyfile);

                    // copyFile(srcFilePath, dstFilePath)
                    findViewById(R.id.exec).setOnClickListener(view15 -> {
                        EditText srcFilePathBox = findViewById(R.id.copyFile_srcFilePath);
                        EditText dstFilePathBox = findViewById(R.id.copyFile_dstFilePath);
                        String srcFilePath = srcFilePathBox.getText().toString();
                        String dstFilePath = dstFilePathBox.getText().toString();
                        if (srcFilePath.endsWith("/") || dstFilePath.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mUtilService.copyFile(srcFilePath, dstFilePath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view16 -> {
                        finish();
                        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    });
                });

                // deleteFile
                findViewById(R.id.btn_deleteFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_deletefile);

                    // deleteFile(path)
                    findViewById(R.id.exec).setOnClickListener(view17 -> {
                        EditText pathBox = findViewById(R.id.deleteFile_path);
                        String path = pathBox.getText().toString();
                        try {
                            String result = String.valueOf(mUtilService.deleteFile(path));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view18 -> backHome());
                });

                // existsFile
                findViewById(R.id.btn_existsFile).setOnClickListener(view -> {
                    setContentView(R.layout.layout_existsfile);

                    // existsFile(path)
                    findViewById(R.id.exec).setOnClickListener(view19 -> {
                        EditText pathBox = findViewById(R.id.existsFile_path);
                        String path = pathBox.getText().toString();
                        try {
                            String result = String.valueOf(mUtilService.existsFile(path));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view110 -> backHome());
                });

                // getCanonicalExternalPath
                findViewById(R.id.btn_getCanonicalExternalPath).setOnClickListener(view -> {
                    setContentView(R.layout.layout_getcanonicalexternalpath);

                    // getCanonicalExternalPath(linkPath)
                    findViewById(R.id.exec).setOnClickListener(view111 -> {
                        EditText linkPathBox = findViewById(R.id.getCanonicalExternalPath_linkPath);
                        String linkPath = linkPathBox.getText().toString();
                        try {
                            String result = String.valueOf(mUtilService.getCanonicalExternalPath(linkPath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view112 -> backHome());
                });

                // getDisplaySize
                findViewById(R.id.btn_getDisplaySize).setOnClickListener(view -> {
                    try {
                        int[] result = mUtilService.getDisplaySize();
                        Toast.makeText(getApplicationContext(), "getDisplaySize：" + Arrays.toString(result), Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // getLcdSize
                findViewById(R.id.btn_getLcdSize).setOnClickListener(view -> {
                    try {
                    /*
                      jp.co.benesse.touch.allgrade.b003.touchhomelauncher が存在する場合は
                      [1280, 800] を返す
                     */
                        int[] result = mUtilService.getLcdSize();
                        Toast.makeText(getApplicationContext(), "getLcdSize：" + Arrays.toString(result), Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // getUserCount
                findViewById(R.id.btn_getUserCount).setOnClickListener(view -> {
                    try {
                        int result = mUtilService.getUserCount();
                        Toast.makeText(getApplicationContext(), "getUserCount：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // hideNavigationBar
                findViewById(R.id.btn_hideNavigationBar).setOnClickListener(view -> {
                    setContentView(R.layout.layout_hidenavigationbar);
                    // hideNavigationBar(true)
                    findViewById(R.id.hideNavBar_true).setOnClickListener(view113 -> {
                        try {
                            mUtilService.hideNavigationBar(true);
                        } catch (RemoteException ignored) {
                        }
                    });
                    // hideNavigationBar(false)
                    findViewById(R.id.hideNavBar_false).setOnClickListener(view114 -> {
                        try {
                            mUtilService.hideNavigationBar(false);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view115 -> backHome());
                });

                // listFiles
                findViewById(R.id.btn_listFiles).setOnClickListener(view -> {
                    setContentView(R.layout.layout_listfiles);

                    // listFiles(path)
                    findViewById(R.id.exec).setOnClickListener(view116 -> {
                        EditText fileBox = findViewById(R.id.listFiles_path);
                        String filePath = fileBox.getText().toString();
                        try {
                            String result = Arrays.toString(mUtilService.listFiles(filePath));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view117 -> backHome());
                });

                // makeDir
                findViewById(R.id.btn_makeDir).setOnClickListener(view -> {
                    setContentView(R.layout.layout_makedir);

                    // makeDir(path, dirname)
                    findViewById(R.id.exec).setOnClickListener(view118 -> {
                        EditText pathBox = findViewById(R.id.makeDir_path);
                        EditText dirnameBox = findViewById(R.id.makeDir_dirname);
                        String path = pathBox.getText().toString();
                        String dirname = dirnameBox.getText().toString();
                        if (dirname.startsWith("/") || dirname.endsWith("/")) {
                            Toast.makeText(getApplicationContext(), "正しい名前を指定してください", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String result = String.valueOf(mUtilService.makeDir(path, dirname));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view119 -> backHome());
                });

                // sdUnmount
                findViewById(R.id.btn_sdUnmount).setOnClickListener(view -> {
                    try {
                        mUtilService.sdUnmount();
                        Toast.makeText(getApplicationContext(), "実行しました：sdUnmount", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException ignored) {
                    }
                });

                // setDefaultPreferredHomeApp
                findViewById(R.id.btn_setDefaultPreferredHomeApp).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setdefaultpreferredhomeapp);

                    // setDefaultPreferredHomeApp(packageName)
                    findViewById(R.id.exec).setOnClickListener(view120 -> {
                        EditText packageNameBox = findViewById(R.id.setDefaultPreferredHomeApp_packageName);
                        String packageName = packageNameBox.getText().toString();
                        if (packageName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "パッケージIDを指定してください", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            mUtilService.setDefaultPreferredHomeApp(packageName);
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view121 -> backHome());
                });

                // setForcedDisplaySize
                findViewById(R.id.btn_setForcedDisplaySize).setOnClickListener(view -> {
                    setContentView(R.layout.layout_setforceddisplaysize);

                    // setForcedDisplaySize(width, height)
                    findViewById(R.id.exec).setOnClickListener(view122 -> {
                        EditText widthBox = findViewById(R.id.setForcedDisplaySize_width);
                        EditText heightBox = findViewById(R.id.setForcedDisplaySize_height);
                        String width = widthBox.getText().toString();
                        String height = heightBox.getText().toString();
                        if (width.isEmpty()) {
                            if ((Build.MODEL).startsWith("TAB-A05-B")) {
                                width = "1920";
                            } else {
                                // jp.co.benesse.dcha.util.WindowManagerAdapter.RESOLUTION_WXGA_WIDTH
                                width = "1280";
                            }
                        }
                        if (height.isEmpty()) {
                            if ((Build.MODEL).startsWith("TAB-A05-B")) {
                                height = "1200";
                            } else {
                                // jp.co.benesse.dcha.util.WindowManagerAdapter.RESOLUTION_WXGA_HEIGHT
                                height = "800";
                            }
                        }
                        // [1024, 768]: XGA
                        try {
                            String result = String.valueOf(mUtilService.setForcedDisplaySize(Integer.parseInt(width), Integer.parseInt(height)));
                            Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                        } catch (RemoteException ignored) {
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(view123 -> backHome());
                });

            } else {
                Toast.makeText(this, "DchaUtilService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }

        } catch (SecurityException ignored) {
            Toast.makeText(this, "｢許可｣を押して権限を昇格して下さい", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW).setClassName(getPackageName(), getPackageName() + ".RequestPermission").addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            finish();
        }

    }

    @Deprecated
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
