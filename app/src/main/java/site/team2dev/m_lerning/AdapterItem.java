package site.team2dev.m_lerning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemHolder> {
    private List<ListItem1> listData;
    private Context context;
    private CallDetailItem callDetailItem;


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public String id;

      public ItemHolder(@NonNull View itemView) {
          super(itemView);
          item=itemView.findViewById(R.id.item);

          item.setOnClickListener(this);
      }

        @Override
        public void onClick(View v) {
            callDetailItem.detail(id);
        }
    }

    public AdapterItem(List<ListItem1> listData, Context context, CallDetailItem callDetailItem){
        this.listData=listData;
        this.context=context;
        this.callDetailItem=callDetailItem;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1, parent, false);
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
