package com.swufe.calender;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{
    private String TAG="mylist2";
    Handler handler;
    private List<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;
    private GridView mGridView;
    String val;
    String str1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGridView();

        Thread t = new Thread(this);
        t.start();

        handler=new Handler(){
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    mGridView = findViewById(R.id.MyGridView);
                    listItems=(List<HashMap<String,String>>)msg.obj;
                    listItemAdapter=new SimpleAdapter(MainActivity.this,listItems,
                            R.layout.item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    mGridView.setAdapter(listItemAdapter);

                    mGridView.setOnItemClickListener(MainActivity.this);

                }
                super.handleMessage(msg);
            }
        };




    }
    private void initGridView(){
        mGridView = findViewById(R.id.MyGridView);
        listItems=new ArrayList<HashMap<String, String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("ItemTitle","阳"+i);
            map.put("ItemDetail","农"+i);
            listItems.add(map);
        }

        //为itme.xml添加适配器
        listItemAdapter = new SimpleAdapter(MainActivity.this,
                listItems, R.layout.item, new String[]{"ItemTitle", "ItemDetail"}, new int[]{R.id.itemTitle, R.id.itemDetail});
        mGridView.setAdapter(listItemAdapter);

    }

    @Override
    public void run(){
    List<HashMap<String,String>> retlist=new ArrayList<HashMap<String,String>>();

    Document doc;
        try {
            Thread.sleep(1000);
            doc = Jsoup.connect("https://wannianli.tianqi.com/2020/06/").get();
            Log.i(TAG, "run: "+ doc.title());
            Elements uls =  doc.getElementsByTag("ul");
            Element ul = uls.get(6);
            Elements dds = ul.getElementsByTag("dd");
            int num =1;
            for (int i = 0; i < dds.size(); i = i + 7) {
                Element dd2 = dds.get(i + 1);
                str1 = num + "";
                num++;
                String val1 = dd2.text();
                if (val1.length() == 17) {
                    val = val1.substring(13);
                } else {
                    val = val1.substring(14);
                }
                Log.i(TAG, "run: " + num + "==>" + dd2.text());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);
                retlist.add(map);
            }
        } catch (
                IOException | InterruptedException e) {
        e.printStackTrace();
    }

        Message msg = handler.obtainMessage(5);
        msg.obj=retlist;
        handler.sendMessage(msg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent record=new Intent(MainActivity.this,RecordActivity.class);
        startActivity(record);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: parent="+parent);
        Log.i(TAG, "onItemClick: view="+view);
        Log.i(TAG, "onItemClick: position="+position);
        Log.i(TAG, "onItemClick: id="+id);

        HashMap<String,String>map=(HashMap<String,String>)mGridView.getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        Log.i(TAG, "onItemClick: "+titleStr);

        TextView title=view.findViewById(R.id.itemTitle);
        String title2=String.valueOf(title.getText());
        Log.i(TAG, "onItemClick: title2"+title2);

        Intent festival=new Intent(this,FestivalActivity.class);
        festival.putExtra("title",titleStr);
        startActivity(festival);

    }


}

