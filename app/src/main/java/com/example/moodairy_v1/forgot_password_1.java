package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class forgot_password_1 extends AppCompatActivity implements View.OnClickListener { // המחלקה הזאת מאפשר למשתמש לאפס את הסיסמה שלו
    ImageButton btn_send_code, btn_back;
    EditText et_input_email,et_input_phone;
    Animation btn_touch,btn_notouch;
    DatabaseReference numbRef;
    String uid;
    String currentDate;
    String current_user_phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password_1);

        btn_back = findViewById(R.id.btn_back_white);
        btn_send_code =findViewById(R.id.btn_send_code);
        et_input_email = findViewById(R.id.et_input_email_forgot);


        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);
        //numbRef= FirebaseDatabase.getInstance().getReference("Phones/");
        //currentDate = new SimpleDateFormat("dd-MM-yyyy",
                //Locale.getDefault()).format(new Date());


        btn_send_code.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        buttonsAnims(btn_send_code);
        buttonsAnims(btn_back);




    }

    @Override
    public void onClick(View view) {
        Intent usr_data = getIntent();
        String USER_EMAIL = usr_data.getStringExtra("USER_EMAIL");
        String USER_PHONE = usr_data.getStringExtra("USER_PHONE");

        if(view == btn_back){
            Intent intent = new Intent(forgot_password_1.this, MainActivity.class);
            startActivity(intent);
        }
        if(view == btn_send_code){ //פעולה הזאת שולחת מכתב למייל של המשתמש לאיפוס סיסמה
           FirebaseAuth.getInstance().sendPasswordResetEmail(et_input_email.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Log.d("tag", "Email sent.");
                           }
                       }
                   });
           Intent intent = new Intent(forgot_password_1.this,MainActivity.class);
           startActivity(intent);
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