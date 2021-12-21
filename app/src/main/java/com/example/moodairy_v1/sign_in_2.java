package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_in_2 extends AppCompatActivity implements View.OnClickListener { //מחלקה שניה לכניסה של מתשמש לאזור אישי
    ImageButton btn_back, btn_signin;
    EditText et_new_user_pass, et_new_user_pass2;
    SharedPreferences user_data;
    String user_pass="", TAG = "tag";
    Animation btn_touch,btn_notouch;
   // private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in_2);
        user_data = getSharedPreferences("pass_and_mail",MODE_PRIVATE);
        btn_back = findViewById(R.id.btn_back_signin2);
        btn_signin = findViewById(R.id.btn_signin2);
        et_new_user_pass = findViewById(R.id.et_new_user_pass);
        et_new_user_pass2 = findViewById(R.id.et_new_user_pass2);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);


        btn_back.setOnClickListener(this);
        btn_signin.setOnClickListener(this);

        buttonsAnims(btn_back);
        buttonsAnims(btn_signin);
    }

    @Override
    public void onClick(View view) {

        if(view == btn_back){
            Intent intent2 = new Intent(sign_in_2.this, sign_in_1.class);
            startActivity(intent2);
        }
        if(view == btn_signin){
            Intent intent1 = getIntent();
            String USER_NAME = intent1.getStringExtra("USER_NAME");
            String USER_EMAIL = intent1.getStringExtra("USER_EMAIL");
            String USER_PHONE = intent1.getStringExtra("USER_PHONE");
            if(et_new_user_pass.getText().toString().length()<6){
                Toast.makeText(this, "Your password should be 6 symbols ot longer",Toast.LENGTH_SHORT).show();
            } else {
                if (et_new_user_pass2.getText().toString().equals(et_new_user_pass.getText().toString())) {
                    SharedPreferences.Editor myEdit = user_data.edit();
                    myEdit.putString("USER_PASSWORD", et_new_user_pass.getText().toString());
                    myEdit.commit();
                    Intent intent = new Intent(sign_in_2.this, MainActivity.class);
                    intent.putExtra("USER_EMAIL", USER_EMAIL);
                    intent.putExtra("USER_PASSWORD", et_new_user_pass.getText().toString());
                    intent.putExtra("USER_NAME",USER_NAME);
                    intent.putExtra("USER_PHONE",USER_PHONE);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Your passwords don't match. Please check again.", Toast.LENGTH_LONG).show();
                }
            }

        }

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






}