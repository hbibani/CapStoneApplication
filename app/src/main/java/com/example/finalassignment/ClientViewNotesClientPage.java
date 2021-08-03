package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ClientViewNotesClientPage extends AppCompatActivity {

    SharedPreferences pref;
    String mrn;
    String patientnotesid;
    String notesdata;
    EditText notesinfo;
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
            setContentView(R.layout.activity_client_view_notes_client_page);
            Intent intent = getIntent();
            patientnotesid = intent.getStringExtra("patientNotesID");
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            notesinfo = (EditText) findViewById(R.id.patientHistoryModify);
            fetchNotes();
            notesinfo.setText(notesdata);
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

    public void fetchNotes()
    {
        String[] field = new String[1];
        field[0] = "patientnotesid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = patientnotesid;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/getsinglenotes.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    JSONArray array = new JSONArray(result);
                    JSONObject names = array.getJSONObject(0);
                    notesdata = names.getString("Notes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean modifyPatientInformation(String notes)
    {
        String[] field = new String[2];
        field[0] = "patientnotesid";
        field[1] = "notes";

        //Creating array for data
        String[] data = new String[2];
        data[0] = patientnotesid;
        data[1] = notes;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/modifypatientnotes.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.i("Tag", result);
                if(result.contains("Modified"))
                {
                    return true;
                }
                else return false;
            }
        }

        return false;
    }


    public boolean deletePatientInformation()
    {
        String[] field = new String[1];
        field[0] = "patientnotesid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = patientnotesid;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/deletenotespatient.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Toast.makeText(ClientViewNotesClientPage.this, result, Toast.LENGTH_SHORT).show();
                //Log.i("Tag", result);
                if(result.contains("Deleted"))
                {
                    return true;
                }
                else return false;
            }
        }

        return false;
    }

    public void goBackbutton(View view)
    {
        Intent intent1 = new Intent(this, ClientViewClientPage.class);
        intent1.putExtra("mrn", mrn);
        intent1.putExtra("admissionid", admissionid);
        startActivity(intent1);
    }

    public void ModifyButton(View view)
    {
        String notes;
        notes = notesinfo.getText().toString();
        if(modifyPatientInformation(notes) == true)
        {
            Intent intent2 = new Intent(getApplicationContext(), ClientViewNotesClientPage.class);
            intent2.putExtra("patientNotesID", patientnotesid);
            intent2.putExtra("mrn", mrn);
            intent2.putExtra("admissionid", admissionid);
            startActivity(intent2);
        }
        else
        {
            Toast.makeText(ClientViewNotesClientPage.this, "Modify did not work.", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteNotes(View view)
    {
        if(deletePatientInformation() == true)
        {
            Intent intent3 = new Intent(getApplicationContext(), ClientViewClientPage.class);
            intent3.putExtra("mrn", mrn);
            intent3.putExtra("admissionid", admissionid);
            startActivity(intent3);
        }
        else
        {
            Toast.makeText(ClientViewNotesClientPage.this, "Delete unsuccessful.", Toast.LENGTH_SHORT).show();
        }

    }
}