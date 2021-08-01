package com.example.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdminRoleModifyPage extends AppCompatActivity
{
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
            setContentView(R.layout.activity_admin_role_modify_page);
            Toast.makeText(AdminRoleModifyPage.this, pref.getString("Type",null), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AdminRoleModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminRoleModifyPage.this,  MainActivity.class));
        }
    }


    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminRoleModifyPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void modifyRoleButton(View view)
    {
         startActivity(new Intent(AdminRoleModifyPage.this, AdminRoleModifyPage.class));
    }

    public void deleteRoleButton(View view)
    {
        startActivity(new Intent(AdminRoleModifyPage.this, AdminRolePage.class));
    }

    public void goBackButton(View view)
    {
        startActivity(new Intent(AdminRoleModifyPage.this, AdminRolePage.class));
    }
}