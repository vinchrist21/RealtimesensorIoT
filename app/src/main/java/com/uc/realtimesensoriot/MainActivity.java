package com.uc.realtimesensoriot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView nilai, nilai2;
    private Firebase mRef, mRef2;
    private Button button, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nilai = (TextView)findViewById(R.id.nilai);
        nilai2 = (TextView)findViewById(R.id.nilai2);

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
//        button3 = (Button)findViewById(R.id.button3);

        mRef = new Firebase("https://sampah-b68af-default-rtdb.asia-southeast1.firebasedatabase.app/logam/capacity");
        mRef2 = new Firebase("https://sampah-b68af-default-rtdb.asia-southeast1.firebasedatabase.app/nonlogam/capacity");


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                nilai.setText(status);
                if (Objects.equals(status, "Full")){
                    notification();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                nilai2.setText(status);
                if (Objects.equals(status, "Full")){
                    notification();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomor = "https://api.whatsapp.com/send?phone=6281331410406";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(nomor));
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });
    }
    private void notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentTitle("Tempat Sampah Logam")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentText("One of Your Bins is Full")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }
}