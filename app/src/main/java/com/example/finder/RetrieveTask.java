package com.example.finder;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RetrieveTask extends AsyncTask {

    private HttpURLConnection myConnection;
    private URL myURL;
    private String userToken = "439e2e4d7bc0ca1d2e9601f5b65cdd199c5a130b";
    private OutputStream out = null;
    private Map<String, String> parameters;

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            myURL = new URL("https://api.logmeal.es/v2/recognition/dish");
            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            myConnection.setRequestProperty("Accept", "application/json");

            parameters = new HashMap<>();
            parameters.put("Authorization", "Bearer " + userToken);
            parameters.put("model_version", "v0.9");
            parameters.put("skip_types", "[1,3]");
            parameters.put("language", "eng");

            myConnection.setDoOutput(true); // folosim POST
            OutputStream os = myConnection.getOutputStream();
            byte[] intput = ParameterStringBuilder.getParamsString(parameters).getBytes("utf-8");
            os.write(intput,0,intput.length);

            /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(ParameterStringBuilder.getParamsString(parameters));
            writer.flush();
            writer.close();
            out.close();
*/
            myConnection.connect();
            // handle cookies?
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myConnection.disconnect();
        }
        return null;
    }
}
