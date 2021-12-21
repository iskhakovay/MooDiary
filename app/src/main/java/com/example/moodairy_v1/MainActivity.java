package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener { //מחלקה זאת היא מסך הראשון שמאפשר למשתמש להיכנס לאזור האישי, להירשם, לאפס סיסמה
    ImageButton btn_signin, btn_login, btn_forgot_pass;
    SharedPreferences user_data;
    SharedPreferences.Editor myEdit;
    Animation btn_touch, btn_notouch;
    private FirebaseAuth mAuth;
    String USER_PASSWORD = "default", USER_NAME, USER_EMAIL = "default", TAG = "tag";
    String USER_PHONE;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference phoneRef, nameRef;

    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        user_data = getSharedPreferences("pass_and_mail", MODE_PRIVATE);
        myEdit = user_data.edit();
        myEdit.putString("USER_EMAIL", USER_EMAIL);
        myEdit.putString("USER_PASSWORD", USER_PASSWORD);
        myEdit.commit();
        mAuth = FirebaseAuth.getInstance();



        btn_forgot_pass = findViewById(R.id.btn_forgot_pass);
        btn_login = findViewById(R.id.btn_log_in_1);
        btn_signin = findViewById(R.id.btn_signin2);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);

        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        USER_PASSWORD = intent.getStringExtra("USER_PASSWORD");
        USER_EMAIL = intent.getStringExtra("USER_EMAIL");
        USER_NAME = intent.getStringExtra("USER_NAME");
        USER_PHONE = intent.getStringExtra("USER_PHONE");


        if (USER_EMAIL != null && USER_PASSWORD != null) { //הרשמה - לאחר שמשתמש מכניס את נתונים שלו במסכים אחרים, הוא חוזר למסך הזה firebase מייצר משתמש חדש
            mAuth.createUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                                        Locale.getDefault()).format(new Date());

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                Log.d(TAG,uid);
                                nameRef = firebaseDatabase.getReference("Names/").push();
                                phoneRef = firebaseDatabase.getReference("Phones/").push();
                                Name name = new Name(USER_NAME,uid,"");
                                Phone phone = new Phone(USER_PHONE,uid,"");
                                Log.d(TAG, name.getName());
                                name.key = nameRef.getKey();
                                phone.key = phoneRef.getKey();
                                nameRef.setValue(name);
                                phoneRef.setValue(phone);

                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        /*else{
            Toast.makeText(this,"Data is null",Toast.LENGTH_LONG).show();
        }*/

        btn_signin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_forgot_pass.setOnClickListener(this);



        buttonsAnims(btn_signin);
        buttonsAnims(btn_login);
        buttonsAnims(btn_forgot_pass);

    }



    public void buttonsAnims(ImageButton button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button.startAnimation(btn_notouch);
                    btn_notouch.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            onClick(button);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button.startAnimation(btn_touch);
                    btn_touch.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            onClick(button);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                return true;
            }
        });

    }

    public void onClick(View view) {


        if(view == btn_login){

            Intent intent1 = new Intent(MainActivity.this, log_in.class);
            intent1.putExtra("USER_EMAIL",user_data.getString("USER_EMAIL",USER_EMAIL));
            intent1.putExtra("USER_PASSWORD",user_data.getString("USER_PASSWORD",USER_PASSWORD));
            startActivity(intent1);
        }
        if(view == btn_signin){
            Intent intent2 = new Intent(MainActivity.this, sign_in_1.class);
            myEdit.putString("USER_EMAIL",USER_EMAIL);
            myEdit.putString("USER_PASSWORD",USER_PASSWORD);
            myEdit.commit();
            startActivity(intent2);


        }
        if(view == btn_forgot_pass){

            Intent intent3 = new Intent(MainActivity.this, forgot_password_1.class);
            intent3.putExtra("USER_EMAIL",USER_EMAIL);
            startActivity(intent3);

        }

    }



}