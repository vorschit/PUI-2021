package com.UniKL.PUITrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.MainActivity;
import com.example.prototype.R;
import com.example.prototype.ui.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //declaration
        final TextView UserName,class1,class2,class3,class4,class5;
        UserName = findViewById(R.id.tvuserName);
        class1 = findViewById(R.id.tvclass1);
        class2 = findViewById(R.id.tvclass2);
        class3 = findViewById(R.id.tvclass3);
        class4 = findViewById(R.id.tvclass4);
        class5 = findViewById(R.id.tvclass5);

        SharedPreferences settings = getSharedPreferences("UserInfo",0);
        String userEmail = settings.getString("Username","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Variable");
        DatabaseReference emailReference = reference.child("email");
        Query studentInformation = FirebaseDatabase.getInstance().getReference().child("Variable").orderByChild("email").equalTo(userEmail);
        studentInformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {

                    String UserNameDB = snapshot.child("Name").getValue().toString();
                    String UserClass01 = snapshot.child("class 1").getValue().toString();
                    String UserClass02 = snapshot.child("class 2").getValue().toString();
                    String UserClass03 = snapshot.child("class 3").getValue().toString();
                    String UserClass04 = snapshot.child("class 4").getValue().toString();
                    String UserClass05 = snapshot.child("class 5").getValue().toString();

                    UserName.setText(UserNameDB);
                    class1.setText(UserClass01);
                    class2.setText(UserClass02);
                    class3.setText(UserClass03);
                    class4.setText(UserClass04);
                    class5.setText(UserClass05);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(UserProfile.this, "Request Denied", Toast.LENGTH_SHORT).show();
            }
        });


    }
}