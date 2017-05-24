package com.etuloser.padma.rohit.homework08;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rohit on 4/8/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    Context context;
    ArrayList<CustomW> clist = new ArrayList<CustomW>();

    public CustomAdapter(Context context, ArrayList<CustomW> clist) {
        this.context = context;
        this.clist = clist;
    }


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.childrowlayout, parent, false);
        CustomAdapter.ViewHolder cv = new ViewHolder(v);

        return cv;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {


        ViewHolder item = (CustomAdapter.ViewHolder)holder;
        item.cname.setText(clist.get(position).getName().toUpperCase() +", "+ clist.get(position).getCoun().toUpperCase());
        item.temp.setText("Temperature:" +  clist.get(position).getTempC() + (char) 0x00B0 + "C");



        try {
            PrettyTime t = new PrettyTime();

            item.lupdate.setText("Updated " +
             t.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(clist.get(position).getLupdat().substring(0,19))));
        } catch (ParseException e) {
            e.printStackTrace();
        }


((ViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {

        ((MainActivity)(context)).ondeleteclick(position);

        return false;
    }
});




    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cname, temp,lupdate;
        ImageView fbtn;


        public ViewHolder(View v) {

            super(v);
            cname = (TextView) v.findViewById(R.id.tempcity);
            temp = (TextView) v.findViewById(R.id.tempdegree);
            lupdate = (TextView) v.findViewById(R.id.tempupdatedtime);
            fbtn = (ImageView) v.findViewById(R.id.btnfv);


        }
    }

    @Override
    public int getItemCount() {
        return this.clist.size();
    }
}
