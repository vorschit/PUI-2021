package com.example.prototype;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class AdminHelperClass
{
    String Title, message, Date, Password;
    long timestamp;

    public AdminHelperClass(String Title, String message,String Date)
    {
        this.Title= Title;
        this.message = message;
        this.Date = Date;
    }

    public static String getTimeDate (long timestamp){
        try{
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            java.util.Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        }catch (Exception e){
            return "date";
        }
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
