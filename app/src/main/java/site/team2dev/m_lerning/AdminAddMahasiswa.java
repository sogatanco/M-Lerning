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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAddMahasiswa extends AppCompatActivity {
    AppCompatTextView title;
    AppCompatImageView back;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ListItem1> mahasiswaItems;
    DatabaseReference mahasiswas, users;
    Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        title=findViewById(R.id.title);
        title.setText("New Mahasiswa");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mahasiswaItems=new ArrayList<>();

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(mahasiswaItems, this, this::deleteItem);

        mahasiswas= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mahasiswa");
        users=FirebaseDatabase.getInstance().getReference("users");

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AdminAddMahasiswa.this, getIntent()
//                        .getStringExtra("id_prodi")+"-"+getIntent().getStringExtra("id_kelas"), Toast.LENGTH_LONG).show();

                Intent i=new Intent(AdminAddMahasiswa.this, AdminAddMahasiswaForm.class);
                i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mahasiswas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mahasiswaItems.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    mahasiswaItems.add(new ListItem1(dataSnapshot1.getValue(ListItem1.class).getItemId(),
                            dataSnapshot1.getValue(ListItem1.class).getItemName()));
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteItem(String id) {
        String[] data=id.split(" ");
        if(data[1].equals("delete")){
            mahasiswas.child(data[0]).removeValue();
            users.child(data[0]).removeValue();
            Toast.makeText(AdminAddMahasiswa.this, "item was deleted", Toast.LENGTH_SHORT).show();
        }
        else if(data[1].equals("detail")){
            Intent i=new Intent(AdminAddMahasiswa.this, AdminUpdateMahasiswa.class);
            i.putExtra("id", data[0]);
            i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
            i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
            startActivity(i);
        }
    }

}
