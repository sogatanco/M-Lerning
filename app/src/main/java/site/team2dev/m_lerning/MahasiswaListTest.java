package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MahasiswaListTest extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    ArrayList<ListItem2> testItems;
    AppCompatImageView back;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        testItems=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test");

        title=findViewById(R.id.title);
        title.setText("Tests");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add=findViewById(R.id.add);
        add.setVisibility(View.GONE);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem2(testItems,this, this::detail);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testItems.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    testItems.add(new ListItem2(dataSnapshot1.getValue(TestItem.class).getItemId(),
                            dataSnapshot1.getValue(TestItem.class).getItemName(),"Deadline : "+dataSnapshot1.getValue(TestItem.class).getItemDeadline()));
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
        List<String>  ids=new ArrayList<>();
        databaseReference.child(s).child("soal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ids.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ids.add(dataSnapshot1.getKey());
                }
                Intent i=new Intent(MahasiswaListTest.this, MahasiswaTest.class);
                i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
                i.putExtra("id_test", s);
                i.putExtra("idSoal", (Serializable) ids);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
