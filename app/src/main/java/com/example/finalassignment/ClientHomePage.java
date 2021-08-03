package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ClientHomePage extends AppCompatActivity
{
    //ArrayList<String> patientList;
    SharedPreferences pref;
    ArrayList<String> patientList;
    ArrayAdapter<String> listAdapter;
    ListView patientsListView;
    JSONArray patients;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";

        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_home_page);
            initializePatientList();
        }
        else
        {
            Toast.makeText(ClientHomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientHomePage.this,  MainActivity.class));

        }
    }



    private void initializePatientList()
    {
        patientList = new ArrayList<String>();
        fetchActivePatientList();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientList);
        patientsListView = (ListView) findViewById(R.id.listview1);
        patientsListView.setAdapter(listAdapter);

        patientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ClientViewClientPage.class);
                try
                {
                    JSONObject names = patients.getJSONObject(position);
                    intent.putExtra("mrn", names.getString("mrn").toString());
                    intent.putExtra("admissionid", names.getString("AdmissionID"));
                    //Toast.makeText(ClientHomePage.this, names.getString("mrn").toString(), Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                startActivity(intent);
            }
        });
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientHomePage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientHomePage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientHomePage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientHomePage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void fetchActivePatientList()
    {
        FetchData fetchData = new FetchData("http://pxassignment123.atwebpages.com/fetchActivePatients.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();
                try {
                    patients = new JSONArray(result);
                    patientList.clear();
                    Log.i("Tag:", result);
                    for(int i = 0; i < patients.length();i++)
                    {
                        JSONObject names = patients.getJSONObject(i);
                        String mrn = names.getString("mrn");
                        mrn = mrn + " " + names.getString("AdmissionID");
                        mrn = mrn + " " + names.getString("PatientAge");
                        mrn = mrn + " " + names.getString("PatientGender");
                        patientList.add(mrn);
                    }
                    //Log.i("Hello1", String.valueOf(patientList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void activePatientsOnClick(View view)
    {
        startActivity(new Intent(ClientHomePage.this, ClientViewClientPage.class));
    }


}