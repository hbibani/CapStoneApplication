package com.example.finalassignment;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Looper;
import android.content.SharedPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        setContentView(R.layout.activity_login);

    }

    public void loginButton (View view)
    {
        EditText username1;
        EditText password1;
        //mydManager.updateRecord(Integer.parseInt(myid.getText().toString()), fn.getText().toString(), ln.getText().toString(), String.valueOf(spinner1.getSelectedItem()), Integer.parseInt(age.getText().toString()), address.getText().toString(), img.getText().toString());
        //startActivity(new Intent(LoginActivity.this, ClientHomePage.class));
        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        String usernameString = username1.getText().toString();
        String passwordString = password1.getText().toString();
        //Toast.makeText(LoginActivity.this, usernameString , Toast.LENGTH_SHORT).show();
        loginUser(usernameString, passwordString);

    }

    public void loginUser(String usernameString, String passwordString)
    {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[2];
            field[0] = "username";
            field[1] = "password";
            //Creating array for data
            String[] data = new String[2];
            data[0] = usernameString;
            data[1] = passwordString;
            PutData putData = new PutData("http://pxassignment123.atwebpages.com/login.php", "POST", field, data);
            if (putData.startPut())
            {
                if (putData.onComplete())
                {
                    String result = putData.getResult();
                    if(result.contains("Role: '1'") || result.contains("Role: '2'") || result.contains("Role: '3'")  )
                    {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Username", usernameString);
                        editor.putString("Type", result );
                        editor.apply();

                        if(result.contains("Role: '1'"))
                        {
                            startActivity(new Intent(LoginActivity.this, AdminHomePage.class));
                        }
                        else if(result.contains("Role: '2'"))
                        {
                            startActivity(new Intent(LoginActivity.this, ClientHomePage.class));
                        }
                        else if(result.contains("Role: '3'"))
                        {
                            startActivity(new Intent(LoginActivity.this, PatientHomePage.class));
                        }

                        //Toast.makeText(LoginActivity.this, putData.getResult() , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, putData.getResult(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

    }

}