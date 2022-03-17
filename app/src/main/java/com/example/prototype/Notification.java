package com.example.prototype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    ArrayList<String> myArrayList = new ArrayList<>();
    ListView myListView;
    Firebase myFirebase;
    private Object Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        myListView = findViewById(R.id.ListView);
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, myArrayList);
        myListView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notification");
        reference.addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}