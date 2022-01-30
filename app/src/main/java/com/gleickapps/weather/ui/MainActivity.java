package com.gleickapps.weather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gleickapps.weather.R;
import com.gleickapps.weather.model.City;
import com.gleickapps.weather.service.RecyclerAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements RecyclerAdapter.ItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ArrayList<City> citiesList;
    private RecyclerView recyclerView;
    RecyclerAdapter adapter;

    // Unidade de medida e volume do áudio dos efeitos
    static boolean isMetric;
    static float volume;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citiesList = new ArrayList<>();

        initViews();
        setPreferences();

        setActionBar();
        setCityInfo();
        setAdapter();
    }

    private void initViews() {
        toolbar = findViewById(R.id.main_toobar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setPreferences() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        isMetric = sharedPref.getBoolean("isMetric", true);
        volume = sharedPref.getFloat("volume", 0.05f);
    }

    private void setActionBar() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle(R.string.select_city);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_settings) {
            SettingsPopup settingsPopup = new SettingsPopup(this, R.layout.popup_settings);
            settingsPopup.showPopupWindow(findViewById(android.R.id.content).getRootView());
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void setCityInfo() {
        Resources res = this.getResources();
        String[] names = res.getStringArray(R.array.city_names);

        for (String name : names) {
            citiesList.add(new City(name));
        }
    }

    private void setAdapter() {
        adapter = new RecyclerAdapter(citiesList);
        adapter.setClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("key", position);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isMetric", isMetric);
        editor.putFloat("volume", volume);
        editor.apply();
        super.onStop();
    }
}