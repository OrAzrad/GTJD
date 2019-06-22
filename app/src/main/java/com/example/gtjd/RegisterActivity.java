package com.example.gtjd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, confirm_password;
    ProgressBar progress_bar;
    private FirebaseAuth my_auth;
    Pattern pattern_letter = Pattern.compile(".*[a-z]+.*");
    Pattern pattern_CAPITAL_letter = Pattern.compile(".*[A-Z]+.*");
    Pattern pattern_number = Pattern.compile(".*[0-9]+.*");


    //Turns EditText variable to String variable
    public String GetString(EditText str){

        String str_to_return = "";
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    //Responsible for all user registration process
    public void Register(EditText email, EditText password, EditText confirm_password, final ProgressBar progress_bar) {



        String email_str = GetString(email);
        String password_str =   GetString(password);
        String confirm_password_str =   GetString(confirm_password);
        Matcher matcher_check_letter = pattern_letter.matcher(password_str);
        Matcher matcher_check_capital_letter = pattern_CAPITAL_letter.matcher(password_str);
        Matcher matcher_check_number = pattern_number.matcher(password_str);


        //Check that An email address has been entered
        if(email_str.length() == 0)
        {
            email.setError("Email is requierd");
            email.requestFocus();
            return;
        }
        //Check the integrity of the email address
        if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            email.setError("Enter a valid Email");
            email.requestFocus();
            return;
        }
        //Checks if all password is entered (password must be at least 8 characters long)
        if (password_str.length() < 8){
            password.setError("Password should contain at least 8 characters");
            password.requestFocus();
            return;
        }
        //checks if there is at least one letter in password
        if (!matcher_check_letter.matches())
        {
            password.setError("Password should contain at least one letter");
            password.requestFocus();
            return;
        }
        //checks if there is at least one CAPITAL letter in password
        if (!matcher_check_capital_letter.matches())
        {
            password.setError("Password should contain at least one CAPITAL letter");
            password.requestFocus();
            return;
        }
        //checks if there is at least one number in password
        if (!matcher_check_number.matches())
        {
            password.setError("Password should contain at least one number");
            password.requestFocus();
            return;
        }
        //checks if both password and confirm password equals
        if (!password_str.equals(confirm_password_str)) {
            confirm_password.setError("Password's are different");
            return;
        }

        //Show progress_bar in order that the user see that data is checked with Firebase
        progress_bar.setVisibility(View.VISIBLE);

        //Call to the built-in registration user login function
        my_auth.createUserWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if register succeeded
                        if (task.isSuccessful()) {

                            //close progress bar
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //close progress bar
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

    //when the screen opens this function start to run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //attach this Activity with his layout file
        setContentView(R.layout.activity_register_screen);


        //get data from screen
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirm_password = findViewById(R.id.editTextConfirm_password);
        progress_bar = findViewById(R.id.progress_bar);

        my_auth = FirebaseAuth.getInstance();

        //call to register function
        findViewById(R.id.Regisrer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register(email, password, confirm_password, progress_bar);


            }
        });
    }
}
