package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import 	android.os.Build.VERSION;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ShareActionProvider shareActionProvider;
    private Snackbar snackbar;
    private CharSequence komunikat;
    private int czas = Snackbar.LENGTH_LONG;

    private TextView wersja;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.design_navigation_view_1);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent("Polecam Ci aplikację My TV-series List, jest na prawdę świetna! [link do sklep play]");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

//            case R.id.send:
//               // Toast.makeText(this,"Wyslij",Toast.LENGTH_LONG).show();
//                Intent intent2 = new Intent(this, WyslijWiadomosc.class);
//                startActivity(intent2);
//                return true;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.share:
                Toast.makeText(this, "Udostępnij", Toast.LENGTH_LONG).show();
                return true;

            case R.id.about:
//                Toast.makeText(this, "Autorem programu jest Marcin",
//                        Toast.LENGTH_LONG).show();
                komunikat = "Autorem programu jest Marcin";
                snackbar = Snackbar.make(findViewById(R.id.snackbar), komunikat, czas);
                snackbar.setAction("Wersja API", new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        Toast.makeText(MainActivity.this, getAndroidVersion(), Toast.LENGTH_LONG).show();
                    }

                });
                snackbar.show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setShareActionIntent(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    public String getAndroidVersion(){
        String release = VERSION.RELEASE;
        int sdkVer = VERSION.SDK_INT;

        return "Android SDK: " + sdkVer + " (" + release + ")";
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        switch (id){
            case R.id.nav_l6:
                Intent intentL6 = new Intent(this, Planowane.class);
                startActivity(intentL6);
                break;
            case R.id.nav_l7:
                Intent intentL10 = new Intent(this, Ogladane.class);
                startActivity(intentL10);
                break;
            case R.id.nav_l8:
                Intent intentL8 = new Intent(this, Porzucone.class);
                startActivity(intentL8);
                break;
            case R.id.nav_l9:
                Intent intentL9 = new Intent(this, Obejrzane.class);
                startActivity(intentL9);
                break;

            ///////////////////////
            case R.id.temat:
                Intent intentUlub = new Intent(this, Ulubione.class);
                startActivity(intentUlub);
                break;

            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);

    }
}
