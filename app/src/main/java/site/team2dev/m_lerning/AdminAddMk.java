package site.team2dev.m_lerning;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAddMk extends AppCompatActivity {

    DatabaseReference dosen, mk;
    List<String> kodeDosen, namaDosen;
    Spinner sdosen;
    ArrayAdapter<String> dataAdapter;
    Button add;
    AppCompatImageView back;
    EditText newfield;
    AppCompatTextView title;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ListItem1> mkItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_mk);

        kodeDosen=new ArrayList<>();
        namaDosen=new ArrayList<>();
        dosen= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("dosen");
        mk=FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi").child(getIntent()
                .getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas")).child("mk");

        newfield=findViewById(R.id.newfield);
        sdosen=findViewById(R.id.dosen);
        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMk=newfield.getText().toString().trim();
                String dosenmk=kodeDosen.get(sdosen.getSelectedItemPosition());

                if(!TextUtils.isEmpty(newMk)&& !TextUtils.isEmpty(dosenmk)){
                    String id=mk.push().getKey();

                    MkItem mkItem=new MkItem(id, newMk, dosenmk);
                    mk.child(id).setValue(mkItem);

                    Toast.makeText(AdminAddMk.this, "Item baru telah ditambahakan", Toast.LENGTH_LONG).show();
                }
            }
        });

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mkItems=new ArrayList<>();
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(mkItems,this, this::deleteItem);

        title=findViewById(R.id.title);
        title.setText("New MK");

    }



    @Override
    protected void onStart() {
        super.onStart();

        dosen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kodeDosen.clear();
                namaDosen.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    kodeDosen.add(postSnapshot.getValue(UserItem.class).getItemId());
                    namaDosen.add(postSnapshot.getValue(UserItem.class).getItemName());
                }
                dataAdapter = new ArrayAdapter<String>(AdminAddMk.this, android.R.layout.simple_spinner_item, namaDosen);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sdosen.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mkItems.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
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

    public void deleteItem(String s) {
        String[] data=s.split(" ");
        if(data[1].equals("delete")){
            mk.child(data[0]).removeValue();
            Toast.makeText(AdminAddMk.this, "item was deleted", Toast.LENGTH_SHORT).show();
        }

        else if(data[1].equals("detail")){
            mk.child(data[0]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String namaMk=dataSnapshot.getValue(MkItem.class).getItemName();

                        dosen.child(dataSnapshot.getValue(MkItem.class).getItemDosen()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(AdminAddMk.this);
                                alertDialogBuilder.setTitle(namaMk);
                                alertDialogBuilder
                                        .setMessage("Diasuh oleh "+dataSnapshot.getValue(UserItem.class).getItemName())
                                        .setCancelable(false)
                                        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
