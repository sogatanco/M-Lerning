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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DosenSubmitterTask extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference detailmahasiswa;
    ArrayList<ListItem2> submitterItems;
    AppCompatImageView back;
    Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        add=findViewById(R.id.add);
        add.setVisibility(View.GONE);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        title.setText("Task Submitter");

        detailmahasiswa=FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"));

        submitterItems=new ArrayList<>();
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem2(submitterItems,this, this::detail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        detailmahasiswa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submitterItems.clear();
                if(dataSnapshot.child("mk").child(getIntent().getStringExtra("id_mk")).child("task").child(getIntent().getStringExtra("id_task"))
                        .hasChild("submitter")){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.child("mk").child(getIntent().getStringExtra("id_mk")).child("task").child(getIntent().getStringExtra("id_task"))
                            .child("submitter").getChildren()){

                        submitterItems.add(new ListItem2(dataSnapshot1.child("id").getValue().toString(),
                                dataSnapshot.child("mahasiswa").child(dataSnapshot1.child("id").getValue().toString())
                                        .child("itemName").getValue().toString(),
                                "Nilai : "+ dataSnapshot1.child("nilai").getValue().toString()));
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void detail(String s) {
        Intent i=new Intent(DosenSubmitterTask.this, DosenSubmitterTaskDetail.class);
        i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
        i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
        i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
        i.putExtra("id_task", getIntent().getStringExtra("id_task"));
        i.putExtra("id_submitter", s);
        startActivity(i);
    }
}
