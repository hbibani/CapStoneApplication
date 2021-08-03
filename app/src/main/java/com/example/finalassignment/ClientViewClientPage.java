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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientViewClientPage extends AppCompatActivity {
    SharedPreferences pref;


    ArrayAdapter<String> medlistAdapter;
    ListView medListView;
    String mrn;
    String admissionid;
    JSONArray medications;
    ArrayList<String> medList;
    JSONArray notes;
    ArrayList<String> noteList;
    ArrayAdapter<String> noteslistAdapter;
    ListView notesListView;
    Spinner medSpin;
    ArrayList<String> medicationSingleList;
    ArrayAdapter<String> medicationAdapater;
    JSONArray med;
    Spinner clinSpin;
    ArrayList<String> clinSingleList;
    ArrayAdapter<String> clinAdapter;
    JSONArray clin;
    EditText notes2;

    ListView clinaddView;
    ArrayList<String> clinaddList;
    ArrayAdapter<String> clinaddAdapter;
    JSONArray clinadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type", null);
        String s1 = "Role: '2'";
        if (s.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_view_client_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            if (!(mrn == null))
            {
                initializeMedList();
                initializeNotesList();
                initializeMedicationList();
                initializeClinicianList();
                initializeClinicianListAdmission();
                //Toast.makeText(ClientViewClientPage.this, mrn, Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(ClientViewClientPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ClientViewClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewClientPage.this, MainActivity.class));
        }
    }


    private void initializeMedicationList()
    {
        medicationSingleList = new ArrayList<String>();
        fetchSingleMedList();
        medicationAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicationSingleList);
        medSpin = (Spinner) findViewById(R.id.spinner_medication);
        medSpin.setAdapter(medicationAdapater);
    }

    private void initializeClinicianListAdmission()
    {
        clinaddList = new ArrayList<String>();
        fetchClinsAdmissionList();
        clinaddAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clinaddList );
        clinaddView = (ListView) findViewById(R.id.clinician_list);
        clinaddView.setAdapter(clinaddAdapter);

        clinaddView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    JSONObject names = clinadd.getJSONObject(position);
                    Intent intent = new Intent(getApplicationContext(), ClientViewClinicianClientPage.class);
                    intent.putExtra("clincianid", names.getString("ClinicianID").toString());
                    intent.putExtra("clinicianadmissionid", names.getString("ClinicianAdmissionID"));
                    intent.putExtra("mrn", mrn);
                    intent.putExtra("admissionid", admissionid);
                    startActivity(intent);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    private void fetchClinsAdmissionList()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;


        PutData putData = new PutData("http://pxassignment123.atwebpages.com/getclinicianadmissionlist.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    //Log.i("Result", result);
                    clinadd = new JSONArray(result);
                    clinaddList.clear();

                    for(int i = 0; i < clinadd.length() ; i++)
                    {
                        JSONObject names = clinadd.getJSONObject(i);
                        String clinname = names.getString("StaffNumber");
                        clinname = clinname + " " + names.getString("ClinicianType");
                        clinaddList.add(clinname);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void fetchSingleMedList()
    {
        FetchData fetchData = new FetchData("http://pxassignment123.atwebpages.com/fetchAllMeds.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();
                try
                {
                    med = new JSONArray(result);
                    medicationSingleList.clear();
                    Log.i("Tag:", result);
                    for (int i = 0; i < med.length(); i++) {
                        JSONObject names = med.getJSONObject(i);
                        String medbrand = names.getString("MedBrand");
                        medbrand = medbrand + " " + names.getString("Dosage");
                        medbrand = medbrand + " " + names.getString("DoseForm");
                        medicationSingleList.add(medbrand);
                    }
                    //Log.i("Hello1", String.valueOf(patientList.size()));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addMedication(View view) throws JSONException {
        medSpin = (Spinner) findViewById(R.id.spinner_medication);
        JSONObject names = med.getJSONObject(medSpin.getSelectedItemPosition());
        String medicationID = names.getString("MedicationID");
        addMedicationToPatient(medicationID);
        Intent intent = new Intent(getApplicationContext(), ClientViewClientPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
        //startActivity(new Intent(ClientViewClientPage.this, ClientViewClientPage.class));
    }

    void addMedicationToPatient(String medicationID) {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] = "medicationid";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = medicationID;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/addmedicationtoadmission.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                try {
                    Toast.makeText(ClientViewClientPage.this, mrn, Toast.LENGTH_SHORT).show();
                    Log.i("Result Note", result);
                    notes = new JSONArray(result);
                    noteList.clear();

                    for (int i = 0; i < notes.length(); i++) {
                        JSONObject names = notes.getJSONObject(i);
                        String notes = names.getString("AdmissionID");
                        notes = notes + " " + names.getString("PatientNotesID");
                        notes = notes + " " + names.getString("Notes");
                        noteList.add(notes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void addClinician(View view) throws JSONException {
        clinSpin = (Spinner) findViewById(R.id.spinner_clinician);
        JSONObject names = clin.getJSONObject(clinSpin.getSelectedItemPosition());
        String clintoadd = names.getString("ClinicianID");
        if (addClinToPatientdata(clintoadd)) {
            Intent intent = new Intent(getApplicationContext(), ClientViewClientPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        } else {
            Toast.makeText(ClientViewClientPage.this, "Clinician could not be added.", Toast.LENGTH_SHORT).show();
        }

    }

    public void AddClientNotes(View view)
    {
        notes2 = (EditText) findViewById(R.id.notestoadd);
        String notesdata = notes2.getText().toString();
        if(addpatientnotestodatabase(notesdata))
        {
            Intent intent = new Intent(getApplicationContext(), ClientViewClientPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(ClientViewClientPage.this, "Notes could not be added.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean addpatientnotestodatabase(String notesdata)
    {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] = "notes";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = notesdata;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/addpatientnotestoadmission.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.i("Tag", result);
                if(result.contains("Success"))
                {
                    return true;
                }
                else return false;
            }
        }

        return false;
    }



    private boolean addClinToPatientdata(String clintoadd)
    {

        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] =  "clinicianid";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = clintoadd;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/addcliniciantoadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                if(result.isEmpty())
                {
                    return false;
                }

                try
                {

                    clin = new JSONArray(result);
                    clinSingleList.clear();

                    for(int i = 0; i <clin.length() ; i++)
                    {
                        JSONObject names = clin .getJSONObject(i);
                        String clinicianstats = names.getString("ClincianAdmissionID");
                        clinicianstats = clinicianstats + " " + names.getString("ClinicianID");
                        clinicianstats = clinicianstats + " " + names.getString("AdmissionID");
                        clinicianstats  = clinicianstats + " " + names.getString("PushNotification");
                        clinSingleList.add(clinicianstats);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private void initializeClinicianList()
    {
        clinSingleList = new ArrayList<String>();
        fetchSingleClinList();
        clinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clinSingleList);
        clinSpin = (Spinner) findViewById(R.id.spinner_clinician);
        clinSpin.setAdapter(clinAdapter);
    }

    private void fetchSingleClinList()
    {
        FetchData fetchData = new FetchData("http://pxassignment123.atwebpages.com/fetchAllClin.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();
                try {
                    clin = new JSONArray(result);
                    clinSingleList.clear();
                    Log.i("Tag:", result);
                    for(int i = 0; i < clin.length();i++)
                    {
                        JSONObject names = clin.getJSONObject(i);
                        String clincian = names.getString("ClinicianID");
                        clincian = clincian + " " + names.getString("UserID");
                        clincian = clincian + " " + names.getString("StaffNumber");
                        clincian = clincian + " " + names.getString("ClinicianType");
                        clinSingleList.add(clincian);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fetchMedList()
    {
        String[] field = new String[1];
        field[0] = "mrn";

        //Creating array for data
        String[] data = new String[1];
        data[0] = mrn;
        PutData putData = new PutData("http://pxassignment123.atwebpages.com/getpatientmedication.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    //Log.i("fetchmed", result);
                    medications = new JSONArray(result);
                    medList.clear();

                    for(int i = 0; i <medications.length() ; i++)
                    {
                        JSONObject names = medications.getJSONObject(i);
                        String medbrand = names.getString("MedBrand");
                        medbrand = medbrand + " " + names.getString("Dosage");
                        medbrand = medbrand + " " + names.getString("DoseForm");
                        medList.add(medbrand);
                    }
                    Log.i("Size1", String.valueOf(medList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    void initializeNotesList()
    {
        noteList = new ArrayList<>();
        fetchNotesList();
        //Log.i("Size2 ", String.valueOf(medList.size()));
        noteslistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteList);
        notesListView = (ListView) findViewById(R.id.patient_notes_list);
        notesListView.setAdapter(noteslistAdapter);
        Toast.makeText(ClientViewClientPage.this, "In.", Toast.LENGTH_SHORT).show();
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    JSONObject names = notes.getJSONObject(position);
                    Intent intent = new Intent(getApplicationContext(), ClientViewNotesClientPage.class);
                    intent.putExtra("patientNotesID", names.getString("PatientNotesID").toString());
                    intent.putExtra("mrn", mrn);
                    intent.putExtra("admissionid", admissionid);
                    startActivity(intent);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }


    void initializeMedList()
    {
        medList = new ArrayList<>();
        fetchMedList();
        Log.i("Size2 ", String.valueOf(medList.size()));
        medlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medList);
        medListView = (ListView) findViewById(R.id.medication_list);
        medListView.setAdapter(medlistAdapter);
        Toast.makeText(ClientViewClientPage.this, "In.", Toast.LENGTH_SHORT).show();
        medListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    JSONObject names = medications.getJSONObject(position);
                    Intent intent = new Intent(getApplicationContext(), ClientViewMedicationClientPage.class);
                    intent.putExtra("MedicationStayID", names.getString("MedicationStayID").toString());
                    intent.putExtra("mrn", mrn);
                    intent.putExtra("admissionid", admissionid);
                    startActivity(intent);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
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


    public void fetchNotesList()
    {
        String[] field = new String[1];
        field[0] = "mrn";
        //Creating array for data
        String[] data = new String[1];
        data[0] = mrn;

        PutData putData = new PutData("http://pxassignment123.atwebpages.com/getadmissionnoteslist.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    Toast.makeText(ClientViewClientPage.this, mrn, Toast.LENGTH_SHORT).show();
                    Log.i("Result Note", result);
                    notes = new JSONArray(result);
                    noteList.clear();

                    for(int i = 0; i <notes.length() ; i++)
                    {
                        JSONObject names = notes.getJSONObject(i);
                        String notes = names.getString("AdmissionID");
                        notes = notes + " " + names.getString("PatientNotesID");
                        notes = notes + " " + names.getString("Notes");
                        noteList.add(notes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public void ClientDischargeFeedbackPage (View view)
    {
        startActivity(new Intent(ClientViewClientPage.this, ClientFinaliseFeedbackPage.class));
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