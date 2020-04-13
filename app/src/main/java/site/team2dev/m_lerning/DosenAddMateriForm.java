package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DosenAddMateriForm extends AppCompatActivity {

    private static final int PICK_PDF_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_materi_form);
//        Toast.makeText(this, getIntent().getStringExtra("id_prodi")+" "+getIntent().getStringExtra("id_kelas")+" "+getIntent().getStringExtra("id_mk"), Toast.LENGTH_LONG).show();

    }

    public void select(View view) {
        Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
        intentPDF.setType("application/pdf");
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intentPDF , "Select Picture"), PICK_PDF_CODE);
    }
}
