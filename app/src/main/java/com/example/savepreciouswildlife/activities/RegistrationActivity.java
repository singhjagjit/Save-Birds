package com.example.savepreciouswildlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button btnRegister, btnLogin;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFirebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);


        /**
         * @author Jagjit Singh
         * button to register the user
         * if no error occurs, user will stored in the Firebase as well as in the local storage (only user's email will be stored in the local storage)
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(RegistrationActivity.this, "Registering.. Please wait..", Toast.LENGTH_SHORT).show();
                final String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email.isEmpty() && password.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Text fields are empty!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else if(email.isEmpty()){
                    editTextEmail.setError("Please enter email id");
                    editTextEmail.requestFocus();
                } else if(password.isEmpty()){
                    editTextPassword.setError("Please enter your password");
                    editTextPassword.requestFocus();
                }  else if(!(email.isEmpty() && password.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Snackbar snackbar = Snackbar.make(view, "Email already exists or email format is wrong!", Snackbar.LENGTH_LONG);
                                snackbar.setAction("OKAY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            } else{
                                User user = new User(email);
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RegistrationActivity.this);
                                dataBaseHelper.addUser(user);
                                dataBaseHelper.closeDB();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            }
                        }
                    });
                } else{
                    Snackbar snackbar = Snackbar.make(view, "Something went wrong!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });


        /**
         * @author Jagjit Singh
         * button will take to LoginActivity
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}