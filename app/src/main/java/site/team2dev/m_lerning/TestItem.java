package site.team2dev.m_lerning;

class TestItem {
    private String itemId, itemName, itemDeadline;

    public TestItem(){
//
    }


    public TestItem(String itemId, String itemName, String itemDeadline) {

        this.itemId=itemId;
        this.itemName=itemName;
        this.itemDeadline=itemDeadline;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemDeadline(){
        return itemDeadline;
    }


}