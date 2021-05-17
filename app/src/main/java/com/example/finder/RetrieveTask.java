package com.example.finder;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RetrieveTask extends AsyncTask {
    private URL myURL;
    private String userToken = "1fa188b56ea07ffc27742278d888d24d95dd045c", serverResponseMessage;
    private Bitmap photoFile;
    private String url = "https://api.logmeal.es/v2/recognition/dish";
    private String charset = "UTF-8";
    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private String CRLF = "\r\n"; // Line separator required by multipart/form-data.
    private String filePath;
    private String newIngredientDetected;

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            newIngredientDetected = "";
            photoFile = (Bitmap) objects[0];
            filePath = (String) objects[1];
            OkHttpClient client = new OkHttpClient();
            File sourceFile = new File(filePath);

                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        //.addFormDataPart("Authorization", "Bearer " + userToken)
                        .addFormDataPart("image",getName(filePath), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(req)
                        .addHeader("Authorization", "Bearer " + userToken)
                        .build();

                Response response = client.newCall(request).execute();
                newIngredientDetected = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        //if(newIngredientDetected.length()>1)
            //return newIngredientDetected;
        return null; // un string cu numele ingredientului detectat
    }

    private String getName(String fielpath)
    {
        String[] split = fielpath.split("/");
        return split[9];
    }
}
