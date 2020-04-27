package site.team2dev.m_lerning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class MahasiswaTugasDetail extends AppCompatActivity {
    AppCompatImageView back;
    TextView detail;
    AppCompatTextView deadline, title;
    private static final int PICK_PDF_CODE =2342;
    DatabaseReference databaseReference;
    Button upload;
    FirebaseAuth firebaseAuth;
    StorageReference mStorageReference;
    ProgressBar progressBar;
    EditText filename;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dateDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_tugas_detail);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar=findViewById(R.id.progressBar);
        filename=findViewById(R.id.filename);
        filename.setKeyListener(null);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("task").child(getIntent().getStringExtra("id_task"));

        firebaseAuth=FirebaseAuth.getInstance();

        upload=findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
            }
        });

        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        deadline=findViewById(R.id.deadline);
        title=findViewById(R.id.title);
        detail=findViewById(R.id.detail);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("submitter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseAuth.getUid())){
                   filename.setText(dataSnapshot.child(firebaseAuth.getUid()).child("file").getValue().toString());
                   upload.setText("reupload");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deadline.setText("Deadline\n"+dataSnapshot.getValue(TaskItem.class).getItemDeadline());
                title.setText(dataSnapshot.getValue(TaskItem.class).getItemName());
                detail.setText(dataSnapshot.getValue(TaskItem.class).getItemDetail());
                dateDeadline=dataSnapshot.getValue(TaskItem.class).getItemDeadline();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child("tugas/"
                +System.currentTimeMillis()+".pdf" );
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        upload.setText("Reupload");
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                filename.setText(uri.toString());
                                Toast.makeText(getApplicationContext(),"Upload succesfully!", Toast.LENGTH_LONG).show();
                                databaseReference.child("submitter").child(firebaseAuth.getUid()).child("file").setValue(uri.toString());
                                databaseReference.child("submitter").child(firebaseAuth.getUid()).child("time").setValue(simpleDateFormat.format(calendar.getTime()));
                                databaseReference.child("submitter").child(firebaseAuth.getUid()).child("nilai").setValue("0");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                    }
                });
    }
}
