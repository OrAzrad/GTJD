package com.example.gtjd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, confirm_password;
    ProgressBar progress_bar;
    private FirebaseAuth my_auth;


    public String GetString(EditText str){

        String str_to_return = "";
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    public void Register(EditText email, EditText password, EditText confirm_password, final ProgressBar progress_bar) {



        String email_str = GetString(email);
        String password_str =   GetString(password);
        String confirm_password_str =   GetString(confirm_password);


        if(email_str.length() == 0)
        {
            email.setError("Email is requierd");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            email.setError("Enter a valid Email");
            email.requestFocus();
            return;
        }

        if (password_str.length() < 8){
            password.setError("Password should contain at least 8 characters");
            password.requestFocus();
            return;
        }
        if (!password_str.equals(confirm_password_str)) {
            confirm_password.setError("Password's are different");
            return;
        }

        progress_bar.setVisibility(View.VISIBLE);
        my_auth.createUserWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progress_bar.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);





        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirm_password = findViewById(R.id.editTextConfirm_password);
        progress_bar = findViewById(R.id.progress_bar);

        my_auth = FirebaseAuth.getInstance();

        findViewById(R.id.Regisrer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register(email, password, confirm_password, progress_bar);


            }
        });
    }
}
