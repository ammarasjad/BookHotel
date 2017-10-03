package ammar.com.bookhotel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.security.AccessControlContext;
import java.util.Calendar;

import ammar.com.bookhotel.Object.AppLocationService;
import ammar.com.bookhotel.Object.Touch_objects;

import static java.security.AccessController.getContext;

public class splash extends AppCompatActivity {

    double curlat,curlog;

    int battery_level;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            battery_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Utils.start_scrn = "Unitylab Scrn";
        Calendar c = Calendar.getInstance();
        Utils.hour = c.get(Calendar.HOUR);
        Utils.mint = c.get(Calendar.MINUTE);
        int ap = c.get(Calendar.AM_PM);
        if (ap == 1){
            Utils.sap = "PM";
        }
        else {
            Utils.sap = "AM";
        }
        RelativeLayout view = (RelativeLayout) findViewById(R.id.activity_splash);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                Touch_objects objects = new Touch_objects(X,Y);
                Utils.splash.add(objects);
                return false;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this,easy_or_difficult.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
