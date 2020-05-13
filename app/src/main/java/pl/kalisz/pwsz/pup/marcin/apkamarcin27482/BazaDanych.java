package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BazaDanych extends AppCompatActivity {
    protected ProjektSQLiteOpenHelper bazaDanych;
    protected SQLiteDatabase db;
    protected Cursor cursor;
    protected ListView listViewSeriale;
    protected TextView textView;
    protected SimpleCursorAdapter listAdapter;
    protected Spinner changeCategory;
    protected SharedPreferences sharedPreferences;
    protected String sortBy;
    protected String sortDirection;
    protected String currentCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza_danych);

        // Ustawienia
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString("sortby", "_id");
        sortDirection = sharedPreferences.getString("sort", "ASC");

        listViewSeriale = (ListView) findViewById(R.id.listViewSeriale);

        try {
            bazaDanych = new ProjektSQLiteOpenHelper(this);
            db = bazaDanych.getWritableDatabase();
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Baza danych niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listViewOrganizacje, View view, int position, long id) {
                Intent intent = new Intent(BazaDanych.this, BazaDanychAktualizacja.class);
                intent.putExtra(BazaDanychAktualizacja.EXTRA_ORGANIZACJA_ID, (int) id);
                startActivity(intent);
            }
        };
        listViewSeriale.setOnItemClickListener(itemClickListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BazaDanych.this, BazaDanychDopisz.class);
                intent.putExtra(BazaDanychDopisz.EXTRA_CURRENT_CATEGORY, currentCategory);
                startActivity(intent);
            }
        });

    }

//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onResume() {
//        super.onResume();
//        cursor = db.query("SERIAL", new String[]{"_id", "NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY"}, "KATEGORIA = ?", new String[]{"Planowane"}, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            listAdapter = new SimpleCursorAdapter(this, R.layout.my_custom_list,
//                    cursor,
//                    new String[]{"NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY"},
//                    new int[]{R.id.textNazwa, R.id.textSerwis, R.id.podanaOcena, R.id.Sezon, R.id.aktualnyOdcinek, R.id.iloscOdcinkow, R.id.Ulubione},
//                    0);
//
//            listViewSeriale.setAdapter(listAdapter);
//        }
//
//    }

    public void odejmijOdcinekZListy(View view) {

        int position = listViewSeriale.getPositionForView((View) view.getParent());

        Cursor c = (Cursor) listAdapter.getItem(position);
        Integer id = cursor.getInt(0); // column number in database
        Integer aktualnyOdc = cursor.getInt(5);

        if (aktualnyOdc <= 0) {
            Toast toast = Toast.makeText(this, "Nie można odjąć wartości", Toast.LENGTH_LONG);
            toast.show();
        } else {

            ContentValues obiektValues = new ContentValues();
            obiektValues.put("AKTUALNY_ODC", (aktualnyOdc - 1));
            db.update("SERIAL", obiektValues, "_id = ?", new String[]{Integer.toString(id)});

            // refresh list
            cursor.requery();
            listAdapter.notifyDataSetChanged();
        }
    }

    public void dodajOdcinekZListy(View view) {

        int position = listViewSeriale.getPositionForView((View) view.getParent());

        Cursor c = (Cursor) listAdapter.getItem(position);
        Integer id = cursor.getInt(0); // column number in database
        Integer aktualnyOdc = cursor.getInt(5);
        Integer odcinki = cursor.getInt(6);

        if (aktualnyOdc >= odcinki) {
            Toast toast = Toast.makeText(this, "Nie można dodać wartości", Toast.LENGTH_LONG);
            toast.show();
        } else {

            ContentValues obiektValues = new ContentValues();
            obiektValues.put("AKTUALNY_ODC", (aktualnyOdc + 1));
            db.update("SERIAL", obiektValues, "_id = ?", new String[]{Integer.toString(id)});

            // refresh list
            cursor.requery();
            listAdapter.notifyDataSetChanged();
        }
    }

    public void addToFavourite(View view) {

        int position = listViewSeriale.getPositionForView((View) view.getParent());
        int status;

        Cursor c = (Cursor) listAdapter.getItem(position);
        Integer id = cursor.getInt(0); // column number in database
        Integer currentFavStatus = cursor.getInt(7);

        if (currentFavStatus == 0) {
            status = 1;
            Toast toast = Toast.makeText(getApplicationContext(), "Dodano pozycję do ulubionych", Toast.LENGTH_LONG);
            toast.show();
        } else {
            status = 0;
            Toast toast = Toast.makeText(getApplicationContext(), "Usunięto pozycję z ulubionych", Toast.LENGTH_LONG);
            toast.show();
        }

        ContentValues obiektValues = new ContentValues();
        obiektValues.put("ULUBIONY", status);
        db.update("SERIAL", obiektValues, "_id = ?", new String[]{Integer.toString(id)});

        // refresh list
        cursor.requery();
        listAdapter.notifyDataSetChanged();

    }


}