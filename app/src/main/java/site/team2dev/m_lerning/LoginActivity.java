package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText email, password;
    Button tombol;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.uname);
        password=findViewById(R.id.pass);
        tombol=findViewById(R.id.button);

        databaseReference= FirebaseDatabase.getInstance().getReference();


        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vemail=email.getText().toString();
                String vpassword=password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(vemail, vpassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "not match", Toast.LENGTH_SHORT).show();
                        }
                        else{
                           level();
                        }
                    }
                });
            }
        });

    }


    public  void level(){
        databaseReference.child("users").child(firebaseAuth.getUid()).child("level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("data", dataSnapshot.getValue().toString());
                if(dataSnapshot.getValue().toString().equals("admin")){
                    startActivity(new Intent(LoginActivity.this, AdminHome.class));
                }
                else if(dataSnapshot.getValue().toString().equals("dosen")){
                    startActivity(new Intent(LoginActivity.this, DosenHome.class));
                }
                else if(dataSnapshot.getValue().toString().equals("mahasiswa")){
                    startActivity(new Intent(LoginActivity.this, MahasiswaHome.class));
                }else{
                    Toast.makeText(LoginActivity.this, "your account not registered", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
