package com.etuloser.padma.rohit.homework08;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity {

    ProgressDialog pd;
    String city,Country;
    OkHttpClient client;
LinearLayout li;

    TextView txtitleid;
    TextView txHeadc;
    TextView txforecast;
    TextView txtempc;
    ImageView imgdayimg;
    ImageView imgnightimg;
    TextView txctxtday;
    TextView txctxtnight;
    TextView txttexturl1;
    TextView txttexturl2;
    Forecast fc;
    String url1,url2;
    private DatabaseReference mData;
    SharedPreferences sharedpreferences;
    public static final String CPreference = "Location";



    ArrayList<Forecast> fclist=new ArrayList<Forecast>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
li=(LinearLayout)findViewById(R.id.mainlayout);
        li.setVisibility(View.GONE);
        pd=new ProgressDialog(this);
        pd.setTitle("Loading Data");


        txtitleid =(TextView)findViewById(R.id.titleid);
        txHeadc =(TextView)findViewById(R.id.Headc);
        txforecast=(TextView)findViewById(R.id.forecast);
        txtempc=(TextView)findViewById(R.id.tempc);
        imgdayimg=(ImageView)findViewById(R.id.dayimg);
        imgnightimg=(ImageView) findViewById(R.id.nightimg);
        txctxtday=(TextView)findViewById(R.id.ctxtday);
        txctxtnight=(TextView)findViewById(R.id.ctxtnight);
        txttexturl1=(TextView)findViewById(R.id.texturl1);
        txttexturl2=(TextView)findViewById(R.id.texturl2);


        mData = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dref = mData.child("Cities");



        if(getIntent().getExtras()!=null)
        {

            city=getIntent().getExtras().getString("scity");
            Country=getIntent().getExtras().getString("scoun");
           // pd.show();

            Log.d("step1","In intent");
            getData(city,Country);
        }



    }

    public void getData(String city,String country)
    {
            final String c=city;
            final String cou=country;
          //  final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();
            final String url=URLS.BaseUrl+URLS.LocationApi+cou+"/search"+URLS.KeyApi+"&q="+c;
            client=new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    try {
                        JSONArray qarra = new JSONArray(response.body().string());
                        final String locationid=qarra.getJSONObject(0).getString("Key");

                        if(locationid.length()>0) {
                                    getcurrentcitydata(locationid);


                        }
                        else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                              pd.hide();
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                     //               Toast.makeText(getApplicationContext(),"No city found",Toast.LENGTH_SHORT).show();
                                }

                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                   // editor.putString("city",c);
                   // editor.putString("country",cou);
                   // editor.putString("key",locationid);
                   // editor.commit();

                   // getcurrentcitydata();

                }
            });


        }

    public void getcurrentcitydata(String key)
    {


        final String loc=key;

Log.d("key",loc);


        String url = URLS.BaseUrl+URLS.FivedayFFSCApi+key+URLS.KeyApi;
        Log.d("url",url);
        client=new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try {


                    Log.d("Response",response.body().toString());
                    Log.d("JSONOBJECT",response.toString());

                    JSONObject qarra = new JSONObject(response.body().string());

                    Log.d("name",qarra.getJSONObject("Headline").getString("Text"));

                    Forecast fc;


                    for(int i=0;i<5;i++){


                        fc=new Forecast();
                         fc.setLocationId(loc);
                        fc.setHeadlines( qarra.getJSONObject("Headline").getString("Text").toString());
                        fc.setCity(city);
                        fc.setCountry(Country);
                        fc.setDate(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getString("Date").toString());
                        fc.setMintemp(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum").getString("Value"));
                        fc.setMaxtemp(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum").getString("Value"));
                        fc.setDayimg(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Day").getString("Icon"));
                        fc.setNightimg(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Night").getString("Icon"));
                        fc.setDayphrase(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Day").getString("IconPhrase"));
                        fc.setNightphrase(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getJSONObject("Night").getString("IconPhrase"));
                        fc.setFirsturl(qarra.getJSONArray("DailyForecasts").getJSONObject(i).getString("MobileLink"));
                        fc.setSecondurl(qarra.getJSONObject("Headline").getString("MobileLink"));

                        fclist.add(fc);
                        fc=null;

                    }


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Log.d("Indispaly","Indisplay");
                            try {
                                display(0);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });





                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
            });

            }


            public void display(int i) throws ParseException {

                li.setVisibility(View.VISIBLE);
                pd=new ProgressDialog(this);
                pd.setTitle("Loading Data");


                 fc=fclist.get(i);

                txtitleid.setText("Daily forecast for "+fc.getCity()+","+fc.getCountry());
                txHeadc.setText(fc.getHeadlines());

                DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

                Date date = originalFormat.parse(fc.getDate().substring(0,10));

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                txforecast.setText(c.get(Calendar.MONTH) + c.get(Calendar.DATE)+"," + c.get(Calendar.YEAR));


                //txforecast.setText(fc.getDate());
                        txtempc.setText("Temperature:"+fc.getMaxtemp()+(char) 0x00B0+"/"+fc.getMintemp()+(char) 0x00B0);
                String imaurl=(URLS.ImageApi).replace("###",((fc.getDayimg().length()<2)?"0"+fc.getDayimg():fc.getDayimg()));
                Picasso.with(this).load(imaurl).into(imgdayimg);
                imaurl=(URLS.ImageApi).replace("###",((fc.getNightimg().length()<2)?"0"+fc.getNightimg():fc.getNightimg()));
                Picasso.with(this).load(imaurl).into(imgnightimg);
                txctxtday.setText(fc.getDayphrase());
                txctxtnight.setText(fc.getNightphrase());
               // txttexturl1.setTag(fc.getFirsturl());
               // txttexturl2.setTag(fc.getSecondurl());
                url1=fc.getFirsturl();
                url2=fc.getSecondurl();




                final ForecastRecycleAdapter adaptor = new ForecastRecycleAdapter(this,fclist);
                RecyclerView rv = (RecyclerView)findViewById(R.id.rvforecast);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rv.setLayoutManager(linearLayoutManager);
                rv.setAdapter(adaptor);






            }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.city_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.Save_city: {

 if(fc!=null)
 {
     mData = FirebaseDatabase.getInstance().getReference();
     DatabaseReference dref = mData.child("Cities").child(fc.getLocationId());
     dref.setValue(fc);
 }


            }
            break;
            case R.id.Set_Current: {
                final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();

                int flag= getSharedPreferences(CPreference,MODE_PRIVATE).getString("key","").toString().length()>0 ?1:0;
                if(fc.getLocationId().length()>0)
                {

                    editor.remove("key");
                    editor.remove("city");
                    editor.remove("country");
                    editor.putString("key",fc.getLocationId());
                    editor.putString("city",fc.getCity());
                    editor.putString("country",fc.getCountry());
                    editor.commit();

                    if(flag==1) {
                        Toast.makeText(this, "Current City Updated",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this,"Current City saved",Toast.LENGTH_SHORT).show();
                    }

                    }


            }
            break;
            case R.id.Settings:{


                Intent i=new Intent(this,PrefActivity.class);
                startActivity(i);

            }
            break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }



    public void text2click(View v)
    {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url2));
        startActivity(browserIntent);


    }


    public void text1click(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url1));
        startActivity(browserIntent);

    }


}
