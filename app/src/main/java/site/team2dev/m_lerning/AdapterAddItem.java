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

public class AdapterAddItem extends RecyclerView.Adapter<AdapterAddItem.ItemHolder> {
    private List<ListItem1> listData;
    private Context context;
    private CallDeleteItem callDeleteItem;

    public AdapterAddItem(List<ListItem1> listData, Context context, CallDeleteItem callDeleteItem){
        this.listData=listData;
        this.context=context;
        this.callDeleteItem = callDeleteItem;
    }



    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public String id;
        ImageView delete;

      public ItemHolder(@NonNull View itemView) {
          super(itemView);
          item=itemView.findViewById(R.id.item);
          delete=itemView.findViewById(R.id.icon);

          delete.setOnClickListener(this);
          item.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  callDeleteItem.deleteItem(id+" detail");
              }
          });
      }

        @Override
        public void onClick(View v) {
            callDeleteItem.deleteItem(id+" delete");
        }
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_item, parent, false);
        ItemHolder ih=new ItemHolder(view);
        return  ih;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListItem1 currentItem=listData.get(position);
        holder.item.setText(currentItem.getItemName());
        holder.id=currentItem.getItemId();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }




}
