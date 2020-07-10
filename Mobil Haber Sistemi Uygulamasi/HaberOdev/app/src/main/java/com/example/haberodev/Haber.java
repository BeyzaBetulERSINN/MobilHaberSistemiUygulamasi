package com.example.haberodev;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
//sonn kodd
//deneme
public class Haber implements Serializable {
    private String host;
    private  int id,turid,begenme,begenmeme,goruntulenme;
    private String baslik,icerik,resim;
    private Date tarih;

    public Haber(){}


    public Haber(int id, int turId, int begenme, int begenmeme, int goruntulenme, String isim, String icerik, String resim, Date tarih) {
        this.id = id;
        this.turid = turId;
        this.begenme = begenme;
        this.begenmeme = begenmeme;
        this.goruntulenme = goruntulenme;
        this.baslik = isim;
        this.icerik = icerik;
        this.resim = resim;
        this.tarih = tarih;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTurId() {
        return turid;
    }

    public void setTurId(int turId) {
        this.turid = turId;
    }

    public int getBegenme() {
        return begenme;
    }

    public void setBegenme(int begenme) {
        this.begenme = begenme;
    }

    public int getBegenmeme() {
        return begenmeme;
    }

    public void setBegenmeme(int begenmeme) {
        this.begenmeme = begenmeme;
    }

    public int getGoruntulenme() {
        return goruntulenme;
    }

    public void setGoruntulenme(int goruntulenme) {
        this.goruntulenme = goruntulenme;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String isim) {
        this.baslik = isim;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }
    public Drawable getResimImg(Context ctx){
        SharedPreferences sharedPref = ctx.getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);
        host=sharedPref.getString("host","0.0.0.0")+"/Haber/";
        try {
            InputStream is = (InputStream) new URL(host+this.resim).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Haber{" +
                "id=" + id +
                ", turId=" + turid +
                ", begenme=" + begenme +
                ", begenmeme=" + begenmeme +
                ", goruntulenme=" + goruntulenme +
                ", baslik='" + baslik + '\'' +
                ", icerik='" + icerik + '\'' +
                ", resim='" + resim + '\'' +
                ", tarih=" + tarih +
                '}';
    }
}
