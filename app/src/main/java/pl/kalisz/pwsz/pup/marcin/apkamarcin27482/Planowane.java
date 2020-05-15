package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

//########################
//######## Author ########
//##### Marcin Olek ######
//########################

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class Planowane extends BazaDanych {

    private String updateCategory = "";

    @Override
    public void onResume() {
        super.onResume();
        listViewSeriale.setAdapter(null);

        // Ustawienia
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString("sortby", "_id");
        sortDirection = sharedPreferences.getString("sort", "ASC");

        currentCategory = "Planowane";

        cursor = db.query("SERIAL", new String[]{"_id", "NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY", "KATEGORIA"},
                "KATEGORIA = ?", new String[]{"Planowane"},
                null,
                null,
                sortBy + " COLLATE NOCASE " + sortDirection);

        if (cursor.moveToFirst()) {
            listAdapter = new SimpleCursorAdapter(this, R.layout.my_custom_list,
                    cursor,
                    new String[]{"NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY"},
                    new int[]{R.id.textNazwa, R.id.textSerwis, R.id.podanaOcena, R.id.Sezon, R.id.aktualnyOdcinek, R.id.iloscOdcinkow, R.id.Ulubione},
                    0) {


                // XDDDDD
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    // Zamiana wartości 0/1 w kolumnie Ulubione na puste serce/pełne serce
                    TextView tx = (TextView) v.findViewById(R.id.Ulubione);
                    if (Integer.parseInt(tx.getText().toString()) == 1)
                        tx.setText("❤️");
                    else
                        tx.setText("♡");

                    // deklaracja spinnera elementu
                    changeCategory = (Spinner) v.findViewById(R.id.spinnerListKategoria);
                    changeCategory.setSelection(0, false);
                    changeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            // id wybranego elementu spinnera
                            String selectedItem = parent.getSelectedItem().toString();
                            // id elementu na którym spinner się znajduje
                            int selectedViewId = listViewSeriale.getPositionForView((View) view.getParent());

                            // wybranie kolumny w bazie której spinner dotyczy czyli Kategoria
                            Cursor c = (Cursor) listAdapter.getItem(selectedViewId);
                            Integer idRow = cursor.getInt(0); // column number in database
                            String currentCategory = cursor.getString(8); // column number in database

                            if (!selectedItem.equals("--") && !selectedItem.equals(currentCategory)) {

                                ContentValues obiektValues = new ContentValues();
                                obiektValues.put("KATEGORIA", selectedItem);
                                db.update("SERIAL", obiektValues, "_id = ?", new String[]{Long.toString(idRow)});

                                // refresh list
                                cursor.requery();
                                listAdapter.notifyDataSetChanged();
                                listViewSeriale.invalidateViews();

                                System.out.println("Przeniesiono pozycję do sekcji: " + selectedItem + " " + currentCategory);
                                Toast toast = Toast.makeText(getApplicationContext(), "Przeniesiono pozycję do sekcji: " + selectedItem, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    return v;
                }
            };

            listViewSeriale.setAdapter(listAdapter);

        }

    }

}
