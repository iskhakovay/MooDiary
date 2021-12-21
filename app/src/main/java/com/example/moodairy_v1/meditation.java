package com.example.moodairy_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class meditation extends AppCompatActivity implements View.OnClickListener { //מחלקה שאת היא מחלקת טיימר ומדיה פלייר למדיטציה
    ImageView breath;
    ImageButton btn_back, btn_music_lib, btn_set, btn_reset, btn_pause,btn_start, btn_startSong, btn_nextSong, btn_prevSong, btn_pauseSong;
    TextView time,tv_inhale, tv_exhale, tv_hold, tv_song, tv_fromStart, tv_tillEnd;
    EditText input_time;
    SeekBar seekBar;
    Animation inhale, exhale,hold, fade_in, fade_out, hold_text, inhale_text,exhale_text,btn_touch,btn_notouch;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference medRef;
    Resources resources ;

    MediaPlayer mediaPlayer;
    double startTime = 0;
    double finalTime = 0;
    Handler myHandler = new Handler();
    static int oneTimeOnly = 0;
    String TAG = "";
    int [] songs;
    int current_index=0;


    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long millisInput;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meditation);
        exhale = AnimationUtils.loadAnimation(this,R.anim.inhale_anim);
        inhale = AnimationUtils.loadAnimation(this, R.anim.exhale_anim);
        hold = AnimationUtils.loadAnimation(this,R.anim.hold_anim);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        inhale_text =  AnimationUtils.loadAnimation(this, R.anim.exhale_text);
        exhale_text = AnimationUtils.loadAnimation(this,R.anim.inhale_text);
        hold_text =  AnimationUtils.loadAnimation(this,R.anim.hold_text);
        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);
        resources = getApplicationContext().getResources();

        firebaseDatabase = FirebaseDatabase.getInstance();

        breath = findViewById(R.id.breath);
        btn_back = findViewById(R.id.btn_backk);
        btn_set = findViewById(R.id.btn_set);

        btn_pause = findViewById(R.id.btn_pause);
        btn_reset = findViewById(R.id.btn_reset);
        time = findViewById(R.id.tv_time);
        btn_start = findViewById(R.id.btn_start);
        input_time = findViewById(R.id.input_time);
        btn_startSong = findViewById(R.id.btn_startSong);
        btn_nextSong = findViewById(R.id.btn_nextSong);
        btn_prevSong = findViewById(R.id.btn_previousSong);
        btn_pauseSong = findViewById(R.id.btn_pauseSong);

        tv_exhale = findViewById(R.id.tv_exhale);
        tv_hold = findViewById(R.id.tv_hold);
        tv_inhale = findViewById(R.id.tv_inhale);
        tv_song = findViewById(R.id.tv_song);
        tv_fromStart = findViewById(R.id.tv_fromStart);
        tv_tillEnd = findViewById(R.id.tv_tillEnd);

        buttonsAnims(btn_back);


        seekBar = findViewById(R.id.seekBar);

        songs = new int []{R.raw.down_days, R.raw.gentle_ballerina, R.raw.in_the_light, R.raw.quiet_time,
                R.raw.serenity, R.raw.mr_rager,R.raw.time_alone, R.raw.tranquility, R.raw.upon_reflection};

        mediaPlayer = MediaPlayer.create(this, songs[0]);
        String name = resources.getResourceEntryName((songs[current_index]));
        name = name.replace("_"," ");
        tv_song.setText(name);


        btn_back.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_startSong.setOnClickListener(this);
        btn_pauseSong.setOnClickListener(this);
        btn_prevSong.setOnClickListener(this);
        btn_nextSong.setOnClickListener(this);

        tv_inhale.setVisibility(View.INVISIBLE);
        tv_exhale.setVisibility(View.INVISIBLE);
        tv_hold.setVisibility(View.INVISIBLE);
        btn_pauseSong.setEnabled(false);
        btn_pauseSong.setVisibility(View.INVISIBLE);









    }

    @Override
    public void onClick(View view) {
        if(view == btn_startSong){
            play();
        }
        if(view == btn_pauseSong){
            mediaPlayer.pause();
            btn_pauseSong.setEnabled(false);
            btn_pauseSong.setVisibility(View.INVISIBLE);
            btn_startSong.setEnabled(true);
            btn_startSong.setVisibility(View.VISIBLE);

        }
        if(view == btn_set){ //הגדרת זמן לטיימר
            String input = input_time.getText().toString();
            if (input.length() == 0) {
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            millisInput = Long.parseLong(input) * 60000;
            if (millisInput <0) {
                Toast.makeText(this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                setTime(millisInput);
            }
        }
        if(btn_back == view){
            Intent intent = new Intent(meditation.this, home_screen.class);
            startActivity(intent);
        }
        if(view == btn_reset){ //איפוס טיימר
            String input = input_time.getText().toString();
            millisInput = Long.parseLong(input) * 60000;
            long time_data = millisInput - mTimeLeftInMillis;
            if(time_data==0){
                time_data=millisInput;
            }
            Log.d("tag",""+time_data);
            addDataMood(time_data);
            stopAnim();
            mTimeLeftInMillis=0;
            updateCountDownText();
            mCountDownTimer.cancel();
            mTimerRunning = false;
            input_time.setText("");
            input_time.setVisibility(View.VISIBLE);
            time.setVisibility(View.INVISIBLE);
            btn_reset.setVisibility(View.INVISIBLE);
            btn_set.setVisibility(View.VISIBLE);
            btn_start.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);



        }
        if( view == btn_pause ){
            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        }
        if( view == btn_start ){
            String input = input_time.getText().toString();
            if (input.length() == 0) {
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            millisInput = Long.parseLong(input) * 60000;
            if (millisInput < 0 ) {
                Toast.makeText(this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                return;
            } else {

                startAnim();

                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        }
        if(view == btn_nextSong){ //לעבור לשיר הבאה
          if  (mediaPlayer.isPlaying()) {
              mediaPlayer.pause();
              btn_pauseSong.setEnabled(false);
              btn_pauseSong.setVisibility(View.INVISIBLE);
              btn_startSong.setEnabled(true);
              btn_startSong.setVisibility(View.VISIBLE);
          }
            if(current_index+1<songs.length) {
                current_index += 1;
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[current_index]);
                try
                {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                    mediaPlayer.prepare();
                    afd.close();
                }
                catch (IllegalArgumentException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IllegalStateException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                String name = resources.getResourceEntryName((songs[current_index]));
                name = name.replace("_"," ");
                tv_song.setText(name);
                return;
            } if(current_index + 1 == songs.length){
                current_index = 0;
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[current_index]);
                try
                {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                    mediaPlayer.prepare();
                    afd.close();
                }
                catch (IllegalArgumentException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IllegalStateException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                String name = resources.getResourceEntryName((songs[current_index]));
                name = name.replace("_"," ");
                tv_song.setText(name);
                return;
            }


        }
        if(view == btn_prevSong){ //לעבור לשיר הקודם
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btn_pauseSong.setEnabled(false);
                btn_pauseSong.setVisibility(View.INVISIBLE);
                btn_startSong.setEnabled(true);
                btn_startSong.setVisibility(View.VISIBLE);
            }
            if(current_index>0){
                current_index -= 1;
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[current_index]);
                try
                {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                    mediaPlayer.prepare();
                    afd.close();
                }
                catch (IllegalArgumentException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IllegalStateException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                String name = resources.getResourceEntryName((songs[current_index]));
                name = name.replace("_"," ");
                tv_song.setText(name);
                return;
            } if(current_index == 0){
                current_index = songs.length-1;
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[current_index]);
                try
                {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                    mediaPlayer.prepare();
                    afd.close();
                }
                catch (IllegalArgumentException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IllegalStateException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
                }
                String name = resources.getResourceEntryName((songs[current_index]));
                name = name.replace("_"," ");
                tv_song.setText(name);
                return;
            }

        }



    }
    public void play(){ //הפעלת טיימר
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();


        if(oneTimeOnly == 0){
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        tv_fromStart.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );
        tv_tillEnd.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)) - TimeUnit.MILLISECONDS.toSeconds((long)startTime))
        );
        seekBar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
        btn_pauseSong.setEnabled(true);
        btn_pauseSong.setVisibility(View.VISIBLE);
        btn_startSong.setEnabled(false);
        btn_startSong.setVisibility(View.INVISIBLE);
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tv_fromStart.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            tv_tillEnd.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)) - TimeUnit.MILLISECONDS.toSeconds((long)startTime))
            );
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    public void addDataMood(long time_data){ //מכינס את זמן המדיטציה לdatabase
        String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Meditations meditations= new Meditations(time_data, currentDate, uid,"");
        medRef = firebaseDatabase.getReference("Meditations/").push();
        meditations.key = medRef.getKey();
        medRef.setValue(meditations);

    }
    private void setTime(long milliseconds){ //מכניס זמן לטיימר
        mStartTimeInMillis = milliseconds;
       resetTimer();
       closeKeyboard();
    }
    private void pauseTimer() {
        stopAnim();
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }
    private void startTimer() { //מפעיל טיימר
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        if(mTimeLeftInMillis==0){
            Toast.makeText(this, "Timer can't start without a input time. Please enter the desired time.", Toast.LENGTH_SHORT).show();
        }
        else {
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
                    mTimerRunning = false;
                    updateWatchInterface();
                }
            }.start();
            mTimerRunning = true;
            updateWatchInterface();
        }
    }
    private void resetTimer() { //איפוס טיימר
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();

    }
    private void stopTimer(){ //עצירת טיימר
        if(mediaPlayer.isPlaying())
             mediaPlayer.pause();
        else{
            MediaPlayer mp = MediaPlayer.create(this, R.raw.stoptime);
            mp.start();
        }
        stopAnim();
        mTimeLeftInMillis=0;
        updateCountDownText();
        mCountDownTimer.cancel();
        mTimerRunning = false;
        addDataMood(mStartTimeInMillis);
        input_time.setText("");
        input_time.setVisibility(View.VISIBLE);
        time.setVisibility(View.INVISIBLE);
        btn_reset.setVisibility(View.INVISIBLE);
        btn_set.setVisibility(View.VISIBLE);
        btn_start.setVisibility(View.VISIBLE);
        btn_pause.setVisibility(View.INVISIBLE);

    }
    private void updateCountDownText() { //מחדש את השעון
        int minutes = (int) mTimeLeftInMillis/1000/60;
        int seconds = (int) (mTimeLeftInMillis/1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        time.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() { //מחדש את הממשק
        if (mTimerRunning) {
            input_time.setVisibility(View.INVISIBLE);
            btn_set.setVisibility(View.INVISIBLE);
            btn_reset.setVisibility(View.VISIBLE);
            btn_start.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        } else {
            btn_start.setVisibility(View.VISIBLE);
            if (mTimeLeftInMillis < 1000) {
                btn_pause.setVisibility(View.INVISIBLE);
                input_time.setVisibility(View.VISIBLE);
                btn_set.setVisibility(View.VISIBLE);
                btn_reset.setVisibility(View.INVISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                time.setVisibility(View.INVISIBLE);
                stopTimer();
            } else {
                btn_pause.setVisibility(View.VISIBLE);
                btn_set.setVisibility(View.INVISIBLE);
            }
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                btn_reset.setVisibility(View.VISIBLE);
            } else {
                btn_reset.setVisibility(View.INVISIBLE);
                btn_set.setVisibility(View.VISIBLE);
            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void startAnim(){ //אנימצית נשיצה
        breath.startAnimation(inhale);
        exhale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation){breath.startAnimation(inhale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                breath.startAnimation(exhale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        inhale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                breath.startAnimation(hold);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void stopAnim(){ //עצירת אנימציה נשימה
        tv_inhale.setVisibility(View.INVISIBLE);
        tv_hold.setVisibility(View.INVISIBLE);
        tv_exhale.setVisibility(View.INVISIBLE);
        exhale_text.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        exhale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation){
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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

