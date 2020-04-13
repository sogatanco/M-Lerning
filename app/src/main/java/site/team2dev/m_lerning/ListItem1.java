package site.team2dev.m_lerning;

import android.os.Build;

import androidx.annotation.RequiresApi;

import static java.util.Objects.hash;

class ListItem1 {
    private String itemId, itemName;

    public ListItem1(){
//
    }


    public ListItem1(String itemId, String itemName) {

        this.itemId=itemId;
        this.itemName=itemName;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem1 customer = (ListItem1) o;
        return itemId == customer.itemId;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return hash(itemId);
    }



}
