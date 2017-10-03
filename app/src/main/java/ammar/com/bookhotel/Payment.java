package ammar.com.bookhotel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ammar.com.bookhotel.Object.AppLocationService;
import ammar.com.bookhotel.Object.Touch_objects;

import static ammar.com.bookhotel.Utils.latitude;
import static ammar.com.bookhotel.Utils.longitude;
import static android.R.attr.start;

public class Payment extends AppCompatActivity {
    EditText card_number,cvc,name;
    TextView expiry;
    String card_Number,Expiry,CVC,Name;
    ImageView pay;
    FirebaseAnalytics mFirebaseAnalytics;
    Bundle params = new Bundle();
    Boolean bol = true;
    static int time = 0;

    Boolean name_bol = false;
    int name_time = 0;

    Boolean cardno_bol = false;
    int cardno_time = 0;

    Boolean expiry_bol = false;
    int expiry_time = 0;

    Boolean cvc_bol = false;
    int cvc_time = 0;

    DatabaseReference reference;

    String easy_dif;
    String id_or_name;


    AppLocationService appLocationService;

    int no_of_clicks_name = 0;
    int no_of_clicks_card_no = 0;
    int no_of_clicks_expiry = 0;
    int no_of_clicks_cvc = 0;
    int no_of_clicks_pay_button = 0;

    int name_counter_handler , card_counter_handler , cvc_counter_handler, expiry_counter_handler;

    ProgressDialog dialog;
    int total_pay = 0 ;
    int total_descount = 0;

    TextView descount;
    TextView to_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        descount = (TextView) findViewById(R.id.descount);
        to_pay = (TextView) findViewById(R.id.to_pay);
        name_counter_handler = cvc_counter_handler = card_counter_handler = expiry_counter_handler = 0;
        reference = FirebaseDatabase.getInstance().getReference();

        dialog= new ProgressDialog(Payment.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        if (Utils.iseasy){
            easy_dif = "Easy";
            id_or_name = Utils.easy_user_name;
        }
        else {
            easy_dif = "Difficult";
            id_or_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        for (int i=0;i<Utils.crt_obj.size();i++){
            total_pay = total_pay+Utils.crt_obj.get(i).getPayment_value();
            total_descount = total_descount + Utils.crt_obj.get(i).getDescount_percent();
        }
        descount.setText("-€"+total_descount);
        to_pay.setText("€"+total_pay);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_payment);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                card_counter_handler = cvc_counter_handler = name_counter_handler = expiry_counter_handler =0;
                uplaod_click_values("Payment",X,Y);
                return false;
            }
        });
