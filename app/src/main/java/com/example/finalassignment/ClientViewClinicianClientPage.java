package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ClientViewClinicianClientPage extends AppCompatActivity {

    SharedPreferences pref;
    String mrn;
    String admissionid;
    String clinaddid;
    String clinicianinfo;
    String clinadmissionid;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type", null);
        String s1 = "Role: '2'";
        if (s.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_view_clinician_client_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            clinaddid = intent.getStringExtra("clincianid");
            clinadmissionid = intent.getStringExtra("clinicianadmissionid");
            setTextOnView();

        }
        else
        {
            Toast.makeText(ClientViewClinicianClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewClinicianClientPage.this, MainActivity.class));
        }

    }

    public void setTextOnView()
    {
        getClientDetails();
        textview = (TextView) findViewById(R.id.clinician_details);
        textview.setText(clinicianinfo);
    }

    public void getClientDetails()
    {
        String[] field = new String[1];
        field[0] = "clinicianid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = clinaddid;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/getcliniciandetails.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    JSONArray array = new JSONArray(result);
                    JSONObject names = array.getJSONObject(0);
                    clinicianinfo = names.getString("StaffNumber");
                    clinicianinfo = clinicianinfo + " " + names.getString("ClinicianType");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
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
                startActivity(new Intent(ClientViewClinicianClientPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewClinicianClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewClinicianClientPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(ClientViewClinicianClientPage.this,  MainActivity.class));
            }
            break;
        }
    }


    public void DeleteClinButton(View view)
    {
        if(delClinDatabase())
        {
            Intent intent1 = new Intent(this, ClientViewClientPage.class);
            intent1.putExtra("mrn", mrn);
            intent1.putExtra("admissionid", admissionid);
            startActivity(intent1);
        }
        else
        {
            //Toast.makeText(ClientViewClinicianClientPage.this, "Could not be deleted.", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean delClinDatabase()
    {
        String[] field = new String[1];
        field[0] = "clinicianaddid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = clinadmissionid;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/delclinicianadmission.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Toast.makeText(ClientViewClinicianClientPage.this, result, Toast.LENGTH_SHORT).show();
                if(result.equals("Deleted"))
                {
                    return true;
                }
                else return false;

            }
        }

        return false;
    }


    public void gobackButton(View view)
    {
        Intent intent1 = new Intent(this, ClientViewClientPage.class);
        intent1.putExtra("mrn", mrn);
        intent1.putExtra("admissionid", admissionid);
        startActivity(intent1);
    }
}