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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminAdmissionGraphOtherPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{


    SharedPreferences pref;
    String username;
    String role;
    String mrn;
    String admissionid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextInputEditText bed;
    String bednumber2;
    EditText date_time_in;
    String datetime;
    EditText date_time_in1;
    String datetime1;
    Switch switchstatus;
    String status;
    Spinner timerforpain;
    String timerForPainGraph;
    String painTypeString;
    String painRegionString;
    EditText paintypeedit;
    EditText painregionedit;
    JSONArray admissionarray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type", null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_admin_admission_graph_other_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            timerforpain = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.Timer_Array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            switchstatus = (Switch) findViewById(R.id.status_admission);
            timerforpain.setAdapter(adapter);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            SharedPreferences.Editor editor1 = pref.edit();
            editor1.putString("mrn", mrn);
            editor1.putString("admissionid", admissionid);
            editor1.apply();
            painregionedit = (EditText) findViewById(R.id.editPainRegion);
            paintypeedit = (EditText) findViewById(R.id.editPaintType);
            bed = (TextInputEditText) findViewById(R.id.bednumber);
            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            datetime = " ";
            date_time_in1=findViewById(R.id.date_time_input2);
            date_time_in1.setInputType(InputType.TYPE_NULL);
            datetime1 = " ";

            if(getAllValues2())
            {
                painregionedit.setText(painRegionString);
                paintypeedit.setText(painTypeString);
                timerforpain.setSelection(Integer.parseInt(timerForPainGraph)-15);
                bed.setText(bednumber2);
                if(status.equals("1"))
                {
                    switchstatus.setChecked(true);
                }
                else if(status.equals("0"))
                {
                    switchstatus.setChecked(false);
                }

                date_time_in.setText(datetime);
                date_time_in1.setText(datetime1);
            }

            date_time_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog1(date_time_in);
                }
            });

            date_time_in1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog1(date_time_in1);
                }
            });
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void showDateTimeDialog1(final EditText date_time_in0in)
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
                        date_time_in0in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(AdminAdmissionGraphOtherPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AdminAdmissionGraphOtherPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public boolean getAllValues2()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/getAllAdmissionDetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not get admission details.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            admissionarray = new JSONArray(result);
                            for(int i = 0; i < admissionarray.length();i++)
                            {
                                JSONObject names =  admissionarray.getJSONObject(i);
                                bednumber2 = names.getString("Bed");
                                status = names.getString("Status");
                                datetime = names.getString("StartTime");
                                datetime1 = names.getString("EndTime");
                                painTypeString = names.getString("PainType");
                                painRegionString = names.getString("PainRegion");
                                timerForPainGraph = names.getString("PainTimer");
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

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_assigned_patient_client:
            {
                Intent intent = new Intent(getApplicationContext(), ClientSearchAdmissionPage.class);
                startActivity(intent);
            }
            break;
            case R.id.action_logout_client:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionGraphOtherPage.this,  MainActivity.class));
            }
            break;
        }
    }


    public void ModifyBed(View view)
    {
        String modifyBedNumber = bed.getText().toString();
        boolean validatebednumber = validateBedNumber(modifyBedNumber);
        if(validatebednumber)
        {
            updateBedInfo(modifyBedNumber);
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphOtherPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    private void updateBedInfo(String modifyBed)
    {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] = "bednumber";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = modifyBed;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/modifyBedNumber.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not modify bed number.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                    {
                        bednumber2 = modifyBed;
                        break;
                    }
                }
            }
        }

    }

    private boolean validateBedNumber(String bedNumberString)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(bedNumberString.isEmpty())
        {
            bed.setError("Field cannot be empty");
            return false;
        }
        else if(!bedNumberString.matches(regex ))
        {
            bed.requestFocus();
            bed.setError("Bed Number of length 1-30");
            return false;
        }
        else
        {
            bed.setError(null);
            return true;
        }
    }


    public void ModifyPainType(View view)
    {
        String modifypaintype = paintypeedit.getText().toString();
        String modifypainregion = painregionedit.getText().toString();
        boolean validatetype = validatePainType(modifypaintype);
        boolean validateregion = validatePainRegion(modifypainregion);

        if(validatetype && validateregion)
        {
            updatePainDetails(modifypaintype, modifypainregion);
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphOtherPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }


    private void updatePainDetails(String updatepaintype, String updatepainregion)
    {
        String[] field = new String[3];
        field[0] = "admissionid";
        field[1] = "painregion";
        field[2] = "paintype";

        //Creating array for data
        String[] data = new String[3];
        data[0] = admissionid;
        data[1] = updatepainregion;
        data[2] = updatepaintype;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/modifyPainDetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not modify pain information.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Successfully modifyed pain information.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

    }

    private boolean validatePainType(String painTypeValid)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(painTypeValid.isEmpty())
        {
            paintypeedit.setError("Field cannot be empty");
            return false;
        }
        else if(!painTypeValid.matches(regex ))
        {
            paintypeedit.requestFocus();
            paintypeedit.setError("Pain Type of length 1-100");
            return false;
        }
        else
        {
            paintypeedit.setError(null);
            return true;
        }
    }


    private boolean validatePainRegion(String painRegionValid)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(painRegionValid.isEmpty())
        {
            painregionedit.setError("Field cannot be empty");
            return false;
        }
        else if(!painRegionValid.matches(regex ))
        {
            painregionedit.requestFocus();
            painregionedit.setError("Pain region of length 1-100");
            return false;
        }
        else
        {
            painregionedit.setError(null);
            return true;
        }
    }

    public void ModifyTimes(View view)
    {
        String datetime3 = date_time_in.getText().toString();
        String datetime4 = date_time_in1.getText().toString();
        Log.d("Datetime4", datetime4);
        modifyAdmissionTime(datetime3,datetime4);
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphOtherPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void ModifyTimer(View view)
    {
        modifyTimerFunction();
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphOtherPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Interval succesfully modified.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void modifyTimerFunction()
    {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] = "paintimer";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = timerforpain.getSelectedItem().toString();;


        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/modifyPainTimer.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not modify timer interval.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Time interval modified.", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }

    }

    public void modifyAdmissionTime(String datetime3, String datetime4)
    {
        String[] field = new String[3];
        field[0] = "admissionid";
        field[1] = "starttime";
        field[2] = "endtime";

        //Creating array for data
        String[] data = new String[3];
        data[0] = admissionid;
        data[1] = datetime3;
        data[2] = datetime4;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/modifyadmissiontime.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not modify admission times.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Admission times modified.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

    }


    public void ModifyStatus(View view)
    {
        String switchstatusupdate;
        if (switchstatus.isChecked()) { switchstatusupdate = "1"; } else { switchstatusupdate = "0"; }
        changeStatusAdmission(switchstatusupdate);
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphOtherPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void changeStatusAdmission(String statusupdate)
    {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] = "status";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = statusupdate;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/updatestatusadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Could not modify status.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminAdmissionGraphOtherPage.this, "Status has been modified.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

    }

}