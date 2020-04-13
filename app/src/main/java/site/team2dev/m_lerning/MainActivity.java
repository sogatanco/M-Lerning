package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser !=null){
                    level();
                }
                else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    public  void level(){
        databaseReference.child("users").child(firebaseAuth.getUid()).child("level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("data", dataSnapshot.getValue().toString());
                if(dataSnapshot.getValue().toString().equals("admin")){
                    startActivity(new Intent(MainActivity.this, AdminHome.class));
                }
                else if(dataSnapshot.getValue().toString().equals("dosen")){
                    startActivity(new Intent(MainActivity.this, DosenHome.class));
                }
                else if(dataSnapshot.getValue().toString().equals("mahasiswa")){
                    startActivity(new Intent(MainActivity.this, MahasiswaHome.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
