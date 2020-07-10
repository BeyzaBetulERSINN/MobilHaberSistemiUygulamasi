package com.example.haberodev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Context ctx;
    ListView listView;
    ArrayList<Haber> haberler;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==123){
            haberler=haberList();
            HaberArrayAdapter adapter=(HaberArrayAdapter)listView.getAdapter();
            adapter.setHaberler(haberler);
            adapter.notifyDataSetChanged();
            listView.invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);   //Genel ayarları tutacak olan SharedPreferences nesnesini çağırıyorum.
        sharedPref.edit().putString("host","http://192.168.43.140").apply(); //Bilgisayarımın ağdaki ip adresini buraya ekliyorum. IP adresin değiştikçe düzenlemen gerekecek.

        ctx=this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //HTTP requestlerini yaparken hata aldığım için bu ayarları yaptım, stackoverflow.com'da aldığım hataların çözümü olarak buldum.
        StrictMode.setThreadPolicy(policy);//Üstteki satırın devamı.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button kategoriSecBtn=findViewById(R.id.turBtn);

        kategoriSecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx,Kategoriler.class);
                startActivityForResult(i,123);
            }
        });

        haberler=haberList();    //Haberleri Sunucumdan çekiyorum.
        listView=findViewById(R.id.haberListview); //Haberlerin listeleneceği Listview'i değikene atıyorum
        HaberArrayAdapter arrayAdapter=new HaberArrayAdapter(this,haberler);    //Haberleri resimleriyle birlikte göstermek için yazdığım Adaptörü çağırıyorum.
        listView.setAdapter(arrayAdapter);  //Adaptörü Listviewe bağlıyorum.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      //Bir habere tıklandığında ne olacağını belirtecek listener'ı yazıyorum.
                Intent i=new Intent(ctx,HaberGoster.class); //Yeni bir sayfada haberi göstermek için intent oluşturuyorum.
                i.putExtra("haber",haberler.get(position)); //Seçili haberi Extra olarak intente ekliyorum.
                startActivity(i);   //İntenti çağırıyorum.
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("asd", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d("asd", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private ArrayList<Haber> haberList(){   //Haberleri çeken fonksiyon
        ArrayList<Haber> haberler=new ArrayList<>();    //Çekilen haberlerin tutulacağı Liste
        String host=this.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE).getString("host","0.0.0.0");
        String kategoriler=this.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE).getString("seciliTurler","");
        try {
            String url=host+"/Haber/api.php/Haber";
            if(!kategoriler.isEmpty()) {
                url += ("?turler=" + kategoriler.substring(0,kategoriler.length()-1)+"&");
            }

            InputStream is=new URL(url).openConnection().getInputStream();  //Sunucudan json şeklinde data çekip bunu bir class'a atmak için gerekli kodlar.
            InputStreamReader reader=new InputStreamReader(is);             //Detaylı bilgi için : http://ilkaygunel.com/blog/2016/gson-tutorial/
            BufferedReader br=new BufferedReader(reader);
            Gson gson=new GsonBuilder().create();
            JsonReader jsonReader=new JsonReader(reader);
            jsonReader.setLenient(true);
            JsonParser parser=new JsonParser();
            JsonArray array=parser.parse(jsonReader).getAsJsonArray();

            for(JsonElement el:array){
                haberler.add(gson.fromJson(el,Haber.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return haberler;
    }
}
