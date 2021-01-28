package com.example.nmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText emailT, passwordT;
    Button signinButton;
    TextView signupTv;
    private FirebaseAuth mAuth;
    ProgressBar pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_in);
       // this.setTitle("Login");

        emailT = findViewById(R.id.emailId);
        passwordT = findViewById(R.id.passId);
        signinButton = findViewById(R.id.signinButton);
        signupTv = findViewById(R.id.signuptext);

        pro=findViewById(R.id.prgressId);
        mAuth = FirebaseAuth.getInstance();
        signupTv.setOnClickListener(this);
        signinButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signinButton) {

         loginUser();

        }
        else if (v.getId() == R.id.signuptext) {
            Intent in = new Intent(getApplicationContext(), signUp.class);
            startActivity(in);
        }

    }

    private void loginUser() {
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
        if(password.length()<6)
        {
            passwordT.setError("Enter a password more than 7 characters ");
            passwordT.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pro.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Intent in = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(in);

                        } else {


                            Toast.makeText(getApplicationContext(),"passwors anad email may be incorrect",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }


}



