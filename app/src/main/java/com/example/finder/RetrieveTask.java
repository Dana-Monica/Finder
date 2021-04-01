package com.example.finder;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RetrieveTask extends AsyncTask {

    private HttpURLConnection myConnection;
    private URL myURL;
    private String userToken = "439e2e4d7bc0ca1d2e9601f5b65cdd199c5a130b", serverResponseMessage;
    private DataOutputStream out = null;
    private Map<String, String> parameters;
    private File photoFile;
    private int serverResponseCode = 0;

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            photoFile = (File) objects[0];
            myURL = new URL("https://api.logmeal.es/v2/recognition/dish");
            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setRequestProperty("Content-Type", "multipart/form-data");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.setRequestProperty("Authorization", "Bearer " + userToken);

            parameters = new HashMap<>();
            parameters.put("Authorization", "Bearer " + userToken);
            parameters.put("model_version", "v0.9");
            parameters.put("skip_types", "[1,3]");
            parameters.put("language", "eng");
            //parameters.put("image", photoFile);

            myConnection.setDoOutput(true); // folosim POST
            out = new DataOutputStream(myConnection.getOutputStream());
            byte[] intput = ParameterStringBuilder.getParamsString(parameters).getBytes("utf-8");
            //out.write(intput,0,intput.length);

            byte[] fileData = new byte[(int)photoFile.length()];
            FileInputStream in = new FileInputStream(photoFile);
            in.read(fileData);
            in.close();
            out.write(fileData,0,fileData.length);

            /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(ParameterStringBuilder.getParamsString(parameters));
            writer.flush();
            writer.close();*/
            out.close();

            myConnection.connect();

            serverResponseCode = myConnection.getResponseCode();
            serverResponseMessage = myConnection.getResponseMessage();
            // handle cookies?
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myConnection.disconnect();
        }
        return null;
    }
}
