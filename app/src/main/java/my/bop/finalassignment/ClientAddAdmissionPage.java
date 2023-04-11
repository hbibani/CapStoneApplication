package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ClientAddAdmissionPage extends AppCompatActivity
{
    EditText mrn;
    EditText PainType;
    EditText painRegion;
    EditText bedNumber;
    EditText date_time_in;
    String datetime;
    SharedPreferences pref;
    Spinner timerforpain;
    String mrnString;
    String painTypeString;
    String painRegionString;
    String bedNumberString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";

        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_add_admission_page);
            timerforpain = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.Timer_Array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timerforpain.setAdapter(adapter);
            mrn = (EditText)findViewById(R.id.patient_mrn_number);
            Intent intent = getIntent();
            String str = intent.getStringExtra("mrn");
            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            datetime = "";
            date_time_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog(date_time_in);
                }
            });
            if(!(str==null))
            {
                mrn.setText(str);
            }

        }
        else
        {
            Toast.makeText(ClientAddAdmissionPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientAddAdmissionPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_assigned_patient_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientSearchAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientAddAdmissionPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void addAdmission(View view)
    {
        mrn = findViewById(R.id.patient_mrn_number);
        PainType = findViewById(R.id.editPaintType);
        painRegion = findViewById(R.id.editPainRegion);
        bedNumber = findViewById(R.id.bednumber);
        timerforpain.findViewById(R.id.spinner1);
        String timerForPain = String.valueOf(timerforpain.getSelectedItem());
        mrnString = mrn.getText().toString();
        painTypeString = PainType.getText().toString();
        painRegionString = painRegion.getText().toString();
        bedNumberString = bedNumber.getText().toString();
        boolean validatemrn = validateMRN();
        boolean validatepaintype = validatePainType();
        boolean validatepainregion = validatePainRegion();
        boolean validatebednumber = validateBedNumber();
        boolean validatedate = validateDate();

        if(validatemrn && validatepainregion && validatepaintype && validatebednumber && validatedate)
        {
            addAdmissionDatabase(mrnString, painTypeString, painRegionString, bedNumberString, timerForPain);
        }
    }

    private boolean validateDate()
    {
        if(datetime.isEmpty())
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

    private boolean validateBedNumber()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(bedNumberString.isEmpty())
        {
            bedNumber.setError("Field cannot be empty");
            return false;
        }
        else if(!bedNumberString.matches(regex ))
        {
            bedNumber.requestFocus();
            bedNumber.setError("Bed Number of length 1-30");
            return false;
        }
        else
        {
            bedNumber.setError(null);
            return true;
        }
    }

    private boolean validatePainType()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(painTypeString.isEmpty())
        {
            PainType.setError("Field cannot be empty");
            return false;
        }
        else if(!painTypeString.matches(regex ))
        {
            PainType.requestFocus();
            PainType.setError("Pain Type of length 1-100");
            return false;
        }
        else
        {
            PainType.setError(null);
            return true;
        }
    }

    private boolean validatePainRegion()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(painRegionString.isEmpty())
        {
            painRegion.setError("Field cannot be empty");
            return false;
        }
        else if(!painRegionString.matches(regex ))
        {
            painRegion.requestFocus();
            painRegion.setError("Pain region of length 1-100");
            return false;
        }
        else
        {
            painRegion.setError(null);
            return true;
        }
    }

    private boolean validateMRN()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(mrnString.isEmpty())
        {
            mrn.setError("Field cannot be empty");
            return false;
        }
        else if(!mrnString.matches(regex ))
        {
            mrn.requestFocus();
            mrn.setError("MRN of length 1-30");
            return false;
        }
        else
        {
            mrn.setError(null);
            return true;
        }
    }

    public void addAdmissionDatabase(String mrnString, String painTypeString, String painRegionString, String bedNumberString, String timerforpainscore )
    {
        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[6];
        field[0] = "paintype";
        field[1] = "mrn";
        field[2] = "painregion";
        field[3] = "datetime";
        field[4] = "bednumber";
        field[5] = "timer";

        //Creating array for data
        String[] data = new String[6];
        data[0] = painTypeString;
        data[1] = mrnString;
        data[2] = painRegionString;
        data[3] = datetime;
        data[4] = bedNumberString;
        data[5] = timerforpainscore;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/clientaddadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.d("Result", result);
                switch (result)
                {
                    case "patientid":
                        Toast.makeText(ClientAddAdmissionPage.this, "Could not retrieve patientid.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Already admitted.":
                        Toast.makeText(ClientAddAdmissionPage.this, "Patient already admitted.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Insert Admission error.":
                        Toast.makeText(ClientAddAdmissionPage.this, "Could not insert into database.", Toast.LENGTH_SHORT).show();
                        break;
                    case "get userid":
                        Toast.makeText(ClientAddAdmissionPage.this, "Could not retrieve the userid.", Toast.LENGTH_SHORT).show();
                        break;
                    case "No admissions with active status.":
                        Toast.makeText(ClientAddAdmissionPage.this, "Patient has active admission.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Problem creating graph.":
                        Toast.makeText(ClientAddAdmissionPage.this, "Could not produce graph for admission.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientAddAdmissionPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Intent intent = new Intent(getApplicationContext(), ClientHomePage.class);
                        Toast.makeText(ClientAddAdmissionPage.this, "Admission added successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                }
            }
        }
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
                        datetime = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(ClientAddAdmissionPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ClientAddAdmissionPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}