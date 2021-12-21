package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class log_in extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener { //המחלקה הזאת נותנת למשתמש להיכנס לאזור האישי שלן
    EditText et_user_email, et_user_pass;
    private static final int SIGN_IN = 1;
    ImageButton btn_back, btn_log_in, btn_log_in_google;
    SharedPreferences user_data;;
    SharedPreferences.Editor myEdit;
    private FirebaseAuth mAuth;
    boolean boolean_google;
    boolean flag = false;
    String start_email = "", start_pass = "", TAG = "tag";
    Animation btn_touch,btn_notouch;
    ProgressBar loading;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);
        user_data = getSharedPreferences("pass_and_mail", MODE_PRIVATE);
        myEdit = user_data.edit();
        et_user_email = findViewById(R.id.et_user_email);
        et_user_pass = findViewById(R.id.et_user_pass);
        btn_back = findViewById(R.id.btn_back_signin);
        btn_log_in = findViewById(R.id.btn_log_in_1);

        mAuth = FirebaseAuth.getInstance();

        btn_log_in.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        loading = findViewById(R.id.loading);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);

        Intent intent1 = getIntent();
        start_email = intent1.getStringExtra("USER_EMAIL");
        start_pass = intent1.getStringExtra("USER_PASSWORD");

        buttonsAnims(btn_log_in);
        buttonsAnims(btn_back);



    }

    @Override
    public void onClick(View view) {

        if (view == btn_back) {
            Intent intent = new Intent(log_in.this, MainActivity.class);
            startActivity(intent);
        }
        if (view == btn_log_in) { //כניסה ל - firbase
            loading.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(et_user_email.getText().toString(), et_user_pass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                //Toast.makeText(getApplicationContext(), "Authentication yes.",
                                       // Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent2 = new Intent(log_in.this, home_screen.class);
                                startActivity(intent2);
                                //updateUI(user);
                            } else {
                                loading.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Entered email or password in incorrect. Please check and try again", Toast.LENGTH_LONG).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });



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
   /* public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }*/


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {//במקרה אי הצלחה

    }
}