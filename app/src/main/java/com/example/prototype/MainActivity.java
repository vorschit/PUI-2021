package com.example.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private EditText username, password;
    private Button Login;
    private TextView Register;
    private FirebaseAuth mAuth;
    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Register = findViewById(R.id.tvRegister);
        mAuth = FirebaseAuth.getInstance();

        drawer = findViewById(R.id.draw_layout);


          //register button
         Register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 Intent i = new Intent(MainActivity.this, Registration.class);
                 startActivity(i);
             }
         });

        //login button
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //test if user/email is match
                            saveData();
                            FirebaseUser userID = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent HomeAct = new Intent(MainActivity.this, com.example.prototype.HomeAct.class);
                            getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(HomeAct);
                            finish();
                        } else {
                            saveData();
                            //save info
                            Query checkadmin = FirebaseDatabase.getInstance().getReference("Admin").orderByChild("AdminName").equalTo(username.getText().toString());
                            checkadmin.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //check if database for admin account
                                    if (dataSnapshot.exists()) {
                                        username.setError(null);
                                        //checking password = password (database)
                                        Query checkadminPassword = FirebaseDatabase.getInstance().getReference("Admin").orderByChild("password").equalTo(password.getText().toString());
                                        checkadminPassword.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Toast.makeText(MainActivity.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                                                Intent intToAdHome = new Intent(MainActivity.this, AdminHome.class);
                                                startActivity(intToAdHome);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError)
                                            {
                                                //database error execute this
                                            }
                                        });
                                    } else
                                        {
                                            //if database isnt exist
                                        Toast.makeText(MainActivity.this, "Account doesnt exist", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(MainActivity.this, "Database Not Connected", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
                }
            });
        }
    private Boolean validateName ()
    {
        //validation textbox
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Field is empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public void saveData(){
        SharedPreferences settings = getSharedPreferences("UserInfo",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Username", username.getText().toString());
        editor.putString("Password", password.getText().toString());
        editor.apply();
    }

    @Override
    public void onBackPressed () {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
        protected void onStart() {
            super.onStart();
            if(mAuth.getCurrentUser() != null){
                finish();
                startActivity(new Intent(this,HomeAct.class));
            }
    }
}
