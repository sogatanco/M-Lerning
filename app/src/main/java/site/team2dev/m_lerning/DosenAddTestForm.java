package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class DosenAddTestForm extends AppCompatActivity {
    AppCompatImageView back;
    Button submitter, addsoal;
    EditText name, deadline;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_test_form);

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitter=findViewById(R.id.submitter);
        submitter.setVisibility(View.GONE);

        name=findViewById(R.id.name);
        deadline=findViewById(R.id.deadline);

        addsoal=findViewById(R.id.addsoal);
        addsoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vname=name.getText().toString().trim(), vdeadline=deadline.getText().toString().trim();

                if(!TextUtils.isEmpty(vname)&& !TextUtils.isEmpty(vdeadline)){
                    String id=databaseReference.push().getKey();
                    TestItem testItem=new TestItem(id, vname, vdeadline);
                    databaseReference.child(id).setValue(testItem);
                    Intent i=new Intent(DosenAddTestForm.this, DosenAddTestSoal.class);
                    i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                    i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                    i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
                    i.putExtra("id_test", id);
                    startActivity(i);
                }
            }
        });

    }
}
