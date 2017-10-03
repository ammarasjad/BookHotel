package ammar.com.bookhotel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import ammar.com.bookhotel.Object.Touch_objects;

public class easy_or_difficult extends AppCompatActivity {
    RelativeLayout easy, difficult;
    FirebaseAnalytics mFirebaseAnalytics;
    Bundle params = new Bundle();
    String button_name;
    static int time = 0;
    Boolean bol=true;
    DatabaseReference database;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_or_difficult);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        dialog.dismiss();
                        if (location != null) {

                        }
                    }
                });
        database = FirebaseDatabase.getInstance().getReference();

        if (!Utils.start_scrn.equals("Unitylab Scrn")){
            Utils.start_scrn = "Main Screen";
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
        }
        Utils.end_scrn = "Main Screen";
        RelativeLayout activity_easy_or_difficult = (RelativeLayout) findViewById(R.id.activity_easy_or_difficult);
        activity_easy_or_difficult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                Touch_objects objects = new Touch_objects(X,Y);
                Utils.easy_difficult.add(objects);
                return false;
            }
        });
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        easy = (RelativeLayout) findViewById(R.id.easy);
        difficult = (RelativeLayout) findViewById(R.id.difficult);
        time_in_miliseconds();





        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetwornkAvailable()) {
                    Utils.iseasy = true;
                    bol = false;
                    Utils.time_on_easy_and_difficult = time;

                    params.putInt("ButtonID", easy.getId());
                    button_name = "Easy_Clicked";
                    mFirebaseAnalytics.logEvent(button_name, params);
                    displayDialogue();
                }
                else {
                    Toast.makeText(easy_or_difficult.this,"Internet Connection is required",Toast.LENGTH_SHORT).show();
                }

            }
        });
        difficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetwornkAvailable()) {
                Utils.iseasy = false;

                bol = false;
                Utils.time_on_easy_and_difficult = time;

                params.putInt("ButtonID",difficult.getId());
                button_name = "Difficult_Clicked";
                mFirebaseAnalytics.logEvent(button_name, params);

                Intent intent = new Intent(easy_or_difficult.this,MainActivity.class);
                startActivity(intent);
                finish();
                }
                else {
                    Toast.makeText(easy_or_difficult.this,"Internet Connection is required",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(easy_or_difficult.this);
        View dialog = getLayoutInflater().inflate(R.layout.dialogue_box,null);
        final EditText editText = (EditText) dialog.findViewById(R.id.dname);
        ImageView okbutton = (ImageView) dialog.findViewById(R.id.ok);
        // find IDS
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(easy_or_difficult.this,"PLease Enter Name", Toast.LENGTH_SHORT).show();
                }

               /* else if (database.child("Easy").child(editText.getText().toString()).getKey().toString().isEmpty()){
                    Toast.makeText(easy_or_difficult.this,"User is already registered please choose another",Toast.LENGTH_SHORT).show();
                }*/

                else {

                        bol = false;

                        Utils.easy_user_name = editText.getText().toString();
                        database.child("Easy").child(editText.getText().toString()).child("Time").child("Main Screen").setValue(time);
                    alertDialog.dismiss();
                        Intent intent = new Intent(easy_or_difficult.this, Booking.class);
                        startActivity(intent);
                        finish();
                }
            }
        });
    }
    void time_in_miliseconds() {
        if (bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    time++;
                    time_in_miliseconds();
                }
            }, 1);
        }
    }
    private boolean isNetwornkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
