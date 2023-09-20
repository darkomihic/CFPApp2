package com.example.cfpapp.klase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Workout {


    private Date date;
    private String workoutname;
    private String userid;
    private Boolean done;

    // Constructors
    public Workout() {
        // Default constructor required for Firestore or other data storage.
    }

    public Workout(Date date, String workoutname) {
        this.date = date;
        this.workoutname = workoutname;
    }

    public Workout(Date date, String workoutname, String userid, Boolean done) {
        this.date = date;
        this.workoutname = workoutname;
        this.userid = userid;
        this.done = done;
    }

    public Date getDate() {
        return date;
    }

    public String getWorkoutname() {
        return workoutname;
    }

    public String getUserid() {
        return userid;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWorkoutname(String workoutname) {
        this.workoutname = workoutname;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
