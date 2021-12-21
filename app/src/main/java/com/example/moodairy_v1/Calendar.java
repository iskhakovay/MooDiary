package com.example.moodairy_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity implements View.OnClickListener { //מחלקה זו מאפשרת למשתמש בכל יום ויום להסתקל על מה שהוא עשה באפליקציה
    CalendarView simpleCalendarView;
    TextView avr_mood, medit, note;
   // @SuppressLint("ResourceAsColor")
    DatabaseReference noteRef;
    DatabaseReference moodRef;
    DatabaseReference mediRef;

    boolean flag = true;

    Adapter adapter;
    Animation btn_touch,btn_notouch;

    ArrayList<Meditations> meditations;
    ArrayList<Mood> moods;
    List<Notes> notes;

    ListView show_data;
    long medTime = 0;
    int minutes = 0, sec = 0;
    ImageButton btn_back_toMain;
    LinearLayout day_board;
    String currentDate;
    String uid;
    int sum = 0;
    int count = 0;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calendar);

        simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView); // get the reference of CalendarView
        simpleCalendarView.setFocusedMonthDateColor(Color.MAGENTA); // set the red color for the dates of  focused month
        simpleCalendarView.setUnfocusedMonthDateColor(Color.MAGENTA); // set the yellow color for the dates of an unfocused month
        simpleCalendarView.setSelectedWeekBackgroundColor(Color.MAGENTA); // red color for the selected week's background
        simpleCalendarView.setWeekSeparatorLineColor(Color.MAGENTA); // green color for the week separator line
        simpleCalendarView.setWeekNumberColor(Color.MAGENTA);
        noteRef = FirebaseDatabase.getInstance().getReference("Notes/");
        moodRef = FirebaseDatabase.getInstance().getReference("Mood/");
        mediRef = FirebaseDatabase.getInstance().getReference("Meditations/");
        show_data = (ListView) findViewById(R.id.listView_show);
        moods = new ArrayList<Mood>();
        meditations = new ArrayList<Meditations>();
        notes = new ArrayList<Notes>();
        avr_mood = findViewById(R.id.tv_avr_mood);
        medit = findViewById(R.id.tv_medit);
        note = findViewById(R.id.tv_note);
        btn_back_toMain = findViewById(R.id.btn_back_toMain);
        day_board = findViewById(R.id.day_board);
        note.setOnClickListener(this);
        btn_touch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_touch);
        btn_notouch = AnimationUtils.loadAnimation(this, R.anim.btn_anim_untouch);


        avr_mood.setText("Nothing here yet!");
        medit.setText("Nothing here yet!");
        note.setText("Nothing here yet!");

        btn_back_toMain.setOnClickListener(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        currentDate="";
        // perform setOnDateChangeListener event on CalendarView
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //בחירת יום
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                currentDate = dayOfMonth+"-"+(month+1)+"-"+year;
                avr_mood.setText("Nothing here yet!");
                medit.setText("Nothing here yet!");
                note.setText("Nothing here yet!");
                notes = new ArrayList<Notes>();

                retriveMed();
                retriveMoods();
                retriveNotes();

            }
        });





    }

    @Override
    public void onClick(View view) {
        if(view == btn_back_toMain){
            Intent intent = new Intent(Calendar.this, home_screen.class);
            startActivity(intent);
        }
        if(view == note && flag == true){
            build_dialog();
        }
    }
   public void retriveNotes() {
        //פעולה זאת לוקחת מידע ממוסד נתונים
       int i=0;
       noteRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               // add meditation and mood arrays
               for (DataSnapshot data : dataSnapshot.getChildren()) {
                   Notes n = data.getValue(Notes.class);
                   if(datesConv(currentDate).equals(n.getDate()) && n.getUid().equals(uid)) {
                       notes.add(n);
                   }
               }

               if(notes.size()==0){
                   note.setText("There are no notes at that day:(");
                   flag=false;
               } else{
                   note.setText("Click to see your notes!");
                   flag = true;
               }


           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Toast.makeText(getApplicationContext(), "error hihi haha", Toast.LENGTH_LONG).show();
           }
       });
   }
    private void build_dialog(){

        dialog = new Dialog(this,R.style.DialogTheme);
        dialog.setContentView(R.layout.notes_dialog);
        show_data = (ListView) dialog.findViewById(R.id.listView_show);
        List<Notes> a = clearNotes( notes);
        adapter = new Adapter(Calendar.this, a);
        show_data.setAdapter(adapter);
        dialog.setCancelable(true);
        dialog.show();

    }
    public List<Notes> clearNotes(List<Notes> n){
        int i = 0;
        List<Notes> a = new ArrayList<Notes>();
        if(n.size()<=2){
            a.add(n.get(0));
        }
        else {
            for (i= 0; i < (n.size()-1); i++) {
                if (n.get(i + 1).getNote().equals(n.get(i).getNote())) {
                    a.add(n.get(i+1));
                    i+=2;
                }else{
                    a.add(n.get(i));
                }
            }
            a.add(n.get(i));
        }
        return a;
    }
    public void retriveMoods() { //פעולה זאת לוקחת מידע ממוסד נתונים

        moodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add meditation and mood arrays

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Mood m = data.getValue(Mood.class);
                  //  Log.d("tag",""+m.getDate()+" vs the current date: "+(datesConv(currentDate)) );
                   // Log.d("tag",""+m.getUid()+" vs the user id: "+(uid) );
                    if(datesConv(currentDate).equals(m.getDate())&& m.getUid().equals(uid)){
                        sum+=m.getMood_id();
                        count++;
                    }
                }
                if(count!=0) {
                    avr_mood.setText(getView(Integer.valueOf(sum/count)));
                    count=0;
                    sum = 0;
                } else
                    avr_mood.setText("There's no data on that day");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "error hihi haha", Toast.LENGTH_LONG).show();
            }
        });
    }
    public String datesConv(String currentDate){ //משנה את המראה של String של תאריך
        String [] cDate = currentDate.split("-");
        String cDay =  cDate[0];
        String cMonth = cDate[1];
        String cYear = cDate[2];
        String ccDay="", ccMonth="";

        if(Integer.valueOf(cDay)<10){
            ccDay = "0"+cDay;
        } else{
            ccDay = cDay;
        }
        if(Integer.valueOf(cMonth)<10){
            ccMonth = "0"+cMonth;
        } else{
            ccMonth = cMonth;
        }
       // Log.d("tag","current date: "+ccDay+"-"+ccMonth+"-"+cYear);

        return ccDay+"-"+ccMonth+"-"+cYear;
    }
    public void retriveMed(){ //פעולה זאת לוקחת מידע ממוסד נתונים

        mediRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add meditation and mood arrays
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Meditations med = data.getValue(Meditations.class);
                    if(datesConv(currentDate).equals(med.getDate())&& med.getUid().equals(uid)){
                        medTime += med.getTime();
                    }
                }
                medit.setText(getViewMed(medTime));
                medTime=0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "error hihi haha", Toast.LENGTH_LONG).show();
            }
        });
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //getView(uid, currentDate);

    }
    public String getViewMed(long time){ // מציג על מסך את הזמן בצורה נכונה
        minutes = (int) time/60000;
        sec = (int) (time%60000)/1000;
        String m=String.valueOf(minutes);
        String s=String.valueOf(sec);
        Log.d("tag","mins: "+minutes+" sec: "+sec);
        if(minutes<10){
            m = "0"+minutes;
        }
        if(sec<10){
            s= "0"+sec;
        }
        return m+":"+s;
    }
    public String getView(int id) { // משנה את מצב רוח ממספרים למילים
        if (id < 25)
            return "mostly sad:(";
        if (id >= 25 && id < 50)
            return "a little bit sad";
        if (id >= 50 && id < 75)
            return "pretty good mood";
        if (id >= 75 && id <= 100)
            return "mostly happy!";
        return "There's no data on that day";

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


