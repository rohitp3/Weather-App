package com.etuloser.padma.rohit.homework08;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity  {

    SharedPreferences sharedpreferences;
    public static final String CPreference = "Location";
    //public static final String CPrefCountry = "country";
    //public static final String CPrefLocation = "Location";
    OkHttpClient client ;
    FirebaseDatabase db;
    DatabaseReference dref;
ArrayList<CustomW> savedcities=new ArrayList<CustomW>();
    RecyclerView rv;
    LinearLayout lli,llf;
    ProgressBar pd;
    private static final int setflag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lli=(LinearLayout)findViewById(R.id.lli);
        llf=(LinearLayout)findViewById(R.id.llf);
        pd=(ProgressBar)findViewById(R.id.pg1);

        sharedpreferences = getSharedPreferences(CPreference, Context.MODE_PRIVATE);

        if(sharedpreferences.getString("key","")!="" && sharedpreferences.getString("key","")!=null){
             getcurrentcitydata();
            try {
                display();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        db = FirebaseDatabase.getInstance();
        dref = db.getReference().child("Cities");
        final CustomAdapter adaptor = new CustomAdapter(this,savedcities);
        rv = (RecyclerView)findViewById(R.id.rvlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adaptor);



        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    CustomW co  = dataSnapshot.getValue(CustomW.class);
                    savedcities.add(co);
                    adaptor.notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                int count = 0;
                try {
                    CustomW co = dataSnapshot.getValue(CustomW.class);

                    for(CustomW city : savedcities)
                    {
                        if(city.getName().contains(co.getName()))
                        {
                            savedcities.set(count,co);
                            adaptor.notifyDataSetChanged();


                        }
                        count++;

                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void display() throws ParseException {

        PrettyTime pt=new PrettyTime();
        String CityNCountry=getSharedPreferences(CPreference,MODE_PRIVATE).getString("city","")+", "+getSharedPreferences(CPreference,MODE_PRIVATE).getString("country","");
        String ctext=getSharedPreferences(CPreference,MODE_PRIVATE).getString("text","");
        String temp="Temperature : "+getSharedPreferences(CPreference,MODE_PRIVATE).getString("tempcv","")+(char) 0x00B0+ "C";

        String time=getSharedPreferences(CPreference,MODE_PRIVATE).getString("time","");
        time=time.substring(0,19);

        String image=getSharedPreferences(CPreference,MODE_PRIVATE).getString("image","");
        ImageView iv=(ImageView)findViewById(R.id.img_climate);

        if(image.length()<2)
        {
            image="0"+image;
        }

        String imaurl=(URLS.ImageApi).replace("###",image);
        Picasso.with(this).load(imaurl).into(iv);

        ((TextView)findViewById(R.id.txtcitycountry)).setText(CityNCountry);
        ((TextView)findViewById(R.id.txtclimatetype)).setText(ctext);
        ((TextView)findViewById(R.id.txttemperature)).setText(temp);
        try {
            ((TextView) findViewById(R.id.txtlastupdated)).setText("Updated "+pt.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(time)));
        }
        catch (Exception e)
        {

        }
        pd.setVisibility(View.INVISIBLE);
        lli.setVisibility(View.GONE);

        llf.setVisibility(View.VISIBLE);

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

            Intent i = new Intent(this, PrefActivity.class);
            startActivityForResult(i, setflag);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==setflag)
        {
           // displayUserSettings();
        }

    }



    public void OnclickSetCurrentCity(View v)
    {

        final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setView(R.layout.alertview);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btncancel = (Button)dialog.findViewById(R.id.btnacancel);
        Button btnset = (Button)dialog.findViewById(R.id.btnaset);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
        final EditText edxcity=(EditText)dialog.findViewById(R.id.edxacity);
        final EditText edxcountry=(EditText)dialog.findViewById(R.id.edxacountry);

        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getlocationinfo(edxcity.getText().toString(),edxcountry.getText().toString());

              if(getSharedPreferences(CPreference,MODE_PRIVATE).getString("key","").length()>0)
                   {
                    showToast(1);
                    dialog.hide();
                    pd.setVisibility(View.VISIBLE);
                try {
                    display();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                   }
               else
                {
                    showToast(2);
                    dialog.hide();
                }


            }
        });



    }

    public void showToast(int flag)
    {
        if(flag==1)
        {
            Toast.makeText(this,"Current City details saved",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this,"City not found",Toast.LENGTH_SHORT).show();

        }
    }

    public void getlocationinfo(String city,String country)
    {
        final String c=city;
        final String cou=country;
        final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();
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
                String locationid="";

                try {


                    JSONArray qarra = new JSONArray(response.body().string());
                    locationid=qarra.getJSONObject(0).getString("Key");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                editor.putString("city",c);
                editor.putString("country",cou);
                editor.putString("key",locationid);
                editor.commit();

                getcurrentcitydata();

            }
        });


    }

    public void getcurrentcitydata()
    {

        String key=getSharedPreferences(CPreference,MODE_PRIVATE).getString("key","");
        final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();
        final String url=URLS.BaseUrl+URLS.CFCCApi+key+URLS.KeyApi;

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
                String LocalObservationDateTime="",WeatherText="",WeatherIcon="",tempcval="",tempfval="";

                try {
                    JSONArray qarra = new JSONArray(response.body().string());
                    LocalObservationDateTime=qarra.getJSONObject(0).getString("LocalObservationDateTime");
                    WeatherText=qarra.getJSONObject(0).getString("WeatherText");
                    WeatherIcon=qarra.getJSONObject(0).getString("WeatherIcon");
                    tempcval=qarra.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getString("Value");
                    tempfval=qarra.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Value");




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                editor.putString("time",LocalObservationDateTime);
                editor.putString("text",WeatherText);
                editor.putString("image",WeatherIcon);
                editor.putString("tempcv",tempcval);
                editor.putString("tempfv",tempfval);
                editor.commit();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            display();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }); }
        });



    }


    public void SearchClick(View v)
    {

        Intent i=new Intent(this,CityActivity.class);
        i.putExtra("scity",((EditText)findViewById(R.id.edxcity)).getText().toString());
        i.putExtra("scoun",((EditText)findViewById(R.id.edxcountry)).getText().toString());
        startActivity(i);


    }


    public void ondeleteclick(int position)
    {

    }
}
