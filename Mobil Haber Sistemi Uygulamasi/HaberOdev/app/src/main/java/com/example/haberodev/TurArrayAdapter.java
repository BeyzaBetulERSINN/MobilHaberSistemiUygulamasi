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
import android.widget.TextView;

import java.util.ArrayList;


public class TurArrayAdapter extends BaseAdapter {
    ArrayList<Tur> turList;
    private Activity activity;
    private LayoutInflater userInflater;
    public TurArrayAdapter(ArrayList<Tur> turList,Activity activity) {
        this.activity=activity;
        this.userInflater=(LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.turList=turList;

    }

    @Override
    public int getCount() {
        return turList.size();
    }

    @Override
    public Object getItem(int position) {
        return turList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return turList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=userInflater.inflate(R.layout.tur_list_item,null);
        TextView idTv=v.findViewById(R.id.turID);
        TextView isimTv=v.findViewById(R.id.turName);
        Tur t=turList.get(position);
        idTv.setText(t.getId()+"");
        isimTv.setText(t.getTur());
        return v;
    }
}
