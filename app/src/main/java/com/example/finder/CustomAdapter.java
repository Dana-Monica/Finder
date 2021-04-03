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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> ingredients;
    private int layoutResID;
    private DatabaseReference databaseReference;

    public CustomAdapter(Context context, int layoutResourceID, List<Item> payments) {
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
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.nameIngredientItem = (TextView) view.findViewById(R.id.ingredientNameItem);
            itemHolder.checkIngredient = (CheckBox) view.findViewById(R.id.checkboxIngredient);
            itemHolder.deleteIngredient = (Button) view.findViewById(R.id.deleteIngredientItem);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final Item pItem = ingredients.get(position);

        itemHolder.nameIngredientItem.setText(pItem.getItem());

        itemHolder.deleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Ingredients").child(pItem.getKey()).removeValue();
            }
        });
        return view;
    }

    private static class ItemHolder {
        TextView nameIngredientItem;
        Button deleteIngredient;
        CheckBox checkIngredient;
    }
}
