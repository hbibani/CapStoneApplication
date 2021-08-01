package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class ClientViewNotesClientPage extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_view_notes_client_page);
            Toast.makeText(ClientViewNotesClientPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ClientViewNotesClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewNotesClientPage.this,  MainActivity.class));
        }

        //setContentView(R.layout.activity_client_view_notes_client_page);
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientViewNotesClientPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void goBackbutton(View view)
    {
        startActivity(new Intent(ClientViewNotesClientPage.this, ClientViewClientPage.class));
    }

    public void ModifyButton(View view)
    {
        startActivity(new Intent(ClientViewNotesClientPage.this, ClientViewNotesClientPage.class));
    }

    public void DeleteNotes(View view)
    {
        startActivity(new Intent(ClientViewNotesClientPage.this, ClientViewClientPage.class));
    }
}