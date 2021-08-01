package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity
{
    Spinner gender;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Username", "");
        editor.putString("Type", "" );
        editor.apply();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    public void screenLogin (View view)
    {
        startActivity(new Intent(MainActivity.this, AdminHomePage.class));
    }

    public void screenPatients (View view)
    {
        startActivity(new Intent(MainActivity.this,  PatientHomePage.class));
    }

    public void screenClinicians (View view)
    {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}