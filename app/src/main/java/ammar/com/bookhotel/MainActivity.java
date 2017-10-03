package ammar.com.bookhotel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ammar.com.bookhotel.Object.Touch_objects;

public class MainActivity extends AppCompatActivity {
RelativeLayout relativeLayout;
    ImageView register;
    ProgressDialog dialog;
    FirebaseAuth firebaseAuth;
    ImageView login;
    EditText txtEmail,txtpassword;
    String email,password;
    FirebaseAnalytics mFirebaseAnalytics;
    Bundle params = new Bundle();
    Boolean bol = true;
    static int time = 0;
    Boolean email_bol = false;
    int email_time = 0;
    int password_counter_handler = 0;
    int email_counter_handler = 0;
    Boolean password_bol = false;
    int password_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                Touch_objects touch_objects = new Touch_objects(X,Y);
                Utils.login_touches.add(touch_objects);

                return false;
            }
        });
        time_in_miliseconds();
        relativeLayout = (RelativeLayout) findViewById(R.id.main_lay);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        register=(ImageView)findViewById(R.id.register_now);
        login=(ImageView)findViewById(R.id.login);
        txtEmail=(EditText)findViewById(R.id.email);
        txtpassword = (EditText) findViewById(R.id.password);
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_counter_handler++;
                if (email_counter_handler==1) {
                    password_counter_handler = 0;
                    Utils.no_of_clicks_email++;
                    email_bol = true;
                    email_time_miliseconds();
                    password_bol = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_counter_handler++;
                if (password_counter_handler == 1) {
                    Utils.no_of_clicks_password++;
                    email_bol = false;
                    password_bol = true;
                    password_time_miliseconds();
                    email_counter_handler = 0;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
/*        txtpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });*/
        firebaseAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.no_of_clicks_login_button++;
                analytic_function("Login_", login);
                email = txtEmail.getText().toString();
                password = txtpassword.getText().toString();

                if (!isNetwornkAvailable()) {
                    Toast.makeText(MainActivity.this, "Please connect to the Internet...", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter Email.", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a password .", Toast.LENGTH_SHORT).show();
                } else {
                    email_bol = false;
                    password_bol = false;
                    password_counter_handler = email_counter_handler = 0;
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Signing In...");
                    dialog.setCancelable(false);
                    dialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Login").child("Email").setValue(email_time);
                                reference.child("Difficult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Time").child("Login").child("Password").setValue(password_time);
                                bol = false;
                                Utils.login = time;
                                dialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, Booking.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });


                }
            }});



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.no_of_clicks_register_button++;
                bol = false;
                email_bol = false;
                password_bol = false;
                Utils.login = time;
                analytic_function("Register_",login);
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void ShowMessage(String Message)
    {
        dialog.dismiss();
        Toast.makeText(MainActivity.this,Message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    private boolean isNetwornkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    void email_time_miliseconds() {
        if (email_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    email_time++;
                    email_time_miliseconds();
                }
            }, 1);
        }
    }

    void password_time_miliseconds() {
        if (password_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    password_time++;
                    password_time_miliseconds();
                }
            }, 1);
        }
    }
}
