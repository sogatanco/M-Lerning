package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MahasiswaHome extends AppCompatActivity {

    ImageView logout, materi, tugas, info, test, nilai;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String id_prodi, id_kelas;
    TextView nama;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_home);

        logout= findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent i=new Intent(MahasiswaHome.this, LoginActivity.class);
                startActivity(i);
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi");
        firebaseAuth=FirebaseAuth.getInstance();

        nama=findViewById(R.id.nama);

        materi=findViewById(R.id.materi);
        materi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MahasiswaHome.this, AaListMk.class);
                i.putExtra("id_prodi", id_prodi);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("next", "site.team2dev.m_lerning.MahasiswaMateri");
                startActivity(i);
            }
        });

        tugas=findViewById(R.id.tugas);
        tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MahasiswaHome.this, AaListMk.class);
                i.putExtra("id_prodi", id_prodi);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("next", "site.team2dev.m_lerning.MahasiswaTugas");
                startActivity(i);
            }
        });

        info=findViewById(R.id.informasi);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MahasiswaHome.this, AaListMk.class);
                i.putExtra("id_prodi", id_prodi);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("next", "site.team2dev.m_lerning.MahasiswaInfo");
                startActivity(i);
            }
        });

        test=findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MahasiswaHome.this, AaListMk.class);
                i.putExtra("id_prodi", id_prodi);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("next", "site.team2dev.m_lerning.MahasiswaListTest");
                startActivity(i);
            }
        });

        nilai=findViewById(R.id.nilai);
        nilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MahasiswaHome.this, AaListMk.class);
                i.putExtra("id_prodi", id_prodi);
                i.putExtra("id_kelas", id_kelas);
                i.putExtra("next", "site.team2dev.m_lerning.MahasiswaNilai");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("uid", firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot prodi: dataSnapshot.getChildren()){
                    if(prodi.hasChild("kelas")){
                        for(DataSnapshot kelas:prodi.child("kelas").getChildren()){
                            if(kelas.child("mahasiswa").hasChild(firebaseAuth.getUid())){
                                id_prodi=prodi.child("itemId").getValue().toString();
                                id_kelas=kelas.child("itemId").getValue().toString();
                                nama.setText(kelas.child("mahasiswa").child(firebaseAuth.getUid())
                                        .child("itemName").getValue().toString());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
