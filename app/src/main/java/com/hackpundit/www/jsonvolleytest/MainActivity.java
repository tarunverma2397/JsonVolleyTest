package com.hackpundit.www.jsonvolleytest;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.util.zip.Inflater;

public class MainActivity extends Activity {

    TextView output ;
    String loginURL="http://www.airworldservice.org/english/wp-json/wp/v2/posts?filter[category_name]=top-national-news-daily";
    String data = "";


    RequestQueue requestQueue;
    ListView l;
    String title,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            Log.d("STATE",savedInstanceState.toString());
        }

        requestQueue = Volley.newRequestQueue(this);
        output = (TextView) findViewById(R.id.jsondata);
        StringRequest jor= new StringRequest(Request.Method.GET, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String request) {
                String response=stripHtml(request);
                try {
                    JSONArray new_array = new JSONArray(response);
                  for(int i=0;i<8;i++) {
                      JSONObject baseObject = new_array.getJSONObject(1);
                      JSONObject titleObject = baseObject.getJSONObject("title");
                       title= titleObject.getString("rendered");
                      JSONObject contentObject = baseObject.getJSONObject("content");
                      content = contentObject.getString("rendered");


                      output.setText(title + " " + content);
                  }
                }
                catch(JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jor);
       /* l= (ListView) findViewById(R.id.listView);
        cusadapter adapt=new cusadapter(this,title,content);
        l.setAdapter(adapt);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }
    class cusadapter extends ArrayAdapter<String>
    {
        Context context;
        String[] title,content;
        cusadapter(Context c,String title[],String content[])
        {
            super(c,R.layout.cus_view,R.id.textView,title);
            this.context=c;
            this.title=title;
            this.content=content;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate(R.layout.cus_view,parent,false);
            TextView head= (TextView) row.findViewById(R.id.textView);
            TextView conten= (TextView) row.findViewById(R.id.textView2);
            head.setText(title[position]);
            conten.setText(content[position]);



            return row;
        }
    }
}
