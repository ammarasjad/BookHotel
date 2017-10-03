package ammar.com.bookhotel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ammar.com.bookhotel.Adapter.FindPricveAdapter;
import ammar.com.bookhotel.Adapter.rooms_adapter;
import ammar.com.bookhotel.Object.ProfilePriceObject;
import ammar.com.bookhotel.Object.Touch_objects;
import ammar.com.bookhotel.Object.cart_object;
import ammar.com.bookhotel.Object.rooms_object;

import static ammar.com.bookhotel.Utils.easy_user_name;
import static ammar.com.bookhotel.Utils.hotel_list;
import static ammar.com.bookhotel.Utils.latitude;
import static ammar.com.bookhotel.Utils.login;
import static ammar.com.bookhotel.Utils.longitude;

public class Booking extends AppCompatActivity {

    FindPricveAdapter priceadapter;
    rooms_adapter room_adap;
    Boolean bol = true;
    static int time=0;
    Boolean start = false;
    TextView check_in_date,check_out_date;
    RelativeLayout hotels,rooms,rooms_quantity,adults,child,date_rel_1,date_rel_2;
    Boolean isclicked_room = false,isclicked_hotel = false,isclicked_rooms_quantity = false,isclicked_adults = false,isclicked_child = false;
    ListView hotel_list,room_list,room_quantity_list,adult_list,child_list;
    String Selected_hotel="",Selected_room="",Selected_room_quantity="",Selected_adult="",Selected_child="";
    RelativeLayout Image_hotels,Image_rooms,Image_rooms_quantity,Image_adults,Image_child;
    TextView hotel_type,room_type,no_of_rooms,no_of_adults,no_of_child;
    ImageView continue_button;
    ImageView hotel_scenery;
    int startYear,startMonth,startDay,endYear,endMonth,endDay;
    static final int DATE_START_DIALOG_ID = 0;
    static final int DATE_END_DIALOG_ID = 1;
    FirebaseAnalytics mFirebaseAnalytics;
    DatabaseReference reference;
    Bundle params = new Bundle();
    Boolean is_hotel_booking_open = false;
    int hotel_booking_time;
    Boolean is_room_booking_open = false;
    int room_booking_time;
    Boolean is_room_booking_quantity_open = false;
    int room_booking_quantity_time;

    Boolean is_adult = false;
    int adult_booking_time;

    Boolean is_kid = false;
    int kid_booking_time;

    Boolean is_check_in_date = false;
    int check_in_time;

    Boolean is_check_out_date = false;
    int check_out_time;

    String easy_dif;
    String id_or_name;

    int no_of_click_hotel = 0;
    int no_of_click_room = 0;
    int no_of_click_room_Quantity = 0;
    int no_of_click_check_in = 0;
    int no_of_click_check_out = 0;
    int no_of_click_adult = 0;
    int no_of_click_child = 0;
    int no_of_click_book_button = 0;

