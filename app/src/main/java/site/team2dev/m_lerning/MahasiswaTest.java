package site.team2dev.m_lerning;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MahasiswaTest extends AppCompatActivity {
    TextView soal, p1, p2, p3, p4, timer, jumlah;
    int soalke=0, benar=0, salah=0;
    List<String> idSoal;
    CountDownTimer countDownTimer;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ImageView back;
    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_test);
        fa=this;

        idSoal=(ArrayList<String>) getIntent().getSerializableExtra("idSoal");

        soal=findViewById(R.id.soal);
        p1=findViewById(R.id.pilihan1);
        p2=findViewById(R.id.pilihan2);
        p3=findViewById(R.id.pilihan3);
        p4=findViewById(R.id.pilihan4);
        timer=findViewById(R.id.timer);
        jumlah=findViewById(R.id.jumlah);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("jurusan").child("tik").child("prodi")
                .child(getIntent().getStringExtra("id_prodi")).child("kelas").child(getIntent().getStringExtra("id_kelas"))
                .child("mk").child(getIntent().getStringExtra("id_mk")).child("test").child(getIntent().getStringExtra("id_test"));


        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soalke<1){
                    finish();
                }else{
                    Toast.makeText(MahasiswaTest.this, "Anda tidak bisa keluar sebelum menyelesaikan test", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
       if(soalke<1){
           super.onBackPressed();
       }else{
           Toast.makeText(MahasiswaTest.this, "Anda tidak bisa keluar sebelum menyelesaikan test", Toast.LENGTH_LONG).show();
       }

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("submitter").hasChild(firebaseAuth.getUid())){
                    Intent i=new Intent(MahasiswaTest.this, MahasiswaScoreTest.class);
                    i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
                    i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
                    i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
                    i.putExtra("id_test", getIntent().getStringExtra("id_test"));
                    startActivity(i);
                }else{
                    getQuestion();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getQuestion() {

        if(soalke>=idSoal.size()){
            countDownTimer.cancel();
            databaseReference.child("submitter").child(firebaseAuth.getUid()).child("jumlah_soal").setValue(idSoal.size());
            databaseReference.child("submitter").child(firebaseAuth.getUid()).child("benar").setValue(benar);
            databaseReference.child("submitter").child(firebaseAuth.getUid()).child("salah").setValue(salah);
            Intent i=new Intent(MahasiswaTest.this, MahasiswaScoreTest.class);
            i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
            i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
            i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
            i.putExtra("id_test", getIntent().getStringExtra("id_test"));
            startActivity(i);

        }else{
            timer(10, timer);
            jumlah.setText("soal "+(soalke+1)+" dari "+idSoal.size());
            databaseReference.child("soal").child(idSoal.get(soalke)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    soal.setText(dataSnapshot.getValue(ListSoal.class).getItemSoal());
                    p1.setText(dataSnapshot.getValue(ListSoal.class).getPilihan1());
                    p2.setText(dataSnapshot.getValue(ListSoal.class).getPilihan2());
                    p3.setText(dataSnapshot.getValue(ListSoal.class).getPilihan3());
                    p4.setText(dataSnapshot.getValue(ListSoal.class).getPilihan4());

                    p1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p1.setEnabled(false);
                            countDownTimer.cancel();
                            soalke++;
                            if(p1.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                benar++;
                                p1.setTextColor(Color.GREEN);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p1.setEnabled(true);
                                        p1.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);
                            }else{
                                salah++;
                                p1.setTextColor(Color.RED);
//                                check the right answer
                                if(p2.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p2.setTextColor(Color.GREEN);
                                }else if(p3.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p3.setTextColor(Color.GREEN);
                                }else if(p4.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p4.setTextColor(Color.GREEN);
                                }

                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p1.setEnabled(true);
                                        p1.setTextColor(getResources().getColor(R.color.text));
                                        p2.setTextColor(getResources().getColor(R.color.text));
                                        p3.setTextColor(getResources().getColor(R.color.text));
                                        p4.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);

                            }
                        }
                    });

                    p2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p2.setEnabled(false);
                            countDownTimer.cancel();
                            soalke++;
                            if(p2.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                p2.setTextColor(Color.GREEN);
                                benar++;
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p2.setEnabled(true);
                                        p2.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);
                            }else{
                                p2.setTextColor(Color.RED);
                                salah++;
//                                check the right answer
                                if(p1.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p1.setTextColor(Color.GREEN);
                                }else if(p3.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p3.setTextColor(Color.GREEN);
                                }else if(p4.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p4.setTextColor(Color.GREEN);
                                }

                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p2.setEnabled(true);
                                        p1.setTextColor(getResources().getColor(R.color.text));
                                        p2.setTextColor(getResources().getColor(R.color.text));
                                        p3.setTextColor(getResources().getColor(R.color.text));
                                        p4.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);

                            }
                        }
                    });

                    p3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p3.setEnabled(false);
                            countDownTimer.cancel();
                            soalke++;
                            if(p3.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                benar++;
                                p3.setTextColor(Color.GREEN);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p3.setEnabled(true);
                                        p3.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);
                            }else{
                                salah++;
                                p3.setTextColor(Color.RED);
//                                check the right answer
                                if(p1.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p1.setTextColor(Color.GREEN);
                                }else if(p2.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p2.setTextColor(Color.GREEN);
                                }else if(p4.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p4.setTextColor(Color.GREEN);
                                }

                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p3.setEnabled(true);
                                        p1.setTextColor(getResources().getColor(R.color.text));
                                        p2.setTextColor(getResources().getColor(R.color.text));
                                        p3.setTextColor(getResources().getColor(R.color.text));
                                        p4.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);

                            }
                        }
                    });

                    p4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p4.setEnabled(false);
                            countDownTimer.cancel();
                            soalke++;
                            if(p4.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                benar++;
                                p4.setTextColor(Color.GREEN);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p4.setEnabled(true);
                                        p4.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);
                            }else{
                                salah++;
                                p4.setTextColor(Color.RED);
//                                check the right answer
                                if(p1.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p1.setTextColor(Color.GREEN);
                                }else if(p3.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p3.setTextColor(Color.GREEN);
                                }else if(p2.getText().toString().trim().equals(dataSnapshot.getValue(ListSoal.class).getBenar())){
                                    p2.setTextColor(Color.GREEN);
                                }

                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        p4.setEnabled(true);
                                        p1.setTextColor(getResources().getColor(R.color.text));
                                        p2.setTextColor(getResources().getColor(R.color.text));
                                        p3.setTextColor(getResources().getColor(R.color.text));
                                        p4.setTextColor(getResources().getColor(R.color.text));
                                        getQuestion();
                                    }
                                },1500);

                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public void timer(int seconds, final TextView tv){
        countDownTimer=new CountDownTimer(seconds * 1000 + 1000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds=(int) (millisUntilFinished/1000);
                int minutes=seconds / 60;
                seconds=seconds%60;
                tv.setText(String.format("%02d", minutes)+" : "+String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                soalke++;
                getQuestion();
            }
        }.start();
    }


}
