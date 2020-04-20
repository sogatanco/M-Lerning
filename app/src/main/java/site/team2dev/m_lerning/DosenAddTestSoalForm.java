package site.team2dev.m_lerning;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class DosenAddTestSoalForm extends AppCompatActivity {
    Spinner benar;
    EditText pilihan1, pilihan2, pilihan3, pilihan4, soal;
    DatabaseReference databaseReference;
    AppCompatImageView back;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_test_soal_form);

        List<String> list = new ArrayList<String>();
        list.add("Choose right answer....");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        soal=findViewById(R.id.soal);
        pilihan1=findViewById(R.id.pilihan1);
        pilihan2=findViewById(R.id.pilihan2);
        pilihan3=findViewById(R.id.pilihan3);
        pilihan4=findViewById(R.id.pilihan4);

        pilihan1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.set(1, s.toString().trim());
            }
        });

        pilihan2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.set(2, s.toString().trim());
            }
        });

        pilihan3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.set(3, s.toString().trim());
            }
        });

        pilihan4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.set(4, s.toString().trim());
            }
        });


        benar=findViewById(R.id.benar);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,  list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        benar.setAdapter(dataAdapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test").child(getIntent().getStringExtra("id_test"))
                .child("soal");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vbenar=benar.getSelectedItem().toString().trim(), vpilihan1=pilihan1.getText().toString().trim(),
                vpilihan2=pilihan2.getText().toString().trim(), vpilihan3=pilihan3.getText().toString().trim(),
                vpilihan4=pilihan4.getText().toString().trim(), vsoal=soal.getText().toString().trim();

                if(!TextUtils.isEmpty(vbenar)&& !TextUtils.isEmpty(vpilihan1)&& !TextUtils.isEmpty(vpilihan2)&&
                        !TextUtils.isEmpty(vpilihan3)&& !TextUtils.isEmpty(vpilihan4) &&!TextUtils.isEmpty(vsoal)){
                    String id=databaseReference.push().getKey();
                    ListSoal listSoal=new ListSoal(id, vsoal, vpilihan1, vpilihan2,vpilihan3, vpilihan4,vbenar);
                    databaseReference.child(id).setValue(listSoal);
                    Toast.makeText(DosenAddTestSoalForm.this, "Soal Berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(DosenAddTestSoalForm.this, "Can't be empty!", Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
