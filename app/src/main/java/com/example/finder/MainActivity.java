package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        setSupportActionBar(myToolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                return true;
            case R.id.ingredients:
                Intent intentIngredients = new Intent(MainActivity.this, IngredientsActivity.class);
                startActivity(intentIngredients);
                return true;
            case R.id.logout:
                sp.edit().putBoolean("logged",false).apply();
                Intent intentLogOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}