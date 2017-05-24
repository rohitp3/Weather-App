package com.etuloser.padma.rohit.homework08;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrefActivity extends PreferenceActivity {

    SharedPreferences preferences;
    public static final String CPreference = "Location";
    OkHttpClient client;
    Button btnset;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferencelayout);


        Preference rreference = (Preference) findPreference("preftemptype");
        rreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {


            @Override
            public boolean onPreferenceClick(Preference preference) {

                Tempclick();
                return true;


            }
        });
        Preference rreference1 = (Preference) findPreference("prefCurrentCity");
        rreference1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {


            @Override
            public boolean onPreferenceClick(Preference preference) {

                currentcityclick();
                return true;


            }
        });


    }


    public void Tempclick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.tempalert);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btndone = (Button) dialog.findViewById(R.id.btndonetemp);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.rggroup);
        if(getSharedPreferences(CPreference,MODE_PRIVATE).getString("TempType","").equals("") || getSharedPreferences(CPreference,MODE_PRIVATE).getString("TempType","").equals("1"))
        {
            final RadioButton rgc=(RadioButton)dialog.findViewById(R.id.radioc);

            rgc.setChecked(true);

        }
        else
        {  final RadioButton rgf=(RadioButton)dialog.findViewById(R.id.radiof);

            rgf.setChecked(true);

        }


        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();

                if (rg.getCheckedRadioButtonId() == R.id.radioc) {

                    editor.putString("TempType","1");
                    editor.commit();
                    Toast.makeText(v.getContext(), "Temperature Unit has been changed to" + (char) 0x00B0 + "C", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("TempType","2");
                    editor.commit();
                    Toast.makeText(v.getContext(), "Temperature Unit has been changed to" + (char) 0x00B0 + "F", Toast.LENGTH_SHORT).show();
                }

                dialog.hide();
            }
        });


    }

    public void currentcityclick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.alertview);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText edtcity = (EditText) dialog.findViewById(R.id.edxacity);
        final EditText edtcountry = (EditText) dialog.findViewById(R.id.edxacountry);
        Button btncancel = (Button) dialog.findViewById(R.id.btnacancel);
         btnset = (Button) dialog.findViewById(R.id.btnaset);

        if (getSharedPreferences(CPreference, MODE_PRIVATE).getString("city", "") != null
                && getSharedPreferences(CPreference, MODE_PRIVATE).getString("city", "") != "") {
            edtcity.setText(getSharedPreferences(CPreference, MODE_PRIVATE).getString("city", ""));
            edtcountry.setText(getSharedPreferences(CPreference, MODE_PRIVATE).getString("country", ""));
            btnset.setText("Update");
        }


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.hide();
            }
        });


        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getlocationinfo(edtcity.getText().toString(),edtcountry.getText().toString());

                dialog.hide();

            }
        });

    }

    public void setflag(int i)
    {
        if(i==1)
        {

            if(btnset.getText().equals("Update"))
            {
                Toast.makeText(this,"Current City is Updated",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(this,"Current City is Added",Toast.LENGTH_SHORT).show();
            }


        }
        else {
            Toast.makeText(this, "No City found", Toast.LENGTH_SHORT).show();
        flag=2;
        }
    }

    public void  getlocationinfo(String city, String country) {
         final String c = city;
        final String cou = country;
        final SharedPreferences.Editor editor = getSharedPreferences(CPreference, MODE_PRIVATE).edit();
        final String url = URLS.BaseUrl + URLS.LocationApi + cou + "/search" + URLS.KeyApi + "&q=" + c;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                 String locationid = "";

                try {
                    JSONArray qarra = new JSONArray(response.body().string());
                    locationid = qarra.getJSONObject(0).getString("Key");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(locationid.length()>0) {
                    editor.remove("city");
                    editor.remove("country");
                    editor.remove("key");
                    editor.putString("city", c);
                    editor.putString("country", cou);
                    editor.putString("key", locationid);
                    editor.commit();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                                setflag(1);

                        }

                    });


                }
                     else
                {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setflag(2);
                        }

                    });

                }



            }
        });

    }

}