package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class MahasiswaHome extends AppCompatActivity {

    ImageView logout;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_home);

        logout= findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(MahasiswaHome.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}
