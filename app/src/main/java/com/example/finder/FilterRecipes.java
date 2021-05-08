package com.example.finder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterRecipes extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private SharedPreferences sp, sp2;
    private String ingredients;
    private String[] ingredientsList;
    private List<Recipe> elements;
    private ListView listView;
    private CustomAdaptorMain customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_recipes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        sp2 = getSharedPreferences("username", MODE_PRIVATE);
        Intent intent = getIntent();
        getAllRecipes();
        if(intent!=null) {
            ingredients = intent.getStringExtra("ingredients");
            if(ingredients != null)
                if(ingredients.length() > 2)
                    searchRecipe();
        }
    }

    private void searchRecipe()
    {
        int counter; // sa gasim cel putin 3 ingrediente din cele cautate in reteta
        ingredientsList = ingredients.split(",");
        List<Recipe> elements2 = new ArrayList<Recipe>();
        for (Recipe a : elements) { // pentru fiecare reteta
            counter = 0;
            for(int i=1; i<ingredientsList.length; i++) {
                // fiecare ingredient pe care il avem
                if (a.getIngredientsString().contains(ingredientsList[i])) {
                    counter++;
                }
            }
            if ( counter > 2)
            {
                //am gasit reteta care contine > 2 ingrediente pe care le cautam  & avem
                //salvam reteta si o afisam
                elements2.add(a);
            }
        }
        listView = (ListView) findViewById(R.id.listViewFilter);
        customAdapter = new CustomAdaptorMain(FilterRecipes.this, R.layout.item_recipe, elements2);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        if(elements2.size()<2)
            Toast.makeText(this, "No recipes found with these ingredients!", Toast.LENGTH_SHORT).show();
    }

    private void getAllRecipes() {
        elements = new ArrayList<Recipe>();
        SingletonRecipes s = SingletonRecipes.getInstance();
        elements = s.getElements();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(FilterRecipes.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.ingredients:
                Intent intentIngredients = new Intent(FilterRecipes.this, IngredientsActivity.class);
                startActivity(intentIngredients);
                return true;
            case R.id.logout:
                sp.edit().putBoolean("logged",false).apply();
                sp2.edit().putString("username", "").apply();
                Intent intentLogOut = new Intent(FilterRecipes.this, LoginActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}