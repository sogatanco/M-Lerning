package site.team2dev.m_lerning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAddItem extends RecyclerView.Adapter<AdapterAddItem.ItemHolder> {
    private ArrayList<ListItem1> listData;


    public static class ItemHolder extends RecyclerView.ViewHolder{
        public TextView item;

      public ItemHolder(@NonNull View itemView) {
          super(itemView);
          item=itemView.findViewById(R.id.item);
      }
    }

    public AdapterAddItem(ArrayList<ListItem1> list){
        listData=list;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_item_layout, parent, false);
        ItemHolder ih=new ItemHolder(view);
        return  ih;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListItem1 currentItem=listData.get(position);
        holder.item.setText(currentItem.getItem());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
