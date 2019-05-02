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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddMissionsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddMissionsActivity";
    private static String date;

    private DatePickerDialog.OnDateSetListener date_set_listener;
    DatabaseReference databaseMissions;
    String email;
    ListView list_of_missions;
    List<Mission> missions_list;

    static String deadline;

    public String GetString(EditText str){

        String str_to_return;
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    private void AddMission(String mission_title, String mission_hours, String mission_deadline, String mission_description)
    {
        String id = databaseMissions.push().getKey();
        Mission mission = new Mission(email, mission_title, mission_hours, mission_deadline, mission_description, id);
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
        final EditText mission_title =  create_mission_view.findViewById(R.id.editTextMissionName);
        final EditText mission_hours =  create_mission_view.findViewById(R.id.editTextMissionHours);
        final EditText mission_description = create_mission_view.findViewById(R.id.editTextMissionDescription);



        //"final TextView display_Date = create_mission_view.findViewById(R.id.textViewEnterDeadline);
        //display_Date.setOnClickListener(new View.OnClickListener() {
          //  public void onClick(View v) {
             //   deadline = pick_date(display_Date);
         //   }
        //});

        create_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mission_title_str = GetString(mission_title);
                String mission_hours_str = GetString(mission_hours);
                String mission_deadline_str = "22-4-2020";
                String mission_description_str = GetString(mission_description);



                if(mission_title_str.length() == 0 ||
                        mission_hours_str.length() == 0 || mission_deadline_str.length() == 0 )
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

    private String pick_date(final TextView display_date)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date_dialog = new DatePickerDialog(
                AddMissionsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                date_set_listener,
                year,month,day);

        date_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        date_dialog.show();

        date_set_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month+1;
                Log.d("add_mission", "On Date set: dd/mm/yy: " + dayOfMonth+"/"+ month+"/"+year);
                date = dayOfMonth+"/"+ month+"/"+year;
                display_date.setText(date);

            }
        };


        return date;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        email = getIntent().getStringExtra("message_key");

        findViewById(R.id.buttonAddMission).setOnClickListener(this);
        databaseMissions = FirebaseDatabase.getInstance().getReference("AddMissionsActivity");

        list_of_missions = (ListView) findViewById(R.id.listViewOfMissions);
        missions_list = new ArrayList<>();


        Toast.makeText(getApplicationContext(), "Hello: "+email, Toast.LENGTH_SHORT).show();

        list_of_missions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mission mission = missions_list.get(position);


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
