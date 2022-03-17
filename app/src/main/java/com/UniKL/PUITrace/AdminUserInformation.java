package com.UniKL.PUITrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.AdminHelperClass;
import com.example.prototype.MainActivity;
import com.example.prototype.QRcode;
import com.example.prototype.R;
import com.example.prototype.ui.TracingHelperClass;
import com.google.errorprone.annotations.Var;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminUserInformation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_information);

        //declaration
        final EditText Name,class1,class2,class3,class4,class5;
        Button TClass01,TClass02,TClass03,TClass04,TClass05,UpdateClass;
        final Spinner spinner1;
        final Button Search = findViewById(R.id.btnSearch);
        final TextView SearchUser = findViewById(R.id.TextSearchName);
        Name = findViewById(R.id.txtName);
        class1 = findViewById(R.id.txtClass1);
        class2 = findViewById(R.id.txtClass2);
        class3 = findViewById(R.id.txtClass3);
        class4 = findViewById(R.id.txtClass4);
        class5 = findViewById(R.id.txtClass5);
        Name.setInputType(InputType.TYPE_NULL);
        TClass01 = findViewById(R.id.btnTraceClass01);
        TClass02 = findViewById(R.id.btnTraceClass02);
        TClass03 = findViewById(R.id.btnTraceClass03);
        TClass04 = findViewById(R.id.btnTraceClass04);
        TClass05 = findViewById(R.id.btnTraceClass05);
        spinner1 = findViewById(R.id.spinner1);

        //string spinner1
        String[] classA = new String[]{"Unregistered","EM110-A","EM110-B","EM220-A","EM220-B"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,classA);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(0);

        UpdateClass = findViewById(R.id.btnUpdateClass);
        //getting admin's name
        final SharedPreferences settings = getSharedPreferences("UserInfo",0);
        //get timestamp
        Map map = new HashMap();
        map.put("timestamp", ServerValue.TIMESTAMP);


        //getting time/date
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        final String dateToStr = format.format(today);


       //search button onClick
       Search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               final Query VariableComparison = FirebaseDatabase.getInstance().getReference().child("Variable").orderByChild("Name").startAt(SearchUser.getText().toString());
               VariableComparison.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           //fetch childnode from db
                           String NameUser = snapshot.child(SearchUser.getText().toString()).child("Name").getValue(String.class);
                           String class01 = snapshot.child(SearchUser.getText().toString()).child("class 1").getValue(String.class);
                           String class02 = snapshot.child(SearchUser.getText().toString()).child("class 2").getValue(String.class);
                           String class03 = snapshot.child(SearchUser.getText().toString()).child("class 3").getValue(String.class);
                           String class04 = snapshot.child(SearchUser.getText().toString()).child("class 4").getValue(String.class);
                           String class05 = snapshot.child(SearchUser.getText().toString()).child("class 5").getValue(String.class);
                           //set textbox according to db
                           Name.setText(NameUser);
                           class1.setText(class01);
                           class2.setText(class02);
                           class3.setText(class03);
                           class4.setText(class04);
                           class5.setText(class05);
                           int spinnerPosition = adapter.getPosition(class01);
                           spinner1.setSelection(spinnerPosition);
                       }
                       else
                           {
                               Toast.makeText(AdminUserInformation.this, "snapshot not exist", Toast.LENGTH_SHORT).show();
                           }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {
                       Toast.makeText(AdminUserInformation.this, "failed", Toast.LENGTH_SHORT).show();
                   }
               });
           }
       });

       //update onClick
       UpdateClass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               final DatabaseReference updateReference = FirebaseDatabase.getInstance().getReference("Variable");
               updateReference.child(SearchUser.getText().toString()).child("class 1").setValue(class1.getText().toString());
               updateReference.child(SearchUser.getText().toString()).child("class 2").setValue(class2.getText().toString());
               updateReference.child(SearchUser.getText().toString()).child("class 3").setValue(class3.getText().toString());
               updateReference.child(SearchUser.getText().toString()).child("class 4").setValue(class4.getText().toString());
               updateReference.child(SearchUser.getText().toString()).child("class 5").setValue(class5.getText().toString());
               CombineStudentClass combineStudentClass = new CombineStudentClass(class1.getText().toString() +"/"+ class2.getText().toString() +"/"+ class3.getText().toString()
               +"/" + class4.getText().toString() + "/"+ class5.getText().toString());
               updateReference.child(SearchUser.getText().toString()).child("combinedClass").setValue(combineStudentClass);

               Toast.makeText(AdminUserInformation.this,"Update Successful",Toast.LENGTH_SHORT).show();
           }
       });



       //Tracing
       TClass01.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Variable");
               Query ClassTrace02 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("combinedClass").startAt(class1.getText().toString());
               ClassTrace02.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           for (DataSnapshot snapshotName : snapshot.getChildren())
                           {
                               String namePUI = snapshotName.child("Name").getValue().toString();
                               TracingHelperClass tracingHelperClass = new TracingHelperClass(namePUI.toUpperCase(),class1.getText().toString(),dateToStr,settings.getString("Username",""));
                               DatabaseReference traceReference = FirebaseDatabase.getInstance().getReference("TraceRecord").child(settings.getString("Username","")).child(dateToStr);
                               traceReference.child(namePUI).setValue(tracingHelperClass);

                           }
                       }
                       else{
                           Toast.makeText(AdminUserInformation.this,"There is no student in the same class", Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {

                   }
               });

           }
       });
       TClass02.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Variable");
               Query ClassTrace02 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("combinedClass").startAt(class2.getText().toString());
               ClassTrace02.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           //get timestamp
                           Map map = new HashMap();
                           map.put("timestamp", ServerValue.TIMESTAMP);

                           //get time/date in string and proper format
                           Date today = new Date();
                           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                           final String dateToStr = format.format(today);

                           for (DataSnapshot snapshotName : snapshot.getChildren())
                           {
                               String namePUI = snapshotName.child("Name").getValue().toString();

                               TracingHelperClass tracingHelperClass = new TracingHelperClass(namePUI.toUpperCase(),class2.getText().toString(),dateToStr,settings.getString("Username",""));
                               DatabaseReference traceReference = FirebaseDatabase.getInstance().getReference("TraceRecord").child(settings.getString("Username","")).child(dateToStr);
                               traceReference.child(namePUI).setValue(tracingHelperClass);


                           }
                           //Toast.makeText(AdminUserInformation.this,"Trace Success",Toast.LENGTH_LONG).show();
                       }
                       else{
                           Toast.makeText(AdminUserInformation.this,"There is no student in the same class", Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {

                   }
               });
           }
       });

       TClass03.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Query ClassTrace02 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("combinedClass").startAt(class3.getText().toString());
               ClassTrace02.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           for (DataSnapshot snapshotName : snapshot.getChildren())
                           {
                               String namePUI = snapshotName.child("Name").getValue().toString();
                               TracingHelperClass tracingHelperClass = new TracingHelperClass(namePUI.toUpperCase(),class3.getText().toString(),dateToStr,settings.getString("Username",""));
                               DatabaseReference traceReference = FirebaseDatabase.getInstance().getReference("TraceRecord").child(settings.getString("Username","")).child(dateToStr);
                               traceReference.child(namePUI).setValue(tracingHelperClass);
                           }
                       }
                       else{
                           Toast.makeText(AdminUserInformation.this,"There is no student in the same class", Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {

                   }
               });
           }
       });
       TClass04.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Query ClassTrace02 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("combinedClass").startAt(class4.getText().toString());
               ClassTrace02.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           for (DataSnapshot snapshotName : snapshot.getChildren())
                           {
                               String namePUI = snapshotName.child("Name").getValue().toString();
                               TracingHelperClass tracingHelperClass = new TracingHelperClass(namePUI.toUpperCase(),class4.getText().toString(),dateToStr,settings.getString("Username",""));
                               DatabaseReference traceReference = FirebaseDatabase.getInstance().getReference("TraceRecord").child(settings.getString("Username","")).child(dateToStr);
                               traceReference.child(namePUI).setValue(tracingHelperClass);
                           }
                           Query testNode = FirebaseDatabase.getInstance().getReference("Experiment");

                       }
                       else{
                           Toast.makeText(AdminUserInformation.this,"There is no student in the same class", Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {

                   }
               });
           }
       });

       TClass05.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Query ClassTrace02 = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("combinedClass").startAt(class4.getText().toString());
               ClassTrace02.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                       if(snapshot.exists())
                       {
                           if(class5.toString().equals("Unregistered"))
                           {
                               Toast.makeText(AdminUserInformation.this,"Student has not been registered", Toast.LENGTH_LONG).show();

                           }
                           else{
                               for (DataSnapshot snapshotName : snapshot.getChildren())
                               {
                                   String namePUI = snapshotName.child("Name").getValue().toString();
                                   TracingHelperClass tracingHelperClass = new TracingHelperClass(namePUI.toUpperCase(),class5.getText().toString(),dateToStr,settings.getString("Username",""));
                                   DatabaseReference traceReference = FirebaseDatabase.getInstance().getReference("TraceRecord").child(settings.getString("Username","")).child(dateToStr);
                                   traceReference.child(namePUI).setValue(tracingHelperClass);

                               }

                           }
                       }
                       else{
                           Toast.makeText(AdminUserInformation.this,"There is no student in the same class", Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error)
                   {

                   }
               });
           }
       });
    }
}
class CombineStudentClass
{
    String combinedClass;

    public CombineStudentClass(String combinedClass)
    {
        this.combinedClass = combinedClass;
    }

    public String getCombinedClass() {
        return combinedClass;
    }

    public void setCombinedClass(String combinedClass) {
        this.combinedClass = combinedClass;
    }
}

