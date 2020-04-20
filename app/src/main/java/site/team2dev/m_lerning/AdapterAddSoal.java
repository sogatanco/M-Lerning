package site.team2dev.m_lerning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAddSoal extends RecyclerView.Adapter<AdapterAddSoal.ItemHolder> {
    private List<ListSoal> listSoals;
    private Context context;
    private CallDeleteItem callDeleteItem;

    public AdapterAddSoal(List<ListSoal> listSoals, Context context, CallDeleteItem callDeleteItem){
        this.listSoals=listSoals;
        this.context=context;
        this.callDeleteItem = callDeleteItem;
    }



    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView soal, pilihan1, pilihan2, pilihan3, pilihan4, benar;
        public String id;
        ImageView delete;

      public ItemHolder(@NonNull View itemView) {
          super(itemView);
          delete=itemView.findViewById(R.id.icon);
          soal=itemView.findViewById(R.id.soal);
          pilihan1=itemView.findViewById(R.id.pilihan1);
          pilihan2=itemView.findViewById(R.id.pilihan2);
          pilihan3=itemView.findViewById(R.id.pilihan3);
          pilihan4=itemView.findViewById(R.id.pilihan4);
          benar=itemView.findViewById(R.id.pilihanbenar);


          delete.setOnClickListener(this);
      }

        @Override
        public void onClick(View v) {
            callDeleteItem.deleteItem(id);
        }
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_soal, parent, false);
        ItemHolder ih=new ItemHolder(view);
        return  ih;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListSoal currentItem=listSoals.get(position);
        holder.id=currentItem.getItemId();
        holder.soal.setText(currentItem.getItemSoal());
        holder.pilihan1.setText(currentItem.getPilihan1());
        holder.pilihan2.setText(currentItem.getPilihan2());
        holder.pilihan3.setText(currentItem.getPilihan3());
        holder.pilihan4.setText(currentItem.getPilihan4());
        holder.benar.setText(currentItem.getBenar());
    }

    @Override
    public int getItemCount() {
        return listSoals.size();
    }


}
