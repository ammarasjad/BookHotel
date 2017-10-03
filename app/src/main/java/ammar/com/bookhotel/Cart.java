package ammar.com.bookhotel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ammar.com.bookhotel.Adapter.FindPricveAdapter;
import ammar.com.bookhotel.Adapter.cart_list_adapter;
import ammar.com.bookhotel.Object.ProfilePriceObject;
import ammar.com.bookhotel.Object.cart_object;

import static ammar.com.bookhotel.Utils.latitude;
import static ammar.com.bookhotel.Utils.longitude;

public class Cart extends AppCompatActivity {

    ImageView pay,another;
    cart_list_adapter cart_list_Adapter;
    ListView cart_list;

    String easy_dif;
    String id_or_name;

    DatabaseReference reference;

    Boolean bol = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        time_in_miliseconds();
        reference = FirebaseDatabase.getInstance().getReference();
        if (Utils.iseasy){
            easy_dif = "Easy";
            id_or_name = Utils.easy_user_name;
        }
        else {
            easy_dif = "Difficult";
            id_or_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        pay = (ImageView) findViewById(R.id.pay);
        another = (ImageView) findViewById(R.id.another);
        cart_list = (ListView) findViewById(R.id.list);

            cart_list_Adapter = new cart_list_adapter(Cart.this, Utils.crt_obj);
            cart_list.setAdapter(cart_list_Adapter);



        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.crt_obj.size() > 0) {
                    bol = false;
                    reference.child(easy_dif).child(id_or_name).child("Time").child("Cart").child("total").setValue(Utils.cart_time);
                    reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Cart").child("Added").setValue(Utils.Total_hotels_book);
                    reference.child(easy_dif).child(id_or_name).child("Clicks count").child("Cart").child("Deleted").setValue(Utils.Total_hotels_book_deleted);
                    move();
                }
                else {
                    Toast.makeText(Cart.this,"No hotel Selected",Toast.LENGTH_SHORT).show();
                }
            }
        });
        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol = false;
                Intent intent = new Intent(Cart.this,Booking.class);
                finish();
                startActivity(intent);
            }
        });
    }
    void time_in_miliseconds() {
        if (bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.cart_time++;
                    time_in_miliseconds();
                }
            }, 1);
        }
    }
    void move(){
        Intent intent = new Intent(Cart.this, Payment.class);
        finish();
        startActivity(intent);
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
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Screen").setValue("Cart");
        reference.child(easy_dif).child(id_or_name).child("End Screen").child("Time").setValue(hour+":"+mint+":"+sap);
    }
}
