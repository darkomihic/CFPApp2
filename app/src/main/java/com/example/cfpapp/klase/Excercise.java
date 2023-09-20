package com.example.cfpapp.klase;

public class Excercise {

    private String excersizename;

    private String type;

    public Excercise() {
    }

    public Excercise(String excersizename, String type) {
        this.excersizename = excersizename;
        this.type = type;
    }

    public String getExcersizename() {
        return excersizename;
    }

    public String getType() {
        return type;
    }

    public void setExcersizename(String excersizename) {
        this.excersizename = excersizename;
    }

    public void setType(String type) {
        this.type = type;
    }
}
