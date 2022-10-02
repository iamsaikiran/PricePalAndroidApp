package com.app.pricepal.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pricepal.MainActivity;
import com.app.pricepal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AdminActivity extends AppCompatActivity {
    Button login_btn_admin, loginpage;
    EditText email_et_admin;
    EditText password_et_admin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        login_btn_admin = findViewById(R.id.login_btn_admin);
        loginpage = findViewById(R.id.login_page);
        email_et_admin = findViewById(R.id.email_et_admin);
        password_et_admin = findViewById(R.id.password_et_admin);

        mAuth = FirebaseAuth.getInstance();

        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        login_btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_et_admin.getText().toString().trim();
                String userpassword = password_et_admin.getText().toString();
                if (!email.equals("") && !userpassword.equals("")) {
                    signin(email, userpassword);
                } else {
                    Toast.makeText(AdminActivity.this, "Please enter the Username and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signin(String email, String userpassword) {
        if (email.equals("pricepalapp@gmail.com") && userpassword.equals("Pricepal@123")) {
            Intent i = new Intent(AdminActivity.this, ManagePrices.class);
            startActivity(i);
            Toast.makeText(AdminActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AdminActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
        }

    }
}

