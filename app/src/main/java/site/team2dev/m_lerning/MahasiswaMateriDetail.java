package site.team2dev.m_lerning;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

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
import androidx.appcompat.widget.AppCompatTextView;

public class MahasiswaMateriDetail extends AppCompatActivity {
    AppCompatImageView back;
    AppCompatTextView title;
    PDFView pdfView;
    DatabaseReference databaseReference;
    Boolean flags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_materi_detail);
         flags=false;

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=findViewById(R.id.title);
        pdfView=findViewById(R.id.pdfView);

        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("materi")
                .child(getIntent().getStringExtra("id_materi"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.getValue(MateriItem.class).getItemName());
                if(flags==false) {
                    new AsyncTask<Void, Void, Void>() {
                        @SuppressLint("WrongThread")
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                InputStream input = new URL(dataSnapshot.getValue(MateriItem.class).getItemFile()).openStream();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
