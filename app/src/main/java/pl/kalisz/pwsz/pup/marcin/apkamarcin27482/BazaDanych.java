package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BazaDanych extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
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

    private ShareActionProvider shareActionProvider;
    private Snackbar snackbar;
    private CharSequence komunikat;
    private int czas = Snackbar.LENGTH_LONG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza_danych);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.design_navigation_view_2);
        navigationView.setNavigationItemSelectedListener(this);

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

    public void backToHomePage(View view){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent("Polecam Ci aplikację My TV-series List, jest na prawdę świetna! [link do sklep play]");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.share:
                Toast.makeText(this, "Udostępnij", Toast.LENGTH_LONG).show();
                return true;

            case R.id.about:
                komunikat = "Autorem programu jest Marcin";
                snackbar = Snackbar.make(findViewById(R.id.snackbar), komunikat, czas);
                snackbar.setAction("Wersja API", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(BazaDanych.this, getAndroidVersion(), Toast.LENGTH_LONG).show();
                    }

                });
                snackbar.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVer = Build.VERSION.SDK_INT;

        return "Android SDK: " + sdkVer + " (" + release + ")";
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_l6:
                Intent intentPlanowane = new Intent(this, Planowane.class);
                startActivity(intentPlanowane);
                finish();
                break;
            case R.id.nav_l7:
                Intent intentOgladane = new Intent(this, Ogladane.class);
                startActivity(intentOgladane);
                finish();
                break;
            case R.id.nav_l8:
                Intent intentPorzucone = new Intent(this, Porzucone.class);
                startActivity(intentPorzucone);
                finish();
                break;
            case R.id.nav_l9:
                Intent intentObejrzane = new Intent(this, Obejrzane.class);
                startActivity(intentObejrzane);
                finish();
                break;

            ///////////////////////
            case R.id.temat:
                Intent intentUlub = new Intent(this, Ulubione.class);
                startActivity(intentUlub);
                finish();
                break;

            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }




}