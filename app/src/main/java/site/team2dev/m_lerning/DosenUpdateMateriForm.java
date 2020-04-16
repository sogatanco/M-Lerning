package site.team2dev.m_lerning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class DosenUpdateMateriForm extends AppCompatActivity {
    private static final int PICK_PDF_CODE=2342;
    AppCompatTextView title, save;
    AppCompatImageView back;
    PDFView pdfView;
    TextView persent;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    StorageReference mStorageReference;
    ImageView upload;
    EditText filename, nama;
    Boolean flags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_materi_form);

        title=findViewById(R.id.title);
        title.setText("Update Materi");
        flags=false;

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flags=true;
                finish();
            }
        });

        pdfView=findViewById(R.id.pdfView);
        persent=findViewById(R.id.persen);
        progressBar=findViewById(R.id.progressBar);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("materi").child(getIntent().getStringExtra("id"));

        upload=findViewById(R.id.uploadicon);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_CODE);
            }
        });

        filename=findViewById(R.id.filename);
        filename.setKeyListener(null);

        nama=findViewById(R.id.nama);

        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vnama=nama.getText().toString().trim();
                String vfile=filename.getText().toString().trim();

                if(!TextUtils.isEmpty(vnama)&& !TextUtils.isEmpty(vfile)){
                    databaseReference.child("itemName").setValue(vnama);
                    databaseReference.child("itemFile").setValue(vfile);

                    Toast.makeText(DosenUpdateMateriForm.this, "Materi berhasil di update", Toast.LENGTH_LONG).show();
                    flags=true;
                    finish();
                }else{
                    Toast.makeText(DosenUpdateMateriForm.this, "Field tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama.setText(dataSnapshot.getValue(MateriItem.class).getItemName());
                filename.setText(dataSnapshot.getValue(MateriItem.class).getItemFile());
                String file=dataSnapshot.getValue(MateriItem.class).getItemFile();
                if(flags==false){
                    new AsyncTask<Void, Void, Void>() {
                        @SuppressLint("WrongThread")
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                InputStream input = new URL(file).openStream();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PDF_CODE && resultCode==RESULT_OK && data != null ){
            if(data.getData() !=null){
                uploadFile(data.getData());
            }else{
                Toast.makeText(DosenUpdateMateriForm.this, "No file choosen", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadFile(Uri data) {
        filename.setText("");
        progressBar.setVisibility(View.VISIBLE);
        persent.setText("Uploading 0/100");
        StorageReference sRef = mStorageReference.child("materi/"
                +System.currentTimeMillis()+".pdf" );
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        persent.setText("");
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                filename.setText(uri.toString());
                                Toast.makeText(getApplicationContext(),"Upload succesfully!", Toast.LENGTH_LONG).show();
                                if(flags==false) {
                                    new AsyncTask<Void, Void, Void>() {
                                        @SuppressLint("WrongThread")
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            try {
                                                InputStream input = new URL(uri.toString()).openStream();
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
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        persent.setText("");
                        Toast.makeText(getApplicationContext(),"Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                        persent.setText("Uploading "+(int)progress+"/100");
                    }
                });

    }
}
