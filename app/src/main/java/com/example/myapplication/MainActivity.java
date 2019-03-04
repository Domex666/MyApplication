package com.example.myapplication;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    private TextView textView;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView1);
        textView=(TextView)findViewById(R.id.textView);
        ParseTitle parseTitle = new ParseTitle();
        parseTitle.execute();
        try {
            final HashMap<String,String> hashMap = parseTitle.get();
            final ArrayList<String> arrayList = new ArrayList<>();
            for(Map.Entry entry : hashMap.entrySet()){
                arrayList.add(entry.getKey().toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseText parseText = new ParseText();
                    parseText.execute(hashMap.get(arrayList.get(position)));
                    try {
                        listView.setVisibility(View.GONE);
                        textView.setText(parseText.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed(){
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }


    class ParseText extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String str = "";

            try {
                Document document = Jsoup.connect(strings[0]).get();
                Elements elements = document.select("div[class=text]");
                Log.i(str,"fgds");
            }catch (IOException e){
                e.printStackTrace();
            }

            return str;

        }
    }
 class ParseTitle extends AsyncTask<Void, Void, HashMap<String,String>>
 {

     @Override
     protected HashMap<String, String> doInBackground(Void... voids) {
         HashMap<String, String> hashMap=new HashMap<>();
         try {
             Document document = Jsoup.connect("http://ktits.ru").get();
             Elements elements = document.select("div[class=news]").get(0).getElementsByTag("a");
             for(Element element:elements) {
                 hashMap.put(element.getElementsByClass("title").text(), element.attr("href"));
            }

         } catch (IOException e) {
             e.printStackTrace();
         }
         return hashMap;
     }
 }



}
