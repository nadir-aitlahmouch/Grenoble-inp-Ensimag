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

import java.util.ArrayList;

public class ListeActivite extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ListView listView;
    private ImageButton retour;
    private ImageButton favorispage;
    private int c = 0;
    ArrayList<String> favoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_activite);

        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
        this.listView = (ListView) findViewById(R.id.liste);
        this.retour = (ImageButton) findViewById(R.id.retour);
        this.favoris = new ArrayList();
        favoris.add("Café");
        this.favorispage = (ImageButton) findViewById(R.id.favoris);

        final ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Bowling");
        arrayList.add("Spider Man: movie");
        arrayList.add("Musée ancien");
        arrayList.add("Restaurant El Italiano");
        arrayList.add("Musée des Plantes");
        arrayList.add("Symposium");
        arrayList.add("Planetarium");
        arrayList.add("Aquaparc Le Requin");
        arrayList.add("RetroGaming");
        arrayList.add("Café game");
        arrayList.add("Exposition Picasso");
        arrayList.add("Karting");
        arrayList.add("Paintball");
        arrayList.add("Laser game");
        arrayList.add("Atelier dessin");
        arrayList.add("Monuments");
        arrayList.add("Jardin");
        arrayList.add("Theme parc");
        arrayList.add("Musée d'histoire naturel");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,arrayList);

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
                    Toast.makeText(ListeActivite.this, "Adresse : 1 Rue Jean Jaurès, Horaire : 18h", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runn, 400);
                } else if (c == 2) {
                    favoris.add(arrayList.get(position));
                    Toast.makeText(ListeActivite.this, "Ajouté dans favoris", Toast.LENGTH_SHORT).show();
                } else if (c == 3) {
                    Toast.makeText(ListeActivite.this, "Misclicked", Toast.LENGTH_SHORT).show();
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

        favorispage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Favoris.class);
                otherActivity.putStringArrayListExtra("item", (ArrayList<String>) favoris);
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
                Intent otherActivity = new Intent(getApplicationContext(), Activites.class);
                Toast.makeText(ListeActivite.this, "Retour", Toast.LENGTH_SHORT).show();
                startActivity(otherActivity);
                finish();
            }
        });

    }
}
