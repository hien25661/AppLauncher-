package app.hiennv.applauncher.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.hiennv.applauncher.R;
import app.hiennv.applauncher.receiver.ScreenMonitorService;
import app.hiennv.applauncher.receiver.UpdateBatteryReceive;


public class BatteryWidget extends RelativeLayout implements UpdateBatteryReceive.UpdateBatteryListener {
    public static final String ACTION_BATTERY_UPDATE = "app.hiennv.battery.action.UPDATE";
    private int batteryLevel = 0;
    private Context mContext;
    UpdateBatteryReceive updateBatteryReceive;
    private TextView mTextViewBatteryLevel;

    public BatteryWidget(Context context) {
        super(context);
        initView(context);
    }

    public BatteryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BatteryWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        View v = inflate(getContext(), R.layout.widget_layout, this);
        mTextViewBatteryLevel = (TextView)v.findViewById(R.id.batteryText);
        updateBatteryReceive = new UpdateBatteryReceive(this);

        turnAlarmOnOff(context, true);
        context.startService(new Intent(context, ScreenMonitorService.class));

        Intent t = new Intent(ACTION_BATTERY_UPDATE);
        context.sendBroadcast(t);

        IntentFilter intentFilter = new IntentFilter(ACTION_BATTERY_UPDATE);
        mContext.registerReceiver(updateBatteryReceive,intentFilter);

        batteryLevel = calculateBatteryLevel(context);
        updateViews(context,batteryLevel);
    }


    public static void turnAlarmOnOff(Context context, boolean turnOn) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_BATTERY_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (turnOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, 300 * 1000, pendingIntent);

        } else {
            alarmManager.cancel(pendingIntent);

        }
    }

    private int calculateBatteryLevel(Context context) {

        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return level * 100 / scale;
    }

    private void updateViews(Context context, int level) {

        mTextViewBatteryLevel.setText(""+level+"%");
    }


    @Override
    public void batteryChanged(int level) {
        updateViews(mContext,level);
    }
}
