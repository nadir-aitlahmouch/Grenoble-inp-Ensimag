package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GestionnaireBudget extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ImageButton maj;
    private ImageButton historique;
    private ImageButton cartes;
    private ProgressBar progress;
    private EditText editText;
    private ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionnaire_budget);


        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
       this.historique = (ImageButton) findViewById(R.id.historique);
        this.cartes = (ImageButton) findViewById(R.id.carte);
        this.maj = (ImageButton) findViewById(R.id.maj);
        this.progress = (ProgressBar) findViewById(R.id.progressBar2);
        this.editText = (EditText) findViewById(R.id.solde);
        this.refresh = (ImageButton) findViewById(R.id.refresh);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GestionnaireBudget.this, "Refresh", Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(editText.getText().toString()) == 0){
                    editText.setText("0");
                }
                int number = Integer.parseInt(editText.getText().toString());
                progress.setProgress(number/10);
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

        maj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Miseajour.class);
                startActivity(otherActivity);
                finish();
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Historique.class);
                startActivity(otherActivity);
                finish();
            }
        });

        cartes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), Cartes.class);
                startActivity(otherActivity);
                finish();
            }
        });
    }
}
