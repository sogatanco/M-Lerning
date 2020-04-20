package site.team2dev.m_lerning;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class DosenAddTaskForm extends AppCompatActivity {
    Button save, submitter;
    AppCompatImageView back;
    DatabaseReference databaseReference;
    EditText name, deadline, detail;
    AppCompatTextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_task_form);
//        Toast.makeText(this, getIntent().getStringExtra("id_mk"), Toast.LENGTH_LONG).show();

        submitter=findViewById(R.id.submitter);
        submitter.setVisibility(View.GONE);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        title.setText("New Task");

        name=findViewById(R.id.name);
        deadline=findViewById(R.id.deadline);
        detail=findViewById(R.id.detail);

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("task");

        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vname=name.getText().toString().trim(), vdeadline=deadline.getText().toString().trim(),
                        vdetail=detail.getText().toString().trim();

                if(!TextUtils.isEmpty(vname)&&!TextUtils.isEmpty(vdeadline)&&!TextUtils.isEmpty(vdetail)){
                    String id=databaseReference.push().getKey();
                    TaskItem taskItem=new TaskItem(id, vname, vdeadline, vdetail);
                    databaseReference.child(id).setValue(taskItem);
                    Toast.makeText(DosenAddTaskForm.this, "Tugas baru berhasil ditambahkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(DosenAddTaskForm.this, "Tidak boleh ada field yang kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
