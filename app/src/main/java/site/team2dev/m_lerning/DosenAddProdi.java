package site.team2dev.m_lerning;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DosenAddProdi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView title;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ArrayList<ListItem1> prodiItems=new ArrayList<>();
        prodiItems.add(new ListItem1("Teknik Informatika"));
        prodiItems.add(new ListItem1("TRKJ"));

        title=findViewById(R.id.title);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        adapter=new AdapterAddItem(prodiItems);

        title.setText("Prodi");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
