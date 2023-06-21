package com.uc.realtimesensoriot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uc.realtimesensoriot.adapter.CatatanAdapter;
import com.uc.realtimesensoriot.model.Catatan;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Catatan> list = new ArrayList<>();
    private CatatanAdapter catatanAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.btn_add);

        progressDialog = new ProgressDialog(LogActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");
        catatanAdapter = new CatatanAdapter(getApplicationContext(), list);
        catatanAdapter.setDialog(new CatatanAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit","Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(LogActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("berat", list.get(pos).getBerat());
                                intent.putExtra("biaya", list.get(pos).getBiaya());
                                startActivity(intent);
                                break;
                            case 1:
                                deleteData(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(catatanAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        progressDialog.show();
        db.collection("catatan")
                //sort datepersubmit
                .orderBy("datepersubmit", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Timestamp timestamp = document.getTimestamp("datepersubmit");
                                if (timestamp != null) {
                                    Date date = timestamp.toDate();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // Format tanggal dan waktu yang diinginkan
                                    String formattedDate = sdf.format(date);

                                    // Mengambil data biaya dari Firestore
                                    // Mengambil data biaya dari Firestore
                                    String biayaString = document.getString("biaya");
// Menghilangkan karakter "Rp" dan tanda titik pada angka desimal
                                    String biayaCleanedString = biayaString.replaceAll("[^\\d]", "");
                                    double biaya = Double.parseDouble(biayaCleanedString);


                                    // Format biaya menjadi Rupiah
                                    Locale localeID = new Locale("in", "ID");
                                    NumberFormat formatter = NumberFormat.getCurrencyInstance(localeID);
                                    String biayaRupiah = formatter.format(biaya);

                                    Catatan catatan = new Catatan(document.getString("berat"), biayaRupiah, document.getString("tanggal"), formattedDate);
                                    catatan.setId(document.getId());
                                    list.add(catatan);
                                }
                            }
                            catatanAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void deleteData(String id){
        progressDialog.show();
        db.collection("catatan").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Gagal dihapus", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                getData();
            }
        });
    }
}
