package com.example.adsadjustment.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adsadjustment.R;
import com.example.adsadjustment.activities.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageView backbtn;
    Button loginbtn, nextSignupbtn;
    TextView titleText;

    TextInputLayout fullName, Email, PhoneNumber, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backbtn = findViewById(R.id.signup_back);
        loginbtn = findViewById(R.id.btn_login);
        nextSignupbtn = findViewById(R.id.singupNextbtn);
        titleText = findViewById(R.id.title_text);

        fullName = findViewById(R.id.full_name);
        Email = findViewById(R.id.email);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        Password = findViewById(R.id.password_text);
    }

    public void backToLogin(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void Registration(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user..");
        if (!validateFullName() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            return;
        }
        progressDialog.show();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(Email.getEditText().getText().toString(), Password.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AddUserData(firebaseAuth.getCurrentUser().getUid());
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Faild to register user! try agin", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                            finish();
                        }

                    }
                });

    }

    private void AddUserData(String uid) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference();

        Map<String, Object> map = new HashMap<>();

        map.put("email", Email.getEditText().getText().toString());
        map.put("name", fullName.getEditText().getText().toString());
        map.put("phoneNumber", PhoneNumber.getEditText().getText().toString());

        reference.child("Users").child(uid).setValue(map);
    }

    //Validation
    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullName.setError("Name is required!");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = Email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            Email.setError("Email is required!");
            return false;
        } else if (!val.matches(checkEmail)) {
            Email.setError("Enter valid email!");
            return false;
        } else {
            Email.setError(null);
            Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String val = PhoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            PhoneNumber.setError("Phone number is required!");
            return false;
        } else if (val.length() < 11) {
            PhoneNumber.setError("Phone lenght is not sufficent");
            return false;
        }

        PhoneNumber.setError(null);
        PhoneNumber.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        String val = Password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=\\S+$)" +
                ".{6,}" +
                "$";

        if (val.isEmpty()) {
            Password.setError("Email is required!");
            return false;
        } else if (!val.matches(checkPassword)) {
            Password.setError("Password must contains minimum six characters, at least one letter and one number");
            return false;
        } else {
            Password.setError(null);
            Password.setErrorEnabled(false);
            return true;
        }
    }
}