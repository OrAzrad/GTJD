package com.example.gtjd;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class missions extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseMissions;
    String email;
    ListView list_of_missions;
    List<Mission> missions_list;

    public String GetString(EditText str){

        String str_to_return;
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    private void add_mission_screen()
    {
        AlertDialog.Builder create_mission_screen = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View create_mission_view = inflater.inflate(R.layout.add_mission, null);

        create_mission_screen.setView(create_mission_view);

        AlertDialog alertDialog = create_mission_screen.create();
        alertDialog.show();

        Button create_mission =  create_mission_view.findViewById(R.id.buttonCreateMission);
        final EditText mission_title =  create_mission_view.findViewById(R.id.editTextMissionName);
        final EditText mission_hours =  create_mission_view.findViewById(R.id.editTextMissionHours);
        final EditText mission_deadline = create_mission_view.findViewById(R.id.editTextMissionDeadline);
        final EditText mission_description = create_mission_view.findViewById(R.id.editTextMissionDescription);


        create_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mission_title_str = GetString(mission_title);
                String mission_hours_str = GetString(mission_hours);
                String mission_deadline_str = GetString(mission_deadline);
                String mission_description_str = GetString(mission_description);



                if(mission_title_str.length() == 0 ||
                        mission_hours_str.length() == 0 || mission_deadline_str.length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    AddMission(mission_title_str, mission_hours_str, mission_deadline_str, mission_description_str);
                    }
            }
        });



    }

    private void AddMission(String mission_title, String mission_hours, String mission_deadline, String mission_description)
    {
        String id = databaseMissions.push().getKey();
        Mission mission = new Mission(email, mission_title, mission_hours, mission_deadline, mission_description, id);
        databaseMissions.child(id).setValue(mission);

        Toast.makeText(getApplicationContext(), "Mission Added", Toast.LENGTH_SHORT).show();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        email = getIntent().getStringExtra("message_key");

        findViewById(R.id.buttonAddMission).setOnClickListener(this);
        databaseMissions = FirebaseDatabase.getInstance().getReference("missions");


        list_of_missions = (ListView) findViewById(R.id.listViewOfMissions);
        missions_list = new ArrayList<>();


        Toast.makeText(getApplicationContext(), "Hello: "+email, Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseMissions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                missions_list.clear();

                for(DataSnapshot mission_snapshot: dataSnapshot.getChildren())
                {
                    Mission mission = mission_snapshot.getValue(Mission.class);

                    if(mission.getEmail().equals(email))
                    {
                        missions_list.add(mission);
                    }
                }

                MissionsList adapter = new MissionsList(missions.this ,missions_list);
                list_of_missions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonAddMission:
                add_mission_screen();
                break;
        }

    }
}
