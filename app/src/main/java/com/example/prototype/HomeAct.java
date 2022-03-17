package com.example.prototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.UniKL.PUITrace.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeAct extends AppCompatActivity {
    ImageButton btnLogout;
    ImageButton btnScanQR, moveNotification,UserProfile;
    TextView EtUsername;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        UserProfile = findViewById(R.id.btnUserProfile);
        moveNotification = findViewById(R.id.btnPost);
        //showAllUserData();

        moveNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToNoti = new Intent(HomeAct.this, Notification.class);
                startActivity(intToNoti);
            }
        });
        btnScanQR = findViewById(R.id.btnScan);
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scan = new Intent(HomeAct.this, QRcode.class);
                startActivity(scan);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeAct.this,"Sign Out Success",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent Logout = new Intent(HomeAct.this,MainActivity.class);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Logout);
                finish();
            }
        });

        UserProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent User = new Intent(HomeAct.this, com.UniKL.PUITrace.UserProfile.class);
                startActivity(User);
            }
        });

    }



    private void showAllUserData()
    {
        Intent intent = getIntent();
        String user_username = intent.getStringExtra("name");

        EtUsername.setText(user_username);
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