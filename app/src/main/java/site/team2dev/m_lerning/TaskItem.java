package site.team2dev.m_lerning;

class TaskItem {
    private String itemId, itemName, itemDeadline, itemDetail;

    public TaskItem(){
//
    }


    public TaskItem(String itemId, String itemName, String itemDeadline, String itemDetail) {

        this.itemId=itemId;
        this.itemName=itemName;
        this.itemDeadline=itemDeadline;
        this.itemDetail=itemDetail;
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
    public  String getItemDetail(){
        return itemDetail;
    }


}