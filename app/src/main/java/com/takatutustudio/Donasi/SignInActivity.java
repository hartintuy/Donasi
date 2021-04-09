package com.takatutustudio.Donasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    TextView btnNewAccount;
    Button btnSignIn;
    EditText etEmail, etPassword;

    //DatabaseReference reference;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mFirebaseDb = FirebaseDatabase.getInstance();
    private DatabaseReference refUsers;

    //String USERNAME_KEY = "usernamekey";
    //String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        refUsers = mFirebaseDb.getReference()
                .child("users");

        btnNewAccount = findViewById(R.id.btn_new_account);
        btnSignIn = findViewById(R.id.btn_sign_in);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.xpassword);

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SignInActivity.this, RegisterOneActivity.class);
                startActivity(gotoregisterone);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btnSignIn.setEnabled(false);
                btnSignIn.setText("Loading...");

                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username kosong!", Toast.LENGTH_SHORT).show();
                    //ubah state menjadi Username kosong
                    btnSignIn.setEnabled(true);
                    btnSignIn.setText("SIGN IN");
                }
                else {
                    if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Password kosong", Toast.LENGTH_SHORT).show();
                        //ubah state menjadi Password kosong
                        btnSignIn.setEnabled(true);
                        btnSignIn.setText("SIGN IN");
                    }
                    else {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null){
                                        getUserData(user.getUid());
                                    }
                                    Intent gotohome = new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(gotohome);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        });
    }

    private void getUserData(String userUid) {
    }
}
