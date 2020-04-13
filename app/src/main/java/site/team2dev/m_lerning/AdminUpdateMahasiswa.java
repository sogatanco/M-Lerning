package site.team2dev.m_lerning;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class AdminUpdateMahasiswa extends AppCompatActivity {
    AppCompatTextView title;
    DatabaseReference mahasiswas;
    Spinner jk;
    AppCompatImageView back;
    ArrayAdapter<String> dataAdapter;
    TextView save;
    EditText email, password, nama, alamat, tempat, tgllahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user_form);

        title=findViewById(R.id.title);
        title.setText("Update Mahasiswa");

        jk=findViewById(R.id.jk);
        List<String> list = new ArrayList<String>();
        list.add("laki");
        list.add("perempuan");

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jk.setAdapter(dataAdapter);

        mahasiswas= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mahasiswa").child(getIntent().getStringExtra("id"));
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        nama=findViewById(R.id.nama);
        alamat=findViewById(R.id.alamat);
        tempat=findViewById(R.id.tempat);
        tgllahir=findViewById(R.id.tgllahir);

        email.setKeyListener(null);
        password.setKeyListener(null);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mahasiswas.child("itemName").setValue(nama.getText().toString().trim());
                mahasiswas.child("itemAlamat").setValue(alamat.getText().toString().trim());
                mahasiswas.child("itemTempat").setValue(tempat.getText().toString().trim());
                mahasiswas.child("itemTglLahir").setValue(tgllahir.getText().toString().trim());
                mahasiswas.child("itemJk").setValue(jk.getSelectedItem().toString().trim());
                Toast.makeText(AdminUpdateMahasiswa.this, "Data Updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mahasiswas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jk.setSelection(dataAdapter.getPosition(dataSnapshot.getValue(UserItem.class).getItemJk()));
                nama.setText(dataSnapshot.getValue(UserItem.class).getItemName());
                tempat.setText(dataSnapshot.getValue(UserItem.class).getItemTempat());
                alamat.setText(dataSnapshot.getValue(UserItem.class).getItemAlamat());
                tgllahir.setText(dataSnapshot.getValue(UserItem.class).getItemTglLahir());
                email.setText(dataSnapshot.getValue(UserItem.class).getItemEmail());
                password.setText(dataSnapshot.getValue(UserItem.class).getItemPassword());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
