package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdminAdmissionModifyPage extends AppCompatActivity
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
            setContentView(R.layout.activity_admin_admission_modify_page);
            Toast.makeText(AdminAdmissionModifyPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AdminAdmissionModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAdmissionModifyPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminAdmissionModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminAdmissionModifyPage.this, LoginActivity.class));
                }
                break;
        }
    }

    public void modifyAdmission(View view)
    {
        startActivity(new Intent(AdminAdmissionModifyPage.this, AdminAdmissionModifyPage.class));
    }

    public void deleteAdmission(View view)
    {
        startActivity(new Intent(AdminAdmissionModifyPage.this, AdminAdmissionPage.class));
    }

    public void ViewMedicationModifyPage(View view)
    {
        startActivity(new Intent(AdminAdmissionModifyPage.this, AdminAdmissionModifyMedicationPage.class));
    }

    public void ViewAdmissionModifyNotesPage(View view)
    {
        startActivity(new Intent(AdminAdmissionModifyPage.this, AdminAdmissionModifyPatientNotesPage.class));
    }
}