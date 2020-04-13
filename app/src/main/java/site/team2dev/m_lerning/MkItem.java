package site.team2dev.m_lerning;

public class MkItem {
    private String itemId;
    private String itemName;
    private String itemDosen;


    public MkItem(){
    }

    public MkItem(String itemId, String itemName, String itemDosen){
        this.itemId=itemId;
        this.itemName=itemName;
        this.itemDosen=itemDosen;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemDosen(){
        return itemDosen;
    }
}
