package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class Cartes extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profil;
    private ImageButton parametres;
    private ImageButton retour;
    private Button camera;
    private RadioButton verouiller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartes);

        this.home = (ImageButton) findViewById(R.id.home);
        this.profil = (ImageButton) findViewById(R.id.profil);
        this.parametres = (ImageButton) findViewById(R.id.parametres);
        this.retour = (ImageButton) findViewById(R.id.retour);
        this.camera = (Button) findViewById(R.id.camera);
        this.verouiller = (RadioButton) findViewById(R.id.radioButton);

        Drawable background = retour.getBackground();
        background.setAlpha(80);

        verouiller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    Toast.makeText(Cartes.this, "Votre carte est maintenant dévérouillée", Toast.LENGTH_SHORT).show();
                    verouiller.setChecked(true);
                }
                else{
                    Toast.makeText(Cartes.this, "Votre carte est maintenant vérouillée", Toast.LENGTH_SHORT).show();
                    verouiller.setChecked(false);
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    Toast.makeText(Cartes.this, "Scannez votre carte", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
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
                Intent otherActivity = new Intent(getApplicationContext(), GestionnaireBudget.class);
                Toast.makeText(Cartes.this, "Retour", Toast.LENGTH_SHORT).show();
                startActivity(otherActivity);
                finish();
            }
        });
    }
}
