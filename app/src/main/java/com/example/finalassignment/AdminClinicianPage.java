package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.os.IResultReceiver;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AdminClinicianPage extends AppCompatActivity
{

    EditText patientmrn;
    EditText gender;
    EditText Age;
    EditText Weight;
    EditText UserName;
    EditText Password;
    Button addpatient;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_clinician_page);
            Toast.makeText(AdminClinicianPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AdminClinicianPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminClinicianPage.this,  MainActivity.class));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminClinicianPage.this,  MainActivity.class));
                }
                break;

        }
    }

    public void addClinicianButton(View view)
    {
        startActivity(new Intent(AdminClinicianPage.this, AdminHomePage.class));
    }

    private void registerNewAccount(final String mrn, final String patient, final String gender, final String age, final String weight, final String username, final String password )
    {

    }

    public void searchClinicanButton(View view) {
        startActivity(new Intent(AdminClinicianPage.this, AdminClinicianModifyPage.class));
    }
}