package site.team2dev.m_lerning;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
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

public class MahasiswaNilaiDetail extends AppCompatActivity {
    AppCompatTextView title;
    AppCompatImageView back;
    DatabaseReference databaseReference;
    Button add;
    ArrayList<ListItem2> nilaiItems;
    FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        add=findViewById(R.id.add);
        add.setVisibility(View.GONE);

        title=findViewById(R.id.title);
        title.setText("Nilai "+getIntent().getStringExtra("jenis"));

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child(getIntent().getStringExtra("jenis"));

        nilaiItems=new ArrayList<>();
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem2(nilaiItems,this, this::detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nilaiItems.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.child("submitter").hasChild(firebaseAuth.getUid())){
                        if(getIntent().getStringExtra("jenis").equals("task")){
                            nilaiItems.add(new ListItem2(dataSnapshot1.getKey(), dataSnapshot1.child("itemName").getValue().toString(),
                                    "score : "+dataSnapshot1.child("submitter").child(firebaseAuth.getUid()).child("nilai").getValue().toString()));
                        }else if(getIntent().getStringExtra("jenis").equals("test")){
                            nilaiItems.add(new ListItem2(dataSnapshot1.getKey(), dataSnapshot1.child("itemName").getValue().toString(),
                                    "score : "+
                                            ((100/(long)dataSnapshot1.child("submitter").child(firebaseAuth.getUid()).child("jumlah_soal").getValue())*(long)dataSnapshot1.child("submitter").child(firebaseAuth.getUid()).child("benar").getValue())
                            ));
                        }
                    }
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void detail(String s) {
    }
}
