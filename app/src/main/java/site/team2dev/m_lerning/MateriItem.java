package site.team2dev.m_lerning;

class MateriItem {
    private String itemId, itemName, itemFile;

    public MateriItem(){
//
    }


    public MateriItem(String itemId, String itemName, String itemFile) {

        this.itemId=itemId;
        this.itemName=itemName;
        this.itemFile=itemFile;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemFile(){
        return itemFile;
    }


}