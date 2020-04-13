package site.team2dev.m_lerning;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class AdminAddDosenForm extends AppCompatActivity {

    AppCompatTextView title;
    Spinner jk;
    FirebaseAuth firebaseAuth2;
    DatabaseReference dosen;
    DatabaseReference users;
    TextView save;
    AppCompatImageView back;

    EditText email, password, nama, alamat, tempat, tgllahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user_form);

        title=findViewById(R.id.title);
        title.setText("New Dosen");

        jk=findViewById(R.id.jk);
        List<String> list = new ArrayList<String>();
        list.add("laki");
        list.add("perempuan");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jk.setAdapter(dataAdapter);


        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        nama=findViewById(R.id.nama);
        alamat=findViewById(R.id.alamat);
        tempat=findViewById(R.id.tempat);
        tgllahir=findViewById(R.id.tgllahir);

        dosen=FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("dosen");
        users=FirebaseDatabase.getInstance().getReference("users");

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://m-leaning.firebaseio.com/")
                .setApiKey("AIzaSyAhWwqEHpd5Sn6aulfupbSXtc9TsTcy2f8")
                .setApplicationId("m-leaning").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "M-Leaning");
           firebaseAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            firebaseAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("M-Leaning"));
        }

        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vemail=email.getText().toString().trim();
                String vpassword=password.getText().toString().trim();
                String vnama=nama.getText().toString().trim();
                String valamat=alamat.getText().toString().trim();
                String vtempat=tempat.getText().toString().trim();
                String vtgllahir=tgllahir.getText().toString().trim();
                String vjk=jk.getSelectedItem().toString().trim();

                if(!TextUtils.isEmpty(vemail) && !TextUtils.isEmpty(vpassword) && !TextUtils.isEmpty(vnama) && !TextUtils.isEmpty(valamat)
                        && !TextUtils.isEmpty(vtempat) && !TextUtils.isEmpty(vtgllahir) && !TextUtils.isEmpty(vjk)){
                    firebaseAuth2.createUserWithEmailAndPassword(vemail, vpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                String ex = task.getException().toString();
                                Toast.makeText(AdminAddDosenForm.this, ex, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                UserItem dosenitem=new UserItem(firebaseAuth2.getUid(), vnama, vemail, vpassword, vjk, vtempat, vtgllahir, valamat);
                                dosen.child(firebaseAuth2.getUid()).setValue(dosenitem);
                                LevelItem levelItem=new LevelItem("dosen");
                                users.child(firebaseAuth2.getUid()).setValue(levelItem);
                                Toast.makeText(AdminAddDosenForm.this, "Dosen Added ",Toast.LENGTH_SHORT).show();
                                firebaseAuth2.signOut();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AdminAddDosenForm.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
