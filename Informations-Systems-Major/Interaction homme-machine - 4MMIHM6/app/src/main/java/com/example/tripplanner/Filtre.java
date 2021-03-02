package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class Filtre extends Activity {

    private Button save;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private static final String[] ACTIVITES = new String[]{"Musée", "Bowling", "Cinéma", "Restaurants", "Sport", "Visite", "Jardins et parcs", "Balades"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        this.save = (Button) findViewById(R.id.buttonsave);
        this.multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ACTIVITES);

        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Filtre.this, "Données enregistrées", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showInput(View v) {
        String input = multiAutoCompleteTextView.getText().toString().trim();
        String[] singleInputs = input.split("\\s*,\\s*");

        String toastText = "";

        for (int i = 0; i < singleInputs.length; i++) {
            toastText += "Item " + i + ": " + singleInputs[i] + "\n";
        }

        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}
