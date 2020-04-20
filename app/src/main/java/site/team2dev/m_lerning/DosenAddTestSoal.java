package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DosenAddTestSoal extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ListSoal> soalItems;

    DatabaseReference databaseReference;

    AppCompatImageView back;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_test_soal);
//        Toast.makeText(this, getIntent().getStringExtra("id_test"),Toast.LENGTH_LONG).show();

        databaseReference=FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test").child(getIntent().getStringExtra("id_test")).child("soal");

        soalItems=new ArrayList<>();

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddSoal(soalItems,this, this::delete);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenAddTestSoal.this, DosenAddTestSoalForm.class);
                i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
                i.putExtra("id_test", getIntent().getStringExtra("id_test"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                soalItems.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    soalItems.add(new ListSoal(dataSnapshot1.getValue(ListSoal.class).getItemId(),
                            dataSnapshot1.getValue(ListSoal.class).getItemSoal(), dataSnapshot1.getValue(ListSoal.class).getPilihan1(),
                            dataSnapshot1.getValue(ListSoal.class).getPilihan2(), dataSnapshot1.getValue(ListSoal.class).getPilihan3(),
                            dataSnapshot1.getValue(ListSoal.class).getPilihan4(),dataSnapshot1.getValue(ListSoal.class).getBenar()));
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void delete(String s) {
        databaseReference.child(s).removeValue();
        Toast.makeText(DosenAddTestSoal.this, "Deleted succesfully", Toast.LENGTH_LONG).show();
    }
}
