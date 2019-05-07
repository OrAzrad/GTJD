package com.example.gtjd;

public class Mission

{
    String email;
    String mission_title;
    String mission_hours;
    String mission_deadline;
    String mission_description;
    String mission_id;
    int mission_progress_hours;

    public Mission()
    {

    }

    public Mission(String email, String mission_title,
                    String mission_hours, String mission_deadline,
                    String mission_description,String mission_id)
    {
        this.email = email;
        this.mission_title = mission_title;
        this.mission_hours = mission_hours;
        this.mission_deadline = mission_deadline;
        this.mission_description = mission_description;
        this.mission_id = mission_id;
        this.mission_progress_hours = 0;
    }

    public String getEmail() {
        return email;
    }
    public String getMission_title() {
        return mission_title;
    }
    public String getMission_id() {
        return mission_id;
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

}
