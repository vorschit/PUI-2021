package com.example.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;


public class Registration extends AppCompatActivity
{
    private TextView regname,regusername,regpassword,regstudentID,regEmail;
    private Button btnReg;
    private TextView Login;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        regname = findViewById(R.id.etFullName);
        regusername = findViewById(R.id.etUsername);
        regpassword = findViewById(R.id.etPassword);
        regstudentID = findViewById(R.id.etStudentID);
        btnReg = findViewById(R.id.btnRegister);
        regEmail = findViewById(R.id.etEmail);
        Login = findViewById(R.id.tvLog);

        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                if(!validateName() | !validateStudentID() | !validateUsername() | !validatePassword() | !validateEmail())
                {
                    return;
                }

                //get all the values from fieldtext
                final String name = regname.getText().toString();
                final String studentID = regstudentID.getText().toString();
                final String username = regusername.getText().toString();
                final String password = regpassword.getText().toString();
                final String email = regEmail.getText().toString();

                //UserHelperClass helperClass = new UserHelperClass(name,studentID,username,password,email);
                //reference.child(username).setValue(helperClass);

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            //store other data into database
                            User user = new User(name,username,studentID,email,password);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        //create copy of data(user) to be use as variable to trace
                                        DatabaseReference CreateVariableUser = FirebaseDatabase.getInstance().getReference().child("Variable");
                                        CreateVariableUser.child(name).child("Name").setValue(name);
                                        CreateVariableUser.child(name).child("username").setValue(username);
                                        CreateVariableUser.child(name).child("password").setValue(password);
                                        CreateVariableUser.child(name).child("studentID").setValue(studentID);
                                        CreateVariableUser.child(name).child("email").setValue(email);
                                        CreateVariableUser.child(name).child("class 1").setValue("Unregistered");
                                        CreateVariableUser.child(name).child("class 2").setValue("Unregistered");
                                        CreateVariableUser.child(name).child("class 3").setValue("Unregistered");
                                        CreateVariableUser.child(name).child("class 4").setValue("Unregistered");
                                        CreateVariableUser.child(name).child("class 5").setValue("Unregistered");

                                        Toast.makeText(Registration.this,"Registration Success", Toast.LENGTH_LONG).show();
                                        Intent toLogin = new Intent(Registration.this, MainActivity.class);
                                        getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(toLogin);
                                        finish();
                                    }
                                    else{

                                        Toast.makeText(Registration.this,"Registration Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
                //Toast.makeText(Registration.this,"Registration success",Toast.LENGTH_SHORT).show();
                //Intent toLogin = new Intent(Registration.this, MainActivity.class);
                //startActivity(toLogin);




        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Registration.this, MainActivity.class);
                startActivity(i);
            }
        });

            }

    });
    }
    private Boolean validateName()
    {
        String val = regname.getText().toString();
        if(val.isEmpty()){
            regname.setError("Field is empty");
            return false;
        }
        else{
            regname.setError(null);
            return true;
        }
    }
    private Boolean validateStudentID()
    {
        String val = regstudentID.getText().toString();
        if(val.isEmpty()){
            regstudentID.setError("Field is empty");
            return false;
        }
        else{
            regstudentID.setError(null);
            return true;
        }
    }    private Boolean validateUsername()
{
    String val = regusername.getText().toString();
    if(val.isEmpty()){
        regusername.setError("Field is empty");
        return false;
    }else if(val.length()>=20){

        regusername.setError("Username too long");
        return false;

    }
    else{
        regusername.setError(null);
        return true;
    }
}    private Boolean validatePassword()
{
    String val = regpassword.getText().toString();
    if(val.isEmpty()){
        regpassword.setError("Field is empty");
        return false;
    }


    else{
        regpassword.setError(null);
        return true;
    }
}    private Boolean validateEmail()
{
    String val = regEmail.getText().toString();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    if(val.isEmpty()){
        regEmail.setError("Field is empty");
        return false;
    }else if(!val.matches(emailPattern)){
        regEmail.setError("Invalid email");
        return false;
    }
    else{
        regEmail.setError(null);
        return true;
    }
}

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {
            //handle login user
        }
    }

}
