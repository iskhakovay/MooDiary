package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_in_1 extends AppCompatActivity  implements View.OnClickListener { //מחלקה ראשונה לכניסה של משתמש לאזור אישי
    ImageButton btn_back_signin, btn_next;
    EditText et_new_user_name, et_new_user_email, et_new_user_phone;
    SharedPreferences user_data;
    Animation btn_touch, btn_notouch;
    FirebaseAuth auth;
    Context v;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in_1);
        user_data = getSharedPreferences("pass_and_mail",MODE_PRIVATE);
        et_new_user_email = findViewById(R.id.et_new_user_email);
        et_new_user_name = findViewById(R.id.et_new_user_name);
        et_new_user_phone = findViewById(R.id.et_new_user_phone);
        btn_back_signin = findViewById(R.id.btn_back_signin);
        btn_next = findViewById(R.id.btn_next);

        // Write a message to the database
        auth = FirebaseAuth.getInstance();
        v = getApplicationContext();


        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);


        btn_next.setOnClickListener(this);
        btn_back_signin.setOnClickListener(this);

        buttonsAnims(btn_next);
        buttonsAnims(btn_back_signin);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_back_signin){
            Intent intent = new Intent (sign_in_1.this, MainActivity.class);
            startActivity(intent);

        }
        if(view == btn_next){
            SharedPreferences.Editor myEdit = user_data.edit();
            Intent intent1 = new Intent(sign_in_1.this, sign_in_2.class);

            if(!(et_new_user_name.getText().toString().isEmpty() || et_new_user_phone.getText().toString().isEmpty() || et_new_user_email.getText().toString().isEmpty()))
            {
                auth.fetchSignInMethodsForEmail(et_new_user_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                if (isNewUser) {
                                    Log.e("TAG", "Is New User!");
                                    intent1.putExtra("USER_EMAIL", et_new_user_email.getText().toString());
                                    intent1.putExtra("USER_NAME", et_new_user_name.getText().toString());
                                    myEdit.putString("EMAIL", et_new_user_email.getText().toString());
                                    intent1.putExtra("USER_PHONE",et_new_user_phone.getText().toString());

                                    myEdit.commit();
                                    startActivity(intent1);
                                } else {
                                    Log.e("TAG", "Is Old User!");
                                    Toast.makeText(v,"This email is already taken!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            } else {
                Toast.makeText(this, "One of fields is empty. The field can't be empty",Toast.LENGTH_SHORT).show();
                Log.e("TAG", "smth empty");
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