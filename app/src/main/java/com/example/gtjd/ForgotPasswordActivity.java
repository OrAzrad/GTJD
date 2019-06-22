package com.example.gtjd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    ProgressBar progressBar;
    EditText user_mail;
    Button send_new_password;
    FirebaseAuth my_auth;

    //Turns EditText variable to String variable
    public String GetString(EditText str){

        String str_to_return = "";
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    //when the screen opens this function start to run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //attach this Activity with his layout file
        setContentView(R.layout.activity_forgot_password);

        //get data from screen
        progressBar = findViewById(R.id.progress_bar);
        user_mail = findViewById(R.id.editTextEmailToSendNewPassword);
        send_new_password = findViewById(R.id.buttonSendNewPassword);

        my_auth = FirebaseAuth.getInstance();

        //button send_new_password function
        send_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show progress_bar in order that the user see that data is checked with Firebase
                progressBar.setVisibility(View.VISIBLE);

                //send mail to reset password
                my_auth.sendPasswordResetEmail(GetString(user_mail))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //close progress bar
                                progressBar.setVisibility(View.GONE);

                                //if mail truly sent
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "password sent to your email", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    //print system message
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }
}
