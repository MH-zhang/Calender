package com.swufe.calender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FestivalActivity extends AppCompatActivity {
    String TAG="festival";
    String festival;
    String title;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        title=getIntent().getStringExtra("title");
        Log.i(TAG, "onCreate: title="+title);

        IsFestival();

        show=findViewById(R.id.festival);
        show.setText(festival);
    }

    public void IsFestival(){
        if(title.equals("1")){
            festival ="儿童节";
        }else if(title.equals("5")){
            festival ="环境日";
        }else if(title.equals("13")){
            festival ="文化遗产日";
        }else if(title.equals("21")){
            festival ="父亲节";
        }else if(title.equals("22")){
            festival ="夏至";
        }else if(title.equals("25")){
            festival ="端午节";
        }else if(title.equals("26")){
            festival ="国际禁毒日";
        }else{
            festival="平凡的一天";
        }
    }
}
