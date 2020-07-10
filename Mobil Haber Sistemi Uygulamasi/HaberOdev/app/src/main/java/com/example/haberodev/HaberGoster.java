package com.example.haberodev;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HaberGoster extends AppCompatActivity {
    String host;
    Haber haber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context ctx=this;
        super.onCreate(savedInstanceState);
        host=this.getSharedPreferences("Ayarlar", Context.MODE_PRIVATE).getString("host","0.0.0.0");

        setContentView(R.layout.activity_haber_goster);

        if(getIntent().getExtras().containsKey("id")){
            haber=new Haber();
            haber.setId(Integer.parseInt(getIntent().getStringExtra("id")));
        }else
            haber=(Haber) getIntent().getSerializableExtra("haber");    //Ana aktiviteden gelen haberi çektim.
        haber=haberCek(haber.getId());      //Oradan gelen id bilgisini kullanarak haberin en güncel halini çektim.
        Log.d("asd", "onCreate: "+haber.getId());
        ImageView iV=findViewById(R.id.imageView);  //Arayüz için gerekli değişkenler...
        TextView baslik=findViewById(R.id.baslikTV);
        TextView icerik=findViewById(R.id.icerikTV);
        TextView goruntulenme=findViewById(R.id.goruntulenmeTV);
        final Button begenBtn=findViewById(R.id.likeBtn);
        final Button begenmeBtn=findViewById(R.id.dislikeBtn);

        iV.setImageDrawable(haber.getResimImg(ctx));    //Fotoğrafı imageView'e atadım.
        baslik.setText(haber.getBaslik());      //Başlık ve içerik kısmını da doldurdum.
        icerik.setText(haber.getIcerik());
        goruntulenme.setText("Görüntülenme:"+haber.getGoruntulenme());
        begenBtn.setText("Beğen("+haber.getBegenme()+")");
        begenmeBtn.setText("Beğenme("+haber.getBegenmeme()+")");

        begenBtn.setOnClickListener(new View.OnClickListener() {    //Beğenme ve beğenmeme butonuna tıklandığında sunucuya bu yönde istek gönderdim.
            @Override
            public void onClick(View v) {
                try {
                    URL url=new URL(host+"/Haber/api.php/Haber/"+haber.getId()+"?action=begen");
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.getInputStream();
                    //haber=haberCek(haber.getId());
                    haber.setBegenmeme(0);
                    haber.setBegenme(1);
                    begenmeBtn.setText("Beğenme("+haber.getBegenmeme()+")");
                    begenBtn.setText("Beğen("+haber.getBegenme()+")");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        begenmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url=new URL(host+"/Haber/api.php/Haber/"+haber.getId()+"?action=begenme");
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.getInputStream();
                    //haber=haberCek(haber.getId());
                    haber.setBegenmeme(1);
                    haber.setBegenme(0);
                    begenmeBtn.setText("Beğenme("+haber.getBegenmeme()+")");
                    begenBtn.setText("Beğen("+haber.getBegenme()+")");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    Haber haberCek(int id){     //Haberi çekmek için kullandığım fonksiyon.
        Haber haber=new Haber();
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new URL(host+"/Haber/api.php/Haber/"+id).openConnection().getInputStream()));
            String data=br.readLine();
            Gson gson=new GsonBuilder().setLenient().create();
            haber=gson.fromJson(data,Haber.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return haber;
    }
}
