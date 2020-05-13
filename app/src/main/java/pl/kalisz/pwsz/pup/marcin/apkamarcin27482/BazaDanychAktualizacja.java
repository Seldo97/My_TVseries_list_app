package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BazaDanychAktualizacja extends AppCompatActivity {

    public static final String EXTRA_ORGANIZACJA_ID = "organizacjaId";
    private SQLiteDatabase db;
    private Cursor cursor;
    private int organizacjaId;
    private EditText nazwa1, rodzaj1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza_danych_aktualizacja);

        organizacjaId = (Integer) getIntent().getExtras().get(EXTRA_ORGANIZACJA_ID);
        try {
            SQLiteOpenHelper organizacjaSQLiteOpenHelper = new ProjektSQLiteOpenHelper(this);
            db = organizacjaSQLiteOpenHelper.getWritableDatabase();
            cursor = db.query("ORGANIZACJA", new String[]{"_id", "NAZWA", "RODZAJ"}, "_id = ?",
                    new String[]{Integer.toString(organizacjaId)}, null, null, null);

            if(cursor.moveToFirst()) {

                String nazwa = cursor.getString(1);
                String rodzaj = cursor.getString(2);
                nazwa1 = (EditText) findViewById(R.id.editTextNazwa);
                nazwa1.setText(nazwa);
                rodzaj1 = (EditText) findViewById(R.id.editTextRodzaj);
                rodzaj1.setText(rodzaj);

            }
            cursor.close();
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

    }

    public void onClickZapisz(View view) {

        ContentValues obiektValues = new ContentValues();
        obiektValues.put("NAZWA", nazwa1.getText().toString());
        obiektValues.put("RODZAJ", rodzaj1.getText().toString());
        db.update("ORGANIZACJA", obiektValues, "_id = ?", new String[] {Integer.toString(organizacjaId)});

        db.close();
        finish();

    }

    public void onClickUsun(View view) {

        db.delete("ORGANIZACJA", "_id = ?", new String[] {Integer.toString(organizacjaId)});
        db.close();
        finish();

    }

}
