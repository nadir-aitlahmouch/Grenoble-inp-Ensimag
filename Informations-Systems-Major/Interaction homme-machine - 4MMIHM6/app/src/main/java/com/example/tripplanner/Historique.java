package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Historique extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ImageButton retour;
    private Button filtre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
        this.retour = (ImageButton) findViewById(R.id.retour);
        this.filtre = (Button) findViewById(R.id.filtre);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Filtre.class);
                startActivity(otherActivity);
                //finish();
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
                Intent otherActivity = new Intent(getApplicationContext(), GestionnaireBudget.class);
                Toast.makeText(Historique.this, "Retour", Toast.LENGTH_SHORT).show();
                startActivity(otherActivity);
                finish();
            }
        });
    }
}
