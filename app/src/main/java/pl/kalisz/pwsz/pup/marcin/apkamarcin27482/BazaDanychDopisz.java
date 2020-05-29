package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class BazaDanychDopisz extends AppCompatActivity {

    public static final String EXTRA_CURRENT_CATEGORY = "currentCategory";
    public static final String EXTRA_SERIAL_ID = "idSerial";
    private String currentCategory;
    private SQLiteDatabase db;
    private EditText editTextNazwa2, editTextSerwis2, editTextSezon2, editText2Odcinki;
    private Spinner spinner2Kategoria, spinner2Ocena;
    private CheckBox checkBox2Favourite;
    private Cursor cursor;
    private int idSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza_danych_dopisz);

        editTextNazwa2 = (EditText) findViewById(R.id.EditTextNazwa2);
        editTextSerwis2 = (EditText) findViewById(R.id.EditTextSerwis2);
        editTextSezon2 = (EditText) findViewById(R.id.editTextSezon2);
        editText2Odcinki = (EditText) findViewById(R.id.editText2Odcinki);
        spinner2Kategoria = (Spinner) findViewById(R.id.spinner2Kategoria);
        spinner2Ocena = (Spinner) findViewById(R.id.spinner2Ocena);
        checkBox2Favourite = (CheckBox) findViewById(R.id.checkBox2Favourite);

        currentCategory = (String) getIntent().getExtras().get(EXTRA_CURRENT_CATEGORY); // wartość jakiej szukamy
        ArrayAdapter myAdap = (ArrayAdapter) spinner2Kategoria.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(currentCategory); // pozycja w której nasza wartośc się znajduje
        spinner2Kategoria.setSelection(spinnerPosition); // ustawiamy selecta na tej pozycji

        try {

            SQLiteOpenHelper organizacjaSQLiteOpenHelper = new ProjektSQLiteOpenHelper(this);
            db = organizacjaSQLiteOpenHelper.getWritableDatabase();

        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    public void onClickDodaj(View view) {

        String nazwa = editTextNazwa2.getText().toString().trim();
        String serwis = editTextSerwis2.getText().toString().trim();
        String sezon = editTextSezon2.getText().toString().trim();
        String odcinki = editText2Odcinki.getText().toString().trim();
        String kategoria = spinner2Kategoria.getSelectedItem().toString().trim();
        String ocena = spinner2Ocena.getSelectedItem().toString().trim();
        String ulubiony = "0";

        if (nazwa.matches("") ||
                serwis.matches("") ||
                sezon.matches("") ||
                odcinki.matches("") ||
                kategoria.equals("--")
        ) {

            Toast toast = Toast.makeText(this, "Wypełnij wymagane pola!", Toast.LENGTH_LONG);
            toast.show();

        } else {

            if (checkBox2Favourite.isChecked())
                ulubiony = "1";

            ContentValues obiektValues = new ContentValues();

            obiektValues.put("NAZWA", nazwa);
            obiektValues.put("SEZON", Integer.parseInt(sezon));
            obiektValues.put("SERWIS", serwis);
            obiektValues.put("KATEGORIA", kategoria);
            obiektValues.put("OCENA", ocena);
            obiektValues.put("ODCINKI", Integer.parseInt(odcinki));
            obiektValues.put("AKTUALNY_ODC", 0);
            obiektValues.put("ULUBIONY", Integer.parseInt(ulubiony));

            db.insert("SERIAL", null, obiektValues);
            finish();

        }

    }

    public void onClickCancelForm(View view) {

        finish();

    }

    public void dodajTest(View view) {

        EditText odcinki = (EditText) findViewById(R.id.editText2Odcinki);
        if (odcinki.getText().toString().matches("")) {
            odcinki.setText("1");
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(odcinki.getText().toString()) + 1;
            odcinki.setText(liczbaOdcinkow.toString());
        }
    }

    public void odejmijTest(View view) {

        EditText odcinki = (EditText) findViewById(R.id.editText2Odcinki);
        if (odcinki.getText().toString().matches("") || odcinki.getText().toString().equals("0")) {
            odcinki.setText("0");
        } else {
            Integer liczbaOdcinkow = Integer.parseInt(odcinki.getText().toString()) - 1;
            odcinki.setText(liczbaOdcinkow.toString());
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

