package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdminClinicianModifyPage extends AppCompatActivity
{

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_clinician_modify_page);
            Toast.makeText(AdminClinicianModifyPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AdminClinicianModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminClinicianModifyPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminClinicianModifyPage.this,  MainActivity.class));
                }
                break;

        }
    }

    public void ModifyClinicianButton(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianModifyPage.class));
    }

    public void DeleteClinicianButton(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianPage.class));
    }

    public void GoBackButton(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianPage.class));
    }
}