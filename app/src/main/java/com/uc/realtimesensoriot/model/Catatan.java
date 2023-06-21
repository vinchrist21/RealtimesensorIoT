package com.uc.realtimesensoriot.model;

public class Catatan {
    private String id, berat, biaya, datepersubmit;

    public Catatan(){

    }

    public Catatan(String berat, String biaya, String datepersubmit, String formattedDate){
        this.berat = berat;
        this.biaya = biaya;
        this.datepersubmit = formattedDate; // Menggunakan formattedDate sebagai nilai datepersubmit
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public void setDatepersubmit(String datepersubmit) {
        this.datepersubmit = datepersubmit;
    }

    public String getDatepersubmit() {
        return datepersubmit;
    }

    public String getBerat() {
        return berat;
    }

    public String getBiaya() {
        return biaya;
    }

}
