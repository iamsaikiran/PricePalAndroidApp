package com.app.pricepal.main;
import static com.google.android.gms.vision.L.TAG;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.app.pricepal.MainActivity;
import com.app.pricepal.R;
import com.app.pricepal.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends BaseActivity {
    Button signup_btn;
    TextView login_tv;
    EditText userName_et, password_et, firstName_et, lastName_et;
    String email, password, fName, lName;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().hide();

        signup_btn = findViewById(R.id.signup_btn);
        login_tv = findViewById(R.id.login_tv);

        userName_et = findViewById(R.id.email_st);
        password_et = findViewById(R.id.password_et);
        firstName_et = findViewById(R.id.fname_et);
        lastName_et = findViewById(R.id.lname_et);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        signup_btn.setOnClickListener(this::signUp);
        login_tv.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    private void signUp(View view) {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        email = userName_et.getText().toString().trim();
        password = password_et.getText().toString().trim();
        fName = firstName_et.getText().toString().trim();
        lName = lastName_et.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {

                            Snackbar.make(view, "error: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view1) {
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.teal_200))
                                    .show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), fName, lName, user.getEmail());
        // Go to MainActivity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(firstName_et.getText().toString())) {
            firstName_et.setError("Required");
            result = false;
        } else {
            firstName_et.setError(null);
        }

        if (TextUtils.isEmpty(lastName_et.getText().toString())) {
            lastName_et.setError("Required");
            result = false;
        } else {
            lastName_et.setError(null);
        }

        if (TextUtils.isEmpty(userName_et.getText().toString())) {
            userName_et.setError("Required");
            result = false;
        } else {
            userName_et.setError(null);
        }

        if (TextUtils.isEmpty(password_et.getText().toString())) {
            password_et.setError("Required");
            result = false;
        } else {
            password_et.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String fName, String lName, String email) {
        User user = new User(fName, lName, email, userId);
        mDatabase.child("users").child(userId).setValue(user);
    }
}