package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class PatientPainPage extends AppCompatActivity
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
            setContentView(R.layout.activity_patient_pain_page);
            Toast.makeText(PatientPainPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(PatientPainPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PatientPainPage.this,  MainActivity.class));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_patient:
                startActivity(new Intent(PatientPainPage.this, PatientHomePage.class));
                break;
            case R.id.action_pain_score:
                startActivity(new Intent(PatientPainPage.this, PatientPainPage.class));
                break;
            case R.id.action_logout_admin:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(PatientPainPage.this,  MainActivity.class));
            }
                break;
        }
    }

    public void addPatientPainButton(View view)
    {
        startActivity(new Intent(PatientPainPage.this, PatientPainPage.class));
    }
}