package site.team2dev.m_lerning;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class DosenSubmitterTaskDetail extends AppCompatActivity {

    DatabaseReference databaseReference;
    AppCompatImageView back;
    PDFView pdfView;
    EditText nilai;
    Boolean flags;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_submitter_task_detail);
        flags=true;

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("task").child(getIntent().getStringExtra("id_task"))
                .child("submitter").child(getIntent().getStringExtra("id_submitter"));

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pdfView=findViewById(R.id.pdfView);

        nilai=findViewById(R.id.nilai);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vnilai=nilai.getText().toString().trim();
                if(!TextUtils.isEmpty(vnilai)){
                    boolean numeric = true;
                    try {
                      Double  num = Double.parseDouble(vnilai);
                    } catch (NumberFormatException e) {
                        numeric = false;
                    }

                    if(numeric){
                       Double num=Double.parseDouble(vnilai);
                        if(num >=0 && num<=100){
                            databaseReference.child("nilai").setValue(vnilai);
                            Toast.makeText(DosenSubmitterTaskDetail.this, "Nilai telah diubah", Toast.LENGTH_LONG).show();
                            finish();

                        }else{
                            Toast.makeText(DosenSubmitterTaskDetail.this, "Field must be 0 to 100", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(DosenSubmitterTaskDetail.this, "must be nummeric", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(DosenSubmitterTaskDetail.this, "Field can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(flags==true) {
                    new AsyncTask<Void, Void, Void>() {
                        @SuppressLint("WrongThread")
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                InputStream input = new URL(dataSnapshot.child("file").getValue().toString()).openStream();
                                pdfView.fromStream(input)
                                        .enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .enableDoubletap(true)
                                        .defaultPage(0)
                                        .spacing(0)
                                        .autoSpacing(false)
                                        .pageFitPolicy(FitPolicy.WIDTH)
                                        .fitEachPage(false)
                                        .scrollHandle(null)
                                        .enableAntialiasing(true)
                                        .load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }
                nilai.setText(dataSnapshot.child("nilai").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
