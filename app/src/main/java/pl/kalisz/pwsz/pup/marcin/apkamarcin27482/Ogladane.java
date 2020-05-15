package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

//########################
//######## Author ########
//##### Marcin Olek ######
//########################

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

public class Ogladane extends BazaDanych {

    @Override
    public void onResume() {
        super.onResume();
        listViewSeriale.setAdapter(null);

        // Ustawienia
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString("sortby", "_id");
        sortDirection = sharedPreferences.getString("sort", "ASC");

        currentCategory = "Ogladane";

        cursor = db.query("SERIAL", new String[]{"_id", "NAZWA", "SERWIS", "OCENA", "SEZON", "AKTUALNY_ODC", "ODCINKI", "ULUBIONY", "KATEGORIA"},
                "KATEGORIA = ?", new String[]{"Ogladane"},
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
                    //final int pos = position;

                    TextView tx = (TextView) v.findViewById(R.id.Ulubione);
                    if (Integer.parseInt(tx.getText().toString()) == 1)
                        tx.setText("❤️");
                    else
                        tx.setText("♡");

                    changeCategory = (Spinner) v.findViewById(R.id.spinnerListKategoria);
                    changeCategory.setSelection(0, false);
                    changeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getSelectedItem().toString();
                            int selectedViewId = listViewSeriale.getPositionForView((View) view.getParent());
                            ;

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
