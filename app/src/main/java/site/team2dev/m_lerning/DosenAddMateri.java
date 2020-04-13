package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DosenAddMateri extends AppCompatActivity {
    AppCompatTextView title;
    AppCompatImageView back;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ListItem1> materiItems;
    DatabaseReference databaseReference;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        title=findViewById(R.id.title);
        title.setText("Materi");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        materiItems=new ArrayList<>();

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(materiItems, this, this::deleteItem);

        databaseReference=FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("materi");

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenAddMateri.this, DosenAddMateriForm.class);
                i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
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
                materiItems.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    materiItems.add(new ListItem1(dataSnapshot1.getValue(ListItem1.class).getItemId(), dataSnapshot1.getValue(ListItem1.class).getItemName()));
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteItem(String s) {
    }
}
