package my.bop.finalassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientModifyPatientPage extends AppCompatActivity
{
    SharedPreferences pref;
    String username1;
    String password1;
    String username2;
    String password2;
    String userID;
    String mrn1;
    String gender1;
    String age1;
    String weight1;
    String mrn2;
    String gender2;
    String age2;
    String weight2;
    String patientID1;
    Spinner gender;
    EditText age;
    EditText weight;
    EditText username;
    EditText password;
    EditText mrn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_modify_patient_page);
            gender = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.Gender_Array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gender.setAdapter(adapter);
            Intent intent = getIntent();
            mrn1 = intent.getStringExtra("mrn");
            age1 = intent.getStringExtra("age");
            weight1 = intent.getStringExtra("weight");
            gender1 = intent.getStringExtra("gender");
            userID = intent.getStringExtra("userid");
            patientID1 = intent.getStringExtra("patientid");

            if(!getUsernameAndPassword())
            {
                Toast.makeText(ClientModifyPatientPage.this, "Cannot get username and password.", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), ClientSearchPatientPage.class);
                startActivity(intent1);
            }

            age = (EditText) findViewById(R.id.editAge);
            weight = (EditText) findViewById(R.id.editWeight);
            mrn = (EditText)findViewById(R.id.patient_mrn_number);
            gender = (Spinner) findViewById(R.id.spinner1);
            username = (EditText) findViewById(R.id.username_client);
            password = (EditText) findViewById(R.id.password_client);

            age.setText(age1);
            weight.setText(weight1);
            mrn.setText(mrn1);
            username.setText(username1);
            password.setText(password1);

            if(gender1.equals("Male"))
            {
                gender.setSelection(0);
            }
            else
            {
                gender.setSelection(1);
            }

        }
        else
        {
            Toast.makeText(ClientModifyPatientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientModifyPatientPage.this,  MainActivity.class));
        }
    }

    public boolean getUsernameAndPassword()
    {
        String[] field = new String[1];
        field[0] = "userid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = userID;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/retusernameandpassword.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not retrieve username and password.":
                        Toast.makeText(ClientModifyPatientPage.this, "No username or password for that account.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientModifyPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            JSONArray array = new JSONArray(result);
                            JSONObject names = array.getJSONObject(0);
                            username1 = names.getString("Username");
                            password1 = names.getString("Password");
                            return true;

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }

                }
            }
        }

        return false;
    }

    public void updatePasswordAndUsername()
    {
        String[] field = new String[3];
        field[0] = "username";
        field[1] = "password";
        field[2] = "userid";

        //Creating array for data
        String[] data = new String[3];
        data[0] = username2;
        data[1] = password2;
        data[2] = userID;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/updatepatientusernameandpassword.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "Could not update username and password.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not update username and password.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientModifyPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                    {
                        Toast.makeText(ClientModifyPatientPage.this, "Updated username and password successfully.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    public boolean updatePatientDetails()
    {
        String[] field = new String[5];
        field[0] = "mrn";
        field[1] = "gender";
        field[2] = "age";
        field[3] = "weight";
        field[4] = "userid";
        //Creating array for data

        String[] data = new String[5];
        data[0] = mrn2;
        data[1] = gender2;
        data[2] = age2;
        data[3] = weight2;
        data[4] = userID;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/updatepatientdetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not update patient info.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not update patient info..", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientModifyPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                    {
                        Toast.makeText(ClientModifyPatientPage.this, "Updated patient details successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean deletePatient()
    {
        String[] field = new String[2];
        field[0] = "userid";
        field[1] = "patientid";
        //Creating array for data

        String[] data = new String[2];
        data[0] = userID;
        data[1] = patientID1;
        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/deletepatient.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "Could not delete patient.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not delete patient.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete user.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not delete user.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientModifyPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(ClientModifyPatientPage.this, "Deleted patient successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void DeletePatientButton(View view)
    {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    if(deletePatientSingle())
                    {
                        Intent intent = new Intent(getApplicationContext(), ClientSearchPatientPage.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), ClientModifyPatientPage.class);
                        intent.putExtra("mrn", mrn1);
                        intent.putExtra("age", age1);
                        intent.putExtra("weight", weight1);
                        intent.putExtra("gender", gender1);
                        intent.putExtra("userid", userID);
                        intent.putExtra("patientid", patientID1);
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

    private boolean deletePatientSingle()
    {
        String[] field = new String[2];
        field[0] = "userid";
        field[1] = "patientid";
        //Creating array for data

        String[] data = new String[2];
        data[0] = userID;
        data[1] = patientID1;
        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/deletepatientsingle.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not delete patient.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not delete patient.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete user.":
                        Toast.makeText(ClientModifyPatientPage.this, "Could not delete user.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientModifyPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(ClientModifyPatientPage.this, "Deleted patient successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void GoBackButton(View view)
    {
        Intent intent1 = new Intent(getApplicationContext(), ClientSearchPatientPage.class);
        startActivity(intent1);
    }

    public void DeletePatientAllButton(View view)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    if(deletePatient())
                    {
                        Intent intent = new Intent(getApplicationContext(), ClientSearchPatientPage.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), ClientModifyPatientPage.class);
                        intent.putExtra("mrn", mrn1);
                        intent.putExtra("age", age1);
                        intent.putExtra("weight", weight1);
                        intent.putExtra("gender", gender1);
                        intent.putExtra("userid", userID);
                        intent.putExtra("patientid", patientID1);
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

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientModifyPatientPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientModifyPatientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientModifyPatientPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientModifyPatientPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void onClickStaffSearch(View view)
    {
        startActivity(new Intent(ClientModifyPatientPage.this, ClientSearchStaffAdmissionPage.class));
    }

    public void onClickPatientSearch(View view)
    {
        startActivity(new Intent(ClientModifyPatientPage.this, ClientSearchAdmissionPage.class));
    }

    public void onClickPatientActualSearch(View view)
    {
        startActivity(new Intent(ClientModifyPatientPage.this, ClientSearchPatientPage.class));
    }


    private boolean validateWeight()
    {
        String regex = "[0-9]{1,5}+";
        if(weight2.isEmpty())
        {
            weight.setError("Field cannot be empty");
            return false;
        }
        else if(!weight2.matches(regex ))
        {
            weight.requestFocus();
            weight.setError("Weight must be numeric and of length 1-5");
            return false;
        }
        else
        {
            weight.setError(null);
            return true;
        }
    }

    private boolean validateAge()
    {
        String regex = "[0-9]{1,4}+";
        if(age2.isEmpty())
        {
            age.setError("Field cannot be empty");
            return false;
        }
        else if(!age2.matches(regex ))
        {
            age.requestFocus();
            age.setError("Age must be numeric and of length 1-4");
            return false;
        }
        else
        {
            age.setError(null);
            return true;
        }
    }


    private boolean validateMRN()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(mrn2.isEmpty())
        {
            mrn.setError("Field cannot be empty");
            return false;
        }
        else if(!mrn2.matches(regex ))
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

    private boolean validatePassword()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(password2.isEmpty())
        {
            password.setError("Field cannot be empty");
            return false;
        }
        else if(!password2.matches(regex ))
        {
            password.requestFocus();
            password.setError("Password of length 8-50");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    private boolean validateUserName()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(username2.isEmpty())
        {
            username.setError("Field cannot be empty");
            return false;
        }
        else if(!username2.matches(regex ))
        {
            username.requestFocus();
            username.setError("Username of length 8-50");
            return false;
        }
        else
        {
            username.setError(null);
            return true;
        }
    }

    public void ModifyPatientButton(View view)
    {
        age2 = age.getText().toString();
        weight2 = weight.getText().toString();
        mrn2 = mrn.getText().toString();
        gender2 = gender.getSelectedItem().toString();

        boolean agevalidate = validateAge();
        boolean weightvalidate = validateWeight();
        boolean mrnvalidate = validateMRN();

        if(agevalidate && weightvalidate && mrnvalidate)
        {
            if(updatePatientDetails())
            {
                Intent intent = new Intent(getApplicationContext(), ClientModifyPatientPage.class);
                intent.putExtra("mrn", mrn2);
                intent.putExtra("age", age2);
                intent.putExtra("weight", weight2);
                intent.putExtra("gender", gender2);
                intent.putExtra("userid", userID);
                intent.putExtra("patientid", patientID1);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(),  ClientModifyPatientPage.class);
                intent.putExtra("mrn", mrn1);
                intent.putExtra("age", age1);
                intent.putExtra("weight", weight1);
                intent.putExtra("gender", gender1);
                intent.putExtra("userid", userID);
                intent.putExtra("patientid", patientID1);
                startActivity(intent);
            }

        }
    }

    public void UpdateLoginDetails(View view)
    {
        username2 = username.getText().toString();
        password2 = password.getText().toString();

        boolean usernamevalidate = validateUserName();
        boolean passwordvalidate = validatePassword();

        if(usernamevalidate && passwordvalidate)
        {
            updatePasswordAndUsername();
            Intent intent = new Intent(getApplicationContext(), ClientModifyPatientPage.class);
            intent.putExtra("mrn", mrn1);
            intent.putExtra("age", age1);
            intent.putExtra("weight", weight1);
            intent.putExtra("gender", gender1);
            intent.putExtra("userid", userID);
            intent.putExtra("patientid", patientID1);
            startActivity(intent);
        }
    }

}