package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.tripplanner.ListeActivite;

import java.util.ArrayList;

public class Favoris extends ListeActivite {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ListView listView;
    private ImageButton retour;
    private int c=0;
    ArrayList<String> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
        this.listView = (ListView) findViewById(R.id.liste);
        this.retour = (ImageButton) findViewById(R.id.retour);

        //Bundle bundle = getIntent().getExtras();
        //if (bundle != null){
        favs = getIntent().getStringArrayListExtra("item");
      //}


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, favs);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c++;
                Handler handler = new Handler();
                Runnable runn = new Runnable() {
                    @Override
                    public void run() {
                        c = 0;
                    }
                };
                if (c == 1){
                    Toast.makeText(Favoris.this, "Adresse : 1 Rue Jean Jaurès, Horaire : 18h", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runn, 400);
                } else if (c == 2) {
                    favs.remove(position);
                    Toast.makeText(Favoris.this, "Supprimé de favoris", Toast.LENGTH_SHORT).show();
                } else if (c == 3) {
                    Toast.makeText(Favoris.this, "Misclicked", Toast.LENGTH_SHORT).show();
                    c = 0;
                }
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });

        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Parametres.class);
                startActivity(otherActivity);
                finish();
            }
        });


        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Profil.class);
                startActivity(otherActivity);
                finish();
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), ListeActivite.class);
                Toast.makeText(Favoris.this, "Retour", Toast.LENGTH_SHORT).show();
                startActivity(otherActivity);
                finish();
            }
        });
    }
}
