package dcha.test;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import static android.content.pm.PackageManager.*;

public class e extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // アクティビティを有効化
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, HideNavBar.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, ShowNavBar.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Reboot.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Reset.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        // このアクティビティを無効化
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, e.class), COMPONENT_ENABLED_STATE_DISABLED, 0);
    }
}
