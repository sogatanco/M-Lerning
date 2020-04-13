package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class DosenListMahasiswa extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    ArrayList<ListItem1> mahasiswaItems;
    AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_layout);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        title.setText("Mahasiswa");

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas")).child("mahasiswa");
        mahasiswaItems=new ArrayList<>();

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem(mahasiswaItems,this, this::detail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mahasiswaItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    mahasiswaItems.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void detail(String s) {
        Intent i=new Intent(DosenListMahasiswa.this, DosenDetailMahasiswa.class);
        i.putExtra("id", s);
        i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
        i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
        startActivity(i);
    }
}
