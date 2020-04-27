package site.team2dev.m_lerning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MahasiswaNilai extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<ListItem1> classItems;
    AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_layout);

        title=findViewById(R.id.title);
        title.setText("Nilai");

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        classItems=new ArrayList<>();
        classItems.add(new ListItem1("task", "Nilai Task"));
        classItems.add(new ListItem1("test", "Nilai Test"));

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterItem(classItems,this, this::detail);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void detail(String s) {
        Intent i=new Intent(MahasiswaNilai.this, MahasiswaNilaiDetail.class);
        i.putExtra("id_prodi", getIntent().getStringExtra("id_prodi"));
        i.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
        i.putExtra("id_mk", getIntent().getStringExtra("id_mk"));
        i.putExtra("jenis", s);
        startActivity(i);
    }
}
