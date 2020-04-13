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
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AaListProdi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    ArrayList<ListItem1> prodiItems;
    AppCompatImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_layout);

        prodiItems=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("jurusan").child("tik").child("prodi");

        title=findViewById(R.id.title);
        back=findViewById(R.id.back);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem(prodiItems,this, this::detail);

        title.setText("Prodi");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

       if(getIntent().getStringExtra("id_dosen")!=null){
           Set<ListItem1> prodis = new HashSet<>();
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   prodiItems.clear();
                   for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        if(postSnapshot.hasChild("kelas")){
                            for(DataSnapshot kelas: postSnapshot.child("kelas").getChildren()){
                                if(kelas.hasChild("mk")){
                                    for(DataSnapshot mk:kelas.child("mk").getChildren()){

                                        if(mk.child("itemDosen").getValue().toString().equals(getIntent().getStringExtra("id_dosen"))){
                                            prodis.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

                                        }
                                    }
                                }
                            }
                        }

                   }

                   for(ListItem1 x:prodis){
                       prodiItems.add(x);
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
                   prodiItems.clear();
                   for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                       prodiItems.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

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

    public void detail(String id)
    {
        String next=getIntent().getStringExtra("next");
        Class<?> cnext = null;
        try {
            cnext = Class.forName(next);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Intent i=new Intent(AaListProdi.this, cnext);
        i.putExtra("id_dosen", getIntent().getStringExtra("id_dosen"));
        i.putExtra("id_prodi", id);
        i.putExtra("next", getIntent().getStringExtra("next2"));
        i.putExtra("next2", getIntent().getStringExtra("next3"));
        startActivity(i);
    }
}
