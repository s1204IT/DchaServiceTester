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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import jp.co.benesse.dcha.dchautilservice.IDchaUtilService;

public class UtilTester extends Activity {
    IDchaUtilService mUtilService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util);

        // DchaUtilService をバインド
        if (bindService(new Intent("jp.co.benesse.dcha.dchautilservice.DchaUtilService").setPackage("jp.co.benesse.dcha.dchautilservice"), new ServiceConnection() {
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
                                mUtilService.clearDefaultPreferredApp(pkgId);
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

            // copyDirectory
            findViewById(R.id.btn_copyDirectory).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_copydirectory);

                    // copyDirectory(from, to)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText fromBox = findViewById(R.id.copyDirectory_from);
                            EditText toBox = findViewById(R.id.copyDirectory_to);
                            EditText topBox = findViewById(R.id.copyDirectory_makeTopDir);
                            String fromPath = fromBox.getText().toString();
                            String toPath = toBox.getText().toString();
                            String makeTop = topBox.getText().toString();
                            if (makeTop.equals("")) {
                                makeTop = "false";
                            } else if (!makeTop.equals("true") && !makeTop.equals("false")) {
                                Toast.makeText(getApplicationContext(), "真理値を正しく入力してください", Toast.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.copyDirectory(fromPath, toPath, Boolean.parseBoolean(makeTop)));
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
                            if (fromPath.endsWith("/") || toPath.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.copyFile(fromPath, toPath));
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
                            if (filePath.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.deleteFile(filePath));
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

            // existsFile
            findViewById(R.id.btn_existsFile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_existsfile);

                    // existsFile(file)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText fileBox = findViewById(R.id.existsFile_file);
                            String filePath = fileBox.getText().toString();
                            if (filePath.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.existsFile(filePath));
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

            // getCanonicalExternalPath
            findViewById(R.id.btn_getCanonicalExternalPath).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_getcanonicalexternalpath);

                    // getCanonicalExternalPath(path)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText fileBox = findViewById(R.id.getCanonicalExternalPath_path);
                            String filePath = fileBox.getText().toString();
                            if (filePath.equals("")) {
                                Toast.makeText(getApplicationContext(), "パスを指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.getCanonicalExternalPath(filePath));
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

            // getDisplaySize
            findViewById(R.id.btn_getDisplaySize).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int[] result = mUtilService.getDisplaySize();
                        Toast.makeText(getApplicationContext(), "実行結果：" + Arrays.toString(result), Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // getLcdSize
            findViewById(R.id.btn_getLcdSize).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int[] result = mUtilService.getLcdSize();
                        Toast.makeText(getApplicationContext(), "実行結果：" + Arrays.toString(result), Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });

            // getUserCount
            findViewById(R.id.btn_getUserCount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int result = mUtilService.getUserCount();
                        Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
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
                                mUtilService.hideNavigationBar(true);
                            } catch (RemoteException ignored) {
                            }
                        }
                    });
                    // hideNavigationBar(false)
                    findViewById(R.id.hideNavBar_false).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mUtilService.hideNavigationBar(false);
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

            // listFiles
            findViewById(R.id.btn_listFiles).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_listfiles);

                    // listFiles(path)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText fileBox = findViewById(R.id.listFiles_path);
                            String filePath = fileBox.getText().toString();
                            try {
                                String result = Arrays.toString(mUtilService.listFiles(filePath));
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

            // makeDir
            findViewById(R.id.btn_makeDir).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_makedir);

                    // makeDir(from, to)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText toBox = findViewById(R.id.makeDir_path);
                            EditText nameBox = findViewById(R.id.makeDir_name);
                            String toPath = toBox.getText().toString();
                            String name = nameBox.getText().toString();
                            if (name.startsWith("/") || name.endsWith("/")) {
                                Toast.makeText(getApplicationContext(), "正しい名前を指定してください", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                String result = String.valueOf(mUtilService.makeDir(toPath, name));
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

            // sdUnmount
            findViewById(R.id.btn_sdUnmount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mUtilService.sdUnmount();
                        Toast.makeText(getApplicationContext(), "実行しました：sdUnmount", Toast.LENGTH_SHORT).show();
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
                                mUtilService.setDefaultPreferredHomeApp(pkgId);
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

            // setForcedDisplaySize
            findViewById(R.id.btn_setForcedDisplaySize).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.layout_setforceddisplaysize);

                    // setForcedDisplaySize(width, height)
                    findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText widthBox = findViewById(R.id.setForcedDisplaySize_width);
                            EditText heightBox = findViewById(R.id.setForcedDisplaySize_height);
                            String width = widthBox.getText().toString();
                            String height = heightBox.getText().toString();
                            if (width.equals("")) {
                                if ((Build.MODEL).startsWith("TAB-A05-B")) {
                                    width = "1920";
                                } else {
                                    width = "1280";
                                }
                            }
                            if (height.equals("")) {
                                if ((Build.MODEL).startsWith("TAB-A05-B")) {
                                    height = "1200";
                                } else {
                                    height = "800";
                                }
                            }
                            try {
                                String result = String.valueOf(mUtilService.setForcedDisplaySize(Integer.parseInt(width), Integer.parseInt(height)));
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
            Toast.makeText(this, "DchaUtilService をバインド出来ませんでした", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent().setClassName(getPackageName(), getComponentName().getClassName()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
