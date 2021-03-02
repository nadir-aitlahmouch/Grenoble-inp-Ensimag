package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Activites extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ImageButton map;
    private ImageButton liste;
    private ImageButton filtre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activites);

        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
        this.map = (ImageButton) findViewById(R.id.map);
        this.liste = (ImageButton) findViewById(R.id.liste);
        this.filtre = (ImageButton) findViewById(R.id.filtre);

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Filtre.class);
                startActivity(otherActivity);
                //finish();
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

        liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), ListeActivite.class);
                Toast.makeText(Activites.this, "Liste des activités", Toast.LENGTH_SHORT).show();
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

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Map.class);
                startActivity(otherActivity);
                Toast.makeText(Activites.this, "Map", Toast.LENGTH_SHORT).show();
          finish();
            }
        });

    }
}
