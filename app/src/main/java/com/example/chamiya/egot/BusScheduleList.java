package com.example.chamiya.egot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chamiya.egot.models.BusSchedule;

import java.util.List;

/**
 * Created by Chamiya on 2/25/2018.
 */

public class BusScheduleList extends ArrayAdapter<BusSchedule> {

    private Activity context;
    private List<BusSchedule> busScheduleList;

    public BusScheduleList(Activity context, List<BusSchedule> busScheduleList){
        super(context, R.layout.activity_shedule_view, busScheduleList);
        this.context = context;
        this.busScheduleList = busScheduleList;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_shedule_view, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRouteNo = (TextView) listViewItem.findViewById(R.id.textViewRouteNo);

        BusSchedule busSchedule = busScheduleList.get(position);

        textViewName.setText("From : "+busSchedule.getFrom()+" To : "+busSchedule.getTo());
        textViewRouteNo.setText(busSchedule.getRouteNo());

        return listViewItem;
    }

}
