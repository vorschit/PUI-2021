package com.example.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.Module;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class AdminUpdateNotif extends AppCompatActivity {
    ArrayList<String> myArrayList = new ArrayList<>();
    ListView myListView;
    TextView TitleInput, UpdateTitle,UpdateMessage;
    Button Select,Update;
    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_notif);

        TitleInput = findViewById(R.id.tvInputTitle);
        UpdateTitle = findViewById(R.id.tvUpdateTitle);
        UpdateMessage = findViewById(R.id.tvUpdateMessage);
        myListView = findViewById(R.id.ListView);
        Select = findViewById(R.id.btnSelect);
        Update = findViewById(R.id.btnUpdate);

        //getting new date/time
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        final String dateToStr = format.format(today);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, myArrayList);
        myListView.setAdapter(adapter);
        registerForContextMenu(myListView);


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notification");
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                myArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String title = snapshot.child("title").getValue().toString();
                    String message = snapshot.child("message").getValue().toString();
                    String date = snapshot.child("date").getValue().toString();
                    myArrayList.add("("+date+")\n"+"\nTitle :\n"+title+"\nMessage :\n"+message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //to check title input with title database
                final Query checkTitle = reference.orderByChild("title").equalTo(TitleInput.getText().toString());
                checkTitle.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            //String titleFromDB = dataSnapshot.getValue(String.class);
                            String systemTitle = dataSnapshot.child(TitleInput.getText().toString()).child("title").getValue(String.class);
                            UpdateTitle.setText(systemTitle);
                            String systemMessage = dataSnapshot.child(TitleInput.getText().toString()).child("message").getValue(String.class);
                            UpdateMessage.setText(systemMessage);
                            Toast.makeText(AdminUpdateNotif.this, systemTitle, Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                Toast.makeText(AdminUpdateNotif.this, "Title does not exist", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        TitleInput.setError("Invalid Title");
                    }
                });

            }
        });

        //button update onClick
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                reference.child(TitleInput.getText().toString()).child("message").setValue(UpdateMessage.getText().toString());
                reference.child(TitleInput.getText().toString()).child("date").setValue(dateToStr);
                Toast.makeText(AdminUpdateNotif.this, "Notification updated", Toast.LENGTH_SHORT).show();
                TitleInput.setText("");
                UpdateTitle.setText("");
                UpdateMessage.setText("");

            }
        });
    }

}