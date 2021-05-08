package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp, sp2;
    private DatabaseReference databaseReference;
    private List<Recipe> elements;
    private int numberOfItems = 0;
    private ListView listView;
    private CustomAdaptorMain customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elements = new ArrayList<Recipe>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbarMain);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        sp2 = getSharedPreferences("username", MODE_PRIVATE);
        setSupportActionBar(myToolbar);
        createNewDBListener();
    }

    private void createNewDBListener() {
        databaseReference.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elements.clear();
                SingletonRecipes s = SingletonRecipes.getInstance();
                for (DataSnapshot entrySnaphot : dataSnapshot.getChildren()) {
                    if(entrySnaphot.child("user").getValue() != null) {
                        if (entrySnaphot.child("user").getValue().toString().equals(sp2.getString("username", ""))) {
                            Recipe item = new Recipe();
                            item.setName(entrySnaphot.getKey());
                            for (DataSnapshot ingredientsSnapshot : entrySnaphot.child("Ingredients").getChildren()) {
                                String name = ingredientsSnapshot.getKey();
                                String quantity = ingredientsSnapshot.getValue() + "";
                                item.addIngredient(name, quantity);
                            }
                            item.addInstruction(entrySnaphot.child("Instructions").getValue().toString());
                            s.addElement(item);
                            elements.add(item);
                        }
                    }
                }
                numberOfItems = elements.size();
                listView = (ListView) findViewById(R.id.listViewMain);
                customAdapter = new CustomAdaptorMain(MainActivity.this, R.layout.item_recipe, elements);
                listView.setAdapter(customAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
                sp2.edit().putString("username", "").apply();
                Intent intentLogOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addRcipe(View view) {
        Intent i = new Intent(MainActivity.this, RecipeItem.class);
        i.putExtra("Class","New");
        startActivity(i);
    }
}