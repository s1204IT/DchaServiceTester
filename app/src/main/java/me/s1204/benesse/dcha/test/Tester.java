package me.s1204.benesse.dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class Tester extends Activity {
    IDchaService mDchaService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        // DchaService をバインド
        if (bindService(new Intent("jp.co.benesse.dcha.dchaservice.DchaService").setPackage("jp.co.benesse.dcha.dchaservice"), new ServiceConnection() {
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
            findViewById(R.id.btn_cancelSetup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mDchaService.cancelSetup();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // checkPadRooted
            findViewById(R.id.btn_checkPadRooted).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // false を返す
                        String result = String.valueOf(mDchaService.checkPadRooted());
                        Toast.makeText(getApplicationContext(), "checkPadRooted：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // clearDefaultPreferredApp
            findViewById(R.id.btn_clearDefaultPreferredApp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_cleardefaultpreferredapp);

                    // clearDefaultPreferredApp(pkgId)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText pkgBox = findViewById(R.id.clearDefaultPreferredApp_package);
                            String pkgId = pkgBox.getText().toString();
                            if (pkgId.equals("")) {
                                Toast.makeText(getApplicationContext(), "パッケージIDを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                mDchaService.clearDefaultPreferredApp(pkgId);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // copyFile
            findViewById(R.id.btn_copyFile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_copyfile);

                    // copyFile(from, to)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // copyUpdateImage
            findViewById(R.id.btn_copyUpdateImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_copyupdateimage);

                    // copyUpdateImage(from, to)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // deleteFile
            findViewById(R.id.btn_deleteFile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_deletefile);

                    // deleteFile(file)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // disableADB
            findViewById(R.id.btn_disableADB).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mDchaService.disableADB();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // getForegroundPackageName
            findViewById(R.id.btn_getForegroundPackageName).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String result = mDchaService.getForegroundPackageName();
                        Toast.makeText(getApplicationContext(), "getForegroundPackageName：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // getSetupStatus
            findViewById(R.id.btn_getSetupStatus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int result = mDchaService.getSetupStatus();
                        Toast.makeText(getApplicationContext(), "getSetupStatus：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // getUserCount
            findViewById(R.id.btn_getUserCount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int result = mDchaService.getUserCount();
                        Toast.makeText(getApplicationContext(), "getUserCount：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // hideNavigationBar
            findViewById(R.id.btn_hideNavigationBar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_hidenavigationbar);
                    // hideNavigationBar(true)
                    findViewById(R.id.hideNavBar_true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.hideNavigationBar(true);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });
                    // hideNavigationBar(false)
                    findViewById(R.id.hideNavBar_false).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.hideNavigationBar(false);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // installApp
            findViewById(R.id.btn_installApp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_installapp);

                    // installApp(file)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText fileBox = findViewById(R.id.installApp_packagePath);
                            EditText flagBox = findViewById(R.id.installApp_flag);
                            String filePath = fileBox.getText().toString();
                            String flag = flagBox.getText().toString();
                            if (!filePath.startsWith("/")) {
                                Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!filePath.endsWith(".apk")) {
                                Toast.makeText(getApplicationContext(), "APKファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (flag.equals("")) {
                                flag = "2";
                            }
                            try {
                                String result = String.valueOf(mDchaService.installApp(filePath, Integer.parseInt(flag)));
                                Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // isDeviceEncryptionEnabled
            findViewById(R.id.btn_isDeviceEncryptionEnabled).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // false を返す
                        String result = String.valueOf(mDchaService.isDeviceEncryptionEnabled());
                        Toast.makeText(getApplicationContext(), "isDeviceEncryptionEnabled：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // rebootPad
            findViewById(R.id.btn_rebootPad).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_rebootpad);

                    // rebootPad(file)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText flagBox = findViewById(R.id.rebootPad_flag);
                            EditText fileBox = findViewById(R.id.rebootPad_packagePath);
                            String flag = flagBox.getText().toString();
                            String filePath = fileBox.getText().toString();
                            if (flag.equals("")) {
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
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // removeTask
            findViewById(R.id.btn_removeTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_removetask);

                    // removeTask(pkgId)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText pkgBox = findViewById(R.id.removeTask_package);
                            String pkgId = pkgBox.getText().toString();
                            if (pkgId.equals("")) {
                                pkgId = null;
                            }
                            try {
                                mDchaService.removeTask(pkgId);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // sdUnmount
            findViewById(R.id.btn_sdUnmount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mDchaService.sdUnmount();
                        Toast.makeText(getApplicationContext(), "実行しました：sdUnmount", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // setDefaultParam
            findViewById(R.id.btn_setDefaultParam).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mDchaService.setDefaultParam();
                        Toast.makeText(getApplicationContext(), "実行しました：setDefaultParam", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // setDefaultPreferredHomeApp
            findViewById(R.id.btn_setDefaultPreferredHomeApp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_setdefaultpreferredhomeapp);

                    // setDefaultPreferredHomeApp(pkgId)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText pkgBox = findViewById(R.id.setDefaultPreferredHomeApp_package);
                            String pkgId = pkgBox.getText().toString();
                            if (pkgId.equals("")) {
                                Toast.makeText(getApplicationContext(), "パッケージIDを指定してください", Toast.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                mDchaService.setDefaultPreferredHomeApp(pkgId);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // setPermissionEnforced
            findViewById(R.id.btn_setPermissionEnforced).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_setpermissionenforced);
                    // setPermissionEnforced(true)
                    findViewById(R.id.setPermissionEnforced_true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setPermissionEnforced(true);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });
                    // setPermissionEnforced(false)
                    findViewById(R.id.setPermissionEnforced_false).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setPermissionEnforced(false);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // setSetupStatus
            findViewById(R.id.btn_setSetupStatus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_setsetupstatus);

                    // setSetupStatus(0)
                    findViewById(R.id.setSetupStatus_0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setSetupStatus(0);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // setSetupStatus(1)
                    findViewById(R.id.setSetupStatus_1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setSetupStatus(1);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // setSetupStatus(2)
                    findViewById(R.id.setSetupStatus_2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setSetupStatus(2);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // setSetupStatus(3)
                    findViewById(R.id.setSetupStatus_3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mDchaService.setSetupStatus(3);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // setSystemTime
            findViewById(R.id.btn_setSystemTime).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_setsystemtime);

                    // setSystemTime(date, format)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText dateBox = findViewById(R.id.setSystemTime_value);
                            String date = dateBox.getText().toString();
                            EditText formatBox = findViewById(R.id.setSystemTime_format);
                            String format = formatBox.getText().toString();
                            if (date.equals("") || format.equals("")) {
                                Toast.makeText(getApplicationContext(), "値を入力してください", Toast.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                mDchaService.setSystemTime(date, format);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // uninstallApp
            findViewById(R.id.btn_uninstallApp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_uninstallapp);

                    // uninstallApp(pkg)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText pkgBox = findViewById(R.id.uninstallApp_pkgId);
                            EditText flagBox = findViewById(R.id.uninstallApp_flag);
                            String pkgId = pkgBox.getText().toString();
                            String flag = flagBox.getText().toString();
                            if (pkgId.equals("")) {
                                Toast.makeText(getApplicationContext(), "パッケージIDを入力してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (flag.equals("")) {
                                flag = "1"; // 内部フラグが 2(既定) から 3 に変更
                            }
                            try {
                                String result = String.valueOf(mDchaService.uninstallApp(pkgId, Integer.parseInt(flag)));
                                Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                            } catch (RemoteException ignored) {
                            }
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

            // verifyUpdateImage
            findViewById(R.id.btn_verifyUpdateImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_verifyupdateimage);

                    // verifyUpdateImage(file)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                        }
                    });

                    // メニューに戻る
                    findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    });
                }
            });

        } else {
            Toast.makeText(this, "DchaService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
