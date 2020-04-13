package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class DosenHome extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView logout, mahasiswa, materi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_home);

        firebaseAuth=FirebaseAuth.getInstance();

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
    }
}
