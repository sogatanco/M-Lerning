package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {

    ImageView logout, prodi, kelas, dosen, mk, mahasiswa;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout=findViewById(R.id.logout);
        prodi=findViewById(R.id.prodi);
        kelas=findViewById(R.id.kelas);
        dosen=findViewById(R.id.dosen);
        mk=findViewById(R.id.mk);
        mahasiswa=findViewById(R.id.mahasiswa);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminHome.this, LoginActivity.class));
            }
        });

        prodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this, AdminAddProdi.class));
            }
        });

        kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminHome.this, AaListProdi.class);
                i.putExtra("next", "site.team2dev.m_lerning.AdminAddClass");
                startActivity(i);
            }
        });

        dosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this, AdminAddDosen.class));
            }
        });

        mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminHome.this, AaListProdi.class);
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AdminAddMk");
                startActivity(i);
            }
        });

        mahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminHome.this, AaListProdi.class);
                i.putExtra("next", "site.team2dev.m_lerning.AaListKelas");
                i.putExtra("next2", "site.team2dev.m_lerning.AdminAddMahasiswa");
                startActivity(i);
            }
        });


    }
}
