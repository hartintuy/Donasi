package com.takatutustudio.Donasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    LinearLayout btn_shodaqoh, btn_zakat,
            btn_wakaf;

    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap, bio, user_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btn_zakat = findViewById(R.id.btn_zakat);
        btn_to_profile = findViewById(R.id.btn_to_profile);

        btn_wakaf = findViewById(R.id.btn_wakaf);
        btn_shodaqoh = findViewById(R.id.btn_shodaqoh);


        photo_home_user = findViewById(R.id.photo_home_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " + dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit()
                        .into(photo_home_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeActivity.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });

        btn_shodaqoh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoshodaqoh = new Intent(HomeActivity.this, ShodaqohActivity.class);
                //meletakan data kepada intent
                gotoshodaqoh.putExtra("jenis_tiket", "Pisa");
                startActivity(gotoshodaqoh);
            }
        });

        btn_zakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotozakat = new Intent(HomeActivity.this, Zakat.class);
                gotozakat.putExtra("jenis_tiket", "Tori");
                startActivity(gotozakat);
            }
        });

        btn_wakaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotowakaf = new Intent(HomeActivity.this, Wakaf.class);
                gotowakaf.putExtra("jenis_tiket", "Pagoda");
                startActivity(gotowakaf);
            }
        });

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
