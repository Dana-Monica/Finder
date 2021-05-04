package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecipeItem extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private SharedPreferences sp, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbarMain);
        setSupportActionBar(myToolbar);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        sp2 = getSharedPreferences("username", MODE_PRIVATE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                return true;
            case R.id.ingredients:
                Intent intentIngredients = new Intent(RecipeItem.this, IngredientsActivity.class);
                startActivity(intentIngredients);
                return true;
            case R.id.logout:
                sp.edit().putBoolean("logged",false).apply();
                sp2.edit().putString("username", "").apply();
                Intent intentLogOut = new Intent(RecipeItem.this, LoginActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveRecipe(View view) {
        //save to database and then go to main activity
        String name = ((EditText) findViewById(R.id.recipeNameItem)).getText().toString();
        String instructions = ((EditText) findViewById(R.id.instructionsRecipeItem)).getText().toString();
        HashMap<String, String> ingredients = parseIngredients();

        if (name.length()>1) {
            if(instructions.length()>2) {
                if(ingredients.isEmpty() == false) {
                    String user = sp2.getString("username", "");
                    databaseReference.child("Recipes").child(name).child("Instructions").setValue(instructions);
                    Iterator it = ingredients.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        databaseReference.child("Recipes").child(name).child("Ingredients").child(pair.getKey().toString()).setValue(pair.getValue());
                        it.remove();
                    }
                    databaseReference.child("Recipes").child(name).child("user").setValue(user);

                    Intent i = new Intent(RecipeItem.this, MainActivity.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(this, "Please enter ingredients!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Please enter instructions!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Please enter recipe name!", Toast.LENGTH_SHORT).show();
    }

    public HashMap<String,String> parseIngredients()
    {
        HashMap<String,String> list = new HashMap<String,String>();
        String text = ((EditText) findViewById(R.id.ingredientsRecipeItem)).getText().toString();
        String[] lines = text.split("\\r?\\n");
        String[] lines2;
        for (String line : lines) {
            lines2 = line.split(":");
            list.put(lines2[0],lines2[1]);
        }
        return list;
    }

    public void cancelButton(View view) {
        //don't save
        Intent i = new Intent(RecipeItem.this, MainActivity.class );
        startActivity(i);
    }
}