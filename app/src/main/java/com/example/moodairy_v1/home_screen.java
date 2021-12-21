package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class home_screen extends AppCompatActivity implements View.OnClickListener { //המחלרה הזאת היא מחלקה של תפריט הראשי ל אפליקציה
    EditText et_note;
    FloatingActionButton fb_main, fb_meditation ,fb_stats, fb_help, fb_mood, fb_calendar;
    ImageButton btn_log_out, btn_setting, dialog_mood_happy, dialog_mood_normal, dialog_mood_okay, dialog_mood_br, dialog_mood_sad, btn_save;
    Animation rotateOpen, rotateClose, from_btn, to_btn, from_btn2, to_btn2;
    Boolean click = false;
    Dialog dialog;
    TextView tv_date, tv_date_dialog;
    int happy = 100, normal = 75, okay = 50 , br = 25, sad = 0;
    int current_mood;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference noteRef, moodRef;
    FirebaseAuth mAuth;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions gso;

    Mood moods = new Mood();
    int mood_count = 0;
    Animation btn_touch,btn_notouch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        et_note = findViewById(R.id.et_note);
        fb_calendar = findViewById(R.id.fb_calendar);
        fb_help = findViewById(R.id.fb_help);
        fb_main = findViewById(R.id.fb_main);
        fb_meditation = findViewById(R.id.fb_meditation);
        fb_stats = findViewById(R.id.fb_stat);
        fb_mood = findViewById(R.id.fb_mood);
        btn_log_out = findViewById(R.id.btn_log_out);
        btn_save = findViewById(R.id.btn_save);
        tv_date = findViewById(R.id.tv_datee);

        tv_date.setText(setDate(currentDate));

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        to_btn = AnimationUtils.loadAnimation(this, R.anim.to_button_anim);
        from_btn = AnimationUtils.loadAnimation(this, R.anim.from_button_anim);
        from_btn2 = AnimationUtils.loadAnimation(this, R.anim.from_button_anim_stats);
        to_btn2 = AnimationUtils.loadAnimation(this, R.anim.to_button_anim_stats);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        buttonsAnims(btn_log_out);
        buttonsAnims(btn_save);

        fb_main.setOnClickListener(this);
        fb_stats.setOnClickListener(this);
        fb_calendar.setOnClickListener(this);
        fb_help.setOnClickListener(this);
        fb_mood.setOnClickListener(this);
        fb_meditation.setOnClickListener(this);

        btn_log_out.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == fb_main){
            click = !click;
            setVisibility(click);
            setAnimation(click);
        }
        if(view == fb_calendar){
         Intent intent = new Intent(home_screen.this, Calendar.class);
         startActivity(intent);
        }
        if(view == fb_help){
            Intent intent = new Intent(home_screen.this, emergency.class);
            startActivity(intent);

        }
        if(view == fb_meditation){
            Intent intent = new Intent(home_screen.this, meditation.class);
            startActivity(intent);
        }
        if(view == fb_mood){
            build_dialog();
        }
        if(view == fb_stats){
            Intent intent = new Intent(home_screen.this, stats.class);
            startActivity(intent);

        }
        if(view == btn_log_out){
           /* Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        startActivity(new Intent(home_screen.this, MainActivity.class));
                        finish();
                    }
                    else{

                    }
                }
            });*/
           // FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(home_screen.this, MainActivity.class);
            startActivity(intent);
        }
        if(view == btn_save){ // שומרת את ההוראות של משתמש למוסד נתונים
            String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).format(new Date());

            String uid = mAuth.getCurrentUser().getUid();
            if(et_note.getText().toString()==null){
                Toast.makeText(this,"Your note can't be empty",Toast.LENGTH_SHORT);
            } else{
                Notes notes = new Notes(et_note.getText().toString(), currentDate, uid, "");
                noteRef = firebaseDatabase.getReference("Notes/").push();
                notes.key = noteRef.getKey();
                noteRef.setValue(notes);
            }


        }

    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            //TO USE
            firebaseAuthWithGoogle(acct);
        }
    }
   // @Override
   /*protected void onStart(){
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull  GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }

    }*/
    private void firebaseAuthWithGoogle(GoogleSignInAccount gso) { // כניסה עם google
        AuthCredential credential = GoogleAuthProvider.getCredential(gso.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithGoogle:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithGoogle:failure", task.getException());

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void setVisibility(Boolean clicked){
        if(clicked){
            fb_meditation.setVisibility(View.VISIBLE);
            fb_help.setVisibility(View.VISIBLE);
            fb_calendar.setVisibility(View.VISIBLE);
            fb_stats.setVisibility(View.VISIBLE);
            fb_mood.setVisibility(View.VISIBLE);

        }
        else{
            fb_meditation.setVisibility(View.INVISIBLE);
            fb_help.setVisibility(View.INVISIBLE);
            fb_calendar.setVisibility(View.INVISIBLE);
            fb_stats.setVisibility(View.INVISIBLE);
            fb_mood.setVisibility(View.INVISIBLE);
        }

    }
    private  void setAnimation(Boolean clicked){
        if(clicked){
            fb_main.startAnimation(rotateOpen);
            fb_mood.startAnimation(from_btn);
            fb_meditation.startAnimation(from_btn);
            fb_stats.startAnimation(from_btn);
            fb_help.startAnimation(from_btn);
            fb_calendar.startAnimation(from_btn);
        }
        else{
            fb_main.startAnimation(rotateClose);
            fb_mood.startAnimation(to_btn);
            fb_meditation.startAnimation(to_btn);
            fb_stats.startAnimation(to_btn);
            fb_help.startAnimation(to_btn);
            fb_calendar.startAnimation(to_btn);
        }
    }
    private void build_dialog(){

        dialog = new Dialog(this, R.style.DialogTheme);
        dialog.setContentView(R.layout.mood_dialog);
        dialog_mood_br = dialog.findViewById(R.id.btn_br);
        tv_date_dialog = dialog.findViewById(R.id.tv_date_dialog);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());
        tv_date_dialog.setText(setDate(currentDate));
        dialog_mood_br.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                current_mood = br;
                addDataMood(current_mood);
                dialog.dismiss();
            }
        });
        dialog_mood_happy = dialog.findViewById(R.id.btn_happy);
        dialog_mood_happy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                current_mood=happy;
                addDataMood(current_mood);
                dialog.dismiss();
            }
        });
        dialog_mood_normal = dialog.findViewById(R.id.btn_normal);
        dialog_mood_normal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                current_mood = normal;
                addDataMood(current_mood);
                dialog.dismiss();
            }
        });
        dialog_mood_okay = dialog.findViewById(R.id.btn_okay);
        dialog_mood_okay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                current_mood = okay;
                addDataMood(current_mood);
                dialog.dismiss();
            }
        });
        dialog_mood_sad = dialog.findViewById(R.id.btn_sad);
        dialog_mood_sad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                current_mood = sad;
                addDataMood(current_mood);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
    public void addDataMood(int cur_mood){ //מכניס את נתוני של מצב רוח ל- database
        String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Mood mood = new Mood (cur_mood,currentDate,uid, "" );
        moodRef = firebaseDatabase.getReference("Mood/").push();
        mood.key = moodRef.getKey();
        moodRef.setValue(mood);
    }
    public String setDate(String date){
        String [] cDate = date.split("-");
        String cDay =  cDate[0];
        String cMonth = cDate[1];
        String cYear = cDate[2];

        String day = "", month = "", year = "";


        if(cMonth.equals("01"))
            month = "January";
        if(cMonth.equals("02"))
            month = "February";
        if(cMonth.equals("03"))
            month = "March";
        if(cMonth.equals("04"))
            month = "April";
        if(cMonth.equals("05"))
            month = "May";
        if(cMonth.equals("06"))
            month = "June";
        if(cMonth.equals("07"))
            month = "July";
        if(cMonth.equals("08"))
            month = "August";
        if(cMonth.equals("09"))
            month = "September";
        if(cMonth.equals("11"))
            month = "November";
        if(cMonth.equals("10"))
            month = "October";
        if(cMonth.equals("12"))
            month = "December";
        day = cDay;
        year = cYear;
        return day+" "+month+" "+year;
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