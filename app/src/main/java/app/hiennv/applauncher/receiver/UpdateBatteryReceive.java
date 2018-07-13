package app.hiennv.applauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import app.hiennv.applauncher.widgets.BatteryWidget;

public class UpdateBatteryReceive extends BroadcastReceiver {
    private UpdateBatteryListener mListener;

    public UpdateBatteryReceive(UpdateBatteryListener mListener) {
        this.mListener = mListener;
    }

    public UpdateBatteryReceive() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BatteryWidget.ACTION_BATTERY_UPDATE)) {
            int currentLevel = calculateBatteryLevel(context);
            if(mListener!=null){
                mListener.batteryChanged(currentLevel);
            }
        }
    }

    private int calculateBatteryLevel(Context context) {

        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return level * 100 / scale;
    }

    public interface UpdateBatteryListener {
        void batteryChanged(int level);
    }
}
