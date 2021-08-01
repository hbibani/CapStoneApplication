package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class PatientHomePage extends AppCompatActivity
{

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '3'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_patient_home_page);
            Toast.makeText(PatientHomePage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(PatientHomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PatientHomePage.this,  MainActivity.class));
        }


    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_patient:
                startActivity(new Intent(PatientHomePage.this, PatientHomePage.class));
                break;
            case R.id.action_pain_score:
                startActivity(new Intent(PatientHomePage.this, PatientPainPage.class));
                break;
            case R.id.action_logout_admin:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(PatientHomePage.this,  MainActivity.class));
            }
            break;
        }
    }


}

