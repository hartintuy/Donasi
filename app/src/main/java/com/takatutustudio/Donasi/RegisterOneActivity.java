package com.takatutustudio.Donasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    LinearLayout btnBack;
    EditText etUsername, etPassword, etEmailAddress;
    DatabaseReference reference, refUsers;
    Button btnContinue;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDb = FirebaseDatabase.getInstance();

        refUsers = mFirebaseDb.getReference()
                .child("users");

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etEmailAddress = findViewById(R.id.email_address);

        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btnContinue.setEnabled(false);
                btnContinue.setText("Loading...");

                //mengambil username pada firebase

                String email = etEmailAddress.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                saveUserToDatabase(user.getUid());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            btnContinue.setEnabled(true);
                            btnContinue.setText("CONTINUE");
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void saveUserToDatabase(final String userId) {
        refUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("username").setValue(etUsername.getText().toString());
                dataSnapshot.getRef().child("password").setValue(etPassword.getText().toString());
                dataSnapshot.getRef().child("email_address").setValue(etEmailAddress.getText().toString());
                dataSnapshot.getRef().child("user_balance").setValue(800);

                //Jika username tersedia
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Username sudah tersedia", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading Active
                    btnContinue.setEnabled(true);
                    btnContinue.setText("CONTINUE");

                } else {
                    //menyimpan data kepada local storage (handphone)
                    SharedPreferences sharedPreferences = getSharedPreferences(PrefConst.PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PrefConst.USERNAME_KEY, etUsername.getText().toString());
                    editor.apply();

                    //simpan kepada database
                }

                //berpindah activity
                Intent gotonextregister = new Intent(RegisterOneActivity.this, SuccessRegisterActivity.class);
                startActivity(gotonextregister);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}



