package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class ClientAddAdmissionPage extends AppCompatActivity
{
    EditText mrn;
    EditText PainType;
    EditText painRegion;

    SharedPreferences pref;
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
            mrn = (EditText)findViewById(R.id.patient_mrn_number);
            Intent intent = getIntent();
            String str = intent.getStringExtra("mrn");
            if(!(str==null))
            {
                mrn.setText(str);
                Toast.makeText(ClientAddAdmissionPage.this, str, Toast.LENGTH_SHORT).show();
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
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientAddAdmissionPage.this, ClientAddAdmissionPage.class));
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
        String mrnString = mrn.getText().toString();
        String painTypeString = PainType.getText().toString();
        String painRegionString = painRegion.getText().toString();
        String userNameString = pref.getString("Username",null);
        //Toast.makeText(ClientAddAdmissionPage.this, userNameString , Toast.LENGTH_SHORT).show();
        addAdmissionDatabase(mrnString, userNameString, painTypeString, painRegionString);
        //startActivity(new Intent(ClientAddAdmissionPage.this, ClientHomePage.class));
    }

    public void addAdmissionDatabase(String mrnString, String usernameString, String painTypeString, String painRegionString )
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
                String[] field = new String[4];
                field[0] = "username";
                field[1] = "paintype";
                field[2] = "mrn";
                field[3] = "painregion";

                //Creating array for data
                String[] data = new String[4];
                data[0] = usernameString;
                data[1] = painTypeString;
                data[2] = mrnString;
                data[3] = painRegionString;

                PutData putData = new PutData("http://pxassignment123.atwebpages.com/clientaddadmission.php", "POST", field, data);
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
                            Toast.makeText(ClientAddAdmissionPage.this, putData.getResult() , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
    }
}