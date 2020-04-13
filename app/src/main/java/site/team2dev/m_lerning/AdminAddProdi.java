package site.team2dev.m_lerning;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AdminAddProdi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;

    EditText newfield;
    Button add;
    AppCompatImageView back;

    ArrayList<ListItem1> prodiItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        prodiItems=new ArrayList<>();

        title=findViewById(R.id.title);
        back=findViewById(R.id.back);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(  prodiItems , this, this::deleteItem);

        newfield=findViewById(R.id.newfield);
        add=findViewById(R.id.add);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("jurusan").child("tik").child("prodi");

        title.setText("Prodi");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newprodi=newfield.getText().toString().trim();

                if(!TextUtils.isEmpty(newprodi)){
                    String id=databaseReference.push().getKey();

                    ListItem1 prodi=new ListItem1(id, newprodi);

                    databaseReference.child(id).setValue(prodi);

                    newfield.setText("");

                    Toast.makeText(AdminAddProdi.this, "Item baru telah ditambahkan", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AdminAddProdi.this, "Terjadi kesalahan, Coba lagi nanti !", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    public void deleteItem(String id){
        String[] data=id.split(" ");
        if(data[1].equals("delete")){
            databaseReference.child(data[0]).removeValue();
            Toast.makeText(AdminAddProdi.this, "item was deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
