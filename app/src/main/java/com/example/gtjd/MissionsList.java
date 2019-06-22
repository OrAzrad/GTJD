package com.example.gtjd;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


//class that Responsible for each mission view in the list view in AddMissionsActivity class
public class MissionsList extends ArrayAdapter<Mission> {

    private Activity context;

    //create object of mission list
    private List<Mission> missions_list;

    public MissionsList(Activity context, List<Mission> missions_list)
    {
        //attach this Activity with his layout file
        super(context, R.layout.missions_list, missions_list);
        this.context = context;
        this.missions_list = missions_list;
    }

    @NonNull
    @Override
    //create the view of each row of mission object in list view
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //the object that will attach the layout file (XML file) to the pop screen
        LayoutInflater mission_list_item = context.getLayoutInflater();
        View ListViewItem = mission_list_item.inflate(R.layout.missions_list, null, true);

        //show mission top 2 details in The presentation of the mission in the list view
        TextView textViewTitle =  ListViewItem.findViewById(R.id.textViewMissionTitle);
        TextView textViewSupTitle = ListViewItem.findViewById(R.id.textViewMissionSubTitle);

        //get each specific mission from mission list by position
        Mission mission = missions_list.get(position);

        //set mission titles
        textViewTitle.setText(mission.getMission_title());
        textViewSupTitle.setText(mission.getMission_deadline());

        return ListViewItem;

    }
}
