package com.example.gtjd;

public class Mission

{
    String email;
    String misiion_title;
    String mission_hours;
    String mission_deadline;
    String mission_description;
    String id;

    public Mission()
    {

    }

    public Mission(String email, String misiion_title,
                    String mission_hours, String mission_deadline,
                    String mission_description,String id)
    {
        this.email = email;
        this.misiion_title = misiion_title;
        this.mission_hours = mission_hours;
        this.mission_deadline = mission_deadline;
        this.mission_description = mission_description;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public String getMisiion_title() {
        return misiion_title;
    }

    public String getMission_hours() {
        return mission_hours;
    }

    public String getMission_deadline() {
        return mission_deadline;
    }

    public String getMission_description() {
        return mission_description;
    }

    public String getMissionId() {
        return id;
    }
}
