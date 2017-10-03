package ammar.com.bookhotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.chootdev.csnackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.datatype.Duration;

import ammar.com.bookhotel.Object.Touch_objects;
import ammar.com.bookhotel.Object.cart_object;

public class Thank_You extends AppCompatActivity {
ImageView back_button;
    String easy_dif;
    String id_or_name;
    DatabaseReference reference;
    int time = 0;
    int mints,second,milisecond;
    TextView time_;
    int miliseconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank__you);

        time_ = (TextView) findViewById(R.id.time);
        reference = FirebaseDatabase.getInstance().getReference();
        if (Utils.iseasy){
            easy_dif = "Easy";
            id_or_name = Utils.easy_user_name;
        }
        else {
            easy_dif = "Difficult";
            id_or_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        Snackbar.with(this,null)
                .message("Booking successful...!")
                .type(com.chootdev.csnackbar.Type.SUCCESS)
                .duration(com.chootdev.csnackbar.Duration.LONG)
                .show();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        time = Register.time+MainActivity.time+Booking.time+Utils.cart_time+Payment.time+easy_or_difficult.time;
        for (miliseconds=time;miliseconds>=1000;miliseconds=miliseconds-1000){
            second++;
            if (second>=60){
                mints++;
                second = 0;
            }
        }


        String s = null;
        if (Utils.iseasy){
            s = "easy level";
        }
        else {
            s = "difficult level";
        }
        time_.setText("It took "+mints+":"+second+":"+miliseconds+" for you to book hotel with "+s);
        finish_();
        Utils.crt_obj = new ArrayList<>();
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.splash = new ArrayList<Touch_objects>();
                Utils.crt_obj = new ArrayList<cart_object>();
                Utils.firstone = true;
                Utils.Total_hotels_book = 0;
                Utils.time_on_easy_and_difficult = 0;
                Utils.easy_user_name = "";
                Utils.register_time = 0;
                Utils.login = 0;
                Utils.login_touches = new ArrayList<Touch_objects>();
                Utils.register_touches = new ArrayList<Touch_objects>();
                Utils.no_of_clicks_email = 0;
                Utils.no_of_clicks_register_button = 0;
                Utils.no_of_clicks_login_button = 0;
                Utils.no_of_clicks_password = 0;
                Utils.Total_hotels_book = 0;
                Utils.Total_hotels_book_deleted = 0;
                Utils.start_scrn = "";
                Utils.end_scrn = "";
                Intent intent = new Intent(Thank_You.this,easy_or_difficult.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        Calendar c= Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int mint = c.get(Calendar.MINUTE);
        int ap = c.get(Calendar.AM_PM);
        String sap = "";
        if (ap == 1){
            sap = "PM";
        }
        else {
            sap = "AM";
        }
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Screen").setValue("Thanks");
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Time").setValue(hour+":"+mint+":"+sap);
    }
    public void finish_() {
        super.onStop();
        Calendar c= Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int mint = c.get(Calendar.MINUTE);
        int ap = c.get(Calendar.AM_PM);
        String sap = "";
        if (ap == 1){
            sap = "PM";
        }
        else {
            sap = "AM";
        }
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Screen").setValue("Thanks");
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Time").setValue(hour+":"+mint+":"+sap);
    }
}
