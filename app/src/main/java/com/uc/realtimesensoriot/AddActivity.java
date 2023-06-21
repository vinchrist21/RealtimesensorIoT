package com.uc.realtimesensoriot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText editBerat, editBiaya;
    private Button btnSave;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editBerat = findViewById(R.id.berat);
        editBiaya = findViewById(R.id.tanggal);
        btnSave = findViewById(R.id.btn_save);

        progressDialog = new ProgressDialog(AddActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");

        btnSave.setOnClickListener(v -> {
            if (editBerat.getText().length()>0 && editBiaya.getText().length()>0){
                saveData(editBerat.getText().toString(), editBiaya.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
            id = intent.getStringExtra("id");
            editBerat.setText(intent.getStringExtra("berat"));
            editBiaya.setText(intent.getStringExtra("biaya"));
        }
    }

    private void saveData(String berat, String biaya){
        Map<String, Object> user = new HashMap<>();
        user.put("berat", berat);
        user.put("biaya", biaya);
        //tambahan no 57
        user.put("datepersubmit", FieldValue.serverTimestamp());

        progressDialog.show();
        if (id!=null){
            db.collection("catatan").document(id)
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            db.collection("catatan")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}