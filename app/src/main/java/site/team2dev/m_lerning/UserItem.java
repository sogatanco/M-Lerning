package site.team2dev.m_lerning;

class UserItem {
    private String itemId, itemName, itemEmail, itemPassword, itemJk, itemTempat, itemTglLahir, itemAlamat;

    public UserItem (){
//
    }


    public UserItem (String itemId, String itemName, String itemEmail, String itemPassword, String itemJk, String itemTempat, String itemTglLahir, String itemAlamat) {

        this.itemId=itemId;
        this.itemName=itemName;
        this.itemEmail=itemEmail;
        this.itemPassword=itemPassword;
        this.itemJk=itemJk;
        this.itemTempat=itemTempat;
        this.itemTglLahir=itemTglLahir;
        this.itemAlamat=itemAlamat;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public String getItemEmail(){
        return itemEmail;
    }

    public String getItemPassword(){
        return itemPassword;
    }

    public String getItemJk(){
        return itemJk;
    }

    public String getItemTempat(){
        return itemTempat;
    }

    public String getItemTglLahir(){
        return itemTglLahir;
    }

    public String getItemAlamat(){
        return itemAlamat;
    }


}