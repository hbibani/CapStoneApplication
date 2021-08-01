package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ClientViewClientPage extends AppCompatActivity
{
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
            setContentView(R.layout.activity_client_view_client_page);
            Toast.makeText(ClientViewClientPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ClientViewClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewClientPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewClientPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewClientPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientViewClientPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void fetchList()
    {

        //Starting Read data from URL
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                FetchData fetchData = new FetchData("https://projects.vishnusivadas.com/AdvancedHttpURLConnection/readTest.php");
                if (fetchData.startFetch())
                {
                    if (fetchData.onComplete())
                    {
                        String result = fetchData.getResult();

                        //textView.setText(result);
                    }
                }
            }
        });
    }

    public void addMedication (View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientViewClientPage.class));
    }

    public void addClinician(View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientViewClientPage.class));
    }

    public void ClientDischargeFeedbackPage (View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientFinaliseFeedbackPage.class));
    }

    public void AddClientNotes(View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientViewClientPage.class));
    }

    public void ModifyNotesPage(View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientViewNotesClientPage.class));
    }

    public void ViewModMedPage(View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientViewMedicationClientPage.class));
    }
}