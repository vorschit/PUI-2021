package com.UniKL.PUITrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.prototype.R;
import com.firebase.client.core.Repo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ListView reportListView = findViewById(R.id.genReportListView);
        final ArrayList<String> myArrayList = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, myArrayList);
        reportListView.setAdapter(adapter);

        //getting admin's name
        final SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String nameOfAdmin = settings.getString("Username", "");
        //database query
        final DatabaseReference report = FirebaseDatabase.getInstance().getReference("TraceRecord").child(nameOfAdmin);


        //query addvalueEventListener
        report.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    myArrayList.clear();
                    for (DataSnapshot reportSnapshot : snapshot.getChildren())
                    {
                        String adminName = reportSnapshot.getValue().toString();
                        myArrayList.add(adminName);
                        /*
                        String adminName = snapshot.child("adminName").getValue().toString();
                        String date = reportSnapshot.child("date").getValue().toString();
                        String studentClass = reportSnapshot.child("studentClass").getValue().toString();
                        myArrayList.add("(" + adminName + ")\n" + "\nDate :\n" + date + "\nClass :\n" + studentClass);

                         */
                    }


                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(Report.this,"no datasnapshot",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}