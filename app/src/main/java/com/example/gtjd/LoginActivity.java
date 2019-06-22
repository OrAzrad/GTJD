package com.example.gtjd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {

    FirebaseAuth my_auth;
    EditText email,password;
    ProgressBar progress_bar;
    Pattern pattern_letter = Pattern.compile(".*[a-z]+.*");
    Pattern pattern_CAPITAL_letter = Pattern.compile(".*[A-Z]+.*");
    Pattern pattern_number = Pattern.compile(".*[0-9]+.*");

    //Turns EditText variable to String variable
    public String GetString(EditText str){

        String str_to_return;
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    //Responsible for all user login process
    private void Login(final EditText email, EditText password)
    {

        //Save the extracted values from the xml document as string variables
        final String email_str = GetString(email);
        String password_str = GetString(password);


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
            password.setError("Password must contain at least 8 characters");
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
        //Show progress_bar in order that the user see that data is checked with Firebase
        progress_bar.setVisibility(View.VISIBLE);
        //Call to the built-in Firebase user login function
        my_auth.signInWithEmailAndPassword(email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //if login succeeded
                if(task.isSuccessful())
                {
                    //close progress bar
                    progress_bar.setVisibility(View.GONE);
                    //Open next window
                    Intent intent = new Intent(LoginActivity.this, AddMissionsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Pass email address to next window and open next window
                    intent.putExtra("message_key", email_str);
                    startActivity(intent);
                }
                else
                {
                    //close progress bar
                    progress_bar.setVisibility(View.GONE);
                    //Print to user the message that Firebase sent as a result
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //when the screen opens this function start to run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //attach this Activity with his layout file
        setContentView(R.layout.activity_main);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewForgotPassword).setOnClickListener(this);

        my_auth = FirebaseAuth.getInstance();

        //get data from screen
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        progress_bar = findViewById(R.id.progress_bar);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            //when user click on textViewSignUp
            case R.id.textViewSignUp:
                Intent intent_register = new Intent(this, RegisterActivity.class);
                startActivity(intent_register);
                break;

            //when user click on textViewForgotPassword
            case R.id.textViewForgotPassword:
                Intent intent_forgot_password = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent_forgot_password);
                break;

            //when user click on buttonLogin
            case R.id.buttonLogin:
                Login(email, password);
                break;

        }

    }
}
