package com.kyle.admin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyle.admin.ui.MainActivity;

import es.dmoral.toasty.Toasty;
public class login extends AppCompatActivity {
    String email,password,wantedEmail,wantedPassword;
    EditText editEmail,editPassword;
    ProgressDialog progressDialog;
    CheckBox RememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail=findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPassword);
        RememberMe=findViewById(R.id.RememberMe);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.id_7));
        progressDialog.setCancelable(false);
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        boolean RememberMe = prefs.getBoolean("RememberMe", false);
        if (RememberMe){
            startActivity(new Intent(login.this, MainActivity.class));
        }
    }
    public void login(View view) {
        loadData();
    }
    public void loadData() {
        email=editEmail.getText().toString();
        password=editPassword.getText().toString();
        if (password.isEmpty()||email.isEmpty()){
            Toasty.info(login.this, R.string.id_1, Toast.LENGTH_SHORT, true).show();
        }else {
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("data").child("login").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot data) {

                    wantedEmail = String.valueOf(data.child("email").getValue());
                    wantedPassword = String.valueOf(data.child("password").getValue());
                    if (!wantedEmail.isEmpty() && !wantedEmail.equals("null")&&!wantedPassword.isEmpty() && !wantedPassword.equals("null")) {
                        if (wantedEmail.equalsIgnoreCase(email) && wantedPassword.equalsIgnoreCase(password)) {
                         if (RememberMe.isChecked()){
                          getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("RememberMe", true).apply();
                         }
                            startActivity(new Intent(login.this, MainActivity.class));
                        }else {
                            Toasty.info(login.this, R.string.id_52, Toast.LENGTH_SHORT, true).show();

                        }
                    }

                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });
        }

    }
}