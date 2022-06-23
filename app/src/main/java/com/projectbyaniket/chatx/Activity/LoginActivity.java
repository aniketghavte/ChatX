package com.projectbyaniket.chatx.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projectbyaniket.chatx.R;

public class LoginActivity extends AppCompatActivity {

    TextView text_SignUp;
    EditText login_Email, login_password;
    TextView SignBtn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        text_SignUp = findViewById(R.id.idTxtSignUp);
        login_Email = findViewById(R.id.idLoginEmail);
        login_password = findViewById(R.id.idLoginPass);
        SignBtn = findViewById(R.id.IdBtnLogin);

        SignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = login_Email.getText().toString();
                String password = login_password.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please provide Valid Email or Password", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    login_Email.setError("Invalid Email");
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    login_password.setError("Invalid Password");
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        text_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
    }
}