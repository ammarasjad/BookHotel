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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ammar.com.bookhotel.Object.Touch_objects;

public class Register extends AppCompatActivity {

    EditText txtuserName,txtemail,txtpassword,txtage;
    String userName="",email="",password,age="",gender_value="";
    TextView gender,male,female;
    Boolean gender_open = true;
    RelativeLayout mf;
    ImageView registerNow;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    DatabaseReference databaseReference;

    Boolean bol = true;
    static int time = 0;

    Boolean email_bol = true;
    int email_time = 0;

    Boolean username_bol = true;
    int username_time = 0;

    Boolean password_bol = true;
    int password_time = 0;

    Boolean age_bol = true;
    int age_time = 0;

    Boolean gen_bol = true;
    int gender_time = 0;

    int no_of_click_username = 0;
    int no_of_click_email = 0;
    int no_of_click_password = 0;
    int no_of_click_register_button = 0;
    int no_of_click_age = 0;
    int no_of_click_gender = 0;

    int click_password_handler = 0;
    int click_email_handler = 0;
    int click_username_handler = 0;
    int click_age_handler = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        time_in_miliseconds();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        gender = (TextView) findViewById(R.id.gender);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.registration);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                Touch_objects touch_objects = new Touch_objects(X,Y);
                Utils.register_touches.add(touch_objects);

                return false;
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                no_of_click_gender++;
                if (gender_open){
                    mf.setVisibility(View.VISIBLE);
                    gen_bol = true;
                    gender_open = false;
                    gender_in_miliseconds();
                }
                else{
                    mf.setVisibility(View.GONE);
                    gender_open = true;
                    gen_bol = false;
                }
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mf.setVisibility(View.GONE);
                gender_open = false;
                gen_bol = false;
                gender_value = "Male";
                gender.setText(gender_value+"");
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mf.setVisibility(View.GONE);
                gender_open = false;
                gen_bol = false;
                gender_value = "Female";
                gender.setText(gender_value+"");
            }
        });
        mf = (RelativeLayout) findViewById(R.id.mf);
        txtage = (EditText) findViewById(R.id.age);
        txtage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                click_age_handler++;
                age_bol = true;
                password_bol = false;
                email_bol = false;
                username_bol = false;
                click_password_handler = click_email_handler = click_username_handler =  0;
                if (click_age_handler == 1) {
                    no_of_click_age++;
                    age_in_miliseconds();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtuserName=(EditText)findViewById(R.id.username);
        txtuserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                click_username_handler++;
                username_bol = true;
                password_bol = false;
                email_bol = false;
                age_bol = false;
                click_password_handler = click_email_handler = 0;
                if (click_username_handler == 1) {
                    no_of_click_username++;
                    username_in_miliseconds();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtemail=(EditText)findViewById(R.id.email);
        txtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                click_email_handler++;
                email_bol = true;
                password_bol = false;
                username_bol = false;
                age_bol = false;
                click_password_handler = click_username_handler = 0;
                if (click_email_handler == 1) {
                    no_of_click_email++;
                    email_in_miliseconds();
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtpassword=(EditText)findViewById(R.id.password);
        txtpassword.addTextChangedListener(new TextWatcher() {
               @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                click_password_handler++;
                password_bol = true;
                email_bol = false;
                   age_bol = false;
                username_bol = false;
                click_email_handler = click_username_handler = 0;
                if (click_password_handler == 1) {
                    no_of_click_password++;
                    password_in_miliseconds();
                }
            }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    });
        registerNow=(ImageView)findViewById(R.id.register);
        dialog = new ProgressDialog(Register.this);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_of_click_register_button++;
                userName=txtuserName.getText().toString();
                email=txtemail.getText().toString();
                age = txtage.getText().toString();
                password=txtpassword.getText().toString();
                if (!isNetwornkAvailable()){
                    Toast.makeText(Register.this,"Please connect to the Internet.",Toast.LENGTH_SHORT).show();
                }
                else if (userName.isEmpty())
                {
                    Toast.makeText(Register.this,"Enter a User Name.",Toast.LENGTH_SHORT).show();
                }
                else if (email.isEmpty())
                {
                    Toast.makeText(Register.this,"Enter Your Email.",Toast.LENGTH_SHORT).show();
                }
                else if (gender_value.isEmpty())
                {
                    Toast.makeText(Register.this,"Select Gender.",Toast.LENGTH_SHORT).show();
                }
                else if (age.isEmpty())
                {
                    Toast.makeText(Register.this,"Enter Your Age.",Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty())
                {
                    Toast.makeText(Register.this,"Enter a password .",Toast.LENGTH_SHORT).show();
                }
                else if (password.length()<6)
                {
                    Toast.makeText(Register.this," Password must contain six characters .",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    password_bol = false;
                    email_bol = false;
                    username_bol = false;
                    bol=false;

                    dialog.setMessage("Registring...");
                    dialog.setCancelable(false);
                    dialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userid = firebaseAuth.getCurrentUser().getUid();
                                databaseReference.child("User").child(userid).child("Email").setValue(email);
                                databaseReference.child("User").child(userid).child("User Name").setValue(userName);
                                databaseReference.child("User").child(userid).child("password").setValue(password);
                                databaseReference.child("User").child(userid).child("Age").setValue(age);
                                databaseReference.child("User").child(userid).child("gender").setValue(gender_value);
                                databaseReference.child("Difficult").child(userid).child("Time").child("Registration").child("Email").setValue(email_time);
                                databaseReference.child("Difficult").child(userid).child("Time").child("Registration").child("Password").setValue(password_time);
                                databaseReference.child("Difficult").child(userid).child("Time").child("Registration").child("Username").setValue(username_time);
                                databaseReference.child("Difficult").child(userid).child("Time").child("Registration").child("Age").setValue(age_time);
                                databaseReference.child("Difficult").child(userid).child("Time").child("Registration").child("Gender").setValue(gender_time);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Email").setValue(no_of_click_email);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Username").setValue(no_of_click_username);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Password").setValue(no_of_click_password);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Age").setValue(no_of_click_age);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Gender").setValue(no_of_click_gender);
                                databaseReference.child("Difficult").child(userid).child("Clicks count").child("Registration").child("Register Button").setValue(no_of_click_register_button);

                                Utils.register_time = time;
                                dialog.dismiss();
                                Intent intent=new Intent(Register.this,Booking.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(Register.this,"Registration Unsuccessfull",Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    ;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(Register.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean isNetwornkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    void email_in_miliseconds() {
        if (email_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    email_time++;
                    email_in_miliseconds();
                }
            }, 1);
        }
    }
    void password_in_miliseconds() {
        if (password_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    password_time++;
                    password_in_miliseconds();
                }
            }, 1);
        }
    }
    void username_in_miliseconds() {
        if (username_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    username_time++;
                    username_in_miliseconds();
                }
            }, 1);
        }
    }
    void age_in_miliseconds() {
        if (age_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    age_time++;
                    age_in_miliseconds();
                }
            }, 1);
        }
    }
    void gender_in_miliseconds() {
        if (gen_bol) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gender_time++;
                    gender_in_miliseconds();
                }
            }, 1);
        }
    }
}
