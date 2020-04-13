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

public class AdminAddDosen extends AppCompatActivity {

    AppCompatTextView title;
    AppCompatImageView back;
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    
    ArrayList<ListItem1> dosenItems;
    DatabaseReference databaseReference;
    DatabaseReference users;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        title=findViewById(R.id.title);
        title.setText("Dosen");
        
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dosenItems=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("dosen");
        users=FirebaseDatabase.getInstance().getReference("users");
        
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(dosenItems,this, this::deleteItem);


        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAddDosen.this, AdminAddDosenForm.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dosenItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    dosenItems.add(new ListItem1(postSnapshot.getValue(ListItem1.class).getItemId(), postSnapshot.getValue(ListItem1.class).getItemName()));

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
            databaseReference.child(data[0]).removeValue();
            users.child(data[0]).removeValue();
            Toast.makeText(AdminAddDosen.this, "item was deleted", Toast.LENGTH_SHORT).show();
        }else if(data[1].equals("detail")){
            Intent i=new Intent(AdminAddDosen.this, AdminUpdateDosen.class);
            i.putExtra("id", data[0]);
            startActivity(i);
        }
    }
}
