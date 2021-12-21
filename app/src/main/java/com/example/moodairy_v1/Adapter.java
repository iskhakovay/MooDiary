package com.example.moodairy_v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter extends ArrayAdapter<Notes> { //מחלקה זו מציגה את listview על המסך
    Context context;
    List<Notes> lv;
    public Adapter (Context context, List<Notes> lv){ // פעולה בנויה
        super(context, 0, lv);
        this.context =context;
        this.lv = lv;
    }

    public View getView(int position, View contextView, ViewGroup parent){ // הצגת נתונים על המסך
        View raw_data = contextView;
        raw_data = LayoutInflater.from(context).inflate(R.layout.line,parent,false);
        Notes temp = lv.get(position);
        TextView note_num = raw_data.findViewById(R.id.note_num);
        TextView note_show  = raw_data.findViewById(R.id.note_show);

        note_num.setText("#"+(position+1));
        note_show.setText(temp.getNote());

        return raw_data;


    }
}

