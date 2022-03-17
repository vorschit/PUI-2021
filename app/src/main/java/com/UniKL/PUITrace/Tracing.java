package com.UniKL.PUITrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Tracing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        //declaration
        final TextView studentName,TestView;
        Button traceBTN;
        traceBTN = findViewById(R.id.btnReport);
        studentName = findViewById(R.id.etName01);
        TestView = findViewById(R.id.tvTest);

        //button onClick
        traceBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Query traceStudent = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("studentID").equalTo(studentName.getText().toString());
                traceStudent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            //declaration studentclass
                            String class01 = snapshot.child(studentName.getText().toString()).child("class 1").getValue(String.class);
                            final String class02 = snapshot.child(studentName.getText().toString()).child("class 2").getValue(String.class);
                            String class03 = snapshot.child(studentName.getText().toString()).child("class 3").getValue(String.class);
                            String class04 = snapshot.child(studentName.getText().toString()).child("class 4").getValue(String.class);
                            String class05 = snapshot.child(studentName.getText().toString()).child("class 5").getValue(String.class);

                            //declare Query to be use to search
                            Query sc01 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("class 2").equalTo(class02);
                            sc01.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {

                                    String listName = snapshot.child("Name").toString();
                                    TestView.setText(listName);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {

                                }
                            });

                        }
                        else{
                            Toast.makeText(Tracing.this,"snapshot doesnt exist", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });


    }
}