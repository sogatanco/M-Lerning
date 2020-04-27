package site.team2dev.m_lerning;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class MahasiswaScoreTest extends AppCompatActivity {
    AppCompatImageView back;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    TextView soal, terjawab, benar, salah, score;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_score_test);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MahasiswaTest.fa.finish();
                finish();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test").child(getIntent().getStringExtra("id_test"))
                .child("submitter").child(firebaseAuth.getUid());

        soal=findViewById(R.id.soal);
        terjawab=findViewById(R.id.terjawab);
        benar=findViewById(R.id.benar);
        salah=findViewById(R.id.salah);
        score=findViewById(R.id.score);
        ok=findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MahasiswaTest.fa.finish();
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
                soal.setText("Total jumlah "+dataSnapshot.child("jumlah_soal").getValue()+" soal");
                terjawab.setText("Terjawab "+((long)dataSnapshot.child("benar").getValue() + (long)dataSnapshot.child("salah").getValue())
                        +" soal");
                benar.setText("Benar "+dataSnapshot.child("benar").getValue()+" soal");
                salah.setText("Salah "+dataSnapshot.child("salah").getValue()+" soal");
                score.setText(((100/(long)dataSnapshot.child("jumlah_soal").getValue())*(long)dataSnapshot.child("benar").getValue())+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
