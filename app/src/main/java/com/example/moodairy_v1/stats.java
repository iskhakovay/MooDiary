package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class stats extends AppCompatActivity implements View.OnClickListener {// מחלקה לבניית גרף של מצב רוח
    TextView tv_average, tv_best, tv_worst;
    GraphView graph;
    ImageButton btn_back;
    DatabaseReference moodRef;
    FirebaseAuth mAuth;
    String uid;
    int day=0;
    LineGraphSeries < DataPoint > series;
    String date="", prevDate="";
    ArrayList<Mood> m;
    int ccount=0;
    String text = "Not enouph data, the graph can't be build";
    Animation btn_touch,btn_notouch;
    boolean flag = true;

    int sum = 0, count = 0, max = 0, min = 101, moods_count = 0, moods_sum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stats);

        tv_average = findViewById(R.id.tv_average);
        tv_best = findViewById(R.id.tv_best);
        tv_worst = findViewById(R.id.tv_worst);

        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);

        graph = findViewById(R.id.graph);

        btn_back = findViewById(R.id.btn_back_stats);

        moodRef = FirebaseDatabase.getInstance().getReference("Mood/");
        mAuth = FirebaseAuth.getInstance();
        series = new LineGraphSeries< >();
        m = new ArrayList<Mood>();
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getViewport().setMaxY(101);

        uid = mAuth.getUid();
        retriveMoods();
        Log.d("tag",String.valueOf(flag));


        btn_back.setOnClickListener(this);
        buttonsAnims(btn_back);



    }
    public void buildGraph(ArrayList<Mood> m){
        int sumDay = 0, countDay = 1, dayAv = 0;


        series.setColor(Color.rgb(248,162,183));
        for(int i = 0;i < (m.size()); i++){
            if(i<m.size()-1) {
                while (m.get(i).getDate().equals(m.get(i + 1).getDate())) {
                    sumDay += m.get(i).getMood_id();
                    countDay += 1;
                    if (i < (m.size() - 2))
                        i++;
                    else {
                        i++;
                        break;
                    }
                }
            }
            sumDay+=m.get(i).getMood_id();
            dayAv = sumDay/(countDay);
            day++;
            Log.d("tag",day+" ; "+dayAv);
            series.appendData(new DataPoint(day,dayAv),true,100);
            ccount++;
            dayAv = 0;
            sumDay = 0;
            countDay = 1;

        }

    }


    public void retriveMoods() { //לוקח מידע ממוסד נתונים

        moodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add meditation and mood arrays

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Mood mm = data.getValue(Mood.class);
                    //  Log.d("tag",""+m.getDate()+" vs the current date: "+(datesConv(currentDate)) );
                    // Log.d("tag",""+m.getUid()+" vs the user id: "+(uid) );

                    if(mm.getUid().equals(uid)){
                        date = mm.getDate();
                        m.add(mm);
                        if(mm.getMood_id()>max){
                            max = mm.getMood_id();
                        }
                        if(mm.getMood_id()<min){
                            min = mm.getMood_id();
                        }
                        sum+=mm.getMood_id();
                        count++;

                    }


                }
                buildGraph(m);
                check(ccount);
                graph.addSeries(series);


                if(count!=0) {
                    tv_average.setText(moodToText(Integer.valueOf(sum/count)));
                    tv_best.setText(moodToText(max));
                    tv_worst.setText(moodToText(min));
                    count=0;
                    sum = 0;
                } else {
                    tv_average.setText("There's no data");
                    tv_best.setText("There's no data");
                    tv_worst.setText("There's no data");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "error hihi haha", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void check(int i){ //בודק האם יש מספיק נקודות לבניית גרף
        if(i<2)
           Toast.makeText(this, text ,Toast.LENGTH_LONG).show();
    }

    public String moodToText(int id ){ //מעביר את מצב רוח ממספרים למילים
        if (id < 25)
            return "mostly sad:(";
        if (id >= 25 && id < 50)
            return "a little bit sad";
        if (id >= 50 && id < 75)
            return "pretty good mood";
        if (id >= 75 && id <= 100)
            return "mostly happy!";
        return "There's no data";
    }

    @Override
    public void onClick(View view) {
        if(view == btn_back){
            Intent intent = new Intent(stats.this,home_screen.class);
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