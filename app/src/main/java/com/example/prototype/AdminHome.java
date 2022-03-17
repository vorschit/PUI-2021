package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.UniKL.PUITrace.AdminUserInformation;
import com.UniKL.PUITrace.Report;
import com.UniKL.PUITrace.Tracing;

public class AdminHome extends AppCompatActivity {

    private ImageButton noti,updateNoti,Search,Reportbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        noti = findViewById(R.id.btnPost);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // setContentView(R.layout.activity_admin_noti);
                Intent intToPost = new Intent(AdminHome.this, AdminNoti.class );
                startActivity(intToPost);
            }
        });

        updateNoti = findViewById(R.id.btnUpdate);
        updateNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inToUpdate = new Intent(AdminHome.this,AdminUpdateNotif.class);
                startActivity(inToUpdate);
            }
        });

        Search = findViewById(R.id.btnSearch);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(AdminHome.this,AdminUserInformation.class);
                startActivity(search);
            }
        });
        Reportbtn = findViewById(R.id.btnReport);
        Reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent report = new Intent(AdminHome.this, Report.class);
                startActivity(report);
            }
        });


    }
}