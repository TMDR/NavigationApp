package com.student.navigator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class waypointlist extends AppCompatActivity {

    ArrayList<String> lattitude = new ArrayList<String>();
    ArrayList<String> longitude = new ArrayList<String>();
    ListView listView;
    TextView txtvw;
    static Button buttadd;
    static Button buttcancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypointlist);
        lattitude = prefs.getarray("lattitude",this);
        longitude = prefs.getarray("longitude",this);
        listView = findViewById(R.id.listView);
        ArrayList<locstruct> locationn = new ArrayList<>();
        for(int i = 0; i < longitude.size(); i++){
            locationn.add(new locstruct(longitude.get(i),lattitude.get(i)));
        }
        txtvw = findViewById(R.id.textView);
        final listlocadapter adapterr = new listlocadapter(this, R.layout.listlayout,locationn);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_2, android.R.id.text1, lattitude);
        listView.setAdapter(adapterr);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView latt = view.findViewById(R.id.latt);
                TextView longg = view.findViewById(R.id.longg);
                Intent intent = new Intent();
                intent.putExtra("long",longg.getText());
                intent.putExtra("lat",latt.getText());
                setResult(2,intent);
                MainActivity.LocationSearch = true;
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(waypointlist.this).create();
                LayoutInflater inflater = waypointlist.this.getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.customloclayout, null);
                locstruct ls = (locstruct) view.findViewById(R.id.latt).getTag();
                MainActivity.longitude.remove(ls.longitutde);
                MainActivity.lattitude.remove(ls.latittude);
                prefs.savearray("lattitude",MainActivity.lattitude,waypointlist.this);
                prefs.savearray("longitude",MainActivity.longitude,waypointlist.this);
                listlocadapter lla = (listlocadapter) listView.getAdapter();
                lla.remove(ls);
                return false;
            }
        });
    }

    public void share(View v){
        TextView Mylat = v.getRootView().findViewById(R.id.latt);
        TextView MyLong = v.getRootView().findViewById(R.id.longg);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Lat = "+Mylat.getText()+" AND long = "+MyLong.getText());
        intent.setType("text/plain");
        Intent share = Intent.createChooser(intent,null);
        startActivity(share);
    }

    @Override
    public void onBackPressed() {
        MainActivity.LocationSearch = false;
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu0,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.clearallbutt:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                prefs.clear(waypointlist.this);
                                listlocadapter lla = (listlocadapter) listView.getAdapter();
                                lla.clear();
                                MainActivity.longitude.clear();
                                MainActivity.lattitude.clear();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(waypointlist.this);
                builder.setMessage("Are you sure you wanna clear all the destinations?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            case R.id.customloc:
                final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.customloclayout, null);
                final EditText customlat=dialogView.findViewById(R.id.customloc);
                final EditText customlon=dialogView.findViewById(R.id.customloc0);
                buttadd =dialogView.findViewById(R.id.buttonSubmit);
                buttcancel=dialogView.findViewById(R.id.buttonCancel);
                buttcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                buttadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locstruct newcoord= new locstruct(customlon.getText().toString(),customlat.getText().toString());
                        lattitude.add(customlat.getText().toString());
                        longitude.add(customlon.getText().toString());
                        MainActivity.lattitude.add(customlat.getText().toString());
                        MainActivity.longitude.add(customlon.getText().toString());
                        prefs.savearray("lattitude",lattitude,waypointlist.this);
                        prefs.savearray("longitude",longitude,waypointlist.this);
                        dialogBuilder.dismiss();
                        listlocadapter lla0 = (listlocadapter) listView.getAdapter();
                        lla0.add(newcoord);
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}