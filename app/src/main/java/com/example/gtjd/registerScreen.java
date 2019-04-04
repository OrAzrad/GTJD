package com.example.gtjd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class registerScreen extends AppCompatActivity {

    private EditText password;
    private EditText confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

         password = findViewById(R.id.Password);
         confirm_password = findViewById(R.id.Confirm_password);

        findViewById(R.id.Regisrer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (password.getText().toString().equals(confirm_password.getText().toString())) {

                    Toast.makeText(registerScreen.this, "Or KATAV",Toast.LENGTH_LONG).show();

                }
                else{
                    confirm_password.setError("Error");
                }

            }
        });
    }
}
