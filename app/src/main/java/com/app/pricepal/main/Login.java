package com.app.pricepal.main;

import static android.content.ContentValues.TAG;
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
import com.app.pricepal.admin.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends BaseActivity {
    Button login_btn, admin_btn;
    TextView tvSignup;
    EditText email_et;
    EditText password_et;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        tvSignup=findViewById(R.id.signup_tv);
        login_btn=findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            }
        };
        email_et=findViewById(R.id.email_et);
        password_et=findViewById(R.id.password_et);
        admin_btn=findViewById(R.id.admin_btn);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        login_btn.setOnClickListener(view ->
                signIn(email_et.getText().toString().trim(),password_et.getText().toString().trim(),view));
        tvSignup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Signup.class)));
        admin_btn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AdminActivity.class)));
    }
    private void signIn(String email, String password,View view) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Snackbar.make(view, "Invalid Credentials!!", Snackbar.LENGTH_SHORT)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view1) {
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.teal_200))
                                    .show();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = email_et.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_et.setError("Required.");
            valid = false;
        } else {
            email_et.setError(null);
        }

        String password = password_et.getText().toString();
        if (TextUtils.isEmpty(password)) {
            password_et.setError("Required.");
            valid = false;
        } else {
            password_et.setError(null);
        }
        return valid;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}