package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class DosenUpdateTask extends AppCompatActivity {
    AppCompatImageView back;
    AppCompatTextView title;
    Button submmiter, save;
    EditText name, deadline, detail;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_task_form);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        title.setText("Update Task");

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("task").child(getIntent().getStringExtra("id_task"));
        name=findViewById(R.id.name);
        deadline=findViewById(R.id.deadline);
        detail=findViewById(R.id.detail);

        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vname=name.getText().toString().trim(), vdeadline=deadline.getText().toString().trim(),
                            vdetail=detail.getText().toString().trim();
                if(!TextUtils.isEmpty(vname)&&!TextUtils.isEmpty(vdeadline)&&!TextUtils.isEmpty(vdetail)){
                    databaseReference.child("itemName").setValue(name.getText().toString().trim());
                    databaseReference.child("itemDetail").setValue(detail.getText().toString().trim());
                    databaseReference.child("itemDeadline").setValue(deadline.getText().toString().trim());
                    Toast.makeText(DosenUpdateTask.this, "Tugas berhasil di update", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DosenUpdateTask.this, "Tidak boleh ada field yang kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        submmiter=findViewById(R.id.submitter);
        submmiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenUpdateTask.this, DosenSubmitterTask.class);
                i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
                i.putExtra("id_task", getIntent().getStringExtra("id_task"));
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
                name.setText(dataSnapshot.getValue(TaskItem.class).getItemName());
                deadline.setText(dataSnapshot.getValue(TaskItem.class).getItemDeadline());
                detail.setText(dataSnapshot.getValue(TaskItem.class).getItemDetail());
                if(dataSnapshot.hasChild("submitter")){
                    submmiter.setText( String.valueOf(dataSnapshot.child("submitter").getChildrenCount())+" Submitter");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
