package com.example.nmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class signUp extends AppCompatActivity implements View.OnClickListener {
    EditText emailT, passwordT;
    Button signUpButton;
    TextView signinTv;
    private FirebaseAuth mAuth;
    ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailT = findViewById(R.id.emailId);
        passwordT = findViewById(R.id.passId);
        signUpButton = findViewById(R.id.signUpButton);
        signinTv = findViewById(R.id.LoginText);
        pro=findViewById(R.id.prgressId);
        mAuth = FirebaseAuth.getInstance();
        signUpButton.setOnClickListener(this);
        signinTv.setOnClickListener(this);
       this.setTitle("Sign Up");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.LoginText) {
            Intent in =new Intent(getApplicationContext(),SignIn.class);
            startActivity(in);
            }

        else if (v.getId() == R.id.signUpButton) {
            creatuser();
            }
    }

    private void creatuser() {

        String email=emailT.getText().toString().trim();
        String password=passwordT.getText().toString().trim();
        if(email.isEmpty())
        {
            emailT.setError("Enter an email address");
            emailT.requestFocus();
            return;
        }
    pro.setVisibility(View.VISIBLE);
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailT.setError("Enter a valid email address");
            emailT.requestFocus();
            return;
        }

        //checking the validity of the password

        if(password.isEmpty())
        {
            passwordT.setError("Give a passsword in password field");
            passwordT.requestFocus();
            return;
        }
        if(password.length()<7)
        {
            passwordT.setError("Enter a password more than 7 characters ");
            passwordT.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pro.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_LONG).show();

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"Already registered",Toast.LENGTH_LONG).show();
                            }
                            else
                                {
                            Toast.makeText(getApplicationContext(),"Error"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                           }
                        }

                        // ...
                    }
                });

    }


}