package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.*;

public class IngredientsActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private SharedPreferences sp;
    private String filePath = "";
    private File photoFile;
    private DatabaseReference databaseReference;
    private List<Item> elements;
    private int numberOfItems = 0;
    private ListView listView;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        elements = new ArrayList<Item>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        createNewDBListener();
    }

    private void createNewDBListener() {
        databaseReference.child("Ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elements.clear();
                for (DataSnapshot entrySnaphot : dataSnapshot.getChildren()) {
                        Item item = new Item((String) entrySnaphot.getValue(),(String) entrySnaphot.getKey());

                        elements.add(item);
                        //customAdapter.notifyDataSetChanged();
                }
                numberOfItems = elements.size();
                listView = (ListView) findViewById(R.id.listView);
                customAdapter = new CustomAdapter(IngredientsActivity.this, R.layout.item_ingredient, elements);
                listView.setAdapter(customAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(IngredientsActivity.this, "Clicked on " + elements.get(i), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), ViewPaymentActivity.class));
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void searchPicture(File f)
    {
        try {
            // on new thread, not on main thread !
            new RetrieveTask().execute(f);
            Toast.makeText(IngredientsActivity.this, "so far so good", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);
            /*imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

           try {
                photoFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "Imagename.jpeg");
                photoFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(photoFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            searchPicture(photoFile);
            //createNewDBListener();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        createNewDBListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createNewDBListener();
    }

    private void addNewIngredient(String ingredient)
    {
        databaseReference.child("Ingredients").child(Long.toHexString(System.currentTimeMillis())).setValue(ingredient);
        createNewDBListener();
        customAdapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.ingredientName)).setText("");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile  = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.finder",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intentHome = new Intent(IngredientsActivity.this, MainActivity.class);
                startActivity(intentHome);
                return true;
            case R.id.ingredients:
                return true;
            case R.id.logout:
                sp.edit().putBoolean("logged",false).apply();
                Intent intentLogOut = new Intent(IngredientsActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void adNewIngredient(View view) {
        addNewIngredient(((EditText)findViewById(R.id.ingredientName)).getText().toString());
    }
}