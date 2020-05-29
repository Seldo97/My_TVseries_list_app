package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

//########################
//######## Author ########
//##### Marcin Olek ######
//########################

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BazaDanychAktualizacja extends AppCompatActivity {

    public static final String EXTRA_SERIAL_ID = "idSerial";
    private SQLiteDatabase db;
    private Cursor cursor;
    private int idSerial;

    private EditText editTextNazwa2Update, editTextSerwis2Update, editTextSezon2Update, editText2OdcinkiUpdate, editText2OCurrentEpUpdate;
    private Spinner spinner2KategoriaUpdate, spinner2OcenaUpdate;
    private CheckBox checkBox2FavouriteUpdate;

    private String nazwa, serwis, ocena, sezon, aktualnyOdc, odcinki, ulubiony, kategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza_danych_aktualizacja);

        editTextNazwa2Update = (EditText) findViewById(R.id.EditTextNazwa2Update);
        editTextSerwis2Update = (EditText) findViewById(R.id.EditTextSerwis2Update);
        editTextSezon2Update = (EditText) findViewById(R.id.editTextSezon2Update);
        editText2OdcinkiUpdate = (EditText) findViewById(R.id.editText2OdcinkiUpdate);
        editText2OCurrentEpUpdate = (EditText) findViewById(R.id.editText2OCurrentEpUpdate);
        spinner2KategoriaUpdate = (Spinner) findViewById(R.id.spinner2KategoriaUpdate);
        spinner2OcenaUpdate = (Spinner) findViewById(R.id.spinner2OcenaUpdate);
        checkBox2FavouriteUpdate = (CheckBox) findViewById(R.id.checkBox2FavouriteUpdate);

        idSerial = (Integer) getIntent().getExtras().get(EXTRA_SERIAL_ID);

        try {
            SQLiteOpenHelper organizacjaSQLiteOpenHelper = new ProjektSQLiteOpenHelper(this);
            db = organizacjaSQLiteOpenHelper.getWritableDatabase();
            cursor = db.query("SERIAL", new String[]{"_id", "NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY", "KATEGORIA"}, "_id = ?",
                    new String[]{Integer.toString(idSerial)}, null, null, null);

            if (cursor.moveToFirst()) {

                nazwa = cursor.getString(1);
                editTextNazwa2Update.setText(nazwa);

                serwis = cursor.getString(2);
                editTextSerwis2Update.setText(serwis);

                ocena = cursor.getString(3);
                ArrayAdapter myAdap = (ArrayAdapter) spinner2OcenaUpdate.getAdapter(); //cast to an ArrayAdapter
                int spinnerPositionRat = myAdap.getPosition(ocena);
                spinner2OcenaUpdate.setSelection(spinnerPositionRat);

                sezon = cursor.getString(4);
                editTextSezon2Update.setText(sezon);

                aktualnyOdc = cursor.getString(5);
                editText2OCurrentEpUpdate.setText(aktualnyOdc);

                odcinki = cursor.getString(6);
                editText2OdcinkiUpdate.setText(odcinki);

                ulubiony = cursor.getString(7);
                if (ulubiony.equals("1"))
                    checkBox2FavouriteUpdate.setChecked(true);

                kategoria = cursor.getString(8);
                ArrayAdapter myAdap2 = (ArrayAdapter) spinner2KategoriaUpdate.getAdapter(); //cast to an ArrayAdapter
                int spinnerPositionCat = myAdap2.getPosition(kategoria);
                spinner2KategoriaUpdate.setSelection(spinnerPositionCat);


            }
            cursor.close();
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

    }

    public void onClickUpdate(View view) {

        nazwa = editTextNazwa2Update.getText().toString().trim();
        serwis = editTextSerwis2Update.getText().toString().trim();
        sezon = editTextSezon2Update.getText().toString().trim();
        aktualnyOdc = editText2OCurrentEpUpdate.getText().toString().trim();
        odcinki = editText2OdcinkiUpdate.getText().toString().trim();
        kategoria = spinner2KategoriaUpdate.getSelectedItem().toString().trim();
        ocena = spinner2OcenaUpdate.getSelectedItem().toString().trim();
        ulubiony = "0";

        if (nazwa.matches("") ||
                serwis.matches("") ||
                sezon.matches("") ||
                odcinki.matches("") ||
                kategoria.equals("--")
        ) {

            Toast toast = Toast.makeText(this, "Wypełnij wymagane pola!", Toast.LENGTH_LONG);
            toast.show();

        } else {

            if (checkBox2FavouriteUpdate.isChecked())
                ulubiony = "1";

            ContentValues obiektValues = new ContentValues();

            obiektValues.put("NAZWA", nazwa);
            obiektValues.put("SEZON", Integer.parseInt(sezon));
            obiektValues.put("SERWIS", serwis);
            obiektValues.put("KATEGORIA", kategoria);
            obiektValues.put("OCENA", ocena);
            obiektValues.put("ODCINKI", Integer.parseInt(odcinki));
            obiektValues.put("AKTUALNY_ODC", Integer.parseInt(aktualnyOdc));
            obiektValues.put("ULUBIONY", Integer.parseInt(ulubiony));

            db.update("SERIAL", obiektValues, "_id = ?", new String[]{Integer.toString(idSerial)});

            db.close();
            finish();
        }
    }

    public void onClickDelete(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Uwaga!")
                .setMessage("Czy na pewno chcesz usunąć pozycję?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.delete("SERIAL", "_id = ?", new String[]{Integer.toString(idSerial)});
                        db.close();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

//        db.delete("SERIAL", "_id = ?", new String[]{Integer.toString(idSerial)});
//        db.close();
//        finish();

    }

    public void onClickCancelForm(View view) {

        finish();

    }

    public void dodajTest(View view) {

        EditText odcinki = (EditText) findViewById(R.id.editText2OdcinkiUpdate);
        if (odcinki.getText().toString().matches("")) {
            odcinki.setText("1");
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(odcinki.getText().toString()) + 1;
            odcinki.setText(liczbaOdcinkow.toString());
        }
    }

    public void odejmijTest(View view) {

        EditText odcinki = (EditText) findViewById(R.id.editText2OdcinkiUpdate);
        if (odcinki.getText().toString().matches("") || odcinki.getText().toString().equals("0")) {
            odcinki.setText("0");
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(odcinki.getText().toString()) - 1;
            odcinki.setText(liczbaOdcinkow.toString());
        }

    }

    public void addCurrentEp(View view) {

        EditText odcinki = (EditText) findViewById(R.id.editText2OdcinkiUpdate);
        EditText aktualnyOdc = (EditText) findViewById(R.id.editText2OCurrentEpUpdate);
        if (aktualnyOdc.getText().toString().matches("")) {
            aktualnyOdc.setText("1");
        } else if (Integer.parseInt(aktualnyOdc.getText().toString()) >= Integer.parseInt(odcinki.getText().toString())) {
            aktualnyOdc.setText(odcinki.getText().toString());
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(aktualnyOdc.getText().toString()) + 1;
            aktualnyOdc.setText(liczbaOdcinkow.toString());
        }

    }

    public void reduceCurrentEp(View view) {

        EditText aktualnyOdc = (EditText) findViewById(R.id.editText2OCurrentEpUpdate);
        if (aktualnyOdc.getText().toString().matches("") || aktualnyOdc.getText().toString().equals("0")) {
            aktualnyOdc.setText("0");
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(aktualnyOdc.getText().toString()) - 1;
            aktualnyOdc.setText(liczbaOdcinkow.toString());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
