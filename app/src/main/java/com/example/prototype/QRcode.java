package com.example.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.prototype.ui.QRHelperClass;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QRcode extends AppCompatActivity implements View.OnClickListener{

    CodeScanner codeScanner;
    CodeScannerView scanView;
    TextView resultData, userID,test1,test2;
    Button btnScan,btnHome;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);

        mAuth = FirebaseAuth.getInstance();
        btnScan = findViewById(R.id.btnScan);
        userID = findViewById(R.id.tvuserName);

        SharedPreferences settings = getSharedPreferences("UserInfo",0);
        userID.setText(settings.getString("Username",""));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.getDisplayName() != null)
        {
            userID.setText(user.getDisplayName());
        }

    }

    @Override
    public void onClick(View view) {
    }
    private void scanCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator((this));
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning Code");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //output
                //builder.setMessage(result.getContents());
                String fresult = result.getContents();
                if (fresult.equals("dell9565")) {

                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    final String DBRef = settings.getString("Username", "");
                    //get date format
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    final String dateToStr = format.format(today);
                    //get Timestamp to compare
                    final long millis = new Date().getTime();

                    final Query studentIDFromDB = FirebaseDatabase.getInstance().getReference("Variable").orderByChild("email").equalTo(DBRef);
                    studentIDFromDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if(snapshot.exists())
                            {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                {
                                    String userName = dataSnapshot.child("Name").getValue().toString();
                                    String studentID = dataSnapshot.child("studentID").getValue().toString();

                                    final QRHelperClass QRHelpClass = new QRHelperClass(userName, studentID, dateToStr, millis);
                                    DatabaseReference attendanceRecord = FirebaseDatabase.getInstance().getReference("QRregistered");
                                    attendanceRecord.child(dateToStr).setValue(QRHelpClass);
                                }
                                Toast.makeText(QRcode.this,"Scan Successful",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(QRcode.this,"snapshot not exist",Toast.LENGTH_LONG).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(QRcode.this, "Please Scan Again", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

}


