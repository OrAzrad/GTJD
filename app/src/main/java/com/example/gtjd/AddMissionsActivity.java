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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private void AddMission(String mission_title, String mission_hours, String mission_deadline,
                            String mission_emails_amount, String mission_description)
    {
        //Insert new null value in databaseMissions AND get his key in the same action
        String id = databaseMissions.push().getKey();
        //Create a new Mission object by the values that user enter
        //Entries are drawn from a AddMissionScreen function and insert into this function
        Mission mission = new Mission(email, mission_title, mission_hours,
                mission_deadline, mission_description,mission_emails_amount, id, 0);
        //Push new mission into databaseMissions
        databaseMissions.child(id).setValue(mission);
        //Write a short update to User
        Toast.makeText(getApplicationContext(), "Mission Added", Toast.LENGTH_SHORT).show();
    }


    private void AddMissionScreen()
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
        final EditText mission_emails_amount = create_mission_view.findViewById(R.id.editTextMissionEmailsPerDay);


        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(AddMissionsActivity.this,
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            }
        });


        create_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mission_title_str = GetString(mission_title);
                String mission_hours_str = GetString(mission_hours);
                String mission_deadline_str = deadline;
                String mission_description_str = GetString(mission_description);
                String mission_emails_amount_str = GetString(mission_emails_amount);



                if(mission_title_str.length() == 0 ||
                        mission_hours_str.length() == 0  || deadline == null ||
                        mission_emails_amount_str.length() == 0 || mission_description_str.length() == 0)
                { Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show(); }
                else
                    {
                    if(mission_title_str.length() > 30 )
                    {  Toast.makeText(getApplicationContext(), "Title should be 30 chars at max", Toast.LENGTH_SHORT).show(); }
                    else{
                        deadline = null;
                        AddMission(mission_title_str, mission_hours_str, mission_deadline_str, mission_emails_amount_str, mission_description_str);
                        alertDialog.dismiss();
                    }}
            }
        });
    }
    //Function that delete mission from Firebase DB AddmissionsActivity
    private void DeleteMission(String mission_id)
    {
        //Goes into our Firebase DB and goes inside the table "AddMissionsActivity" and pick the Key title(id)
        // of the mission that suppose to be delete
        DatabaseReference get_mission_to_delete = FirebaseDatabase.getInstance().
                getReference("AddMissionsActivity").child(mission_id);
        //Delete mission By remove her Key Title
        get_mission_to_delete.removeValue();
        //write a short Update to User
        Toast.makeText(getApplicationContext(), "mission deleted", Toast.LENGTH_SHORT).show();
    }

    private void UpdateMissionProgressHours(Mission mission, int progress_hours)
    {

        String mission_id = mission.getMission_id();
        mission.addMission_progress_hours(progress_hours);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AddMissionsActivity").child(mission_id);


        databaseReference.setValue(mission);


        Toast.makeText(getApplicationContext(), "Mission Updated", Toast.LENGTH_SHORT).show();

    }

    private void MissionDataScreen(final Mission mission)
    {
        final AlertDialog.Builder mission_data_screen = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();


        final View mission_data_view = inflater.inflate(R.layout.activity__mission_data, null);
        mission_data_screen.setView(mission_data_view);

        final AlertDialog alertMissionData = mission_data_screen.create();
        alertMissionData.show();


        String mission_title = mission.getMission_title();
        String mission_amount_of_hours = mission.getMission_hours();
        String mission_deadline = mission.getMission_deadline();
        String mission_description = mission.getMission_description();
        int mission_emails_amount = mission.getMission_emails_amount();
        int mission_progress_hours_int = mission.getMission_progress_hours();

        final EditText enter_progress_hours = mission_data_view.findViewById(R.id.editTextMissionProgressHours);
        final Button update_progress_data = mission_data_view.findViewById(R.id.buttonUpdateProgressHours);
        final Button delete_mission  =  mission_data_view.findViewById(R.id.buttonDeleteMission);
        final Button update_mission = mission_data_view.findViewById(R.id.buttonUpdateMission);
        final TextView mission_set_title = mission_data_view.findViewById(R.id.textViewMissionSetTitle);
        final TextView mission_set_amount_of_hours = mission_data_view.findViewById(R.id.textViewMissionSetAmountOfHours);
        final TextView mission_set_deadline = mission_data_view.findViewById(R.id.textViewMissionSetDeadline);
        final TextView mission_set_description = mission_data_view.findViewById(R.id.textViewMissionSetDescription);
        final TextView mission_set_emails_amount = mission_data_view.findViewById(R.id.textViewMissionSetEmailsAmount);
        final TextView mission_set_progress_hours = mission_data_view.findViewById(R.id.textViewMissionSetProgressHoures);

        mission_set_title.setText(mission_title);
        mission_set_amount_of_hours.setText(mission_amount_of_hours);
        mission_set_deadline.setText(mission_deadline);
        mission_set_description.setText(mission_description);
        mission_set_emails_amount.setText(Integer.toString(mission_emails_amount));
        mission_set_progress_hours.setText(Integer.toString(mission_progress_hours_int));




        update_progress_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String progress_hours_str = GetString(enter_progress_hours);

                if (progress_hours_str.length() != 0)
                {
                    alertMissionData.dismiss();
                    int progress_hours = Integer.parseInt(progress_hours_str);
                    UpdateMissionProgressHours( mission, progress_hours);
                }
                else
                {
                    enter_progress_hours.setError("No value entered");
                    enter_progress_hours.requestFocus();
                }
            }
        });


        delete_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertMissionData.dismiss();

                DeleteMission(mission.getMission_id());
            }
        });

        update_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertMissionData.dismiss();
                UpdateDataScreen(mission);


            }
        });

    }

    private void UpdateMissionDeatails(final Mission mission,String mission_title, String mission_hours,
                                       String mission_deadline, String mission_emails_amount, String mission_description)
    {



        mission.setMission_title(mission_title);
        mission.setMission_hours(mission_hours);
        mission.setMission_deadline(mission_deadline);
        mission.setMission_emails_amount(Integer.parseInt(mission_emails_amount));
        mission.setMission_description(mission_description);

        String mission_id = mission.getMission_id();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AddMissionsActivity").child(mission_id);
        databaseReference.setValue(mission);
        Toast.makeText(getApplicationContext(), "Mission Updated", Toast.LENGTH_SHORT).show();


    }

    private void UpdateDataScreen(final Mission mission) {
        final AlertDialog.Builder mission_data_screen = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();


        final View mission_update_data_view = inflater.inflate(R.layout.activity_update_mission_data, null);
        mission_data_screen.setView(mission_update_data_view);

        final AlertDialog alertMissionData = mission_data_screen.create();
        alertMissionData.show();

        final EditText mission_title = mission_update_data_view.findViewById(R.id.editTextMissionName);
        mission_title.setText(mission.getMission_title());

        final EditText mission_amount_of_hours = mission_update_data_view.findViewById(R.id.editTextMissionHours);
        mission_amount_of_hours.setText(mission.getMission_hours());


        final TextView mission_deadline = mission_update_data_view.findViewById(R.id.textViewDeadline);



        final EditText mission_emails_amount = mission_update_data_view.findViewById(R.id.editTextMissionEmailsPerDay);
        mission_emails_amount.setText(Integer.toString(mission.getMission_emails_amount()));

        final EditText mission_description = mission_update_data_view.findViewById(R.id.editTextMissionDescription);
        mission_description.setText(mission.getMission_description());

        Button pick_date = mission_update_data_view.findViewById(R.id.buttonPickDate);
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            }
        });

        Button update_button = mission_update_data_view.findViewById(R.id.buttonUpdateMissionData);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mission_title_str = GetString(mission_title);
                String mission_hours_str = GetString(mission_amount_of_hours);
                String mission_deadline_str = deadline;
                String mission_description_str = GetString(mission_description);
                String mission_emails_amount_str = GetString(mission_emails_amount);



                if(mission_title_str.length() == 0 ||
                        mission_hours_str.length() == 0  || deadline == null ||
                        mission_emails_amount_str.length() == 0 || mission_description_str.length() == 0)
                { Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show(); }
                else
                {
                    if(mission_title_str.length() > 30 )
                    {  Toast.makeText(getApplicationContext(), "Title should be 30 chars at max", Toast.LENGTH_SHORT).show(); }
                    else
                    {

                        UpdateMissionDeatails(mission ,mission_title_str, mission_hours_str,
                                mission_deadline_str, mission_emails_amount_str, mission_description_str);

                        alertMissionData.dismiss();

                }}
            }
        });


    }



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
                MissionDataScreen(mission);
            }
        });


    }

    @Override
    protected void onStart() {
        //As soon as the screen opens
        super.onStart();
        //databaseMissions is variable that represents the DB in Firebase
        //Adds a built-in function that causes it to run in the loop for every row in DB
        databaseMissions.addValueEventListener(new ValueEventListener() {
            @Override
            //Once there is a change in the DB or app was opened
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                missions_list.clear();
                //For each mission in DB
                for(DataSnapshot mission_snapshot: dataSnapshot.getChildren()) {
                    //Get mission
                    Mission mission = mission_snapshot.getValue(Mission.class);

                    String check_deadline = mission.getMission_deadline();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date_to_check = null;
                    try {
                        date_to_check = sdf.parse(check_deadline);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date today = new Date();
                    if (!(date_to_check.compareTo(today) < 0) && (mission.getMission_progress_hours()) < Integer.parseInt(mission.getMission_hours()))
                    {
                        //Checks if the email address of the task feeder is the same as the user's email address
                        if (mission.getEmail().equals(email)) {
                            //Add mission to mission_list
                            missions_list.add(mission);
                        }}
                }
                //Insert user mission into a presentation of row in missions ListView
                MissionsList adapter = new MissionsList(AddMissionsActivity.this ,missions_list);
                //Add mission row to ListView top
                list_of_missions.setAdapter(adapter);
            }
            //Built-in function as part of using addValueEventListener
            //This function do nothing
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonAddMission:
                AddMissionScreen();
                break;

        }

    }
}
