package site.team2dev.m_lerning;

import android.os.Build;

import androidx.annotation.RequiresApi;

import static java.util.Objects.hash;

class ListItem2 {
    private String itemId, itemName, itemKeterangan;

    public ListItem2(){
//
    }


    public ListItem2(String itemId, String itemName, String itemKeterangan) {

        this.itemId=itemId;
        this.itemName=itemName;
        this.itemKeterangan=itemKeterangan;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemKeterangan(){
        return itemKeterangan;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem2 customer = (ListItem2) o;
        return itemId == customer.itemId;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return hash(itemId);
    }


}
