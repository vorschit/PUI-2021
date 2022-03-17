package com.example.prototype.ui;

import java.text.DateFormat;
import java.util.Date;

public class TracingHelperClass
{
    String Name, studentClass, Date,adminName;
    long timestamp;

    public TracingHelperClass(String Name, String studentClass,String Date,String adminName)
    {
        this.Name= Name;
        this.studentClass = studentClass;
        this.Date = Date;
        this.adminName =adminName;
    }
    public static String getTimeDate (long timestamp)
    {
        try
        {
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            java.util.Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        }
        catch (Exception e)
        {
            return "date";
        }
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getName() {
        return Name;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getDate() {
        return Date;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
