package com.example.moodairy_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class emergency extends AppCompatActivity implements View.OnClickListener { // המחלקה זאת מאפשרת למשתמש להתקשר לקו אדום או לפתוח אתרי אינטרנט לעזרה ראשונה
    ImageButton btn_call_1, btn_call_2, btn_link_1, btn_link_2, btn_back;
    TextView tv_number_1, tv_number_2, tv_link_1, tv_link_2;
    Animation btn_touch, btn_notouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_emergency);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);

        btn_call_1 = findViewById(R.id.btn_call_1);
        btn_call_2 = findViewById(R.id.btn_call_2);
        btn_link_1 = findViewById(R.id.btn_link_1);
        btn_link_2 = findViewById(R.id.btn_link_2);
        btn_back = findViewById(R.id.btn_back_help);

        tv_link_1 = findViewById(R.id.tv_link_1);
        tv_link_2 = findViewById(R.id.tv_link_2);
        tv_number_1 = findViewById(R.id.tv_number_1);
        tv_number_2 = findViewById(R.id.tv_number_2);

        btn_call_1.setOnClickListener(this);
        btn_call_2.setOnClickListener(this);
        btn_link_1.setOnClickListener(this);
        btn_link_2.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        buttonsAnims(btn_call_1);
        buttonsAnims(btn_call_2);
        buttonsAnims(btn_link_1);
        buttonsAnims(btn_link_2);
        buttonsAnims(btn_back);

    }
    @Override
    public void onClick(View view) {
        if(view == btn_back){
            Intent intent = new Intent(emergency.this, home_screen.class);
            startActivity(intent);
        }
        if(view == btn_call_1){
            Intent callIntent1 = new Intent(Intent.ACTION_DIAL);
            callIntent1.setData(Uri.parse("tel:"+tv_number_1.getText().toString()));
            startActivity(callIntent1);
        }
        if(view == btn_call_2){
            Intent callIntent2 = new Intent(Intent.ACTION_DIAL);
            callIntent2.setData(Uri.parse("tel:"+tv_number_2.getText().toString()));
            startActivity(callIntent2);
        }
        if(view == btn_link_1){
            Intent linkIntent1 = new Intent(Intent.ACTION_VIEW);
            linkIntent1.setData(Uri.parse("https://www.betipulnet.co.il"));
            startActivity(linkIntent1);
        }
        if(view == btn_link_2){
            Intent linkIntent2 = new Intent(Intent.ACTION_VIEW);
            linkIntent2.setData(Uri.parse("https://sahar.org.il"));
            startActivity(linkIntent2);
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