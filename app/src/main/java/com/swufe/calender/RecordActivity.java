package com.swufe.calender;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity implements  OnItemClickListener, OnItemLongClickListener {
    private Context context;
    private ListView listView;
    private SimpleAdapter recAdapter;
    private List<Map<String, Object>> dataList;
    private Button add;
    TextView tv_content;
    private NotesDB DB;
    private SQLiteDatabase dbRead;
    String TAG ="record";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        tv_content = findViewById(R.id.tv_content);
        listView = findViewById(R.id.listview);
        add = findViewById(R.id.btn_add);


        DB = new NotesDB(this);
        dbRead = DB.getReadableDatabase();

        dataList = new ArrayList<Map<String, Object>>();
        context = this;


        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AddRecordActivity.ENTER_STATE = 0;
                Intent intent = new Intent(context, AddRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });


        initList();
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        AddRecordActivity.ENTER_STATE = 1;

        HashMap<String,String> map = (HashMap<String, String>) listView.getItemAtPosition(arg2);
        String contentStr = map.get("tv_content");
        String dateStr = map.get("tv_date");
        String content1 =contentStr;
        Log.i(TAG, "onItemClick: titleStr=" + contentStr);
        Log.i(TAG, "onItemClick: detailStr=" + dateStr);

        /*
        TextView content =  arg1.findViewById(R.id.tv_content);
        TextView date = arg1.findViewById(R.id.tv_date);
        String title2 = String.valueOf(content.getText());
        String detail2 = String.valueOf(date.getText());
        Log.i(TAG, "onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);

         */
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            Intent myIntent=new Intent(this,AddRecordActivity.class);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            initList();
        }

    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position="+position);

        final int n=position;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String>map=(HashMap<String,String>)listView.getItemAtPosition(n);
                String contentStr=map.get("tv_content");
                Log.i(TAG, "onItemClick: "+contentStr);
                dataList.remove(position);
                recAdapter.notifyDataSetChanged();
                String newcontent ="";
                ContentValues values = new ContentValues();
                values.put("content", newcontent);
                dbRead.update("note",values,"content=?", new String[]{contentStr});
                 initList();

            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    public void initList() {
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            recAdapter.notifyDataSetChanged();
            listView.setAdapter(recAdapter);
        }
        recAdapter = new SimpleAdapter(this, getData(), R.layout.item2,
                new String[] { "tv_content", "tv_date" }, new int[] {R.id.tv_content, R.id.tv_date });
        listView.setAdapter(recAdapter);
    }

    private List<Map<String, Object>> getData() {
        Cursor cursor = dbRead.query("note", null, "content!=\"\"", null, null, null, null);
        while (cursor.moveToNext()) {
            String ccontent = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", ccontent);
            map.put("tv_date", date);
            dataList.add(map);
        }
        cursor.close();
        return dataList;
    }


    public void OpenCalender(View btn) {
        Log.i("open","openone");
        Intent calender = new Intent(this,MainActivity.class);
        startActivity(calender);
        finish();
    }


}
