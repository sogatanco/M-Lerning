package site.team2dev.m_lerning;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class DosenDetailMahasiswa extends AppCompatActivity {
    DatabaseReference mahasiswa;
    TextView nama, ttl, isijk, isialmt, isiemail;
    AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_detail_mahasiswa);

        mahasiswa= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mahasiswa").child(getIntent().getStringExtra("id"));

        nama=findViewById(R.id.nama);
        ttl=findViewById(R.id.ttl);
        isijk=findViewById(R.id.isijk);
        isialmt=findViewById(R.id.isialmt);
        isiemail=findViewById(R.id.isiemail);

        back=findViewById(R.id.back);
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

        mahasiswa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama.setText(dataSnapshot.getValue(UserItem.class).getItemName().toUpperCase());
                ttl.setText(dataSnapshot.getValue(UserItem.class).getItemTempat()+", " +
                        dataSnapshot.getValue(UserItem.class).getItemTglLahir());
                isijk.setText(dataSnapshot.getValue(UserItem.class).getItemJk());
                isialmt.setText(dataSnapshot.getValue(UserItem.class).getItemAlamat());
                isiemail.setText(dataSnapshot.getValue(UserItem.class).getItemEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
