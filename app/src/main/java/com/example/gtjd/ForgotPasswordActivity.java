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


    Toolbar toolbar;
    ProgressBar progressBar;
    EditText user_mail;
    Button send_new_password;
    FirebaseAuth my_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressBar = findViewById(R.id.progress_bar);
        user_mail = findViewById(R.id.editTextEmailToSendNewPassword);
        send_new_password = findViewById(R.id.buttonSendNewPassword);

        my_auth = FirebaseAuth.getInstance();

        send_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                my_auth.sendPasswordResetEmail(user_mail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);

                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "password sent to your email", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }
}
