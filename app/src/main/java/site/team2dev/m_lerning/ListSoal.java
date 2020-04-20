package site.team2dev.m_lerning;

class ListSoal {
    private String itemId, itemSoal, pilihan1, pilihan2, pilihan3, pilihan4, benar;

    public  ListSoal(){

    }


    public ListSoal(String itemId, String itemSoal, String pilihan1, String pilihan2, String pilihan3, String pilihan4, String benar) {

        this.itemId=itemId;
        this.itemSoal=itemSoal;
        this.pilihan1=pilihan1;
        this.pilihan2=pilihan2;
        this.pilihan3=pilihan3;
        this.pilihan4=pilihan4;
        this.benar=benar;
    }

    public String getItemId(){
        return itemId;
    }

    public String getItemSoal(){
        return itemSoal;
    }

    public String getPilihan1(){
        return pilihan1;
    }

    public String getPilihan2(){
        return pilihan2;
    }

    public String getPilihan3(){
        return pilihan3;
    }

    public String getPilihan4(){
        return pilihan4;
    }

    public String getBenar(){
        return benar;
    }

}
