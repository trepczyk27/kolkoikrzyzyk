package com.example.kolkoikrzyzyk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences baza;
    SharedPreferences.Editor editor;
    private Button[][] przycisk = new Button[3][3];
    private boolean gracz1ruch = true;
    private int liczbaRund;
    private int gracz1punkt;
    private int gracz2punkt;
    private TextView Gracz1;
    private TextView Gracz2;
    String wygrany ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baza= getSharedPreferences("com.example.kolkoikrzyzyk", Context.MODE_PRIVATE);
        editor=baza.edit();
        Toast.makeText(getApplicationContext(),baza.getString("win",wygrany), Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gracz1 = findViewById(R.id.wynik_Gracz1);
        Gracz2 = findViewById(R.id.wynik_Gracz2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                przycisk[i][j] = findViewById(resID);
                przycisk[i][j].setOnClickListener(this);
            }
        }
        Button przyciskReset = findViewById(R.id.button_reset);
        przyciskReset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                reset_Gry();
            }
        });
    }

    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (gracz1ruch) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        liczbaRund++;
        if (sprawdz_Wygrana()) {
            if (gracz1ruch) {
                garcz1wygrana();
                wygrany="wczesniej wygral gracz 1";
            } else {
                gracz2wygrana();
                wygrany="wczesnije wygral gracz 2";

                editor.putString("win",wygrany);
                editor.commit();
            }
            editor.putString("win",wygrany);
            editor.commit();
        } else if (liczbaRund == 9) {
            Remis();
            wygrany="remis";
            editor.putString("win",wygrany);
            editor.commit();
        } else {
            gracz1ruch = !gracz1ruch;
        }
    }
    private boolean sprawdz_Wygrana() {
        String[][] pole = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pole[i][j] = przycisk[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (pole[i][0].equals(pole[i][1])
                    && pole[i][0].equals(pole[i][2])
                    && !pole[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (pole[0][i].equals(pole[1][i])
                    && pole[0][i].equals(pole[2][i])
                    && !pole[0][i].equals("")) {
                return true;
            }
        }
        if (pole[0][0].equals(pole[1][1])
                && pole[0][0].equals(pole[2][2])
                && !pole[0][0].equals("")) {
            return true;
        }
        if (pole[0][2].equals(pole[1][1])
                && pole[0][2].equals(pole[2][0])
                && !pole[0][2].equals("")) {
            return true;
        }
        return false;
    }
    private void garcz1wygrana() {
        gracz1punkt++;
        Toast.makeText(this, "Gracz 1 wygrywa!", Toast.LENGTH_SHORT).show();
        wyniki_Gry();
        reset_tablicy();
    }
    private void gracz2wygrana() {
        gracz2punkt++;
        Toast.makeText(this, "Gracz 2  wygrywa!", Toast.LENGTH_SHORT).show();
        wyniki_Gry();
        reset_tablicy();
    }
    private void Remis() {
        Toast.makeText(this, "Remis!", Toast.LENGTH_SHORT).show();
        reset_tablicy();
    }
    private void wyniki_Gry() {
        Gracz1.setText("Gracz 1: " + gracz1punkt);
        Gracz2.setText("Gracz 2: " + gracz2punkt);
    }
    private void reset_tablicy() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                przycisk[i][j].setText("");
            }
        }
        liczbaRund = 0;
        gracz1ruch = true;
    }
    private void reset_Gry() {
        gracz1punkt = 0;
        gracz2punkt= 0;
        wyniki_Gry();
        reset_tablicy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("liczbaRund", liczbaRund);
        outState.putInt("gracz1punkt", gracz1punkt);
        outState.putInt("gracz2punkt", gracz2punkt);
        outState.putBoolean("gracz1ruch", gracz1ruch);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        liczbaRund = savedInstanceState.getInt("liczbaRund");
        gracz1punkt = savedInstanceState.getInt("gracz1punkt");
        gracz2punkt = savedInstanceState.getInt("gracz2punkt");
        gracz1ruch = savedInstanceState.getBoolean("gracz1ruch");
    }
}