package com.example.prototype;

import android.widget.TextView;

public class UserHelperClass {

    String name,studentID,username,password,Email;
    String Title, message;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String studentID, String username, String password, String email) {
        this.name = name;
        this.studentID = studentID;
        this.username = username;
        this.password = password;
        this.Email = email;
    }


    public UserHelperClass(String titlePost, String notiMessage) {
        this.Title = Title;
        this.message = message;
    }

    public void AdminMessageClass (String Title, String message){

        this.Title = Title;
        this.message = message;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
