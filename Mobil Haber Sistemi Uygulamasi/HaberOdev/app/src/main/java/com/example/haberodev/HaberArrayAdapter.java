/*
 * Array adapterler için;
 * https://medium.com/cool-digital-solutions/listview-i%C3%A7in-custom-adapter-yaz%C4%B1m%C4%B1-ve-kullan%C4%B1m%C4%B1-a3727684a7eb
 * */

package com.example.haberodev;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HaberArrayAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private ArrayList<Haber> haberler=new ArrayList<>();
    private Activity activity;

    public ArrayList<Haber> getHaberler() {
        return haberler;
    }

    public void setHaberler(ArrayList<Haber> haberler) {
        this.haberler = haberler;
    }

    public HaberArrayAdapter(Activity activity, ArrayList<Haber> haberler) {
        this.activity=activity;
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.haberler=haberler;
    }

    @Override
    public int getCount() {
        return haberler.size();
    }

    @Override
    public Object getItem(int position) {
        return haberler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return haberler.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=userInflater.inflate(R.layout.haber_list_item,null);
        TextView baslikTv=v.findViewById(R.id.baslik);
        TextView icerikTv=v.findViewById(R.id.icerik);
        ImageView imageView=v.findViewById(R.id.simge);
        Haber h=haberler.get(position);
        baslikTv.setText(h.getBaslik());
        String icerik=h.getIcerik().length()>25?h.getIcerik().substring(0,25)+"...":h.getIcerik()+"...";
        icerikTv.setText(icerik);
        imageView.setImageDrawable(h.getResimImg(activity));
        return v;
    }
}
