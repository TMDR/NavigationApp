package com.student.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class listlocadapter extends ArrayAdapter<locstruct>  {
    ArrayList<locstruct> arr;

    public listlocadapter(Context context, int resource, ArrayList<locstruct> objects) {
        super(context, resource);
        arr = objects;
    }
    public void add(locstruct coord){
        arr.add(coord);
        this.notifyDataSetChanged();
    }



    @Override
    public void clear() {
        arr.clear();
        this.notifyDataSetChanged();
    }

    public void remove(locstruct ob){
        arr.remove(ob);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater i =((Activity) parent.getContext()).getLayoutInflater();
        View v = i.inflate(R.layout.listlayout,null);
        TextView longitude = v.findViewById(R.id.longg);
        TextView lattitude = v.findViewById(R.id.latt);
        longitude.setText(arr.get(position).longitutde);
        lattitude.setText(arr.get(position).latittude);
        lattitude.setTag(arr.get(position));
        return v;
    }

    @Override
    public int getCount() {
        return arr.size();
    }
}
