package com.example.gtjd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class registerScreen extends AppCompatActivity {

    private EditText password;
    private EditText confirm_password;


    public String GetString(EditText str){

        String str_to_return = "";
        str_to_return = str.getText().toString();
        return str_to_return;
    }

    public void validation(EditText password, EditText confirm_password) {

       String password_str =   GetString(password);
       String confirm_password_str =   GetString(confirm_password);



        if (password_str.length() < 7){
            password.setError("Password must contain at least 7 characters");
        }
        if (!password_str.equals(confirm_password_str)) {
            confirm_password.setError("Password's are different");
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

         password = findViewById(R.id.Password);
         confirm_password = findViewById(R.id.Confirm_password);

        findViewById(R.id.Regisrer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation(password, confirm_password);


            }
        });
    }
}
