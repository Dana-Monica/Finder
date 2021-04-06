package com.example.finder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomAdaptorMain extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> ingredients;
    private int layoutResID;
    private DatabaseReference databaseReference;

    public CustomAdaptorMain(Context context, int layoutResourceID, List<Recipe> payments) {
        super(context,
                layoutResourceID,
                payments);
        this.context = context;
        this.ingredients = payments;
        this.layoutResID = layoutResourceID;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CustomAdaptorMain.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new CustomAdaptorMain.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.recipeName = (TextView) view.findViewById(R.id.recipeName);
            itemHolder.ingredients = (TextView) view.findViewById(R.id.recipeIngredients);
            itemHolder.instructions = (TextView) view.findViewById(R.id.recipeInstructions);
            itemHolder.editRecipe = (Button) view.findViewById(R.id.editRecipe);
            itemHolder.seeRecipe = (Button) view.findViewById(R.id.seeRecipe);

            view.setTag(itemHolder);

        } else {
            itemHolder = (CustomAdaptorMain.ItemHolder) view.getTag();
        }

        final Recipe pItem = ingredients.get(position);

        itemHolder.recipeName.setText(pItem.getName());
        //itemHolder.instructions.setText(pItem.getInstructionsString());
        itemHolder.ingredients.setText(pItem.getIngredientsString());

        itemHolder.editRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ce sa faci daca apasa useru pe edit
            }
        });

        itemHolder.seeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemHolder.instructions.getText().toString().equals(""))
                    itemHolder.instructions.setText(pItem.getInstructionsString());
                else
                    itemHolder.instructions.setText("");
            }
        });


        return view;
    }

    private static class ItemHolder {
        TextView recipeName;
        TextView ingredients;
        TextView instructions;
        Button editRecipe;
        Button seeRecipe;
    }
}
