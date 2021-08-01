package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Looper;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ClientAddPatientPage extends AppCompatActivity
{

    EditText mrn;
    //EditText gender;
    EditText age;
    EditText weight;
    EditText username1;
    EditText password1;
    String usernameString;
    String passwordString;
    String mrnString;
    String genderString;
    String weightString;
    String ageString;
    SharedPreferences pref;
    Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String[] sex = {"Male", "Female"};
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_add_patient_page);
            gender = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.Gender_Array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gender.setAdapter(adapter);
            Toast.makeText(ClientAddPatientPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ClientAddPatientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientAddPatientPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientAddPatientPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientAddPatientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientAddPatientPage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_logout_client:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ClientAddPatientPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void addPatientButton(View view)
    {
        age = findViewById(R.id.editAge);
        weight = findViewById(R.id.editWeight);
        mrn = findViewById(R.id.patient_mrn_number);
        //gender = findViewById(R.id.editGender);
        username1 = findViewById(R.id.username_client);
        password1 = findViewById(R.id.password_client);
        String usernameString = username1.getText().toString();
        String passwordString = password1.getText().toString();
        String mrnString = mrn.getText().toString();
        String ageString = age.getText().toString();
        String weightString = weight.getText().toString();
        //String genderString = gender.getText().toString();
        gender.findViewById(R.id.spinner1);
        String genderString = String.valueOf(gender.getSelectedItem());

        if(!TextUtils.isDigitsOnly(ageString) || !TextUtils.isDigitsOnly(mrnString) || !TextUtils.isDigitsOnly(weightString) )
        {
            Toast.makeText(ClientAddPatientPage.this, "MRN, age, weight must be in numerical form." , Toast.LENGTH_SHORT).show();
        }
        else if (usernameString.length() < 5 ||  usernameString.length() > 15 )
        {
            Toast.makeText(ClientAddPatientPage.this, "Username is to be of length between 5-15" , Toast.LENGTH_SHORT).show();
        }else if(passwordString.length() < 5 ||  passwordString.length() > 15 )
        {
            Toast.makeText(ClientAddPatientPage.this, "Password is to be of length between 5-15" , Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(usernameString) && !TextUtils.isEmpty(passwordString) && !TextUtils.isEmpty(mrnString)  && !TextUtils.isEmpty(ageString)  && !TextUtils.isEmpty(weightString ) && !TextUtils.isEmpty(genderString ))
        {
            signUpPatient(usernameString, passwordString, mrnString, genderString, ageString, weightString);
        }
        else
        {
            Toast.makeText(ClientAddPatientPage.this, "All Fields Required" , Toast.LENGTH_SHORT).show();
        }

    }

    public void signUpPatient(String usernameString, String passwordString, String mrnString, String genderString, String ageString, String weightString)
    {
        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[6];
                field[0] = "username";
                field[1] = "password";
                field[2] = "mrn";
                field[3] = "gender";
                field[4] = "age";
                field[5] = "weight";
                //Creating array for data
                String[] data = new String[6];
                data[0] = usernameString;
                data[1] = passwordString;
                data[2] = mrnString;
                data[3] = genderString;
                data[4] = ageString;
                data[5] = weightString;
                PutData putData = new PutData("http://pxassignment123.atwebpages.com/signup.php", "POST", field, data);
                if (putData.startPut())
                {
                    if (putData.onComplete())
                    {
                        String result = putData.getResult();
                        if(result.equals("Sign Up Success"))
                        {
                            //startActivity(new Intent(ClientAddPatientPage.this, ClientAddAdmissionPage.class));
                            Intent intent = new Intent(getApplicationContext(), ClientAddAdmissionPage.class);
                            intent.putExtra("mrn", mrnString);
                            startActivity(intent);
                            //Toast.makeText(ClientAddPatientPage.this, putData.getResult() , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ClientAddPatientPage.this, putData.getResult() , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
    }
}