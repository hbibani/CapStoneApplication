package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientViewMedicationClientPage extends AppCompatActivity
{
    SharedPreferences pref;
    String medicationstayID;
    EditText medicationdisplay;
    String medbrand;
    String mrn;
    String admissionid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";

        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_view_medication_client_page);
            medicationdisplay = (EditText) findViewById(R.id.medication_brand);
            Intent intent = getIntent();
            medicationstayID = intent.getStringExtra("MedicationStayID");
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            fetchMedication();
            medicationdisplay.setText(medbrand);
        }
        else
        {
            Toast.makeText(ClientViewMedicationClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewMedicationClientPage.this,  MainActivity.class));
        }

    }

    public void fetchMedication()
    {
        String[] field = new String[1];
        field[0] = "medicationstayid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationstayID;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/clingetsinglemed.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    JSONArray array = new JSONArray(result);
                    JSONObject names = array.getJSONObject(0);
                    medbrand = names.getString("MedBrand");
                    medbrand = medbrand + " " + names.getString("Dosage");
                    medbrand = medbrand + " " + names.getString("DoseForm");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void delMedication()
    {
        String[] field = new String[1];
        field[0] = "medicationstayid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationstayID;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/deletemedfromadmission.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Deleted"))
                {
                    Intent intent1 = new Intent(this, ClientViewClientPage.class);
                    intent1.putExtra("mrn", mrn);
                    intent1.putExtra("admissionid", admissionid);
                    startActivity(intent1);
                }
                else
                {
                    startActivity(new Intent(ClientViewMedicationClientPage.this, ClientViewClientPage.class));
                    Toast.makeText(ClientViewMedicationClientPage.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientViewMedicationClientPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void DeleteMedButton(View view)
    {
        delMedication();
    }

    public void goBackButton(View view)
    {
        Intent intent1 = new Intent(this, ClientViewClientPage.class);
        intent1.putExtra("mrn", mrn);
        intent1.putExtra("admissionid", admissionid);
        startActivity(intent1);

    }

}