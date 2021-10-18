package my.bop.finalassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ClientViewMedicationClientPage extends AppCompatActivity
{
    SharedPreferences pref;
    String medicationstayID;
    String medicationid2;
    String medicationIDChanger;
    String mrn;
    String admissionid;
    String username;
    String role;
    String doseamount;
    String datetime;
    ArrayList<String> medicationSingleList;
    ArrayAdapter<String> medicationAdapater;
    JSONArray med;
    EditText date_time_in;
    EditText doseamounttext;
    String datetime3;
    MaterialAutoCompleteTextView autoedittext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";

        if( role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_view_medication_client_page);
            Intent intent = getIntent();
            medicationstayID = intent.getStringExtra("medicationstayid");
            mrn = intent.getStringExtra("mrn");
            medicationid2 = intent.getStringExtra("medicationid");
            admissionid = intent.getStringExtra("admissionid");
            medicationIDChanger = intent.getStringExtra("medicationid");
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
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            Toast.makeText(ClientViewMedicationClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewMedicationClientPage.this,  MainActivity.class));
        }

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
                        Toast.makeText(ClientViewMedicationClientPage.this, "Could not fetch medication.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewMedicationClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
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
                        Toast.makeText(ClientViewMedicationClientPage.this, "Could not get medication list for search.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewMedicationClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
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
                        Toast.makeText(ClientViewMedicationClientPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewMedicationClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Deleted":
                        return true;
                }
            }
        }

        return false;
    }


    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewMedicationClientPage.this, ClientAddAdmissionPage.class));
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
                    startActivity(new Intent(ClientViewMedicationClientPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void DeleteMedButton(View view)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    if(delMedication())
                    {
                        Intent intent = new Intent(this, ClientAdmissionMedicationPage.class);
                        intent.putExtra("mrn", mrn);
                        intent.putExtra("admissionid", admissionid);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        myQuittingDialogBox.show();
    }

    public void goBackButton(View view)
    {
        Intent intent = new Intent(this, ClientAdmissionMedicationPage.class);
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
                        Toast.makeText(ClientViewMedicationClientPage.this, "Could not modify medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewMedicationClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
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
                Intent intent = new Intent(getApplicationContext(), ClientViewMedicationClientPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                intent.putExtra("medicationstayid", medicationstayID);
                intent.putExtra("medicationid", medicationIDChanger);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), ClientViewMedicationClientPage.class);
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
        Intent intent = new Intent(getApplicationContext(), ClientViewGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);

    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientFinaliseFeedbackPage.class);
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

                new TimePickerDialog(ClientViewMedicationClientPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ClientViewMedicationClientPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}