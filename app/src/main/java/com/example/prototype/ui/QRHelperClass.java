package com.example.prototype.ui;

public class QRHelperClass {
    String name,studentID,Date;
    long millis;

    public QRHelperClass(String name, String studentID, String Date, long millis){
        this.name = name;
        this.studentID = studentID;
        this.Date = Date;
        this.millis=millis;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
