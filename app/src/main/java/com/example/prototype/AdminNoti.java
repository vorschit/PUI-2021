package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class AdminNoti extends AppCompatActivity {
    private Button Posting, Testing;
    private TextView message, Title;
    private Map<String, String> timestamp;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_noti);

        Posting = findViewById(R.id.btnPost);
        message = findViewById(R.id.etMessage);
        Title = findViewById(R.id.etTitle);

        //get/set date
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        final String dateToStr = format.format(today);


        Posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Notification");

                if (!validateTitle() | !validateMessage()) {
                    return;
                }

                String titlePost = Title.getText().toString();
                String NotiMessage = message.getText().toString();

                //getting time&date using timestamp
                Map map = new HashMap();
                map.put("timestamp", ServerValue.TIMESTAMP);


                //upload to firebase
                AdminHelperClass helperClass = new AdminHelperClass(titlePost, NotiMessage, dateToStr.toString());
                reference.child(titlePost).setValue(helperClass);
                reference.child(titlePost).child("timestamp").updateChildren(map); //save date&time as Timestamp instead of string

                Toast.makeText(AdminNoti.this, "Message Posted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Boolean validateTitle() {
        String val = Title.getText().toString();
        if (val.isEmpty()) {
            Title.setError("Field is empty");
            return false;
        } else {
            Title.setError(null);
            return true;
        }
    }

    private Boolean validateMessage() {
        String val = message.getText().toString();
        if (val.isEmpty()) {
            message.setError("Field is empty");
            return false;
        } else {
            message.setError(null);
            return true;
        }
    }

}