/*
        appLocationService=new AppLocationService(Payment.this);
        Location newlocation=appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if(newlocation !=null)
        {
            Utils.latitude=newlocation.getLatitude();

            Utils.longitude=newlocation.getLongitude();
        }



        reference.child(easy_dif).child(id_or_name).child("Location").child("Latitude").setValue(latitude);
        reference.child(easy_dif).child(id_or_name).child("Location").child("Longitude").setValue(longitude);
        */
        time_in_miliseconds();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        card_number = (EditText) findViewById(R.id.card_number);
        expiry = (TextView) findViewById(R.id.expiry);
        cvc = (EditText) findViewById(R.id.cvc);
        name = (EditText) findViewById(R.id.name);

        card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                card_counter_handler++;
                name_counter_handler = expiry_counter_handler = cvc_counter_handler = 0;
                cardno_bol = true;
                expiry_bol = false;
                name_bol = false;
                cvc_bol = false;
                if (card_counter_handler == 1) {
                    no_of_clicks_card_no++;
                    cardno_in_miliseconds();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //expiry_counter_handler++;

                no_of_clicks_expiry++;
                Log.v("Expiry",no_of_clicks_expiry+"");
                name_counter_handler = cvc_counter_handler = card_counter_handler = 0;
                expiry_bol = true;
                cardno_bol = false;
                name_bol = false;
                cvc_bol = false;
                showDialog(0);
                expiry_in_miliseconds();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name_counter_handler++;
                cvc_counter_handler = expiry_counter_handler = card_counter_handler = 0;
                name_bol = true;
                cardno_bol = false;
                expiry_bol = false;
                cvc_bol = false;
                if (name_counter_handler == 1) {
                    no_of_clicks_name++;
                    name_in_miliseconds();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cvc_counter_handler ++;
                name_counter_handler = card_counter_handler = expiry_counter_handler = 0;
                cvc_bol = true;
                name_bol = false;
                expiry_bol = false;
                cardno_bol = false;
                if (cvc_counter_handler == 1) {
                    no_of_clicks_cvc++;
                    cvc_in_miliseconds();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pay = (ImageView) findViewById(R.id.pay_button);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_clicks_pay_button++;
                analytic_function("Login_",pay);
                card_Number = card_number.getText().toString();
                Expiry = expiry.getText().toString();
                CVC = cvc.getText().toString();
                Name = name.getText().toString();
                name_bol = false;
                expiry_bol = false;
                cvc_bol = false;
                cardno_bol = false;
                check_strings();
            }
        });


    }
    void check_strings(){
        if (Name.isEmpty()){
            Toast.makeText(Payment.this,"Enter Name" , Toast.LENGTH_SHORT).show();
        }
        else if (card_Number.isEmpty()){
            Toast.makeText(Payment.this,"Enter card Number" , Toast.LENGTH_SHORT).show();
        }
        else if (Expiry.isEmpty()){
            Toast.makeText(Payment.this,"Set card expiry" , Toast.LENGTH_SHORT).show();
        }
        else if (CVC.isEmpty()){
            Toast.makeText(Payment.this,"Enter CVC", Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(Payment.this,"Name Click"+no_of_clicks_name+"   "+name_time+"\n"+"Card Click"+no_of_clicks_card_no+"   "+cardno_time+"\n"+"cvc Click"+no_of_clicks_cvc+"   "+cvc_time+"\n"+"expiry Click"+no_of_clicks_expiry+"   "+expiry_time+"\n",Toast.LENGTH_LONG).show();
            bol=false;
            if (!Utils.iseasy){
                //difficult
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Payment").child("Total").setValue(time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Payment").child("Name").setValue(name_time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Payment").child("CVC").setValue(cvc_time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Payment").child("Expiry").setValue(expiry_time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Payment").child("Cardno").setValue(cardno_time);

                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clicks count").child("Payment").child("Pay button").setValue(no_of_clicks_pay_button);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clicks count").child("Payment").child("Name").setValue(no_of_clicks_name);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clicks count").child("Payment").child("CVC").setValue(no_of_clicks_cvc);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clicks count").child("Payment").child("Expiry").setValue(no_of_clicks_expiry);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clicks count").child("Payment").child("Cardno").setValue(no_of_clicks_card_no);
            }
            else{
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Payment").child("Total").setValue(time);
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Payment").child("Cardno").setValue(cardno_time);
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Payment").child("Name").setValue(name_time);
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Payment").child("Expiry").setValue(expiry_time);
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Payment").child("CVC").setValue(cvc_time);


                reference.child("Easy").child(Utils.easy_user_name).child("Clicks count").child("Payment").child("Pay Button").setValue(no_of_clicks_pay_button);
                reference.child("Easy").child(Utils.easy_user_name).child("Clicks count").child("Payment").child("Card no").setValue(no_of_clicks_card_no);
                reference.child("Easy").child(Utils.easy_user_name).child("Clicks count").child("Payment").child("Name").setValue(no_of_clicks_name);
                reference.child("Easy").child(Utils.easy_user_name).child("Clicks count").child("Payment").child("Expiry").setValue(no_of_clicks_expiry);
                reference.child("Easy").child(Utils.easy_user_name).child("Clicks count").child("Payment").child("CVC").setValue(no_of_clicks_cvc);
            }
            move();
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay
                );
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            expiry_bol= false;
            int startYear = selectedYear;
            int startMonth = selectedMonth+1;
            int startDay = selectedDay;
            expiry_bol = false;
            expiry.setTextColor(Color.parseColor("#000000"));
                expiry.setText("" + startDay +"/" + startMonth + "/" + startYear);
        }
    };
    void move(){/*
        Toast toast = Toast.makeText(Payment.this,"Your booking is confirmed,\n Thanks for testing our hotel booking app",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        Intent intent = new Intent(Payment.this,Thank_You.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(Payment.this,Booking.class);
        startActivity(intent);
        finish();
    }
    void analytic_function(String name,View view){
        params.putInt("ButtonID",view.getId());
        mFirebaseAnalytics.logEvent(name, params);
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
    void cardno_in_miliseconds() {
        if (cardno_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardno_time++;
                    cardno_in_miliseconds();
                }
            }, 1);
        }
    }
    void name_in_miliseconds() {
        if (name_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    name_time++;
                    name_in_miliseconds();
                }
            }, 1);
        }
    }
    void expiry_in_miliseconds() {
        if (expiry_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expiry_time++;
                    expiry_in_miliseconds();
                }
            }, 1);
        }
    }
    void cvc_in_miliseconds() {
        if (cvc_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cvc_time++;
                    cvc_in_miliseconds();
                }
            }, 1);
        }
    }
    void uplaod_click_values(String screen, int X, int Y){
        reference.child(easy_dif).child(id_or_name).child("Clicks On Diff Scr").child(screen).push().setValue(new Touch_objects(X,Y));
    }
    @Override
    public void onStop() {
        super.onStop();
        Calendar c= Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int mint = c.get(Calendar.MINUTE);
        int ap = c.get(Calendar.AM_PM);
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Screen").setValue("Payment");
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Time").setValue(hour+":"+mint+ap);
    }
}
