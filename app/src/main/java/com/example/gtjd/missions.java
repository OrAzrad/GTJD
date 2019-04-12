package com.example.gtjd;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class missions extends AppCompatActivity implements View.OnClickListener {


    private void add_mission()
    {
        AlertDialog.Builder create_mission = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View create_mission_view = inflater.inflate(R.layout.add_mission, null);

        create_mission.setView(create_mission_view);

        AlertDialog alertDialog = create_mission.create();
        alertDialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        findViewById(R.id.buttonAddMission).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonAddMission:
                add_mission();
                break;
        }

    }
}
