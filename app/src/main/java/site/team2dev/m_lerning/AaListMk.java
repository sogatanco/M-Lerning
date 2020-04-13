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

public class AaListMk extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    ArrayList<ListItem1> mkItems;
    AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_layout);

        title=findViewById(R.id.title);
        title.setText("Mata Kuliah");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mkItems=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk");

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem(mkItems,this, this::detail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().getStringExtra("id_dosen")!=null){

            databaseReference.orderByChild("itemDosen").equalTo(getIntent().getStringExtra("id_dosen")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mkItems.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        mkItems.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mkItems.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        mkItems.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void detail(String s) {
        String next=getIntent().getStringExtra("next");
        Class<?> cnext = null;
        try {
            cnext = Class.forName(next);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent i=new Intent(AaListMk.this, cnext);
        i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
        i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
        i.putExtra("id_dosen", getIntent().getStringExtra("id_dosen"));
        i.putExtra("id_mk",s);
        startActivity(i);
    }
}
