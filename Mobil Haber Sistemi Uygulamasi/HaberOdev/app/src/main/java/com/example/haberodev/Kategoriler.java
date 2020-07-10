package com.example.haberodev;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Kategoriler extends AppCompatActivity {
    String seciliTurlerStr="";
    String seciliTurlerLabel="";
    TextView seciliTurlerTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoriler);

        //Arayüzdeki Viewleri değişkenlere atadım.
        ListView lv=findViewById(R.id.turLV);
        seciliTurlerTV=findViewById(R.id.seciliKategorilerTxt);


        final ArrayList<Tur> turler=turleriCek();   //Aşağıdaki fonksiyon ile sunucudan tüm türleri çektim.
        final ArrayList<Tur> seciliTurlerList=new ArrayList<>();    //Seçili türleri belirlemek için bir ArrayList oluşturdum.

        final SharedPreferences sharedPref = this.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);   //Seçili türlere her yerden erişmek için SharedPreferenceste tutuyorum, bu yüzden SharedPreferences'i çağırdım.

        seciliTurlerStr=sharedPref.getString("seciliTurler","");    //Seçili türleri virgülle ayrılmış idler olarak SharedPreferences'ten çektim.

        if(!seciliTurlerStr.isEmpty()){
            String[] seciliTurlerStrArr=seciliTurlerStr.split(",");     //Seçili türleri virgüllerden ayırarak dizi haline getirdim.
            for (String s : seciliTurlerStrArr) {   //Bu dizide dönüyorum.
                for(Tur t :turler) {                  //Aynı zamanda ona eşit bir tür var mı diye kıyaslamak için tüm türleri dönüyorum.
                    if (s.equals(t.getId()+"")) {            //Eşit olan çıkarsa,

                        seciliTurlerList.add(t);    //Seçili türlerin Tur tipinde tutulduğu Listeye ekliyorum.
                        seciliTurlerLabel += t.tur + ",";   //Arayüzde göstermek için seçili türlerin string değerlerini bir String değişkende tutuyorum.
                    }
                }
            }
        }
        seciliTurlerTV.setText(seciliTurlerLabel);  //Arayüzdeki seçili türlerin gösterildiği label'i yeniliyorum.
        TurArrayAdapter adapter=new TurArrayAdapter(turler,this);   //Listview'de göstermek için kendi yazdığım adaptörü çağırıyorum.
        lv.setAdapter(adapter);     //ListView'in adaptörünü ayarlıyorum.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //Bir iteme tıklanma durumunda ne olacağını belirleyen listener'i oluşturdum.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(seciliTurlerList.contains(turler.get(position))){    //Eğer seçili türlerden oluşan listemde tıklanan tür varsa,
                    seciliTurlerList.remove(turler.get(position));      //seçili türler listesinden bu türü çıkartıyorum.
                    //Kontrol et silmeyebilir
                }
                else{       //Eğer tıklanan tür listede yoksa,
                    seciliTurlerList.add(turler.get(position)); //Bu türü seçilenlere ekliyorum.
                }
                seciliTurlerLabel="";   //Arayüzde gösterilecek ve SharedPref'te tutulacak stringleri sıfırlıyorum.
                seciliTurlerStr="";
                for(int i=0;i<seciliTurlerList.size();i++){ //Her seçili tür için dönüp,
                    seciliTurlerLabel+=seciliTurlerList.get(i).tur+","; //Önce türlerin isimlerini Arayüzde gösterilecek text'e ekliyorum
                    seciliTurlerStr+=seciliTurlerList.get(i).id+",";    //Sonra SharedPref'te tutulacak Texte id'lerini ekliyorum.
                }
                seciliTurlerTV.setText(seciliTurlerLabel);  //Arayüzdeki seçili türlerin gösterildiği label'i yeniliyorum.
                SharedPreferences.Editor editor = sharedPref.edit();    //SharedPref'i düzenleyecek editör nesnesini çağırıyorum.
                editor.putString("seciliTurler",seciliTurlerStr);       //Yenilenmiş stringi SharedPref'e atıyorum.
                editor.apply(); //Değişiklikleri uyguluyorum.
            }
        });

    }
    ArrayList<Tur> turleriCek(){
        ArrayList<Tur> turler=new ArrayList<>();
        String host=this.getSharedPreferences("Ayarlar", Context.MODE_PRIVATE).getString("host","0.0.0.0");
       // String kategoriler=this.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE).getString("kategoriler","");
        try {
            String url=host+"/Haber/api.php/Tur";
            InputStream is=new URL(url).openConnection().getInputStream();
            InputStreamReader reader=new InputStreamReader(is);
            Gson gson=new GsonBuilder().create();
            JsonReader jsonReader=new JsonReader(reader);
            jsonReader.setLenient(true);
            JsonParser parser=new JsonParser();
            JsonArray array=parser.parse(jsonReader).getAsJsonArray();
            for(JsonElement el:array){
                turler.add(gson.fromJson(el,Tur.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        return turler;
    }
}
