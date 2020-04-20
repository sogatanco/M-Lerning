package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DosenHome extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView logout, mahasiswa, materi, tugas, informasi, test;
    DatabaseReference dosen;
    TextView namaDosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_home);

        firebaseAuth=FirebaseAuth.getInstance();
        dosen= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("dosen").child(firebaseAuth.getUid());

        namaDosen=findViewById(R.id.nama);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });

        mahasiswa=findViewById(R.id.mahasiswa);
        mahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenHome.this, AaListProdi.class);
                i.putExtra("id_dosen",firebaseAuth.getUid());
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.DosenListMahasiswa");
                startActivity(i);
            }
        });

        materi=findViewById(R.id.materi);
        materi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenHome.this, AaListProdi.class);
                i.putExtra("id_dosen",firebaseAuth.getUid());
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AaListMk");
                i.putExtra("next3", "site.team2dev.m_lerning.DosenAddMateri");
                startActivity(i);
            }
        });

        tugas=findViewById(R.id.tugas);
        tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenHome.this, AaListProdi.class);
                i.putExtra("id_dosen",firebaseAuth.getUid());
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AaListMk");
                i.putExtra("next3", "site.team2dev.m_lerning.DosenAddTask");
                startActivity(i);
            }
        });

        informasi=findViewById(R.id.informasi);
        informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenHome.this, AaListProdi.class);
                i.putExtra("id_dosen",firebaseAuth.getUid());
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AaListMk");
                i.putExtra("next3", "site.team2dev.m_lerning.DosenAddInfo");
                startActivity(i);
            }
        });

        test=findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DosenHome.this, AaListProdi.class);
                i.putExtra("id_dosen",firebaseAuth.getUid());
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AaListMk");
                i.putExtra("next3", "site.team2dev.m_lerning.DosenAddTest");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dosen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaDosen.setText(dataSnapshot.getValue(UserItem.class).getItemName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
