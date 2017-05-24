package com.etuloser.padma.rohit.homework08;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rohit on 4/10/2017.
 */

public class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder>{


    Context context;
    ArrayList<Forecast> flist = new ArrayList<Forecast>();

    public ForecastRecycleAdapter(Context context,ArrayList<Forecast> flist) {
        this.context = context;
         this.flist=flist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.childgridlayout, parent, false);
        ForecastRecycleAdapter.ViewHolder cv = new ForecastRecycleAdapter.ViewHolder(v);
        return cv;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Forecast fc=flist.get(position);
        ForecastRecycleAdapter.ViewHolder item = (ForecastRecycleAdapter.ViewHolder)holder;
        String time=fc.getDate();
        time=time.substring(0,19);

        DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd, MMM ''yy ");
        Date date = null;
        try {
            date = originalFormat.parse(fc.getDate().substring(0,10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        item.date.setText(formattedDate);
        String imaurl=(URLS.ImageApi).replace("###",((fc.getDayimg().length()<2)?"0"+fc.getDayimg():fc.getDayimg()));
        Picasso.with(context).load(imaurl).into(item.image);

        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((CityActivity)(context)).display(position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return flist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        ImageView image;


        public ViewHolder(View v) {

            super(v);
            date = (TextView) v.findViewById(R.id.tempdate);
            image = (ImageView) v.findViewById(R.id.tempwimage);

        }
    }
}
