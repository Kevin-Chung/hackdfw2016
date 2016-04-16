package hackdfw2016.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by HeyImRige on 4/16/2016.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            Toast.makeText(context, "SMART ALARM BACKGROUND RUNNING1",Toast.LENGTH_SHORT).show();
        }
    }
}
