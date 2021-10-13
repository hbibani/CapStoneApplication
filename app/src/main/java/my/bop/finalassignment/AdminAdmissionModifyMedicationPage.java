package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminAdmissionModifyMedicationPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    String medicationstayID;
    String medicationIDChanger;
    String medicationid2;
    String mrn;
    String admissionid;
    String username;
    String role;
    ArrayList<String> medicationSingleList;
    ArrayAdapter<String> medicationAdapater;
    JSONArray med;
    EditText date_time_in;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String doseamount;
    EditText doseamounttext;
    MaterialAutoCompleteTextView autoedittext;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";

        if( role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_admission_modify_medication_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            Intent intent = getIntent();
            medicationstayID = intent.getStringExtra("medicationstayid");

            mrn = intent.getStringExtra("mrn");
            medicationid2 = intent.getStringExtra("medicationid");
            medicationIDChanger = intent.getStringExtra("medicationid");
            admissionid = intent.getStringExtra("admissionid");
            autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
            doseamounttext = (EditText) findViewById(R.id.amount_dose_text);
            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            datetime3 = " ";



            autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    try
                    {
                        int positionactual = 0;
                        String selected = (String) parent.getItemAtPosition(position);
                        for(int i = 0; i < medicationSingleList.size(); i++)
                        {
                            if(medicationSingleList.get(i).equals(selected))
                            {
                                positionactual = i;
                                break;
                            }
                        }
                        JSONObject names = med.getJSONObject(positionactual);
                        medicationIDChanger = names.getString("MedicationID");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            date_time_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog(date_time_in);
                }
            });

            try
            {
                if(fetchMedication())
                {
                    date_time_in.setText(datetime3);
                    initializeMedicationList();
                }
                else
                {
                    Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not fetch medication.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAdmissionModifyMedicationPage.this,  MainActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionModifyMedicationPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean fetchMedication()
    {
        String[] field = new String[1];
        field[0] = "medicationstayid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationstayID;

        PutData putData = new PutData("http://bopps2130.net/clingetsinglemed.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not retreive medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            JSONArray array = new JSONArray(result);
                            JSONObject names = array.getJSONObject(0);
                            String medbrand = names.getString("MedBrand");
                            medbrand = medbrand + " "  + names.getString("MedChemName");
                            medbrand = medbrand + " " + names.getString("Dosage");
                            medbrand = medbrand + " " + names.getString("DoseForm");
                            datetime3 = names.getString("Time");
                            doseamount = names.getString("Amount");
                            return true;
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }

        return false;
    }

    private void initializeMedicationList() throws JSONException
    {
        medicationSingleList = new ArrayList<String>();
        if(fetchSingleMedList())
        {
            medicationAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicationSingleList);
            autoedittext.setAdapter(medicationAdapater);

            int x = 0;
            for (int i = 0; i < med.length(); i++)
            {
                JSONObject names = med.getJSONObject(i);
                String medbrand = names.getString("MedicationID");
                x = i;
                if(medbrand.equals(medicationid2))
                {
                    break;
                }
            }

            autoedittext.setText(medicationSingleList.get(x));
        }
        else
        {
            Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not initialize list.", Toast.LENGTH_SHORT).show();
        }

        doseamounttext.setText(doseamount);
        date_time_in.setText(datetime3);
    }

    private boolean fetchSingleMedList()
    {
        FetchData fetchData = new FetchData("http://bopps2130.net/fetchAllMeds.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not retreive medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            med = new JSONArray(result);
                            medicationSingleList.clear();
                            for (int i = 0; i < med.length(); i++) {
                                JSONObject names = med.getJSONObject(i);
                                String medbrand = names.getString("MedBrand");
                                medbrand = medbrand + " "  + names.getString("MedChemName");
                                medbrand = medbrand + " " + names.getString("Dosage");
                                medbrand = medbrand + " " + names.getString("DoseForm");
                                medicationSingleList.add(medbrand);
                            }
                            return true;
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean delMedication()
    {
        String[] field = new String[1];
        field[0] = "medicationstayid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationstayID;

        PutData putData = new PutData("http://bopps2130.net/deletemedfromadmission.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Deleted":
                        return true;
                }
            }
        }

        return false;
    }

    public void DeleteMedButton(View view)
    {
        if(delMedication())
        {
            Intent intent = new Intent(this, AdminAdmissionMedicationPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    public void goBackButton(View view)
    {
        Intent intent = new Intent(this, AdminAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    private boolean modifyPatientMedication(String medicationid)
    {
        String[] field = new String[4];
        field[0] = "medicationstayid";
        field[1] = "medicationid";
        field[2] = "datetime";
        field[3] = "amount";

        //Creating array for data
        String[] data = new String[4];
        data[0] = medicationstayID;
        data[1] = medicationid;
        data[2] = datetime3;
        data[3] = doseamount;

        PutData putData = new PutData("http://bopps2130.net/modifymedicationadmission.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete()) {
                String result = putData.getResult();
                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Could not modify medication.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Modified":
                        return true;
                }
            }
        }

        return false;
    }

    private void modifyMedication() throws JSONException
    {
        doseamount = doseamounttext.getText().toString();
        boolean validatedate = validateDate(datetime3);
        boolean validatedoseform = validateDoseAmount(doseamount);

        if(validatedate && validatedoseform)
        {
            if(modifyPatientMedication(medicationIDChanger))
            {
                Intent intent = new Intent(getApplicationContext(), AdminAdmissionModifyMedicationPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                intent.putExtra("medicationstayid", medicationstayID);
                intent.putExtra("medicationid", medicationIDChanger);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), AdminAdmissionModifyMedicationPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                intent.putExtra("medicationstayid", medicationstayID);
                intent.putExtra("medicationid", medicationid2);
                startActivity(intent);
            }


        }
    }

    private boolean validateDoseAmount(String doseAmountString)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(doseAmountString.isEmpty())
        {
            doseamounttext.setError("Field cannot be empty");
            return false;
        }
        else if(!doseAmountString.matches(regex ))
        {
            doseamounttext.requestFocus();
            doseamounttext.setError("Dose amount of length 1-50");
            return false;
        }
        else
        {
            doseamounttext.setError(null);
            return true;
        }
    }

    private boolean validateDate(String datetime1)
    {
        if(datetime1.isEmpty())
        {
            date_time_in.setError("Field cannot be empty");
            return false;
        }
        else
        {
            date_time_in.requestFocus();
            date_time_in.setError(null);
            return true;
        }
    }


    public void ModifyButton(View view)
    {
        try {
            modifyMedication();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickGraph(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionPatientNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }


    private void showDateTimeDialog(final EditText date_time_in)
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime3 = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(AdminAdmissionModifyMedicationPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AdminAdmissionModifyMedicationPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}