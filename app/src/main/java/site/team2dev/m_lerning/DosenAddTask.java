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

public class DosenAddTask extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    ArrayList<ListItem2> tugasItems;
    AppCompatImageView back;
    Button add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        tugasItems=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("task");

        title=findViewById(R.id.title);
        back=findViewById(R.id.back);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem2(tugasItems,this, this::detail);

        title.setText("Task");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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
                Intent i=new Intent(DosenAddTask.this, DosenAddTaskForm.class);
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
                tugasItems.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    tugasItems.add(new ListItem2(dataSnapshot1.getValue(TaskItem.class).getItemId(),
                            dataSnapshot1.getValue(TaskItem.class).getItemName(),"Deadline : "+dataSnapshot1.getValue(TaskItem.class).getItemDeadline()));
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void detail(String id)
    {
        Intent i=new Intent(DosenAddTask.this, DosenUpdateTask.class);
        i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
        i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
        i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
        i.putExtra("id_task", id);
        startActivity(i);
    }
}
