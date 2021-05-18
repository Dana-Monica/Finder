package com.example.finder;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class RetrieveTask extends AsyncTask {
    private URL myURL;
    private String userToken = "1fa188b56ea07ffc27742278d888d24d95dd045c", serverResponseMessage;
    private String username;
    private String url = "https://api.logmeal.es/v2/recognition/dish";
    private String charset = "UTF-8";
    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private String CRLF = "\r\n"; // Line separator required by multipart/form-data.
    private String filePath;
    private String newIngredientDetected;
    private DatabaseReference databaseReference;

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            newIngredientDetected = "";
            username = (String) objects[0];
            filePath = (String) objects[1];
            OkHttpClient client = new OkHttpClient();
            File sourceFile = new File(filePath);

                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",getName(filePath), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(req)
                        .addHeader("Authorization", "Bearer " + userToken)
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject obj = new JSONObject(response.body().string());
                JSONArray a = obj.getJSONArray("recognition_results");
                JSONObject b = (JSONObject) a.get(0);
                newIngredientDetected = b.getString("name");
                // add new ingredient to list of username
                addNew(newIngredientDetected, username);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null; // un string cu numele ingredientului detectat
    }

    private void addNew(String ingredient, String user)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Ingredients").child(user).child(Long.toHexString(System.currentTimeMillis())).setValue(ingredient);
    }

    private String getName(String fielpath)
    {
        String[] split = fielpath.split("/");
        return split[9];
    }

}
