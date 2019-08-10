package com.example.safe.drivelert;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.multidex.MultiDex;

import com.example.safe.drivelert.Authentication.LoginActivity;
import com.example.safe.drivelert.Utility.Const;
import com.example.safe.drivelert.Utility.TinyDB;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FrameLayout frame;
    Button agree,disagree;
    TextView mHeader;

    TinyDB tinyDB;
    public int PERMISSION_CODE = 23;
    String[] PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mHeader = findViewById(R.id.tv_header);
        frame = (FrameLayout)findViewById(R.id.frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new monitor_menu()).commit();
        Toast.makeText(getApplicationContext(),"Swipe left for menu",Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername =  headerView.findViewById(R.id.tv_header);


        navigationView.setNavigationItemSelectedListener(this);
        boolean isFirstTime = MyPreferences.isFirst(MainActivity.this);
        tinyDB = new TinyDB(getApplicationContext());
        if(!tinyDB.getBoolean(Const.IS_SIGNED_IN))
        {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }
        else
        {
            navUsername.setText(tinyDB.getString(Const.USER_NAME));
           // navUsername.setText("Kamal");

        }

        if(isFirstTime == true)
        {
            Intent help = new Intent(MainActivity.this, help.class);
            startActivity(help);
        }

        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this , "Permission Granted" , Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to close this application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new monitor_menu()).commit();
        }
        else if(id == R.id.help_page)
        {
            Intent hp = new Intent(MainActivity.this,help.class);
            startActivity(hp);
        }
      else if (id == R.id.nav_send) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new contactus()).commit();
        }
        else if (id == R.id.nav_settings) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SettingsActivity.SettingsFragment()).commit();
        }

        if (id == R.id.nav_logout) {

            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        tinyDB.putBoolean(Const.IS_SIGNED_IN , false);
        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
