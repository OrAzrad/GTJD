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

public class MissionsList extends ArrayAdapter<Mission> {

    private Activity context;
    private List<Mission> missions_list;

    public MissionsList(Activity context, List<Mission> missions_list)
    {
        super(context, R.layout.missions_list, missions_list);
        this.context = context;
        this.missions_list = missions_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

       View ListViewIrem = inflater.inflate(R.layout.missions_list, null, true);

        TextView textViewTitle = (TextView) ListViewIrem.findViewById(R.id.textViewMissionTitle);
        TextView textViewSupTitle = (TextView) ListViewIrem.findViewById(R.id.textViewMissionSubTitle);

        Mission mission = missions_list.get(position);


        textViewTitle.setText(mission.getMission_title());
        textViewSupTitle.setText(mission.getMission_deadline());

        return ListViewIrem;

    }
}
