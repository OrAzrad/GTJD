package com.example.gtjd;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    FirebaseAuth mAuth;
    EditText email,password;
    ProgressBar progress_bar;

    public String GetString(EditText str){

        String str_to_return = "";
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    private void Login(final EditText email, EditText password)
    {

        final String email_str = GetString(email);
        String password_str = GetString(password);

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
        if (password_str.length() < 7){
            password.setError("Password must contain at least 7 characters");
            password.requestFocus();
            return;
        }

        progress_bar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progress_bar.setVisibility(View.GONE);
                    Intent intent = new Intent(MainActivity.this, missions.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("message_key", email_str);
                    startActivity(intent);
                }
                else
                {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        progress_bar = findViewById(R.id.progress_bar);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewSignUp:
                Intent intent = new Intent(this, registerScreen.class);
                startActivity(intent);
                break;

            case R.id.buttonLogin:
                Login(email, password);
                break;
        }

    }
}
