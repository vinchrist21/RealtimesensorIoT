package com.uc.realtimesensoriot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView nilai;
    private Firebase mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nilai = (TextView)findViewById(R.id.nilai);

        mRef = new Firebase("https://sampah-641f8-default-rtdb.firebaseio.com/capacity/status");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                nilai.setText(status);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}