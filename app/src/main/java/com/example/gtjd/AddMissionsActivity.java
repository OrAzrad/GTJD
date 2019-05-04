package com.example.gtjd;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddMissionsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddMissionsActivity";

    DatabaseReference databaseMissions;
    String email;
    ListView list_of_missions;
    List<Mission> missions_list;

    private String deadline;
    Calendar calendar;



    public String GetString(EditText str){

        String str_to_return;
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    private void AddMission(String mission_title, String mission_hours, String mission_deadline, String mission_description)
    {
        String id = databaseMissions.push().getKey();
        Mission mission = new Mission(email, mission_title, mission_hours,
                mission_deadline, mission_description, id);
        databaseMissions.child(id).setValue(mission);

        Toast.makeText(getApplicationContext(), "Mission Added", Toast.LENGTH_SHORT).show();
    }


    private void add_mission_screen()
    {
        AlertDialog.Builder create_mission_screen = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View create_mission_view = inflater.inflate(R.layout.add_mission, null);

        create_mission_screen.setView(create_mission_view);

        final AlertDialog alertDialog = create_mission_screen.create();
        alertDialog.show();

        final Button create_mission =  create_mission_view.findViewById(R.id.buttonCreateMission);
        final Button pick_date = create_mission_view.findViewById(R.id.buttonPickDate);
        final EditText mission_title =  create_mission_view.findViewById(R.id.editTextMissionName);
        final EditText mission_hours =  create_mission_view.findViewById(R.id.editTextMissionHours);
        final EditText mission_description = create_mission_view.findViewById(R.id.editTextMissionDescription);
        final TextView mission_deadline = create_mission_view.findViewById(R.id.textViewDeadline);


        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMissionsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                monthOfYear = monthOfYear+1;
                                deadline =  dayOfMonth + "/" + monthOfYear + "/" + year;
                                mission_deadline.setText("Selected date: " + deadline);


                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });


        create_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mission_title_str = GetString(mission_title);
                String mission_hours_str = GetString(mission_hours);
                String mission_deadline_str = deadline;
                String mission_description_str = GetString(mission_description);



                if(mission_title_str.length() == 0 ||
                        mission_hours_str.length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    AddMission(mission_title_str, mission_hours_str, mission_deadline_str, mission_description_str);
                    alertDialog.dismiss();
                    }
            }
        });
    }

    private void delete_mission(String mission_id)
    {
        DatabaseReference delete_mission = FirebaseDatabase.getInstance().getReference("AddMissionsActivity").child(mission_id);
        delete_mission.removeValue();

        Toast.makeText(getApplicationContext(), "mission deleted", Toast.LENGTH_SHORT).show();

    }
    private void mission_data_screen(final Mission mission)
    {
        final AlertDialog.Builder mission_data_screen = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();


        final View mission_data_view = inflater.inflate(R.layout.activity__mission_data, null);
        mission_data_screen.setView(mission_data_view);

        final AlertDialog alertMissionData = mission_data_screen.create();
        alertMissionData.show();

        Toast.makeText(getApplicationContext(), mission.getMission_id(), Toast.LENGTH_SHORT).show();


        final Button delete_mission  =  mission_data_view.findViewById(R.id.buttonDeleteMission);

        delete_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertMissionData.dismiss();

                delete_mission(mission.getMission_id());
            }
        });}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        email = getIntent().getStringExtra("message_key");

        findViewById(R.id.buttonAddMission).setOnClickListener(this);
        databaseMissions = FirebaseDatabase.getInstance().getReference("AddMissionsActivity");

        list_of_missions = findViewById(R.id.listViewOfMissions);
        missions_list = new ArrayList<>();


        Toast.makeText(getApplicationContext(), "Hello: "+email, Toast.LENGTH_SHORT).show();

        list_of_missions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Mission mission = missions_list.get(position);
                mission_data_screen(mission);
            }
        });


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

                MissionsList adapter = new MissionsList(AddMissionsActivity.this ,missions_list);
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
