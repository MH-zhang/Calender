package com.swufe.calender;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AddRecordActivity extends AppCompatActivity {

    public static int ENTER_STATE = 0;
    TextView NowTime;
    EditText write;
    Button btn_record;
    Button btn_cancel;
    NotesDB DB;
    SQLiteDatabase dbRead;
    String last_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        NowTime = findViewById(R.id.NowTime);
        write = findViewById(R.id.write);
        btn_record = findViewById(R.id.btn_record);

        Date date = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String TimeString = time.format(date);
        NowTime.setText(TimeString);


        DB = new NotesDB(this);
        dbRead = DB.getReadableDatabase();


        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        write.setText(last_content);


        btn_record.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String content = write.getText().toString();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNum = sdf.format(date);
                String sql;
                String sql_count = "SELECT COUNT(*) FROM note";
                SQLiteStatement statement = dbRead.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();

                Log.i("ENTER_STATE", String.valueOf(ENTER_STATE));

                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        sql = "insert into " + NotesDB.TABLE_NAME + " values(" + count + "," + "'" + content + "'" + "," + "'" + dateNum + "')";
                        dbRead.execSQL(sql);
                    }
                }else {
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    //values.put("time",dateNum);
                    Log.i("values", String.valueOf(values));
                    dbRead.update("note",values,"content=?", new String[]{last_content});
                }
                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }


}