    int hotel_number;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            reference.child(easy_dif).child(id_or_name).child("Battery Level").setValue(String.valueOf(level));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        reference = FirebaseDatabase.getInstance().getReference();


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_booking);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                uplaod_click_values("Booking"+Utils.Total_hotels_book,X,Y);
                return false;
            }
        });


        time_in_miliseconds();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (Utils.iseasy){
            easy_dif = "Easy";
            id_or_name = Utils.easy_user_name;
        }
        else {
            easy_dif = "Difficult";
            id_or_name = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (Utils.firstone) {
                for (int i = 0; i < Utils.login_touches.size(); i++) {
                    uplaod_click_values("Login", Utils.login_touches.get(i).getX(), Utils.login_touches.get(i).getY());
                }
                for (int i = 0; i < Utils.register_touches.size(); i++) {
                    uplaod_click_values("Register", Utils.register_touches.get(i).getX(), Utils.register_touches.get(i).getY());
                }

                reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Login").child("Email").setValue(Utils.no_of_clicks_email);
                reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Login").child("Passsword").setValue(Utils.no_of_clicks_password);
                reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Login").child("Register Button").setValue(Utils.no_of_clicks_register_button);
                reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Login").child("Login Button").setValue(Utils.no_of_clicks_login_button);
                Utils.firstone=false;
            }
        }
        upload_matrices();


        ///////////////////////////////////////Upload Start Screen/////////////////////////////////////////////////////
        reference.child(easy_dif).child(id_or_name).child("Start Screen").child("Screen").setValue(Utils.start_scrn);
        reference.child(easy_dif).child(id_or_name).child("Start Screen").child("Time").setValue(Utils.hour+":"+Utils.mint+":"+Utils.sap);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        check_in_date = (TextView) findViewById(R.id.check_in_date);
        check_out_date = (TextView) findViewById(R.id.check_out_date);
        date_rel_1 = (RelativeLayout) findViewById(R.id.date_rel_1);
        date_rel_2 = (RelativeLayout) findViewById(R.id.date_rel_2);
        continue_button = (ImageView) findViewById(R.id.continue_button);
        hotel_list = (ListView) findViewById(R.id.hotel_list);
        room_list = (ListView) findViewById(R.id.rooms_list);
        room_quantity_list = (ListView) findViewById(R.id.rooms_quantity_list);
        adult_list = (ListView) findViewById(R.id.adults_list);
        child_list = (ListView) findViewById(R.id.child_list);
        child = (RelativeLayout) findViewById(R.id.child_list_layout);
        adults = (RelativeLayout) findViewById(R.id.adults_list_layout);
        rooms_quantity = (RelativeLayout) findViewById(R.id.rooms_quantity_list_layout);
        hotels = (RelativeLayout) findViewById(R.id.hotel_list_layout);
        rooms = (RelativeLayout) findViewById(R.id.rooms_list_layout);
        Image_rooms = (RelativeLayout) findViewById(R.id.rooms);
        Image_adults = (RelativeLayout) findViewById(R.id.select_adults);
        Image_rooms_quantity = (RelativeLayout) findViewById(R.id.select_rooms);
        Image_child = (RelativeLayout) findViewById(R.id.select_child);
        Image_hotels = (RelativeLayout) findViewById(R.id.hotel);
        hotel_type = (TextView) findViewById(R.id.hotel_type);
        room_type = (TextView) findViewById(R.id.room_type);
        no_of_rooms = (TextView) findViewById(R.id.no_of_room);
        no_of_adults = (TextView) findViewById(R.id.no_of_adults);
        no_of_child = (TextView) findViewById(R.id.no_of_child);
        hotel_scenery = (ImageView) findViewById(R.id.hotel_scenary);

        for (int i=0;i<Utils.splash.size();i++){
            uplaod_click_values("Unitylab scrn",Utils.splash.get(i).getX(),Utils.splash.get(i).getY());
        }
        for (int i=0;i<Utils.easy_difficult.size();i++){
            uplaod_click_values("MainActivity",Utils.easy_difficult.get(i).getX(),Utils.easy_difficult.get(i).getY());
        }

        date_rel_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_check_out++;
                analytic_function("check_out_date",date_rel_2);
                is_check_out_date = true;
                chieck_out_date();
                Clicked(date_rel_2);
            }
        });

        date_rel_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_check_in++;
                analytic_function("check_in_date",date_rel_1);
                is_check_in_date = true;
                chieck_in_date();
                Clicked(date_rel_1);
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_book_button++;
                analytic_function("Continue",continue_button);
                    check_String();
            }
        });

        hotel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Selected_hotel = Utils.hotel_list.get(position).getPriceval().toString();
                set_hotel_scenery(position);
                hotels.setVisibility(View.GONE);
                isclicked_hotel = false;
                hotel_type.setText(""+Selected_hotel);
                analytic_function("Hotel_list_selection",hotel_list);
                is_hotel_booking_open = false;
                upload_time_query("hotel",hotel_booking_time);

            }
        });

        room_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_room = Utils.rooms_list.get(i).getRoom_name().toString();
                rooms.setVisibility(View.GONE);
                isclicked_room = false;
                room_type.setText(""+Selected_room);
                analytic_function("room_list_selection",room_list);

                is_room_booking_open = false;
                upload_time_query("room",room_booking_time);
            }
        });
        room_quantity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_room_quantity = Utils.rooms_quantity_list.get(i).getPriceval().toString();
                rooms_quantity.setVisibility(View.GONE);
                isclicked_rooms_quantity = false;
                no_of_rooms.setText(""+Selected_room_quantity);
                analytic_function("room_quantity_selection",room_quantity_list);

                is_room_booking_quantity_open = false;
                upload_time_query("room quantity",room_booking_quantity_time);
            }
        });
        adult_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_adult = Utils.adult_list.get(i).getPriceval().toString();
                adults.setVisibility(View.GONE);
                isclicked_adults = false;
                no_of_adults.setText(""+Selected_adult);
                analytic_function("adult_list_selection",adult_list);

                is_adult = false;
                upload_time_query("Adult",adult_booking_time);
            }
        });
        child_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_child = Utils.child_list.get(i).getPriceval().toString();
                child.setVisibility(View.GONE);
                isclicked_child = false;
                no_of_child.setText(""+Selected_child);
                analytic_function("child_list_selection",child_list);

                is_kid = false;
                upload_time_query("Child",kid_booking_time);
            }
        });

      Image_hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_hotel++;
                if (isclicked_hotel){
                    hotel_booking_time = 0;
                    is_hotel_booking_open = false;
                    hotels.setVisibility(View.GONE);
                    isclicked_hotel = false;
                }
                else {
                    hotel_booking_time = 0;
                    is_hotel_booking_open = true;
                    hotel_booking_time();
                    hotels.setVisibility(View.VISIBLE);
                    isclicked_hotel = true;
                }
            }
        });
     Image_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_room++;
                if (isclicked_room){
                    room_booking_time = 0;
                    is_room_booking_open = false;
                    rooms.setVisibility(View.GONE);
                    isclicked_room = false;
                }
                else {
                    rooms.setVisibility(View.VISIBLE);
                    isclicked_room = true;
                    room_booking_time = 0;
                    is_room_booking_open = true;
                    room_booking_time();
                }
            }
        });
        Image_rooms_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_room_Quantity++;
                if (isclicked_rooms_quantity){

                    room_booking_quantity_time = 0;
                    is_room_booking_quantity_open = false;

                    isclicked_rooms_quantity = false;
                    rooms_quantity.setVisibility(View.GONE);
                }
                else {
                    isclicked_rooms_quantity = true;
                    rooms_quantity.setVisibility(View.VISIBLE);


                    room_booking_quantity_time = 0;
                    is_room_booking_quantity_open = true;
                    room_booking_quantity_time();
                }
            }
        });
        Image_adults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_adult++;
                if (isclicked_adults) {
                    isclicked_adults = false;
                    adults.setVisibility(View.GONE);

                    adult_booking_time = 0;
                    is_adult = false;

                }
                else {
                    isclicked_adults = true;
                    adults.setVisibility(View.VISIBLE);

                    adult_booking_time = 0;
                    is_adult = true;
                    adult_booking_time();
                }
            }
        });
        Image_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_child++;
                if (isclicked_child){
                    isclicked_child = false;
                    child.setVisibility(View.GONE);

                    kid_booking_time = 0;
                    is_kid = false;
                }
                else {
                    isclicked_child  = true;
                    child.setVisibility(View.VISIBLE);

                    kid_booking_time = 0;
                    is_kid = true;
                    kid_booking_time();
                }
            }
        });


        Utils.hotel_list = new ArrayList<ProfilePriceObject>();


        for (int i=0;i<6;i++){
            set_hotel(i);
        }
        Utils.rooms_list = new ArrayList<rooms_object>();
        for (int i=0;i<10;i++){
            set_rooms(i);
        }

        Utils.rooms_quantity_list = new ArrayList<ProfilePriceObject>();
        for (int i=0;i<10;i++){
            String pval= ""+(i+1);
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.rooms_quantity_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.rooms_quantity_list);
            room_quantity_list.setAdapter(priceadapter);
        }
        Utils.adult_list = new ArrayList<ProfilePriceObject>();
        for (int i=0;i<10;i++){
            String pval= ""+(i+1);
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.adult_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.adult_list);
            adult_list.setAdapter(priceadapter);
        }
        Utils.child_list = new ArrayList<ProfilePriceObject>();
        for (int i=0;i<10;i++){
            String pval= ""+i;
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.child_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.child_list);
            child_list.setAdapter(priceadapter);
        }
    }

    private void upload_matrices() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        reference.child(easy_dif).child(id_or_name).child("Resolution").child("Height").setValue(height);
        reference.child(easy_dif).child(id_or_name).child("Resolution").child("Width").setValue(width);
        reference.child(easy_dif).child(id_or_name).child("RAM").setValue(getTotalRAM());
    }


    private void set_rooms(int i) {
        if (i==0){
            String pval= "Delux Double room with garden View";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(150);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==1){
            String pval= "Double or twin room";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(130);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==2){
            String pval= "Double room with sea view";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(110);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==3){
            String pval= "Double room with pool view";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(100);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==4){
            String pval= "Single room with pool view";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(90);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==5){
            String pval= "Single room with sea view";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(200);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }
        else if (i==6){
            String pval= "Family garden suit";
            rooms_object pObjt = new rooms_object();
            pObjt.setRoom_name(pval);
            pObjt.setPrice(220);
            Utils.rooms_list.add(pObjt);
            room_adap=new rooms_adapter(Booking.this,Utils.rooms_list);
            room_list.setAdapter(room_adap);
        }


    }

    private void set_hotel_scenery(int position) {
        hotel_number = position;
        if (position==0){
            Glide.with(getApplicationContext()).load(R.mipmap.mercury).into(hotel_scenery);
        }
        else if (position==1){
            Glide.with(getApplicationContext()).load(R.mipmap.insel_hotel_heilbronn).into(hotel_scenery);
        }
        else if (position==2){
            Glide.with(getApplicationContext()).load(R.mipmap.hotel_newton_heilbronn).into(hotel_scenery);
        }
        else if (position==3){
            Glide.with(getApplicationContext()).load(R.mipmap.city_hotel_gerny).into(hotel_scenery);
        }
        else if (position==4){
            Glide.with(getApplicationContext()).load(R.mipmap.hogh_hotel_heilbronn).into(hotel_scenery);
        }
        else if (position==5){
            Glide.with(getApplicationContext()).load(R.mipmap.hotel_gasthof_zum_rossle).into(hotel_scenery);
        }
    }

    private void set_hotel(int i) {
        if (i==0){
            String pval= "Hotel Mercury";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
        else if (i==1){
            String pval= "Insel Hotel";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
        else if (i==2){
            String pval= "Hotel Newton";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
        else if (i==3){
            String pval= "City Hotel Gerni";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
        else if (i==4){
            String pval= "Hogh Hotel";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
        else if (i==5){
            String pval= "Hotel Gasthof Zum Rossle";
            ProfilePriceObject pObjt = new ProfilePriceObject();
            pObjt.setPriceval(pval);
            Utils.hotel_list.add(pObjt);
            priceadapter=new FindPricveAdapter(Booking.this,Utils.hotel_list);
            hotel_list.setAdapter(priceadapter);
        }
    }

    void  check_String() {
        if (Selected_hotel.isEmpty()){
            Toast.makeText(Booking.this,"Please Select Hotels",Toast.LENGTH_SHORT).show();
        }

        else if (Selected_room.isEmpty()){
            Toast.makeText(Booking.this,"Please Select Room",Toast.LENGTH_SHORT).show();

        }

        else if (Selected_room_quantity.isEmpty()){
            Toast.makeText(Booking.this,"Please Select Room Quantity",Toast.LENGTH_SHORT).show();

        }
        else if (endYear<startYear){
            Toast.makeText(Booking.this,"End Year Can not less then start",Toast.LENGTH_SHORT).show();
        }
        else if (endYear == startYear && endMonth < startMonth){
            Toast.makeText(Booking.this,"End Month Can not less then start",Toast.LENGTH_SHORT).show();
        }
        else if (endYear == startYear && endMonth == startMonth && endDay < startDay){
            Toast.makeText(Booking.this,"End Day Can not less then start",Toast.LENGTH_SHORT).show();
        }

        else if (Selected_adult.isEmpty()){
            Toast.makeText(Booking.this,"Please Select Adults",Toast.LENGTH_SHORT).show();

        }

        else if (Selected_child.isEmpty()){
            Toast.makeText(Booking.this,"Please Select kids",Toast.LENGTH_SHORT).show();
        }
        else {

            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Hotel").setValue(no_of_click_hotel);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Rooms").setValue(no_of_click_room);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Adults").setValue(no_of_click_adult);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Book Button").setValue(no_of_click_book_button);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Check In").setValue(no_of_click_check_in);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Check out").setValue(no_of_click_check_out);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Kids").setValue(no_of_click_child);
            reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Booking"+Utils.Total_hotels_book).child("Rooms Quantity").setValue(no_of_click_room_Quantity);

            if (!Utils.iseasy){
                bol = false;
                //reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Booking").setValue(time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Registration").child("Total").setValue(Utils.register_time);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Login").child("Total").setValue(Utils.login);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Unitylab scrn").setValue(Utils.time_on_easy_and_difficult);
                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Booking"+Utils.Total_hotels_book).child("Total").setValue(time);
            }
            else {

                bol = false;
                reference.child("Easy").child(Utils.easy_user_name).child("Time").child("Booking"+Utils.Total_hotels_book).child("total").setValue(time);
            }

            cart_object obj = new cart_object();
            obj.setAdult(Selected_adult);
            obj.setKids(Selected_child);
            obj.setRoom_quantity(Selected_room_quantity);
            obj.setRoom(Selected_room);
            obj.setHotel_name(Selected_hotel);
            obj.setHotel_no(hotel_number);
            int total = pay_value();
            total = total-pay_values_by_rooms(total);
            obj.setPayment_value(total);
            obj.setDescount_percent(pay_values_by_rooms(total));
            Utils.crt_obj.add(obj);
            Utils.Total_hotels_book++;
            move();
        }
    }
    void move(){
        if (Utils.iseasy){
            Intent intent = new Intent(Booking.this,Payment.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(Booking.this,Cart.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Booking.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected Dialog onCreateDialog(int id) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_START_DIALOG_ID:
                start = true;
                return new DatePickerDialog(this,
                        mDateStartListener,
                        mYear, mMonth, mDay
                );
            case DATE_END_DIALOG_ID:
                start = false;
                return new DatePickerDialog(this,
                        mDateendListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateStartListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
                startYear = selectedYear;
                startMonth = selectedMonth+1;
                startDay = selectedDay;
                check_in_date.setText("" + startDay + "/" + startMonth + "/" + startYear);
                is_check_in_date = false;
                upload_time_query("checkin",check_in_time);


        }
    };

    private DatePickerDialog.OnDateSetListener mDateendListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
                endYear = selectedYear;
                endMonth = selectedMonth+1;
                endDay = selectedDay;
                check_out_date.setText("" + endDay + "/" + endMonth + "/" + endYear);
                is_check_out_date = false;
                upload_time_query("checkout",check_out_time);
        }
    };

    public void Clicked(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.date_rel_1:
                showDialog(DATE_START_DIALOG_ID);
                break;
            case R.id.date_rel_2:
                showDialog(DATE_END_DIALOG_ID);
            default:
                break;
        }
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
    void hotel_booking_time() {
        if (is_hotel_booking_open) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hotel_booking_time++;
                    hotel_booking_time();
                }
            }, 1);
        }
    }
    void room_booking_time() {
        if (is_room_booking_open) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    room_booking_time++;
                    room_booking_time();
                }
            }, 1);
        }
    }
    void room_booking_quantity_time() {
        if (is_room_booking_quantity_open) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    room_booking_quantity_time++;
                    room_booking_quantity_time();
                }
            }, 1);
        }
    }
    void adult_booking_time() {
        if (is_adult) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adult_booking_time++;
                    adult_booking_time();
                }
            }, 1);
        }
    }

    void kid_booking_time() {
        if (is_kid) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    kid_booking_time++;
                    kid_booking_time();
                }
            }, 1);
        }
    }
    void chieck_in_date() {
        if (is_check_in_date) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    check_in_time++;
                    chieck_in_date();
                }
            }, 1);
        }
    }
    void chieck_out_date() {
        if (is_check_out_date) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    check_out_time++;
                    chieck_out_date();
                }
            }, 1);
        }
    }
    void upload_time_query(String part , int time){
        reference.child(easy_dif).child(id_or_name).child("Time").child("Booking"+Utils.Total_hotels_book).child(part).setValue(time);
    }
    void uplaod_click_values(String screen, int X, int Y){
        reference.child(easy_dif).child(id_or_name).child("Clicks On Diff Scr").child(screen).push().setValue(new Touch_objects(X,Y));
    }
    private  String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        long total = 0;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine().replaceAll("\\D+","");;
            total = Integer.parseInt(load)/1024;
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return total+" MB";
    }
    @Override
    public void onStart() {
        super.onStart();

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
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Screen").setValue("Booking");
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Time").setValue(hour+":"+mint+":"+sap);
    }
    int pay_value(){
        int total = 0;


        if (Selected_room.equals("Delux Double room with garden View")){
            total =150;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Double or twin room")){
            total = 130;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Double room with sea view")){
            total = 110;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Double room with pool view")){
            total = 100;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Single room with pool view")){
            total = 90;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Single room with sea view")){
            total = 200;
            //pay_values_by_rooms(total);
        }
        else if (Selected_room.equals("Family garden suit")){
            total = 220;
        }
        return total;
        }
    int pay_values_by_rooms(int a) {
        int total = a;
        int descount  = 0;
        if (Selected_room_quantity.equals("1")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 2;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 4;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 7;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 9;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 1;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 11;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 15;
            }
        }
        else if (Selected_room_quantity.equals("2")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 10;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 14;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 16;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 17;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 12;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 11;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 15;
            }
        }
        else if (Selected_room_quantity.equals("3")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 11;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 14;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 19;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 21;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 23;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 12;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 17;
            }
        }
        else if (Selected_room_quantity.equals("4")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 21;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 25;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 27;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 29;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 26;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 20;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 30;
            }
        }
        else if (Selected_room_quantity.equals("5")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 31;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 32;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 33;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 34;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 35;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 36;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 37;
            }
        }
        else if (Selected_room_quantity.equals("6")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 38;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 39;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 40;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 41;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 42;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 43;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 44;
            }
        }
        else if (Selected_room_quantity.equals("7")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 45;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 46;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 47;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 48;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 49;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 50;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 20;
            }
        }
        else if (Selected_room_quantity.equals("8")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 56;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 51;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 34;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 28;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 36;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 16;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 52;
            }
        }
        else if (Selected_room_quantity.equals("9")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 8;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 30;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 57;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 19;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 56;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 67;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 43;
            }
        }
        else if (Selected_room_quantity.equals("10")) {
            if (Selected_room.equals("Delux Double room with garden View")) {
                a = a - 70;
            } else if (Selected_room.equals("Double or twin room")) {
                a = a - 30;
            } else if (Selected_room.equals("Double room with sea view")) {
                a = a - 57;
            } else if (Selected_room.equals("Double room with pool view")) {
                a = a - 49;
            } else if (Selected_room.equals("Single room with pool view")) {
                a = a - 56;
            } else if (Selected_room.equals("Single room with sea view")) {
                a = a - 67;
            } else if (Selected_room.equals("Family garden suit")) {
                a = a - 43;
            }
        }
        descount = total - a;
        return descount;
    }
    }



