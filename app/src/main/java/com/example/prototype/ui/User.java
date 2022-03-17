package com.example.prototype.ui;

public class User
{
    public String FullName,username,studentID,email,password;
    public User()
    {
    }
    public User(String FullName, String username, String studentID, String email, String password){
        this.FullName = FullName;
        this.username = username;
        this.studentID=studentID;
        this.email = email;
        this.password = password;
    }

}
